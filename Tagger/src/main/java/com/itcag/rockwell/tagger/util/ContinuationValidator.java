/*
 *
 * Copyright 2020 IT Consulting AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.itcag.rockwell.tagger.util;

import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.util.TokenToolbox;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.lang.ConditionElement;
import com.itcag.rockwell.tagger.lang.Conditions;
import com.itcag.rockwell.tagger.lang.Match;
import com.itcag.rockwell.tagger.patterns.Patterns;
import com.itcag.rockwell.tagger.lang.State;

import java.util.ArrayList;

/**
 * <p>This class evaluates whether a {@link com.itcag.rockwell.lang.Token token} matches any {@link com.itcag.rockwell.tagger.lang.ConditionElement condition element} that continues an existing {@link com.itcag.rockwell.tagger.lang.State state}. If it does, a new instance is created as a copy of the existing state, and it is updated with the new match.</p>
 * <p>The prefix pattern is allowed only as the first condition element. Accordingly, this class provides the following validation methods:</p>
 * <ul>
 *    <li>Quodlibet</li>
 *    <li>Infix pattern</li>
 *    <li>Suffix pattern</li>
 *    <li>Regular element</li>
 * </ul>
 */
public class ContinuationValidator extends Validator {
    
    private final Conditions conditions;
    
    protected final ArrayList<? extends Token> tokens;
    
    public ContinuationValidator(Conditions conditions, Patterns patterns, ArrayList<? extends Token> tokens, Debugger debugger) throws Exception {
        super(patterns, debugger);
        this.conditions = conditions;
        this.tokens = tokens;
    }
    
    /**
     * @param conditionElement Instance of the {@link com.itcag.rockwell.tagger.lang.ConditionElement ConditionElement} class that is being validated.
     * @param token Instance of the {@link com.itcag.rockwell.lang.Token Token} class that is being evaluated.
     * @param state Instance of the {@link com.itcag.rockwell.tagger.lang.State State} class that is to be updated if the condition element is validated.
     * @return New instance of the {@link com.itcag.rockwell.tagger.lang.State State} class .
     * @throws Exception if anything goes wrong.
     */
    public State validateQuodlibet(ConditionElement conditionElement, Token token, State state) throws Exception {

        debugger.print("Quodlibet validation: " + token.toString());
    
        if (isRejectedConditionElement(conditionElement, token)) return null;

        State retVal = state.getCopy();
        retVal.incrementOptionalCount();
        retVal.setState(conditionElement.getOut());
        Match match = new Match(token);
        match.setQuodlibet(true);
        retVal.addMatch(match);

        return retVal;
        
    }
    
    /**
     * @param conditionElement Instance of the {@link com.itcag.rockwell.tagger.lang.ConditionElement ConditionElement} class that is being validated.
     * @param anchor Instance of the {@link com.itcag.rockwell.lang.Token Token} class that anchors the infix.
     * @param state Instance of the {@link com.itcag.rockwell.tagger.lang.State State} class that is to be updated if the condition element is validated.
     * @return New instance of the {@link com.itcag.rockwell.tagger.lang.State State} class if the infix is validated, or null otherwise.
     * @throws Exception if anything goes wrong.
     */
    public State validateInfix(ConditionElement conditionElement, Token anchor, State state) throws Exception {

        ArrayList<Token> tmp = getInfixList(this.tokens, state.getMatches(), anchor, conditionElement.getInfix().isInclusive());
        
        if (tmp.isEmpty()) {

            debugger.print("Infix validation " + conditionElement.getInfix().getKey());
            debugger.print("-> Validating: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));

            if (conditionElement.getInfix().isOptional()) {
                debugger.print("Validated infix (no infix, but it is optional): " + anchor.toString());
                debugger.print("->> Validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return getNewState(state, conditionElement, anchor);
            } else {
                debugger.print("Infix not validated (no infix): " + anchor.toString());
                debugger.print("->> Not validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return null;
            }

        } else {
            
            debugger.print("Infix validation " + conditionElement.getInfix().getKey(), tmp);
            debugger.print("-> Validating: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));

            Tag tag = patterns.getInfix(tmp, conditionElement.getInfix(), super.debugger);
            if (tag != null) {
                debugger.print("Validated infix: " + anchor.toString(), tmp);
                debugger.print("->> Validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return getNewState(state, conditionElement, anchor, tag, tmp, false);
            }

            /**
             * Unlike prefix and suffix, optional infix cannot contain unmatched tokens.
             * If there are tokens, they are either matched, or infix is not validated.
             */
            debugger.print("Infix not validated (infix not matched): " + anchor.toString(), tmp);
                debugger.print("->> Not validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
            return null;

        }
        
    }
    
    /**
     * @param conditionElement Instance of the {@link com.itcag.rockwell.tagger.lang.ConditionElement ConditionElement} class that is being validated.
     * @param anchor Instance of the {@link com.itcag.rockwell.lang.Token Token} class that anchors the suffix.
     * @param state Instance of the {@link com.itcag.rockwell.tagger.lang.State State} class that is to be updated if the condition element is validated.
     * @return New instance of the {@link com.itcag.rockwell.tagger.lang.State State} class if the suffix is validated, or null otherwise.
     * @throws Exception if anything goes wrong.
     */
    public State validateSuffix(ConditionElement conditionElement, Token anchor, State state) throws Exception {

        ArrayList<Token> tmp = getSuffixList(this.tokens, anchor, conditionElement.getSuffix().isInclusive());

        if (tmp.isEmpty()) {

            debugger.print("Suffix validation " + conditionElement.getSuffix().getKey());
            debugger.print("-> Validating: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));

            if (conditionElement.getSuffix().isOptional()) {
                debugger.print("Validated suffix (no suffix, but it is optional): " + anchor.toString());
                debugger.print("->> Validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return getNewState(state, conditionElement, anchor);
            } else {
                debugger.print("Suffix not validated (no suffix): " + anchor.toString());
                debugger.print("->> Not validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return null;
            }

        } else {
            
            debugger.print("Suffix validation " + conditionElement.getSuffix().getKey(), tmp);
            debugger.print("-> Validating: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));

            Tag tag = patterns.getSuffix(tmp, conditionElement.getSuffix(), super.debugger);
            if (tag != null) {
                debugger.print("Validated suffix: " + anchor.toString(), tmp);
                debugger.print("->> Validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return getNewState(state, conditionElement, anchor, tag, tmp, true);
            }

            if (conditionElement.getSuffix().isOptional()) {
                debugger.print("Validated suffix (suffix not matched, but it is optional): " + anchor.toString(), tmp);
                debugger.print("->> Validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return getNewState(state, conditionElement, anchor);
            } else {
                debugger.print("Suffix not validated (suffix not matched): " + anchor.toString(), tmp);
                debugger.print("->> Not validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return null;
            }

        }

    }
    
    private State getNewState(State state, ConditionElement conditionElement, Token token, Tag tag, ArrayList<Token> tmp, boolean before) {
        
        State retVal = state.getCopy();
        retVal.resetOptionalCount();
        retVal.setState(conditionElement.getOut());

        /**
         * Suffix inserts the token first, and then the tagged tokens from the suffix.
         */
        if (before) {
            Match match = new Match(token);
            retVal.addMatch(match);
        }
        
        /**
         * Infix inserts first the tagged tokens from the prefix, and then the token.
         */
        for (int i = 0; i < tmp.size(); i++) {
            Token test = tmp.get(i);
            if (test.getIndex() >= tag.getStart() && test.getIndex() <= tag.getEnd()) {
                Match match = new Match(test);
                retVal.addMatch(match);
            }
        }

        if (!before) {
            Match match = new Match(token);
            retVal.addMatch(match);
        }
        
        if (retVal.getState() == Conditions.FINAL_STATE_CODE) debugger.print("Validated completely: " + TokenToolbox.getStringFromTokens(new ArrayList<>(retVal.getMatches().values())));

        return retVal;
        
    }
    
    private State getNewState(State state, ConditionElement conditionElement, Token token) {
        
        State retVal = state.getCopy();
        retVal.setState(conditionElement.getOut());

        Match match = new Match(token);
        retVal.addMatch(match);

        if (retVal.getState() == Conditions.FINAL_STATE_CODE) debugger.print("Validated completely: " + TokenToolbox.getStringFromTokens(new ArrayList<>(retVal.getMatches().values())));

        return retVal;
        
    }
    
    /**
     * @param key String holding a composite of aspect + "|" + value.
     * @see com.itcag.rockwell.tagger.lang.MatchingSpecification#getKey()
     * @param conditionElement Instance of the {@link com.itcag.rockwell.tagger.lang.ConditionElement ConditionElement} class that is being validated.
     * @param token Instance of the {@link com.itcag.rockwell.lang.Token Token} class that is being evaluated.
     * @param state New instance of the {@link com.itcag.rockwell.tagger.lang.State State} class that is to be updated if the condition element is validated.
     * @return Instance of the {@link com.itcag.rockwell.tagger.lang.State State} class if the condition element is validated, or null otherwise.
     * @throws Exception if anything goes wrong.
     */
    public State validateRegular(String key, ConditionElement conditionElement, Token token, State state) throws Exception {

        debugger.print("Regular validation (" + key + ") " + token.toString());
    
        if (!validateAdditionalConditions(conditionElement, token)) return null;
        if (isRejectedConditionElement(conditionElement, token)) return null;

        State retVal = state.getCopy();
        retVal.resetOptionalCount();
        retVal.setState(conditionElement.getOut());
        retVal.addMatch(new Match(token));

        if (retVal.getState() == Conditions.FINAL_STATE_CODE) debugger.print("Validated completely: " + TokenToolbox.getStringFromTokens(new ArrayList<>(retVal.getMatches().values())));
        debugger.print("Validated: " + token.toString());
        
        return retVal;
        
    }
    
}
