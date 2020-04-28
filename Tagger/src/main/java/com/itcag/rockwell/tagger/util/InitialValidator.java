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
import com.itcag.rockwell.tagger.lang.Conditions;
import com.itcag.rockwell.tagger.lang.AcceptingCondition;
import com.itcag.rockwell.tagger.lang.Condition;
import com.itcag.rockwell.tagger.lang.ConditionElement;
import com.itcag.rockwell.tagger.lang.Match;
import com.itcag.rockwell.tagger.patterns.Patterns;
import com.itcag.rockwell.tagger.lang.RejectingCondition;
import com.itcag.rockwell.tagger.lang.State;

import java.util.ArrayList;

/**
 * <p>This class evaluates whether a {@link com.itcag.rockwell.lang.Token token} matches the first {@link com.itcag.rockwell.tagger.lang.ConditionElement condition element} of any {@link com.itcag.rockwell.tagger.lang.Condition condition}. If it does, this condition creates a new instance of the {@link com.itcag.rockwell.tagger.lang.State State} class.</p>
 * <p>Neither infix pattern, nor quodlibet are allowed as the first condition element. Accordingly, this class provides the following validation methods:</p>
 * <ul>
 *    <li>Prefix pattern</li>
 *    <li>Suffix pattern</li>
 *    <li>Regular element</li>
 * </ul>
 */
public class InitialValidator extends Validator {

    private final Conditions conditions;
    
    private final ArrayList<? extends Token> tokens;
    
    /**
     * @param conditions Instance of the {@link com.itcag.rockwell.tagger.lang.Conditions Conditions} class containing the applicable Rockwell expressions.
     * @param patterns Instance of the {@link com.itcag.rockwell.tagger.patterns.Patterns Patterns} class containing all applicable patterns.
     * @param tokens Array list of instances of the {@link com.itcag.rockwell.lang.Token Token} class representing text to be processed.
     * @param debugger Instance of the {@link com.itcag.rockwell.tagger.debug.Debugger Debugger} class use for debugging only.
     * @throws Exception if anything goes wrong.
     */
    public InitialValidator(Conditions conditions, Patterns patterns, ArrayList<? extends Token> tokens, Debugger debugger) throws Exception {
        super(patterns, debugger);
        this.conditions = conditions;
        this.tokens = tokens;
    }

    /**
     * @param conditionElement Instance of the {@link com.itcag.rockwell.tagger.lang.ConditionElement ConditionElement} class that is being validated.
     * @param anchor Instance of the {@link com.itcag.rockwell.lang.Token Token} class that anchors the prefix.
     * @return Instance of the {@link com.itcag.rockwell.tagger.lang.State State} class if the prefix is validated, or null otherwise.
     * @throws Exception if anything goes wrong.
     */
    public State validatePrefix(ConditionElement conditionElement, Token anchor) throws Exception {

        ArrayList<Token> tmp = getPrefixList(this.tokens, anchor, conditionElement.getPrefix().isInclusive());

        if (tmp.isEmpty()) {
            
            debugger.print("Prefix validation " + conditionElement.getPrefix().getKey());
            debugger.print("-> Validating: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));

            if (conditionElement.getPrefix().isOptional()) {
                debugger.print("Validated prefix (no prefix, but it is optional): " + anchor.toString());
                debugger.print("->> Validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return getNewState(conditionElement, anchor);
            } else {
                debugger.print("Prefix not validated (no prefix): " + anchor.toString());
                debugger.print("->> Not validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return null;
            }
            
        } else {
            
            debugger.print("Prefix validation " + conditionElement.getPrefix().getKey(), tmp);
            debugger.print("-> Validating: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));

            Tag tag = patterns.getPrefix(tmp, conditionElement.getPrefix(), super.debugger);
            if (tag != null) {
                debugger.print("Validated prefix: " + anchor.toString(), tmp);
                debugger.print("->> Validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return getNewState(conditionElement, anchor, tag, tmp, false);
            }       

            if (conditionElement.getPrefix().isOptional()) {
                debugger.print("Validated prefix (prefix not matched, but it is optional): " + anchor.toString(), tmp);
                debugger.print("->> Validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return getNewState(conditionElement, anchor);
            } else {
                debugger.print("Prefix not validated (prefix not matched): " + anchor.toString(), tmp);
                debugger.print("->> Not validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return null;
            }
        
        }
            
    }

    /**
     * @param conditionElement Instance of the {@link com.itcag.rockwell.tagger.lang.ConditionElement ConditionElement} class that is being validated.
     * @param anchor Instance of the {@link com.itcag.rockwell.lang.Token Token} class that anchors the suffix.
     * @return Instance of the {@link com.itcag.rockwell.tagger.lang.State State} class if the suffix is validated, or null otherwise.
     * @throws Exception if anything goes wrong.
     */
    public State validateSuffix(ConditionElement conditionElement, Token anchor) throws Exception {

        ArrayList<Token> tmp = getSuffixList(this.tokens, anchor, conditionElement.getSuffix().isInclusive());
        
        if (tmp.isEmpty()) {

            debugger.print("Suffix validation " + conditionElement.getSuffix().getKey());
            debugger.print("-> Validating: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));

            if (conditionElement.getSuffix().isOptional()) {
                debugger.print("Validated suffix (no suffix, but it is optional): " + anchor.toString());
                debugger.print("->> Validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return getNewState(conditionElement, anchor);
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
                return getNewState(conditionElement, anchor, tag, tmp, true);
            }

            if (conditionElement.getSuffix().isOptional()) {
                debugger.print("Validated suffix (suffix not matched, but it is optional): " + anchor.toString(), tmp);
                debugger.print("->> Validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return getNewState(conditionElement, anchor);
            } else {
                debugger.print("Suffix not validated (suffix not matched): " + anchor.toString(), tmp);
                debugger.print("->> Not validated: "+ " " + this.conditions.getConditions().get(conditionElement.getConditionId()));
                return null;
            }

        }

    }
    
    private State getNewState(ConditionElement conditionElement, Token token, Tag tag, ArrayList<Token> tmp, boolean before) {
        
        Condition condition = conditions.getConditions().get(conditionElement.getConditionId());

        State retVal = new State(conditionElement.getConditionId(), condition.getScript(), conditionElement.getOut());
        if (condition instanceof AcceptingCondition) {
            AcceptingCondition acceptingCondition = (AcceptingCondition) condition;
            retVal.setRejectedById(acceptingCondition.getRejectedById());
            retVal.setTag(acceptingCondition.getTag());
        } else if (condition instanceof RejectingCondition) {
            RejectingCondition rejectingCondition = (RejectingCondition) condition;
            retVal.setIdToBeRejected(rejectingCondition.getIdToBeRejected());
        }
        retVal.setState(conditionElement.getOut());

        /**
         * Suffix inserts the token first, and then the tagged tokens from the suffix.
         */
        if (before) {
            Match match = new Match(token);
            retVal.addMatch(match);
        }

        for (int i = 0; i < tmp.size(); i++) {
            Token test = tmp.get(i);
            if (test.getIndex() >= tag.getStart() && test.getIndex() <= tag.getEnd()) {
                Match match = new Match(test);
                retVal.addMatch(match);
            }
        }
    
        /**
         * Prefix inserts first the tagged tokens from the prefix, and then the token.
         */
        if (!before) {
            Match match = new Match(token);
            retVal.addMatch(match);
        }

        if (retVal.getState() == Conditions.FINAL_STATE_CODE) debugger.print("Validated completely: " + TokenToolbox.getStringFromTokens(new ArrayList<>(retVal.getMatches().values())));

        return retVal;
        
    }
    
    private State getNewState(ConditionElement conditionElement, Token token) {
        
        Condition condition = conditions.getConditions().get(conditionElement.getConditionId());

        State retVal = new State(conditionElement.getConditionId(), condition.getScript(), conditionElement.getOut());
        if (condition instanceof AcceptingCondition) {
            AcceptingCondition acceptingCondition = (AcceptingCondition) condition;
            retVal.setRejectedById(acceptingCondition.getRejectedById());
            retVal.setTag(acceptingCondition.getTag());
        } else if (condition instanceof RejectingCondition) {
            RejectingCondition rejectingCondition = (RejectingCondition) condition;
            retVal.setIdToBeRejected(rejectingCondition.getIdToBeRejected());
        }
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
     * @return Instance of the {@link com.itcag.rockwell.tagger.lang.State State} class if the condition element is validated, or null otherwise.
     * @throws Exception if anything goes wrong.
     */
    public State validateRegular(String key, ConditionElement conditionElement, Token token) throws Exception {

        debugger.print("Regular validation (" + key + ") " + token.toString());
        
        if (!validateAdditionalConditions(conditionElement, token)) return null;
        if (isRejectedConditionElement(conditionElement, token)) return null;

        Condition condition = conditions.getConditions().get(conditionElement.getConditionId());
        
        State retVal = new State(conditionElement.getConditionId(), condition.getScript(), conditionElement.getOut());
        if (condition instanceof AcceptingCondition) {
            AcceptingCondition acceptingCondition = (AcceptingCondition) condition;
            retVal.setRejectedById(acceptingCondition.getRejectedById());
            retVal.setTag(acceptingCondition.getTag());
        } else if (condition instanceof RejectingCondition) {
            RejectingCondition rejectingCondition = (RejectingCondition) condition;
            retVal.setIdToBeRejected(rejectingCondition.getIdToBeRejected());
        }
        retVal.addMatch(new Match(token));

        if (retVal.getState() == Conditions.FINAL_STATE_CODE) debugger.print("Validated completely: " + TokenToolbox.getStringFromTokens(new ArrayList<>(retVal.getMatches().values())));
        debugger.print("Validated: " + token.toString());

        return retVal;
        
    }
    
}
