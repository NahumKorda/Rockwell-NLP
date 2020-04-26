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

package com.itcag.rockwell.vocabulator.count;

import com.itcag.rockwell.vocabulator.PropertyFields;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.itcag.util.Printer;

/**
 * <p>Extracts lemmas, so that inflected words with identical head word are grouped together. Results are sorted descending by the frequency of occurrence.</p>
 */
public class Counter {

    private final Pipeline pipeline;

    private final HashMap<String, Integer> index = new HashMap<>();

    /**
     * LemmaExtractor is initiated by providing processing instructions to it. These processing instructions are described in the {@link com.itcag.rockwell.vocabulator.PropertyFields} enum.
     * @param properties Instance of Java {@link java.util.Properties Properties} class holding the processing instructions.
     * @throws Exception if anything goes wrong.
     */
    public Counter(Properties properties) throws Exception {

        String test = properties.getProperty(PropertyFields.POSITIVE_FILTER.getField(), null);
        if (test != null) {
            String[] tmp = test.split(",");
            for (String item : tmp) {
                item = item.trim();
                if (item.isEmpty()) continue;
                this.index.put(item, 0);
            }
        }
        
        this.pipeline = getPipeline();
    
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
    public void process(String text) throws Exception {
        
        ArrayList<StringBuilder> sentences = this.pipeline.split(text);
        for (StringBuilder sentence : sentences) {
            if (sentence.length() == 0) continue;
            processSentence(sentence.toString());
        }
        
    }
    
    private void processSentence(String sentence) throws Exception {

        if (sentence.length() == 0) return;

        for (ArrayList<Token> tokens : this.pipeline.lemmatize(sentence)) {
            
            for (Token token : tokens) {

                if (token.getLemma() != null) {
                    if (this.index.containsKey(token.getLemma())) {
                        this.index.put(token.getLemma(), this.index.get(token.getLemma()) + 1);
                    }
                } else {
                    for (Token alternative : token.getAlternatives()) {
                        if (this.index.containsKey(alternative.getLemma())) {
                            this.index.put(alternative.getLemma(), this.index.get(alternative.getLemma()) + 1);
                            break;
                        }
                    }
                }

            }

        }
    
    }
   
    /**
     * This method provides access to the accumulated words/phrases.
     * @return Tree map containing the accumulated words/phrases sorted descending by their frequency of occurrence.
     * @throws Exception if anything goes wrong.
     */
    public HashMap<String, Integer> getResults() throws Exception {
        return this.index;
    }
    
    /**
     * This method is used only for debugging.
     * @param includeSentences Boolean indicating whether to list only words/phrases, or also the sentences in which they occurred.
     * @throws Exception if anything goes wrong.
     */
    public void print(boolean includeSentences) throws Exception {
 
        for (Map.Entry<String, Integer> entry : this.index.entrySet()) {
            Printer.print(entry.getValue() + "\t" + entry.getKey());
        }
        
    }
    
}
