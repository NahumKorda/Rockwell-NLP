package com.itcag.rockwell.tagger;

import com.itcag.rockwell.lang.Semtoken;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tagger.lang.Conditions;
import com.itcag.rockwell.tagger.lang.Match;
import com.itcag.rockwell.tagger.lang.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * <p>This class validates a single {@link com.itcag.rockwell.lang.Token token}.</p>
 */
public class TokenAnalyzer {

    private final Processor processor;
    
    private ArrayList<State> currentStates = new ArrayList<>();
    private ArrayList<State> newStates = new ArrayList<>();
    
    private final ArrayList<State> matches = new ArrayList<>();
        
    /**
     * @param processor Instance of the {@link com.itcag.rockwell.tagger.Processor Processor} class.
     */
    public TokenAnalyzer(Processor processor) {
        this.processor = processor;
    }

    /**
     * @param token Instance of the {@link com.itcag.rockwell.lang.Token Token} class.
     * @throws Exception if anything goes wrong.
     */
    public void analyze(Token token) throws Exception {
        
        /**
         * Some tokens have part of speech set during the tokenization.
         */
        if (token.getAlternatives().isEmpty()) {
            addNewStates(processor.getStates(currentStates, token, true), newStates);
        } else {

            if (token instanceof Semtoken || token instanceof Match) {
                addNewStates(processor.getStates(currentStates, token, true), newStates);
            }

            /**
             * Each alternative is processed separately.
             */
            for (Token alternative : token.getAlternatives()) {
                Token newToken = new Token(alternative.getWord(), alternative.getPos(), alternative.getLemma(), token.getIndex());
                /**
                 * Only the first alternative is used for checking the quodlibet.
                 */
                if (alternative.equals(token.getAlternatives().get(0))) {
                    addNewStates(processor.getStates(currentStates, newToken, true), newStates);
                } else {
                    addNewStates(processor.getStates(currentStates, newToken, false), newStates);
                }
            }

        }

        validateStates(newStates, matches);

        /**
         * The current states are updated for further processing.
         */
        currentStates = newStates;
        newStates = new ArrayList<>();

    }
    
    private void addNewStates(ArrayList<State> newStates, ArrayList<State> currentStates) {
        for (State newState : newStates) {
            currentStates.add(newState);
        }
    }
    
    private void validateStates(ArrayList<State> newStates, ArrayList<State> matches) {
        
        HashMap<String, ArrayList<State>> validator = new HashMap<>();
        Iterator<State> stateIterator = newStates.iterator();
        while (stateIterator.hasNext()) {
            State newState = stateIterator.next();
            /**
             * Some rules create multiple identical states:
             * if POS is not checked, and a token has multiple POS,
             * a state will be created for each of them.
             * For example, of "price" is matched by lemma,
             * but it can be NN1, VVB or VVI - three states are created.
             * However, we don't care for the POS,
             * so one rule is sufficient to continue.
             */
            if (isValid(newState, validator)) {
                if (validator.containsKey(newState.getConditionId())) {
                    validator.get(newState.getConditionId()).add(newState);
                } else {
                    validator.put(newState.getConditionId(), new ArrayList<>(Arrays.asList(newState)));
                }
                /**
                 * For each token the states are checked
                 * to see if any tags were identified.
                 */
                if (newState.getState() == Conditions.FINAL_STATE_CODE) {
                    matches.add(newState.getCopy());
                    stateIterator.remove();
                }
            } else {
                stateIterator.remove();
            }
        }
            
    }
    
    private boolean isValid(State newState, HashMap<String, ArrayList<State>> validator) {
        
        if (!validator.containsKey(newState.getConditionId())) return true;
        
        for (State state : validator.get(newState.getConditionId())) {
            if (newState.getState() == state.getState()) {
                return false;
            }
        }
        
        return true;
        
    }

    /**
     * @return Array list containing instances of the {@link com.itcag.rockwell.tagger.lang.State State} class representing the current states of the finite state automaton.
     */
    public ArrayList<State> getCurrentStates() {
        return this.currentStates;
    }

    /**
     * @return Array list containing instances of the {@link com.itcag.rockwell.tagger.lang.State State} class representing the complete matches (i.e. completely satisfied conditions).
     */
    public ArrayList<State> getMatches() {
        return this.matches;
    }
    
}
