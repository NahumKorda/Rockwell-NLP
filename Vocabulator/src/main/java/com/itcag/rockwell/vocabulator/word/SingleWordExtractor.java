package com.itcag.rockwell.vocabulator.word;

import com.itcag.rockwell.vocabulator.PropertyFields;
import com.itcag.rockwell.pipeline.Pipeline;
import com.itcag.util.Converter;
import com.itcag.rockwell.vocabulator.Exclusions;
import com.itcag.rockwell.vocabulator.Term;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.itcag.rockwell.vocabulator.Vocabulator;

/**
 * <p>Extract single words as they occur in text. Results are sorted descending by the frequency of occurrence.</p>
 */
public class SingleWordExtractor implements Vocabulator {

    private final long exclusions;
    private final int threshold;
    
    private final Pipeline pipeline;

    private HashMap<String, Term> index = new HashMap<>();
    
    private int count = 0;

    /**
     * SingleWordExtractor is initiated by providing processing instructions to it. These processing instructions are described in the {@link com.itcag.rockwell.vocabulator.PropertyFields} enum.
     * @param properties Instance of Java {@link java.util.Properties Properties} class holding the processing instructions.
     * @throws Exception if anything goes wrong.
     */
    public SingleWordExtractor(Properties properties) throws Exception {

        long tmp = 0;
        String test = properties.getProperty(PropertyFields.EXCLUSIONS.getField(), null);
        if (test != null) {
            String[] elts = test.split(",");
            for (String elt : elts) {
                elt = elt.trim().toUpperCase();
                Exclusions instruction = Exclusions.valueOf(elt);
                tmp = tmp | instruction.getInstruction();
            }
        }

        this.exclusions = tmp;
        
        test = properties.getProperty(PropertyFields.THRESHOLD.getField(), null);
        if (test != null) {
            this.threshold = Converter.convertStringToInteger(test);
        } else {
            this.threshold = 0;
        }
        
        this.pipeline = getPipeline();
    
    }
    
    private Pipeline getPipeline() throws Exception {
        Properties properties = new Properties();
        properties.put(com.itcag.rockwell.pipeline.PropertyFields.TASK.getField(), Pipeline.Tasks.TOKENIZE.name());
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
        
        ArrayList<StringBuilder> sentences = this.pipeline.split(text);
        for (StringBuilder sentence : sentences) {
            if (sentence.length() == 0) continue;
            processSentence(sentence);
        }
        
    }
    
    private void processSentence(StringBuilder sentence) throws Exception {

        if (sentence.length() == 0) return;

        for (String token : this.pipeline.tokenize(sentence)) {
            
            token = token.toLowerCase();
            
            if (!Validator.isValidWord(token, this.exclusions)) continue;
            
            if (index.containsKey(token)) {
                Term word = index.get(token);
                word.incrementFOO();
                word.addSentence(sentence.toString());
            } else {
                Term word = new Term(token);
                word.addSentence(sentence.toString());
                index.put(token, word);
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
        System.gc();
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
    public final void print(boolean includeSentences) throws Exception {
        
        TreeMap<Integer, ArrayList<Term>> inverted = this.getResults();
        
        inverted.entrySet().forEach((entry) -> {
            entry.getValue().forEach((word) -> {
                word.print(includeSentences);
            });
        });
        
    }
    
}
