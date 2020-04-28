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

package com.itcag.rockwell.lang;

import java.util.ArrayList;

/**
 * <p>Rockwell is unique in that it does not determine categorically part of speech for every token, as other NLP platforms do. In the case that the part of speech for a token is ambiguous, all alternative interpretations are included in the corresponding token. The correct alternative is determined only when the token is matched by a Rockwell expression that identifies the specific word construct in which the ambiguity is eliminated.</p>
 * <p>This class holds alternative interpretations of a particular word in the lexicon.</p>
 */
public class Alternatives {
    
    private final ArrayList<Token> alternatives;
    
    /**
     * @param alternatives Array list containing alternative interpretations (part of speech and lemma pairs) of a word.
     */
    public Alternatives(ArrayList<Token> alternatives) {
        this.alternatives = alternatives;
    }

    /**
     * 
     * @return Array list containing alternative interpretations (part of speech and lemma pairs) of a word.
     */
    public ArrayList<Token> getAlternatives() {
        return this.alternatives;
    }
    
    @Override
    public String toString() {
        
        if (alternatives == null) return "";
        
        StringBuilder retVal = new StringBuilder();
        
        alternatives.stream().forEach((alternative) -> {
            retVal.append(alternative.toString()).append("\n");
        });
        
        return retVal.toString();
        
    }
    
}
