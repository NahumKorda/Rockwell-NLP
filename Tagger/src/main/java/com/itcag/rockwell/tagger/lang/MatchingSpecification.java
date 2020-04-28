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

package com.itcag.rockwell.tagger.lang;

import com.itcag.util.txt.TextToolbox;

/**
 * <p>This class holds a constituent of a {@link com.itcag.rockwell.tagger.lang.ConditionElement condition element}.</p>
 */
public class MatchingSpecification {

    /**
     * Enumerates all possible aspects of a token that could be matched.
     */
    public enum Aspect {
        /** The original word as it occurs in a sentence. */
        VERBATIM,
        /** CAse INsensitive match of the original word. */
        CAIN,
        /** Canonical (lexicon) representation of the original word. */
        LEMMA,
        /** Part of speech of the original word. */
        POS,
        /** Class of the part of speech. */
        TYPE,
        /** Signals that any token is accepted as match. */
        QUODLIBET,
        /** Affix containing tokens from the beginning of the sentence to the anchor token. */
        PREFIX,
        /** Affix containing all tokens from the last matched token to the anchor token. */
        INFIX,
        /** Affix containing all tokens from the anchor token to the end of the sentence. */
        SUFFIX,
        /** Meaning assigned to an instance of the {@link com.itcag.rockwell.lang.Semtoken Semtoken} class. */
        ROLE,
    }

    public final static String KEY_DELIMITER = "|";

    protected final ConditionElement.Aspect aspect;
    protected final String value;

    /**
     * @param aspect Value of the {@link com.itcag.rockwell.tagger.lang.MatchingSpecification.Aspect Aspect} enum that specifies which aspect of a token must be matched.
     * @param value String holding the value that must be matched. This value depends on the aspect.
     */
    public MatchingSpecification(ConditionElement.Aspect aspect, String value) {
        this.aspect = aspect;
        this.value = value;
    }

    /**
     * @return Value of the {@link com.itcag.rockwell.tagger.lang.MatchingSpecification.Aspect Aspect} enum that specifies which aspect of a token must be matched.
     */
    public ConditionElement.Aspect getAspect() {
        return aspect;
    }

    /**
     * @return String holding the value that must be matched. This value depends on the aspect.
     */
    public String getValue() {
        return value;
    }

    /**
     * @return String consisting of the aspect and the value combined with a delimiter character. Key is used by the finite state automaton for fast matching.
     */
    public String getKey() {
        return aspect + KEY_DELIMITER + value;
    }

    @Override
    public String toString() {
        return toString(0);
    }
    
    public String toString(int indentation) {
        
        StringBuilder retVal = new StringBuilder();
        
        String indent = TextToolbox.repeat(indentation, "\t");
        retVal.append(indent).append(getKey());
        
        return retVal.toString();
        
    }
    
}
