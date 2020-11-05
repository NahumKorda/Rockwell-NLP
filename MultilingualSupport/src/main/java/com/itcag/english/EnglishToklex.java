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

package com.itcag.english;

import com.itcag.multilingual.Loader;
import com.itcag.multilingual.Toklex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * <p>This class loads and stores a list of English contractions and their complete forms.</p>
 * <p>This class is implemented as singleton to avoid reloading lexical resources.</p>
 */
public final class EnglishToklex implements Toklex {
    
    private static volatile EnglishToklex instance = null;
    
    /**
     * This method is designed to throw an exception if lexical resources cannot be loaded, and therefore the double checked locking is necessary.
     * @return Instance of this class.
     * @throws Exception If anything goes wrong.
     */
    public static synchronized EnglishToklex getInstance() throws Exception {
        if (instance == null) {
            synchronized(EnglishToklex.class) {
                if (instance == null) {
                    instance = new EnglishToklex();
                }
            }
        }
        return instance;
    } 
    
    private final HashMap<String, ArrayList<String>> index = new HashMap<>();
    
    private EnglishToklex() throws Exception {

        Loader loader = new Loader();
        ArrayList<String> items = loader.load("toklex");

        items.stream().forEach((item) -> {
            String[] elts = item.split("\t");
            String[] subelts = elts[1].trim().split(" ");
            ArrayList<String> tmp = new ArrayList<>(Arrays.asList(subelts));
            index.put(elts[0].trim(), tmp);
        });

    }
   
    /**
     * @param word String holding a word.
     * @return Boolean indicating whether the word is recognized as a contraction.
     */
    @Override
    public final synchronized boolean isRecognized(String word) {
        return (index.containsKey(word)); 
    }
    
    /**
     * @param word String holding a word.
     * @return Array list containing alternative complete forms for the word, or null if the word is not recognized.
     */
    @Override
    public final synchronized ArrayList<String> getReplacement(String word) {
        return index.get(word);
    }
    
}
