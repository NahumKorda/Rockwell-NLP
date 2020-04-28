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

package com.itcag.rockwell.semantex.ner;

import com.itcag.rockwell.POSTag;
import com.itcag.rockwell.lang.Semtoken;
import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;

import java.util.ArrayList;

/**
 * <p>This class validates extracted named entities that require additional validation after they were extracted by applying Rockwell expressions.</p>
 */
public class Validator {
    
    public static Tag validatePersonalName(Tag tag, ArrayList<Token> tokens) {
        
        if (tokens.size() < 2) return null;
        
        for (Token token : tokens) {
            
            if (!Character.isUpperCase(token.getWord().charAt(0))) return null;
            
            /**
             * Do not allow acronyms in personal names,
             * but allow single capitalized letters (initials).
             */
            if (token.getWord().length() > 1) {
                /**
                 * Allow initial followed by a dot.
                 */
                if (!(token.getWord().length() == 2 && token.getWord().charAt(1) == 46)) {
                    if (token.getWord().equals(token.getWord().toUpperCase())) return null;
                }
            }
            
        }
        
        return validateFirstToken(tag, tokens);
        
    }
    
    public static Tag validateCorporateName(Tag tag, ArrayList<Token> tokens) {
        
        if (tokens.size() < 2) return null;
        
        for (Token token : tokens) {
            if (!Character.isUpperCase(token.getWord().charAt(0))) return null;
        }
        
        return validateFirstToken(tag, tokens);
        
    }
    
    private static Tag validateFirstToken(Tag tag, ArrayList<Token> tokens) {
        
        /**
         * If the first token is the first token in the sentence,
         * and it is neither a nominal, nor a personal name,
         * then it is probably just a capitalized noun.
         */
        if (tag.getStart() == 0) {
            Token token = tokens.get(0);
            if (POSTag.NP0.equals(token.getPos())) return tag;
            if (POSTag.XXX.equals(token.getPos())) return tag;
            if (token instanceof Semtoken) {
                Semtoken tmp = (Semtoken) token;
                if (!tmp.getRoles().isEmpty()) return tag;
            }
            Tag retVal = new Tag(tag.getTag(), tag.getScript(), tag.getStart() + 1, tag.getEnd());
            if (retVal.getEnd() - retVal.getStart() < 2) return null;
            return retVal;
        }
        
        return tag;
    
    }
    
}
