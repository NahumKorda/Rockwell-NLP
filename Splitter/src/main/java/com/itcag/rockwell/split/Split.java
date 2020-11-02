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

package com.itcag.rockwell.split;

import com.itcag.util.punct.PunctuationToolbox;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;

/**
 * <p>This class splits text into individual sentences, after the use of all dual purpose characters was "locked".</p>
 */
public final class Split {
    
    private final boolean extended;
    
    public Split(boolean extended) {
        this.extended = extended;
    }
    
    /**
     * @param input String builder holding the original text.
     * @return Array list containing string builders holding individual sentences.
     */
    public final ArrayList<StringBuilder> split(StringBuilder input) {

        TextToolbox.trim(input);
        
        if (TextToolbox.isEmpty(input)) return new ArrayList<>();

        ArrayList<StringBuilder> retVal = new ArrayList<>();

        StringBuilder buffer = new StringBuilder();
        StringBuilder lastWord = new StringBuilder();
        
        char p;
        char n;
        
        int i = 0;
        while (i < input.length()) {

            char c = input.charAt(i);

            if (i > 0) {
                p = input.charAt(i - 1);
            } else {
                p = 0;
            }

            if ((i + 1) < input.length()) {
                n = input.charAt(i + 1);
            } else {
                n = 0;
            }

            switch (c) {
                case 32:
                    
                    /*
                     * First character is empty space - ignore it.
                     */
                    if (p == 0) continue;
                    
                    /*
                     * Last character is empty space - ignore it.
                     * If buffer is not empty,
                     * it will be inserted into the return list by default.
                     */
                    if (n == 0) continue;
                    
                    if (isEndOfSentence(p, lastWord) && buffer.length() > 0) {
                        retVal.add(buffer);
                        buffer = new StringBuilder();
                    } else {
                        buffer.append(c);
                    }
                    
                    lastWord = new StringBuilder();

                    break;

                case 9:
                case 10:
                case 13:
                    /*
                     * Split on line break.
                     */
                    if (buffer.length() > 0) {
                        retVal.add(buffer);
                        buffer = new StringBuilder();
                        lastWord = new StringBuilder();
                    }
                    break;
                default:
                    buffer.append(c);
                    lastWord.append(c);
                    break;
            }
             
            i++;
            
        }

        if (buffer.length() > 0) retVal.add(buffer);
        
        return retVal;
    
    }
    
    private boolean isEndOfSentence(char p, StringBuilder lastWord) {

        /**
         * Do not split after a single letter word.
         */
        if (lastWord.length() == 1) return false;
        
        /**
         * A (single or double) quote could follow a punctuation character
         * (the end of quotation is also the end of that sentence).
         */
        if (p == 34 || p == 39) {
            /**
             * Check the character preceding the quote.
             */
            if (lastWord.length() > 1) p = lastWord.charAt(lastWord.length() - 2);
        }
        
        if (this.extended) {
            if (!PunctuationToolbox.isExtendedTerminalPunctuation(Character.toString(p))) return false;
        } else {
            if (!PunctuationToolbox.isTerminalPunctuation(Character.toString(p))) return false;
        }
        
        return true;
        
    }
    
}
