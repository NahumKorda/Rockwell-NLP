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

package com.itcag.rockwell.vocabulator;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * <p>Vocabulator (VOCABULAry extracTOR) is the common Rockwell Java API for vocabulary extraction. It is implemented by the following classes:</p>
 * <ul>
 * <li>{@link com.itcag.rockwell.vocabulator.VocabularyExtractor VocabularyExtractor} is the main API class through which all other classes are operated.</li>
 * <li>{@link com.itcag.rockwell.vocabulator.word.SingleWordExtractor SingleWordExtractor} extracts frequently occurring single words.</li>
 * <li>{@link com.itcag.rockwell.vocabulator.word.LemmaExtractor LemmaExtractor} extracts frequently occurring lemmas.</li>
 * <li>{@link com.itcag.rockwell.vocabulator.phraser.Phraser Phraser} frequently occurring multi-word phrases.</li>
 * </ul>
 */
public interface Vocabulator {

    /**
     * This method processes a single text item. To process an entire corpus, text items must be fed one by one.
     * @param text String holding a text item to be processed.
     * @throws Exception if anything goes wrong.
     */
    public void process(String text) throws Exception;

    /**
     * This method removes all accumulated extracted words/phrases with very low frequency of occurrence. Following the <a href="https://en.wikipedia.org/wiki/Zipf%27s_law" target="_blank">Zipf's law</a> most of the encountered words/phrases are bound to have very low frequency of occurrence. Nonetheless these words/phrases are kept in hash maps, and could simply overwhelm the available memory resources.
     */
    public void trim() throws Exception;
    
    /**
     * @return Integer indicating the current size of the index containing the extracted phrases.
     * @throws Exception if anything goes wrong
     */
    public int indexSize() throws Exception;
    
    /**
     * @return Integer indicating the number of processed sentences.
     * @throws Exception if anything goes wrong
     */
    public int count() throws Exception;
    
    /**
     * This method provides access to the accumulated words/phrases.
     * @return Tree map containing the accumulated words/phrases sorted descending by their frequency of occurrence.
     * @throws Exception if anything goes wrong.
     */
    public TreeMap<Integer, ArrayList<Term>> getResults() throws Exception;

    /**
     * This method is used only for debugging.
     * @param includeSentences Boolean indicating whether to list only words/phrases, or also the sentences in which they occurred.
     * @throws Exception if anything goes wrong.
     */
    public void print(boolean includeSentences) throws Exception;

}
