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

import com.itcag.multilingual.Lexicon;
import com.itcag.multilingual.Loader;
import com.itcag.rockwell.POSTag;
import com.itcag.rockwell.lang.Alternatives;
import com.itcag.rockwell.lang.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * <p>This class loads and stores an English lexicon.</p>
 * <p>This class is implemented as singleton to avoid reloading lexical resources.</p>
 */
public final class EnglishLexicon implements Lexicon {
    
    private static volatile EnglishLexicon instance = null;
    
    /**
     * This method is designed to throw an exception if lexical resources cannot be loaded, and therefore the double checked locking is necessary.
     * @return Instance of this class.
     * @throws Exception If anything goes wrong.
     */
    public static synchronized EnglishLexicon getInstance() throws Exception {
        if (instance == null) {
            synchronized(EnglishLexicon.class) {
                if (instance == null) {
                    instance = new EnglishLexicon();
                }
            }
        }
        return instance;
    }
    
    /**
     * Key is word and value is a list of tokens holding alternative part of speech and lemma pairs for the word.
     */
    private final HashMap<String, ArrayList<Token>> index = new HashMap<>();
    
    private EnglishLexicon() throws Exception {
        
        Loader loader = new Loader();
        ArrayList<String> items = loader.load("english/lexicon");

        for (String item : items) {
            /**
             * word|POS|lemma
             */
            String[] elts = item.split("\\|");
            String word = elts[0].trim();
            String pos = elts[1].trim();
            String lemma = elts[2].trim();
            if (index.containsKey(word)) {
                index.get(word).add(new Token(word, POSTag.valueOf(pos), lemma));
            } else {
                index.put(word, new ArrayList<>(Arrays.asList(new Token(word, POSTag.valueOf(pos), lemma))));
            }
        }

    }
    
    /**
     * @return Hash map containing all words from the lexicon and their alternative part-of-speech interpretations.
     */
    @Override
    public final synchronized HashMap<String, ArrayList<Token>> getIndex() {
        return index;
    }
    
    /**
     * @param word String holding a word.
     * @return Boolean indication whether the word is in the lexicon or not.
     */
    @Override
    public final synchronized boolean isKnown(String word) {
        return index.containsKey(word);
    }
    
    /**
     * @param word String holding a word.
     * @return Instance of the {@link com.itcag.rockwell.lang.Alternatives Alternatives} class containing alternative part-of-speech interpretations for the word, or null if the word is not recognized.
     */
    @Override
    public final synchronized Alternatives getAlternatives(String word) {
        return new Alternatives(index.get(word));
    }

}
