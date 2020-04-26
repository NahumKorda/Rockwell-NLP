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

package com.itcag.rockwell.tokenizer.res;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>This class loads and stores a list of most frequently encountered misspellings, and their corrections.</p.>
 * <p>This class is implemented as singleton to avoid reloading lexical resources.</p>
 */
public final class Misspellings {

    private static volatile Misspellings instance = null;
    
    /**
     * This method is designed to throw an exception if lexical resources cannot be loaded, and therefore the double checked locking is necessary.
     * @return Instance of this class.
     * @throws Exception If anything goes wrong.
     */
    public static synchronized Misspellings getInstance() throws Exception {
        if (instance == null) {
            synchronized(Misspellings.class) {
                if (instance == null) {
                    instance = new Misspellings();
                }
            }
        }
        return instance;
    } 
    
    private static final String DELIMITER = ">>>";
    
    private final HashMap<String, String> index = new HashMap<>();
    
    private Misspellings() throws Exception {
        
        Loader loader = new Loader();
        ArrayList<String> items = loader.load("misspellings");

        items.stream().forEach((item) -> {
            /*
             * word >>> replacement
             */
            String[] elts = item.split(DELIMITER);
            String word = elts[0].trim();
            String replacement = elts[1].trim();
            index.put(word, replacement);
        });

    }

    /**
     * @return Hash map containing misspellings and their corrections.
     */
    public final synchronized HashMap<String, String> getIndex() {
        return index;
    }
    
    /**
     * @param word String holding a word.
     * @return Boolean indicating whether the word is recognized as a misspelling.
     */
    public final synchronized boolean contains(String word) {
        return index.containsKey(word);
    }
    
    /**
     * @param word String holding a word.
     * @return String holding the correction of the misspelled word, or null if the word is not recognized.
     */
    public final synchronized String getReplacement(String word) {
        return index.get(word);
    }
    
}
