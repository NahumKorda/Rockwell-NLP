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

import com.itcag.rockwell.vocabulator.phraser.Phraser;
import com.itcag.rockwell.vocabulator.word.LemmaExtractor;
import com.itcag.rockwell.vocabulator.word.SingleWordExtractor;

import java.util.ArrayList;
import java.util.Properties;
import java.util.TreeMap;

/**
 * <p>This is the main class that provides access to the verb/phrase extraction functionality.</p>
 */
public final class VocabularyExtractor implements Vocabulator {
    
    /**
     * Enumerates all word/phrase extracting tasks.
     */
    public enum Tasks {
        /** Extracts frequently occurring single words as they occur in text. */
        EXTRACT_SINGLE_WORDS,
        /** Extracts frequently occurring lemmas, so that inflected words with identical head word are grouped together. */
        EXTRACT_LEMMAS,
        /** Extracts frequently occurring multi-word phrases. */
        EXTRACT_PHRASES
    }
    
    private final Tasks currentTask;
    
    private final SingleWordExtractor singleWordExtractor;
    private final LemmaExtractor lemmaExtractor;
    private final Phraser phraser;

    /**
     * VocabularyExtractor is initiated by providing processing instructions to it. These processing instructions are described in the {@link PropertyFields} enum.
     * @param properties Instance of Java {@link java.util.Properties Properties} class holding the processing instructions.
     * @throws Exception if anything goes wrong.
     */
    public VocabularyExtractor(Properties properties) throws Exception {
        
        this.currentTask = Tasks.valueOf(properties.getProperty(PropertyFields.TASK.getField(), null));
        if (this.currentTask == null) throw new IllegalArgumentException("No task was specified and the vocabulary extractor cannot be created without it.");
        
        switch (this.currentTask) {
            case EXTRACT_SINGLE_WORDS:
                this.singleWordExtractor = new SingleWordExtractor(properties);
                this.lemmaExtractor = null;
                this.phraser = null;
                break;
            case EXTRACT_LEMMAS:
                this.singleWordExtractor = null;
                this.lemmaExtractor = new LemmaExtractor(properties);
                this.phraser = null;
                break;
            case EXTRACT_PHRASES:
            default:
                this.singleWordExtractor = null;
                this.lemmaExtractor = null;
                this.phraser = new Phraser(properties);
                break;
        }
    
    }
    
    /**
     * This method processes a single text item. To process an entire corpus, text items must be fed one by one.
     * @param text String holding a text item to be processed.
     * @throws Exception if anything goes wrong.
     */
    @Override
    public final void process(String text) throws Exception {
        switch (this.currentTask) {
            case EXTRACT_SINGLE_WORDS:
                this.singleWordExtractor.process(text);
                break;
            case EXTRACT_LEMMAS:
                this.lemmaExtractor.process(text);
                break;
            case EXTRACT_PHRASES:
                this.phraser.process(text);
                break;
        }
    }
    
    /**
     * This method removes all accumulated extracted words/phrases with very low frequency of occurrence.Following the <a href="https://en.wikipedia.org/wiki/Zipf%27s_law"target="_blank">Zipf's law</a> most of the encountered words/phrases are bound to have very low frequency of occurrence. Nonetheless these words/phrases are kept in hash maps, and could simply overwhelm the available memory resources.
     * @throws java.lang.Exception
     */
    @Override
    public final void trim() throws Exception {
        switch (this.currentTask) {
            case EXTRACT_SINGLE_WORDS:
                this.singleWordExtractor.trim();
                break;
            case EXTRACT_LEMMAS:
                this.lemmaExtractor.trim();
                break;
            case EXTRACT_PHRASES:
                this.phraser.trim();
                break;
        }
    }
    
    @Override
    public final int indexSize() throws Exception {
        switch (this.currentTask) {
            case EXTRACT_SINGLE_WORDS:
                return this.singleWordExtractor.indexSize();
            case EXTRACT_LEMMAS:
                return this.lemmaExtractor.indexSize();
            case EXTRACT_PHRASES:
                return this.phraser.indexSize();
            default:
                return 0;
        }
    }
    
    @Override
    public int count() throws Exception {
        switch (this.currentTask) {
            case EXTRACT_SINGLE_WORDS:
                return this.singleWordExtractor.count();
            case EXTRACT_LEMMAS:
                return this.lemmaExtractor.count();
            case EXTRACT_PHRASES:
                return this.phraser.count();
            default:
                return 0;
        }
    }

    /**
     * This method provides access to the accumulated words/phrases.
     * @return Tree map containing the accumulated words/phrases sorted descending by their frequency of occurrence.
     * @throws Exception if anything goes wrong.
     */
    @Override
    public final TreeMap<Integer, ArrayList<Term>> getResults() throws Exception {
        switch (this.currentTask) {
            case EXTRACT_SINGLE_WORDS:
                return this.singleWordExtractor.getResults();
            case EXTRACT_LEMMAS:
                return this.lemmaExtractor.getResults();
            case EXTRACT_PHRASES:
                return this.phraser.getResults();
            default:
                return new TreeMap<>();
        }
    }
    
    /**
     * This method is used only for debugging.
     * @param includeSentences Boolean indicating whether to list only words/phrases, or also the sentences in which they occurred.
     * @throws Exception if anything goes wrong.
     */
    @Override
    public final void print(boolean includeSentences) throws Exception {
        switch (this.currentTask) {
            case EXTRACT_SINGLE_WORDS:
                this.singleWordExtractor.print(includeSentences);
                break;
            case EXTRACT_LEMMAS:
                this.lemmaExtractor.print(includeSentences);
                break;
            case EXTRACT_PHRASES:
                this.phraser.print(includeSentences);
                break;
        }
    }
    
}
