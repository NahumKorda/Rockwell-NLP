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

package com.itcag.rockwell.tokenizer;

import com.itcag.rockwell.tokenizer.res.Toklex;
import com.itcag.rockwell.tokenizer.res.Misspellings;
import com.itcag.util.punct.Locker;
import com.itcag.util.punct.PunctuationToolbox;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * <p>This class splits text into individual strings. Punctuation is detached, but punctuation characters that are not used for punctuation (dual purpose characters) are recognized and left intact (e.g., in decimal numbers, URLs, acronyms, etc.).</p>
 */
public final class Tokenizer {
    
    private final Locker locker;
    
    private final Toklex toklex;
    private final Misspellings misspellings;

    public Tokenizer() throws Exception {
        this.locker = new Locker();
        this.toklex = Toklex.getInstance();
        this.misspellings = Misspellings.getInstance();
    }
    
    /**
     * @param sentence String builder holding a sentence.
     * @return Array list of strings - each representing a token.
     * @throws java.lang.Exception if anything goes wrong.
     */
    public final synchronized ArrayList<String> getTokens(StringBuilder sentence) throws Exception {
        
        ArrayList<String> retVal = new ArrayList<>();
        
        this.locker.lock(sentence);
        
        ArrayList<String> tokens = tokenize(sentence);
        boolean quote = false;
        for (String token : tokens) {
            
            /**
             * Release all locks except abbreviations and acronyms.
             * They are required for lemmatization.
             */
            token = this.locker.unlock(token);
            
            if (misspellings.contains(token)) token = misspellings.getReplacement(token);
            
            if (toklex.isRecognized(token.toLowerCase())) {
                retVal.addAll(toklex.getReplacement(token.toLowerCase()));
            } else if (token.endsWith("'s")) {
                String tmp = token.substring(0, token.length() - 2);
                if (!tmp.isEmpty()) retVal.add(tmp);
                retVal.add("'s");
            } else if (token.endsWith("s'")) {
                retVal.add(token.substring(0, token.length() - 1));
                retVal.add("'");
            } else if (token.startsWith("'")) {
                String tmp = token.substring(1, token.length());
                if (tmp.isEmpty()) continue;
                if (tmp.endsWith("'")) {
                    tmp = tmp.substring(0, tmp.length() - 1);
                    retVal.add(tmp);
                } else {
                    retVal.add(tmp);
                    quote = true;
                }
            } else if (token.endsWith("'") && quote) {
                String tmp = token.substring(0, token.length() - 1);
                if (tmp.isEmpty()) continue;
                retVal.add(tmp);
                quote = false;
            } else {
                retVal.add(token);
            }
        
        }
        
        return retVal;
    
    }
    
    private ArrayList<String> tokenize(StringBuilder input) {
        
        TextToolbox.fixEmptySpaces(input);
        
        if (input.length() == 0) return new ArrayList<>();

        String[] tokens = input.toString().split(" ");
        
        ArrayList<String> retVal = removeEmpty(tokens);
        retVal = sortOutFromEnd(retVal);
        retVal = sortOutFromStart(retVal);
        
        return retVal;
        
    }

    private ArrayList<String> removeEmpty(String[] tokens) {
        ArrayList<String> retVal = new ArrayList<>();
        for (String token : tokens) {
            if (!TextToolbox.isReallyEmpty(token)) retVal.add(token);
        }
        return retVal;
    }
    
    private ArrayList<String> sortOutFromEnd(ArrayList<String> tokens) {
        ArrayList<String> retVal = new ArrayList<>();
        for (String token : tokens) {
            retVal.addAll(resolveFromEnd(token));
        }
        return retVal;
    }
    
    private LinkedList<String> resolveFromEnd(String token) {
        
        LinkedList<String> retVal = new LinkedList<>();

        boolean found;

        do {

            found = false;

            if (PunctuationToolbox.isPunctuation(token.charAt(token.length() - 1))) {
                retVal.addFirst(token.substring(token.length() - 1));
                token = token.substring(0, token.length() - 1);
                if (token.isEmpty()) break;
                /**
                 * Token could be an acronym that is the last word in a sentence.
                 * In such case an additional period was appended to it.
                 * This additional period was now stripped.
                 * However, the period delimiting the acronym should stay
                 * as the part of the token.
                 */
                if (token.charAt(token.length() - 1) == 46) break;
                found = true;
            } else if (token.endsWith(")") || token.endsWith("]") || token.endsWith("}")) {
                /**
                 * Separate parentheses from the last word.
                 */
                retVal.addFirst(token.substring(token.length() - 1));
                token = token.substring(0, token.length() - 1);
                if (token.isEmpty()) break;
                found = true;
            }
            
        } while (found);

        if (!TextToolbox.isEmpty(token)) retVal.addFirst(token);

        return retVal;
        
    }

    private ArrayList<String> sortOutFromStart(ArrayList<String> tokens) {
        ArrayList<String> retVal = new ArrayList<>();
        for (String token : tokens) {
            retVal.addAll(resolveFromStart(token));
        }
        return retVal;
    }
    
    private LinkedList<String> resolveFromStart(String token) {
        
        LinkedList<String> retVal = new LinkedList<>();

        boolean found;

        do {

            found = false;

            if (token.startsWith("(") || token.startsWith("[") || token.startsWith("{")) {
                /**
                 * Separate parentheses from the first word.
                 */
                retVal.addLast(token.substring(0, 1));
                token = token.substring(1);
                if (token.isEmpty()) break;
                found = true;
            }
            
        } while (found);

        if (!TextToolbox.isEmpty(token)) retVal.addLast(token);

        return retVal;
        
    }

}
