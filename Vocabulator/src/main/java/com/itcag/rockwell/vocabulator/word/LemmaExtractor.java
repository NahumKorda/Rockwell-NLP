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

package com.itcag.rockwell.vocabulator.word;

import com.itcag.rockwell.vocabulator.PropertyFields;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.pipeline.Pipeline;
import com.itcag.util.Converter;
import com.itcag.rockwell.vocabulator.Term;
import com.itcag.rockwell.vocabulator.Vocabulator;
import com.itcag.rockwell.vocabulator.res.Synonyms;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * <p>Extracts lemmas, so that inflected words with identical head word are grouped together. Results are sorted descending by the frequency of occurrence.</p>
 */
public class LemmaExtractor implements Vocabulator {

    private final ArrayList<String> filter;

    private final int threshold;
    
    private final Pipeline pipeline;
    
    private final Validator validator;
    
    private final Synonyms synonyms;

    private final HashMap<String, Term> index = new HashMap<>();

    private int count = 0;

    /**
     * LemmaExtractor is initiated by providing processing instructions to it. These processing instructions are described in the {@link com.itcag.rockwell.vocabulator.PropertyFields} enum.
     * @param config Instance of Java {@link java.util.Properties Properties} class holding the processing instructions.
     * @throws Exception if anything goes wrong.
     */
    public LemmaExtractor(Properties config) throws Exception {

        this.filter = getFilter(config);
        
        Integer test = Converter.convertStringToInteger(config.getProperty(PropertyFields.WORD_THRESHOLD.getField()));
        this.threshold = (test == null) ? 0 : test;
        
        this.pipeline = getPipeline();
        
        this.validator = new Validator(config);
        
        this.synonyms = new Synonyms(config);
    
    }
    
    private ArrayList<String> getFilter(Properties config) throws Exception {
        
        if (!config.containsKey(PropertyFields.POSITIVE_FILTER.getField())) return null;

        String test = config.getProperty(PropertyFields.POSITIVE_FILTER.getField());
        if (TextToolbox.isReallyEmpty(test)) return null;
        
        String[] tmp = test.split(",");
        
        ArrayList<String> retVal = new ArrayList<>();
        
        for (String item : tmp) {
            item = item.trim();
            if (item.isEmpty()) continue;
            retVal.add(item);
        }
        
        return retVal;
        
    }
    
    private Pipeline getPipeline() throws Exception {
        Properties properties = new Properties();
        properties.put(com.itcag.rockwell.pipeline.PropertyFields.TASK.getField(), Pipeline.Tasks.LEMMATIZE.name());
        return new Pipeline(properties);
    }

    /**
     * This method processes a single text item. To process an entire corpus, text items must be fed one by one.
     * @param text String holding a text item to be processed.
     * @throws Exception if anything goes wrong.
     */
    @Override
    public void process(String text) throws Exception {
        
        this.count++;
        
        ArrayList<String> sentences = this.pipeline.split(text);
        for (String sentence : sentences) {
            if (sentence.length() == 0) continue;
            processSentence(sentence);
        }
        
    }
    
    private void processSentence(String sentence) throws Exception {

        if (sentence.length() == 0) return;

        for (ArrayList<Token> tokens : this.pipeline.lemmatize(sentence)) {
            
            for (Token token : tokens) {

                if (this.filter != null) {
                    if (token.getLemma() != null) {
                        if (!this.filter.contains(token.getLemma())) continue;
                    } else {
                        boolean found = false;
                        for (Token alternative : token.getAlternatives()) {
                            if (this.filter.contains(alternative.getLemma())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) continue;
                    }
                }

                if (!this.validator.isValidWord(token.getCain())) continue;

                for (String lemma : this.validator.getValidLemmas(token)) {
                    
                    lemma = this.synonyms.getWordSynonym(lemma);
                    
                    if (index.containsKey(lemma)) {
                        Term word = index.get(lemma);
                        word.incrementFOO();
                        word.addSentence(sentence);
                    } else {
                        Term word = new Term(lemma);
                        word.addSentence(sentence);
                        index.put(lemma, word);
                    }
                
                }

            }

        }
    
    }
   
    /**
     * This method removes all accumulated extracted words/phrases with very low frequency of occurrence.Following the <a href="https://en.wikipedia.org/wiki/Zipf%27s_law"target="_blank">Zipf's law</a> most of the encountered words/phrases are bound to have very low frequency of occurrence. Nonetheless these words/phrases are kept in hash maps, and could simply overwhelm the available memory resources.
     * @throws java.lang.Exception
     */
    @Override
    public final void trim() throws Exception {
        Iterator<Map.Entry<String, Term>> indexIterator = this.index.entrySet().iterator();
        while (indexIterator.hasNext()) {
            Map.Entry<String, Term> entry = indexIterator.next();
            if (entry.getValue().getFOO() < 10) {
                indexIterator.remove();
            }
        }
    }
    
    @Override
    public final int indexSize() throws Exception {
        return this.index.size();
    }
    
    @Override
    public int count() throws Exception {
        return this.count;
    }

    /**
     * This method provides access to the accumulated words/phrases.
     * @return Tree map containing the accumulated words/phrases sorted descending by their frequency of occurrence.
     * @throws Exception if anything goes wrong.
     */
    @Override
    public TreeMap<Integer, ArrayList<Term>> getResults() throws Exception {
        
        TreeMap<Integer, ArrayList<Term>> retVal = new TreeMap<>(Collections.reverseOrder());
        
        for (Map.Entry<String, Term> entry : index.entrySet()) {
            
            if (entry.getValue().getFOO() <= this.threshold) continue;
            
            if (retVal.containsKey(entry.getValue().getFOO())) {
                retVal.get(entry.getValue().getFOO()).add(entry.getValue());
            } else {
                retVal.put(entry.getValue().getFOO(), new ArrayList<>(Arrays.asList(entry.getValue())));
            }
        
        }
        
        return retVal;
        
    }
    
    /**
     * This method is used only for debugging.
     * @param includeSentences Boolean indicating whether to list only words/phrases, or also the sentences in which they occurred.
     * @throws Exception if anything goes wrong.
     */
    @Override
    public void print(boolean includeSentences) throws Exception {
        
        TreeMap<Integer, ArrayList<Term>> inverted = this.getResults();
        
        inverted.entrySet().forEach((entry) -> {
            entry.getValue().forEach((word) -> {
                word.print(includeSentences);
            });
        });
        
    }
    
}
