package com.itcag.rockwell.tagger;

import com.itcag.rockwell.tagger.lang.Conditions;
import com.itcag.rockwell.tagger.util.ContinuationValidator;
import com.itcag.rockwell.tagger.util.InitialValidator;
import com.itcag.rockwell.lang.Semtoken;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.debug.DebuggingLedger;
import com.itcag.rockwell.tagger.lang.AcceptingCondition;
import com.itcag.rockwell.tagger.lang.Condition;
import com.itcag.rockwell.tagger.lang.ConditionElement;
import com.itcag.rockwell.tagger.lang.MatchingSpecification.Aspect;
import com.itcag.rockwell.tagger.lang.Match;
import com.itcag.rockwell.tagger.patterns.Patterns;
import com.itcag.rockwell.tagger.lang.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * <p>This is Rockwell implementation of the non-deterministic finite state automaton.</p>
 * <p>For every processed sentence a new instance of this class is created.</p>
 * <p>This class initiated by an instance of the {@link com.itcag.rockwell.tagger.lang.Conditions Conditions} class containing all applicable Rockwell expressions, as well as by an instance of the {@link com.itcag.rockwell.tagger.patterns.Patterns Patterns} class containing all patterns that these expressions possibly reference.</p>
 * <p>The evaluation then proceeds token by token, calling the {@link #getStates(java.util.ArrayList, com.itcag.rockwell.lang.Token, boolean)} method one token at the time.</p>
 */
public class Processor {

    protected final Conditions conditions;
    
    private final InitialValidator initialValidator;
    private final ContinuationValidator continuationValidator;
    
    /**
     * Shortcuts are validate only against a specific target ID:
     * either a specific shortcut gets validated, or it doesn't matter.
     */
    private final String expectedValue;
    
    private final Debugger debugger;

    /**
     * @param conditions Instance of the {@link com.itcag.rockwell.tagger.lang.Conditions Conditions} class containing all applicable Rockwell expressions.
     * @param patterns Instance of the {@link com.itcag.rockwell.tagger.patterns.Patterns Patterns} class containing all applicable patterns.
     * @param tokens Array list of tokens that are to be processed.
     * @param expectedValue String holding the tag of a targeted pattern, if the processor validates an affix, or null. 
     * @param debugger Instance of the {@link com.itcag.rockwell.tagger.debug.Debugger Debugger} class use for debugging only.
     * @throws Exception if anything goes wrong.
     */
    public Processor(Conditions conditions, Patterns patterns, ArrayList<? extends Token> tokens, String expectedValue, Debugger debugger) throws Exception {

        this.conditions = conditions;
        this.initialValidator = new InitialValidator(conditions, patterns, tokens, debugger);
        this.continuationValidator = new ContinuationValidator(conditions, patterns, tokens, debugger);
        
        this.expectedValue = expectedValue;
        
        this.debugger = debugger;
        
        if (DebuggingLedger.printConditions(debugger.client())) {
            conditions.print();
            debugger.print(tokens);
        }
        
    }

    /**
     * This method evaluates a token, and if matched creates new states or updates the existing states. States that cannot be continued or completed with this token are discarded.
     * The token is possibly one of the alternative part-of-speech interpretations, if the original word is ambiguous.
     * This class is Rockwell implementation if the non-deterministic finite state automaton, which implies that the automaton can be simultaneously in multiple states.
     * @param currentStates Array list containing the instances of the {@link com.itcag.rockwell.tagger.lang.State State} class representing the current states of the automaton.
     * @param token Instance of the {@link com.itcag.rockwell.lang.Token Token} class that is to be evaluated.
     * @param firstAlternative Boolean indicated whether this token is the first of the alternative part-of-speech interpretations of an ambiguous word. The first alternative undergoes additional validations that later alternatives do not.
     * @see com.itcag.rockwell.lang.Alternatives
     * @return Array list containing the instances of the {@link com.itcag.rockwell.tagger.lang.State State} class representing the new states of the automaton.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<State> getStates(ArrayList<State> currentStates, Token token, boolean firstAlternative) throws Exception {
        
        ArrayList<State> retVal = new ArrayList<>();
        
        String key;
        
        if (firstAlternative) {

            key = Aspect.VERBATIM + ConditionElement.KEY_DELIMITER + token.getWord();
            validate(key, token, currentStates, retVal);

            key = Aspect.CAIN + ConditionElement.KEY_DELIMITER + token.getCain();
            validate(key, token, currentStates, retVal);

        }

        if (token.getLemma() != null) {
            key = Aspect.LEMMA + ConditionElement.KEY_DELIMITER + token.getLemma();
            validate(key, token, currentStates, retVal);
        }

        if (token.getPos() != null) {

            key = Aspect.POS + ConditionElement.KEY_DELIMITER + token.getPos();
            validate(key, token, currentStates, retVal);

            key = Aspect.TYPE + ConditionElement.KEY_DELIMITER + token.getType();
            validate(key, token, currentStates, retVal);

        }

        if (token instanceof Semtoken || token instanceof Match) {
            
            Semtoken semtoken = (Semtoken) token;
            for (String role : semtoken.getRoles()) {
                key = Aspect.ROLE + ConditionElement.KEY_DELIMITER + role.toLowerCase();
                validate(key, token, currentStates, retVal);
            }
            
        }
        
        if (firstAlternative) {
            key = Aspect.QUODLIBET + ConditionElement.KEY_DELIMITER + ConditionFactory.KLEENE;
            validate(key, token, currentStates, retVal);
        }
        
        this.debugger.print("");

        return retVal;
        
    }
    
    private void validate(String key, Token token, ArrayList<State> currentStates, ArrayList<State> newStates) throws Exception {
        
        identifyInitial(key, token, newStates);
        
        if (currentStates.isEmpty()) return;
        
        if (conditions.getElements().containsKey(key)) {
            HashMap<Integer, HashMap<Integer, ConditionElement>> medial = conditions.getElements().get(key);
            for (State state : currentStates) {

                /**
                 * Number of optional elements must be limited,
                 * because optional quodlibet
                 * would create a match for every token
                 * until the end of the sentence.
                 */
                if (state.getOptionalCount() >= Conditions.MAX_OPTIONAL) continue;

                if (medial.containsKey(state.getState())) {
                    HashMap<Integer, ConditionElement> inner = medial.get(state.getState());
                    for (Entry<Integer, ConditionElement> entry : inner.entrySet()) {
                        
                        ConditionElement conditionElement = entry.getValue();

                        /**
                         * Ensure that the current state indeed extends
                         * the condition retrieved from the elements collection.
                         * The same sequence of condition elements can exist in different conditions.
                         * However, at some point they will differ, and it must be ensured that the
                         * current state matches the condition that is to be continued,
                         * and does not merely match the expected "in" value.
                         */
                        if (!state.getConditionId().equals(conditionElement.getConditionId())) continue;

                        if (Aspect.QUODLIBET.equals(conditionElement.getAspect())) {

                            State newstate = continuationValidator.validateQuodlibet(conditionElement, token, state);
                            if (newstate == null) continue;
                            newStates.add(newstate);

                        } else {

                            State newstate = continuationValidator.validateRegular(key, conditionElement, token, state);
                            if (newstate == null) continue;

                            if (conditionElement.getInfix() != null) {
                                State test = continuationValidator.validateInfix(conditionElement, token, state);
                                if (test == null) continue;
                                test.getMatches().entrySet().forEach((matchEntry) -> {
                                    newstate.addMatch(matchEntry.getValue());
                                });
                            }

                            if (conditionElement.getSuffix()  != null) {
                                State test = continuationValidator.validateSuffix(conditionElement, token, state);
                                if (test == null) continue;
                                test.getMatches().entrySet().forEach((matchEntry) -> {
                                    newstate.addMatch(matchEntry.getValue());
                                });
                            }

                            newStates.add(newstate);

                        }

                    }
                    
                }
            
            }
        
        }
        
    }
    
    private void identifyInitial(String key, Token token, ArrayList<State> newStates) throws Exception {
        
        if (conditions.getInitial().containsKey(key)) {
            for (ConditionElement conditionElement : conditions.getInitial().get(key)) {

                /**
                 * This is applied only to the pattern validation.
                 * The initial state must match the intended pattern.
                 */
                if (expectedValue != null) {
                    Condition condition = conditions.getConditions().get(conditionElement.getConditionId());
                    if (condition instanceof AcceptingCondition) {
                        AcceptingCondition pattern = (AcceptingCondition) condition;
                        if (!pattern.getTag().equals(expectedValue)) continue;
                    }
                }

                State newstate = initialValidator.validateRegular(key, conditionElement, token);
                if (newstate == null) continue;

                if (conditionElement.getPrefix() != null) {
                    State test = initialValidator.validatePrefix(conditionElement, token);
                    if (test == null) continue;
                    test.getMatches().entrySet().forEach((matchEntry) -> {
                        newstate.addMatch(matchEntry.getValue());
                    });
                }

                if (conditionElement.getSuffix() != null) {
                    State test = initialValidator.validateSuffix(conditionElement, token);
                    if (test == null) continue;
                    test.getMatches().entrySet().forEach((matchEntry) -> {
                        newstate.addMatch(matchEntry.getValue());
                    });
                }

                newStates.add(newstate);
                
                /*
                if (Aspect.QUODLIBET.equals(conditionElement.getAspect())) {

                    State newstate = initialValidator.validateQuodlibet(conditionElement, token);
                    if (newstate == null) continue;
                    newStates.add(newstate);

                } else {


                }
                */
            }

        }

    }
    
}
