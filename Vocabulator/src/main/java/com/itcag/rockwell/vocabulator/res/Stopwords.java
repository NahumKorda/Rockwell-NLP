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

package com.itcag.rockwell.vocabulator.res;

import java.util.HashSet;

/**
 * <p>This class loads and stores stop words. Word extraction can be instructed to ignore these words.</p>
 * <p>This class is implemented as singleton to avoid reloading lexical resources.</p>
 */
public class Stopwords {

    private static volatile Stopwords instance = null;
    
    /**
     * This method is designed to throw an exception if lexical resources cannot be loaded, and therefore the double checked locking is necessary.
     * @return Instance of this class.
     * @throws Exception If anything goes wrong.
     */
    public static Stopwords getInstance() throws Exception {
        if (instance == null) instance = new Stopwords();
        return instance;
    }
    
    private final HashSet<String> index = new HashSet<>();
    
    private Stopwords() throws Exception {
        
        Loader loader = new Loader();
        for (String stopword : loader.load("stopwords")) {
            this.index.add(stopword);
        }
        
    }
    
    /**
     * Evaluates whether a word is a stop word.
     * @param word String holding the word to be evaluated.
     * @return Boolean indicating whether the word is a stop word or not.
     */
    public boolean isStopword(String word) {
        return this.index.contains(word);
    }
    
}
