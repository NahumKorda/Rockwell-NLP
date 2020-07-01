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
 * <p>This class loads and stores a list of most frequently encountered top-level Internet domains.</p>
 * <p>This class is implemented as singleton to avoid reloading lexical resources.</p>
 */
public final class Domains {
    
    private static volatile Domains instance = null;
    
    /**
     * This method is designed to throw an exception if lexical resources cannot be loaded, and therefore the double checked locking is necessary.
     * @return Instance of this class.
     * @throws Exception If anything goes wrong.
     */
    public static Domains getInstance() throws Exception {
        if (instance == null) {
            synchronized(Domains.class) {
                if (instance == null) {
                    instance = new Domains();
                }
            }
        }
        return instance;
    } 
    
    private final ArrayList<String> domains;

    private Domains() throws Exception {
        Loader loader = new Loader();
        this.domains = loader.load("domains");
    }

    /**
     * This method "locks" names with a top-level Internet domain as an integral part by replacing period characters in it with non-printable characters. This ensures that the abbreviation will be treated as single words during splitting and tokenizing. 
     * @param input String builder holding text.
     */
    public final synchronized void lock(StringBuilder input) {
              
        int start = input.indexOf(".");
        if (start == -1) return;
        
        while (start > -1) {
            
            int end = input.indexOf(" ", start);
            if (end == -1) end = input.length() - 1;
            
            if (start == end) return;

            String test = input.substring(start, end).trim();

            while (test.endsWith(".")) {
                test = test.substring(0, test.length() - 1);
                if (test.isEmpty()) return;
            }
            
            if (this.domains.contains(test.toLowerCase())) {
                replace(input, start, end);
            }
            
            start = input.indexOf(".", start + 1);

        }
        
    }
    
    private void replace(StringBuilder input, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (input.charAt(i) == 46) {
                input.replace(i, i + 1, Characters.DOMAIN.getReplacement());
            }
        }
    }

    /**
     * This method "unlocks" locked text by replacing the inserted non-printable characters with the periods.
     * @param input String builder holding text.
     */
    public final synchronized void unlock(StringBuilder input) {
        TextToolbox.replaceCaIn(input, Characters.DOMAIN.getReplacement(), ".");
    }

    /**
     * This method "unlocks" locked text by replacing the inserted non-printable characters with the periods.
     * @param input String holding text.
     * @return Unlocked text.
     */
    public final synchronized String unlock(String input) {
        return TextToolbox.replaceCaIn(input, Characters.DOMAIN.getReplacement(), ".");
    }
    
}
