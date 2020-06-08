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

import com.itcag.util.punct.Locker;
import com.itcag.util.punct.Punctuation;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>This class splits text into sentences.</p>
 * <p>Splitting recognizes the use of punctuation characters for purposes other than punctuation (dual purpose characters). For example, use of a period in decimal numbers, URLs, acronyms, etc.</p>
 */
public final class Splitter {

    /**
     * @param input String builder holding the original text.
     * @return Array list containing string builders holding individual sentences.
     * @throws Exception if anything goes wrong.
     */
    public final ArrayList<StringBuilder> split(StringBuilder input) throws Exception {
        
        if (TextToolbox.isEmpty(input)) throw new IllegalArgumentException("Input is empty.");

        /**
         * Standardize Unicode.
         */
        int originalLength = input.length();
        Unicode unicode = new Unicode();
        unicode.standardize(input);
        TextToolbox.trim(input);
        if (input.length() == 0) throw new IllegalArgumentException("Input is not in Latin alphabet.");
        if (input.length() < 0.5 * originalLength) throw new IllegalArgumentException("Input is mostly not in Latin alphabet.");
        if (TextToolbox.isEmpty(input)) throw new IllegalArgumentException("Input is empty after Unicode standardization.");

        /**
         * Replace HTML special characters with the Unicode.
         * Remove HTML tags if any were left by the collection.
         * Break the text on tags that imply text display in a new line. 
         */
        HTMLCleaner html = new HTMLCleaner();
        html.clean(input);
        TextToolbox.trim(input);
        if (TextToolbox.isEmpty(input)) throw new IllegalArgumentException("Input is empty after cleaning.");
        
        /**
         * Lock URLs, abbreviations, acronyms, decimal numbers.
         */
        Locker locker = new Locker();
        locker.lock(input);

        /**
         * Normalize punctuation.
         */
        Punctuation.normalize(input);
        TextToolbox.trim(input);
        if (TextToolbox.isEmpty(input)) throw new IllegalArgumentException("Input is empty after punctuation normalization.");
        
        /**
         * Split into sentences.
         */
        Split split = new Split();
        ArrayList<StringBuilder> sentences = split.split(input);

        /**
         * Postprocess sentences.
         */
        Iterator<StringBuilder> sentenceIterator = sentences.iterator();
        while (sentenceIterator.hasNext()) {
            
            StringBuilder sentence = sentenceIterator.next();
            
            /**
             * Reinsert punctuation after the split.
             */
            locker.unlock(sentence);

            /**
             * Remove leftovers originating in erroneous punctuation.
             */
            Punctuation.removePunctuationAtBeginning(sentence);

            /**
             * Remove empty sentences.
             */
            TextToolbox.trim(sentence);
            if (sentence.length() == 0) {
                sentenceIterator.remove();
                continue;
            }

            /**
             * Trim extra spaces.
             */
            TextToolbox.trim(sentence);

        }
        
        return sentences;
        
    }

}
