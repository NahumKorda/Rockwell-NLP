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

import com.itcag.rockwell.lang.Semtoken;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.lang.ConditionElement;
import com.itcag.rockwell.tagger.lang.MatchingSpecification;
import com.itcag.rockwell.tagger.lang.Conditions;
import com.itcag.rockwell.tagger.lang.Match;
import com.itcag.rockwell.tagger.patterns.Patterns;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * <p>This class provides generic validation methods for validating single {@link com.itcag.rockwell.lang.Token token}. It also provides methods for extracting {@link com.itcag.rockwell.tagger.lang.Affix affixes}.</p>
 * <p>This class is extended by the {@link com.itcag.rockwell.tagger.util.InitialValidator InitialValidator} and {@link com.itcag.rockwell.tagger.util.ContinuationValidator ContinuationValidator} classes.
 */
public class Validator {
    
    protected final Patterns patterns;
    protected final Debugger debugger;
    
    /**
     * @param patterns Instance of the {@link com.itcag.rockwell.tagger.patterns.Patterns Patterns} class containing all applicable patterns.
     * @param debugger Instance of the {@link com.itcag.rockwell.tagger.debug.Debugger Debugger} class use for debugging only.
     * @throws Exception if anything goes wrong.
     */
    public Validator(Patterns patterns, Debugger debugger) throws Exception {
        this.patterns = patterns;
        this.debugger = debugger;
    }
    
    protected boolean validateAdditionalConditions(ConditionElement conditionElement, Token token) throws Exception {
        for (MatchingSpecification additional : conditionElement.getAdditionalSpecifications()) {
            switch (additional.getAspect()) {
                case VERBATIM:
                    this.debugger.print(token.getWord() + " <-> " + additional.getValue());
                    if (!token.getWord().equals(additional.getValue())) return false;
                    break;
                case CAIN:
                    this.debugger.print(token.getCain() + " <-> " + additional.getValue());
                    if (!token.getCain().equals(additional.getValue())) return false;
                    break;
                case LEMMA:
                    this.debugger.print(token.getLemma() + " <-> " + additional.getValue());
                    if (!token.getLemma().equals(additional.getValue())) return false;
                    break;
                case POS:
                    this.debugger.print(token.getPos() + " <-> " + additional.getValue());
                    if (!token.getPos().name().equals(additional.getValue())) return false;
                    break;
                case TYPE:
                    this.debugger.print(token.getType() + " <-> " + additional.getValue());
                    if (!token.getType().name().equals(additional.getValue())) return false;
                    break;
                case ROLE:
                    if (!(token instanceof Semtoken)) return false;
                    Semtoken semtoken = (Semtoken) token;
                    this.debugger.print(semtoken.getRoles().contains(additional.getValue().toLowerCase()) + ": " + additional.getValue());
                    if (!semtoken.getRoles().contains(additional.getValue().toLowerCase())) return false;
                    break;
            }
        }
        return true;
    }
    
    protected boolean isRejectedConditionElement(ConditionElement conditionElement, Token token) throws Exception {
        
        if (conditionElement.getRejects().isEmpty()) return false;
        
        for (MatchingSpecification reject : conditionElement.getRejects()) {
            switch (reject.getAspect()) {
                case VERBATIM:
                    this.debugger.print(token.getWord() + " <-> " + reject.getValue());
                    if (token.getWord().equals(reject.getValue())) return true;
                    break;
                case CAIN:
                    this.debugger.print(token.getCain() + " <-> " + reject.getValue());
                    if (token.getCain().equals(reject.getValue())) return true;
                    break;
                case LEMMA:
                    this.debugger.print(token.getLemma() + " <-> " + reject.getValue());
                    if (token.getLemma().equals(reject.getValue())) return true;
                    break;
                case POS:
                    this.debugger.print(token.getPos() + " <-> " + reject.getValue());
                    if (token.getPos().name().equals(reject.getValue())) return true;
                    break;
                case TYPE:
                    this.debugger.print(token.getType() + " <-> " + reject.getValue());
                    if (token.getType().name().equals(reject.getValue())) return true;
                    break;
                case ROLE:
                    if (!(token instanceof Semtoken)) return false;
                    Semtoken semtoken = (Semtoken) token;
                    this.debugger.print(semtoken.getRoles().contains(reject.getValue().toLowerCase()) + ": " + reject.getValue());
                    if (semtoken.getRoles().contains(reject.getValue().toLowerCase())) return true;
                    break;
            }
        }
        
        return false;
    
    }
    
    protected ArrayList<Token> getPrefixList(ArrayList<? extends Token> tokens, Token anchor, boolean include) {
        
        ArrayList<Token> retVal = new ArrayList<>();
        
        int first = 0;
        if (anchor.getIndex() > Conditions.MAX_OPTIONAL) first = anchor.getIndex() - Conditions.MAX_OPTIONAL;
        
        for (Token token : tokens) {
            if (token.getIndex() >= first && token.getIndex() < anchor.getIndex()) {
                retVal.add(token);
            } else if (Objects.equals(token.getIndex(), anchor.getIndex())) {
                if (include) retVal.add(token);
                break;
            }
        }
        
        return retVal;
        
    }
    
    protected ArrayList<Token> getInfixList(ArrayList<? extends Token> tokens, TreeMap<Integer, Match> matches, Token anchor, boolean include) {
        
        ArrayList<Token> retVal = new ArrayList<>();
        
        Integer last = null;
        for (Map.Entry<Integer, Match> entry : matches.entrySet()) {
            Match match = entry.getValue();
            /**
             * Infix could be preceded by a quodlibet element,
             * which would imply that some matches were collected by that element,
             * and not by the infix element.
             * Therefore, these matches should not be included in the infix list.
             * Accordingly, when a match is encountered that is not quodlibet,
             * the list is cleared, because all the previous matches
             * must have been collected by the preceding quodlibet,
             * and shouldn't be included.
             */
            if (match.isQuodlibet()) {
                /**
                 * Leave it as is!
                 * If a token has alternative lemmas,
                 * the corresponding match will have only one of them,
                 * while others might be required to complete the validation.
                 * Therefore, instead of matches, the original tokens
                 * (with all alternative lemmas) must be used.
                 */
                for (Token token : tokens) {
                    if (Objects.equals(token.getIndex(), match.getIndex())) {
                        retVal.add(token);
                        last = token.getIndex();
                    }
                }
            } else if (last != null) {
                retVal.clear();
            }
        }
        
        if (include) retVal.add(anchor);
        
        return retVal;
        
    }
    
    protected ArrayList<Token> getSuffixList(ArrayList<? extends Token> tokens, Token anchor, boolean include) {
        
        ArrayList<Token> retVal = new ArrayList<>();
        
        int count = 0;
        for (Token token : tokens) {
            if (Objects.equals(token.getIndex(), anchor.getIndex())) {
                if (include) {
                    count++;
                    retVal.add(token);
                }
            } else if (token.getIndex() > anchor.getIndex()) {
                retVal.add(token);
                count++;
            }
            if (count >= Conditions.MAX_OPTIONAL) break;
        }
        
        return retVal;
        
    }
    
    protected void print(String msg) {
        System.out.println(msg);
    }
    
}
