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

package com.itcag.util.punct;

import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;

/**
 * <p>This class loads and stores a list of most frequently encountered abbreviations.</p>
 * <p>This class is implemented as singleton to avoid reloading lexical resources.</p>
 */
public final class Abbreviations {
    
    private static volatile Abbreviations instance = null;
    
    /**
     * This method is designed to throw an exception if lexical resources cannot be loaded, and therefore the double checked locking is necessary.
     * @return Instance of this class.
     * @throws Exception If anything goes wrong.
     */
    public static Abbreviations getInstance() throws Exception {
        if (instance == null) {
            synchronized(Abbreviations.class) {
                if (instance == null) {
                    instance = new Abbreviations();
                }
            }
        }
        return instance;
    } 
    
    private final ArrayList<String> abbreviations;

    private Abbreviations() throws Exception {
        Loader loader = new Loader();
        this.abbreviations = loader.load("abbreviations");
    }

    /**
     * This method "locks" abbreviations by replacing period characters in it with non-printable characters. This ensures that the abbreviation will be treated as single words during splitting and tokenizing. 
     * @param input String builder holding text.
     */
    public final synchronized void lock(StringBuilder input) {
        
        input.append(" ");
        
        int end = input.indexOf(". ");
        if (end == -1) return;

        while (end > -1) {
            
            int start = input.lastIndexOf(" ", end);
            if (start == -1) start = 0;

            String test = input.substring(start, end + 1).trim();

            if (this.abbreviations.contains(test.toLowerCase())) {
                replace(input, start, end);
            }
            
            end = input.indexOf(". ", end + 1);

        }
        
    }
    
    private void replace(StringBuilder input, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (input.charAt(i) == 46) {
                input.replace(i, i + 1, Punctuation.ABBREVIATION_PERIOD);
            }
        }
    }

    /**
     * This method "unlocks" locked text by replacing the inserted non-printable characters with the periods.
     * @param input String builder holding text.
     */
    public final synchronized void unlock(StringBuilder input) {
        TextToolbox.replaceCaIn(input, Punctuation.ABBREVIATION_PERIOD, ".");
    }

    /**
     * This method "unlocks" locked text by replacing the inserted non-printable characters with the periods.
     * @param input String holding text.
     * @return Unlocked text.
     */
    public final synchronized String unlock(String input) {
        return TextToolbox.replaceCaIn(input, Punctuation.ABBREVIATION_PERIOD, ".");
    }
    
}
