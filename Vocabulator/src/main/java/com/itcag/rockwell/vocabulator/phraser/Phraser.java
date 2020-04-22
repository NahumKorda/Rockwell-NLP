package com.itcag.rockwell.vocabulator.phraser;

import com.itcag.rockwell.vocabulator.Term;
import com.itcag.rockwell.POSTag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.pipeline.Pipeline;
import com.itcag.util.Converter;
import com.itcag.util.txt.TextToolbox;
import com.itcag.rockwell.vocabulator.Exclusions;
import com.itcag.rockwell.vocabulator.PropertyFields;
import com.itcag.rockwell.vocabulator.res.Stopphrases;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import com.itcag.rockwell.vocabulator.Vocabulator;

/**
 * <p>Extracts word sequences (phrases). Results are sorted descending by the frequency of occurrence.</p>
 */
public final class Phraser implements Vocabulator {

    private class Filter {
        
        private final String[] positive;
        private final String[] negative;

        private final String[] required;
        
        private Filter(String[] positive, String[] negative, String[] required) {
            this.positive = positive;
            this.negative = negative;
            this.required = required;
        }
        
        private boolean isAcceptableSentence(String sentence) {
            
            if (this.positive.length == 0) return true;
            
            for (String item : this.positive) {
                if (sentence.contains((item))) {
                    return validateNegative(sentence);
                }
            }
            
            return false;
            
        }
        
        private boolean validateNegative(String sentence) {
            
            if (this.negative.length == 0) return true;
            
            for (String item : this.negative) {
                if (sentence.contains((item))) {
                    return false;
                }
            }
            
            return true;

        }
        
        private boolean isAcceptablePhrase(String phrase) {
            
            if (this.required.length == 0) return true;
            
            for (String item : this.required) {
                if (phrase.contains((item))) {
                    return true;
                }
            }
            
            return false;
            
        }
        
    }
    
    private final int min;
    private final int max;
    
    private final long exclusions;
    private final int threshold;
    private final int trimThreshold;
    
    private final Filter filter;

    private final Pipeline pipeline;
    
    private final Validator validator;
    private final Postprocessor postprocessor;
    
    private final TreeMap<String, Term> index = new TreeMap<>();
    
    private int count = 0;

    /**
     * Phraser is initiated by providing processing instructions to it. These processing instructions are described in the {@link com.itcag.rockwell.vocabulator.PropertyFields} enum.
     * @param properties Instance of Java {@link java.util.Properties Properties} class holding the processing instructions.
     * @throws Exception if anything goes wrong.
     */
    public Phraser(Properties properties) throws Exception {
        
        String test = properties.getProperty(PropertyFields.MIN_PHRASE_LENGTH.getField(), null);
        if (test != null) {
            this.min = Converter.convertStringToInteger(test);
        } else {
            this.min = 0;
        }
        
        test = properties.getProperty(PropertyFields.MAX_PHRASE_LENGTH.getField(), null);
        if (test != null) {
            this.max = Converter.convertStringToInteger(test);
        } else {
            this.max = 0;
        }
        
        long tmp = 0;
        test = properties.getProperty(PropertyFields.EXCLUSIONS.getField(), null);
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
        
        test = properties.getProperty(PropertyFields.TRIM_THRESHOLD.getField(), null);
        if (test != null) {
            this.trimThreshold = Converter.convertStringToInteger(test);
        } else {
            this.trimThreshold = 0;
        }
        
        String[] positive = {};
        test = properties.getProperty(PropertyFields.POSITIVE_FILTER.getField(), null);
        if (test != null) {
            positive = test.split(",");
        }
        
        String[] negative = {};
        test = properties.getProperty(PropertyFields.NEGATIVE_FILTER.getField(), null);
        if (test != null) {
            negative = test.split(",");
        }
        
        String[] required = {};
        test = properties.getProperty(PropertyFields.REQUIRED_FILTER.getField(), null);
        if (test != null) {
            required = test.split(",");
        }
        
        this.filter = new Filter(positive, negative, required);
        
        this.pipeline = getPipeline();
        
        this.validator = new Validator();
        
        this.postprocessor = new Postprocessor(this.pipeline);
        
    }
    
    private Pipeline getPipeline() throws Exception {
        Properties properties = new Properties();
        properties.put(com.itcag.rockwell.pipeline.PropertyFields.TASK.getField(), Pipeline.Tasks.CLASSIFY.name());
        String path2JAR = Phraser.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String location = path2JAR.substring(0, path2JAR.lastIndexOf("/"));
        if (!location.endsWith("/")) location += "/";
        location += "selectors";
        properties.put(com.itcag.rockwell.pipeline.PropertyFields.EXPRESSIONS.getField(), location);
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
            processSentence(sentence.toString());
        }
        
    }
    
    private void processSentence(String sentence) throws Exception {
        
        if (TextToolbox.isReallyEmpty(sentence)) return;
        
        while (sentence.contains("  ")) sentence = sentence.replace("  ", " ");
        sentence = sentence.trim();

        if (!this.filter.isAcceptableSentence(sentence)) return;
        
        for (ArrayList<Token> tokens : this.pipeline.lemmatize(sentence)) {
            processTokens(sentence, new LinkedList<>(tokens));
        }
        
    }

    private void processTokens(String text, LinkedList<Token> tokens) throws Exception {
        
        while (!tokens.isEmpty()) {
            LinkedList<Token> tmp = new LinkedList<>(tokens);
            createPhrases(text, tmp);
            tokens.pop();
        }
        
    }

    private void createPhrases(String sentence, LinkedList<Token> tokens) throws Exception {
        
        /**
         * Remove the first word if it is not allowed to open a phrase.
         */
        if (!validator.isValidFirstWord(tokens.get(0))) return;

        while (tokens.size() >= max) {
            tokens.removeLast();
        }
        
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < tokens.size(); i++) {
            
            Token token = tokens.get(i);

            if (POSTag.PC0.equals(token.getPos())) break;
            if (POSTag.PC1.equals(token.getPos())) {
                if (i == 0) break;
                continue;
            }
            
            if (i < max) {
            
                if (tmp.length() > 0 && !token.getCain().contains("'")) tmp.append(" ");
                tmp.append(token.getCain());

                if (i >= min) {
                    
                    if (!validator.isValidLastWord(token)) continue;
                    
                    if (tmp.indexOf(" ") == -1) continue;
                    
                    if (tmp.toString().trim().isEmpty()) continue;
                    
                    if ((this.exclusions & Exclusions.STOPPHRASES.getInstruction()) == Exclusions.STOPPHRASES.getInstruction()) {
                        Stopphrases stopphrases = Stopphrases.getInstance();
                        if (stopphrases.isStopphrase(tmp.toString().trim())) continue;
                    }
                    
                    if (!this.filter.isAcceptablePhrase(tmp.toString())) continue;
                    
                    Term phrase = new Term(tmp.toString());
                    
                    phrase.addSentence(sentence);
                    
                    if (index.containsKey(phrase.getText())) {
                        this.index.get(phrase.getText()).incrementFOO();
                        this.index.get(phrase.getText()).addSentence(sentence);
                    } else {
                        index.put(phrase.getText(), phrase);
                    }
                
                }
            
            } else {
                break;
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
            if (entry.getValue().getFOO() < this.trimThreshold) {
                indexIterator.remove();
            } else if (!this.postprocessor.isValidFormat(entry.getValue(), this.exclusions)) {
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
    public final TreeMap<Integer, ArrayList<Term>> getResults() throws Exception {
        return postprocessor.sortOut(this.index, this.exclusions, this.threshold);
    }
    
    /**
     * This method is used only for debugging.
     * @param includeSentences Boolean indicating whether to list only words/phrases, or also the sentences in which they occurred.
     * @throws Exception if anything goes wrong.
     */
    @Override
    public final void print(boolean includeSentences) throws Exception {
        
        for (Map.Entry<Integer, ArrayList<Term>> entry : postprocessor.sortOut(this.index, this.exclusions, this.threshold).entrySet()) {
            entry.getValue().forEach((phrase) -> {
                phrase.print(includeSentences);
            });
        }

    }
    
}
