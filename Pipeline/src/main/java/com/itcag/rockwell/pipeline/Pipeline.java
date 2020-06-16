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

package com.itcag.rockwell.pipeline;

import com.itcag.rockwell.extr.Extractor;
import com.itcag.rockwell.lang.Extract;
import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tokenizer.Lemmatizer;
import com.itcag.rockwell.tokenizer.Tokenizer;
import com.itcag.rockwell.semantex.Semantex;
import com.itcag.rockwell.semantex.ner.NER;
import com.itcag.rockwell.split.Splitter;
import com.itcag.rockwell.tagger.Tagger;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.debug.DebuggingClients;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;
import java.util.Properties;

/**
 * <p>Pipeline is Java API class that provides access to the Rockwell NLP pipeline, which consists of the following stages:</p>
 * <ol>
 * <li><b>Split</b> - splits text into individual sentences.</li>
 * <li><b>Tokenize</b> - splits sentence into individual tokens.</li>
 * <li><b>Lemmatize</b> - inserts part-of-speech specifications and lemmas into tokens.</li>
 * <li><b>NER</b> - identifies and extracts named entities.</li>
 * <li><b>Classify</b> - identifies predefined categories.</li>
 * <li><b>Extract</b> - extracts data from text.</li>
 * </ol>
 * <p>This class provides access to every one of these stages. Outputs of every stage can be passed to the next stage - starting with the Split stage, which receives plain text. However, every stage can be also executed by passing the plain text to it. In the latter case, all previous stages are invoked internally.</p>
 * <p>This class is initiated by providing processing instructions to it. These processing instructions are described in the {@link PropertyFields} enum.</p>
 * <p>Once the class is initiated its methods are called to access the corresponding Rockwell functionality. However, the method called must correspond to  the task specified in the processing instructions.</p>
 * <p>To learn more how to use Rockwell Pipeline and its functionalities see: <a href="https://docs.google.com/document/d/1CjDsEowbBLBOoJs1OrC4tV807-B14b1feAvtTepauHc/edit#heading=h.6vbuuw8rdy9w" target="_blank">Rockwell (User Manual)</a>.</p>
 */
public class Pipeline {

    /**
     * Enumerates all tasks that {@link Pipeline} is capable of executing. 
     */
    public enum Tasks {
        /** Splits text into individual sentences. */
        SPLIT,
        /** Splits sentence into individual tokens. */
        TOKENIZE,
        /** Inserts part-of-speech specifications and lemmas into tokens. */
        LEMMATIZE,
        /** Extracts named entities. */
        NER,
        /** Identifies named entities, and inserts them into array of tokens representing a sentence. */
        INSERT_NER,
        /** Identifies concepts using proprietary Rockwell expressions, and inserts them into array of tokens representing a sentence. */
        INSERT_CONCEPTS,
        /** Identifies predefined categories. */
        CLASSIFY,
        /** Extracts data from text. */
        EXTRACT
    }
    
    private final Tasks currentTask;
    
    private final Splitter splitter;
    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;
    private final Tagger tagger;
    private final Semantex semantex;
    private final Extractor extractor;

    /**
     * Pipeline is initiated by providing processing instructions to it. These processing instructions are described in the {@link PropertyFields} enum.
     * @param properties Instance of Java {@link java.util.Properties Properties} class holding the processing instructions.
     * @throws Exception if anything goes wrong.
     */
    public Pipeline(Properties properties) throws Exception {

        this.currentTask = Tasks.valueOf(properties.getProperty(PropertyFields.TASK.getField(), null));
        
        switch (this.currentTask) {
            case EXTRACT:
            {
                this.splitter = getSplitter(properties);
                this.tokenizer = new Tokenizer();
                this.lemmatizer = new Lemmatizer();
                this.tagger = null;
                this.semantex = getSemantex(properties);
                this.extractor = getExtractor(properties);
                break;
            }
            case CLASSIFY:
            {
                this.splitter = getSplitter(properties);
                this.tokenizer = new Tokenizer();
                this.lemmatizer = new Lemmatizer();
                this.tagger = getTagger(properties);
                this.semantex = getSemantex(properties);
                this.extractor = null;
                break;
            }
            case NER:
            {
                this.splitter = getSplitter(properties);
                this.tokenizer = new Tokenizer();
                this.lemmatizer = new Lemmatizer();
                this.tagger = null;
                this.semantex = getSemantex(properties);
                this.extractor = null;
                break;
            }
            case INSERT_NER:
            {
                this.splitter = getSplitter(properties);
                this.tokenizer = new Tokenizer();
                this.lemmatizer = new Lemmatizer();
                this.tagger = null;
                this.semantex = getSemantex(properties);
                this.extractor = null;
                break;
            }
            case INSERT_CONCEPTS:
            {
                this.splitter = getSplitter(properties);
                this.tokenizer = new Tokenizer();
                this.lemmatizer = new Lemmatizer();
                this.tagger = null;
                this.semantex = getSemantex(properties);
                this.extractor = null;
                break;
            }
            case LEMMATIZE:
            {
                this.splitter = getSplitter(properties);
                this.tokenizer = new Tokenizer();
                this.lemmatizer = new Lemmatizer();
                this.tagger = null;
                this.semantex = null;
                this.extractor = null;
                break;
            }
            case TOKENIZE:
            {
                this.splitter = getSplitter(properties);
                this.tokenizer = new Tokenizer();
                this.lemmatizer = null;
                this.tagger = null;
                this.semantex = null;
                this.extractor = null;
                break;
            }
            case SPLIT:
            default:
            {
                this.splitter = getSplitter(properties);
                this.tokenizer = null;
                this.lemmatizer = null;
                this.tagger = null;
                this.semantex = null;
                this.extractor = null;
                break;
            }
        }
        
    }

    private Splitter getSplitter(Properties properties) throws Exception {
        
        String test = properties.getProperty(PropertyFields.EXTENDED_SPLITTING.getField(), null);
        if (test == null) {
            return new Splitter();
        } else {
            return new Splitter(Boolean.parseBoolean(test.toUpperCase()));
        }

    }
    
    private Tagger getTagger(Properties properties) throws Exception {

        String expressionPath = properties.getProperty(PropertyFields.EXPRESSIONS.getField(), null);
        if (expressionPath == null) throw new IllegalArgumentException("No expressions were specificed in the properties used in the constructor.");

        ArrayList<String> expressions = new ArrayList<>();
        ArrayList<String> lines = TextFileReader.read(expressionPath);
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("#")) continue;
            expressions.add(line);
        }

        Debugger debugger = new Debugger(DebuggingClients.PIPELINE, 0);

        String patternPath = properties.getProperty(PropertyFields.PATTERNS.getField(), null);
        if (patternPath != null) {
            return new Tagger(expressions, patternPath, debugger);
        } else {
            return new Tagger(expressions, debugger);
        }
    }

    private Semantex getSemantex(Properties properties) throws Exception {
        
        long instructions = getInstructions(properties);
        if (instructions > 0) return new Semantex(instructions);
        
        String concepts = properties.getProperty(PropertyFields.CONCEPTS.getField(), null);
        if (concepts != null) {
            
            String patternPath = properties.getProperty(PropertyFields.PATTERNS.getField(), null);
            if (patternPath != null) {
                return new Semantex(concepts, patternPath);
            } else {
                return new Semantex(concepts);
            }
            
        }
            
        return null;
        
    }
    
    private long getInstructions(Properties properties) {

        long retVal = 0;

        String test = properties.getProperty(PropertyFields.INSTRUCTIONS.getField(), null);
        if (test != null) {
            String[] elts = test.split(",");
            for (String elt : elts) {
                elt = elt.trim().toUpperCase();
                NER.Instructions instruction = NER.Instructions.valueOf(elt);
                retVal = retVal | instruction.getInstruction();
            }
        }

        return retVal;

    }

    private Extractor getExtractor(Properties properties) throws Exception {
        
        String frameExpressionPath = properties.getProperty(PropertyFields.FRAME_EXPRESSIONS.getField(), null);
        if (frameExpressionPath == null) throw new IllegalArgumentException("No extraction expressions were specificed in the properties used in the constructor.");
        
        String framePath = properties.getProperty(PropertyFields.FRAMES.getField(), null);
        if (framePath == null) throw new IllegalArgumentException("No extraction frames were specificed in the properties used in the constructor.");
        
        return new Extractor(frameExpressionPath, framePath);

    }
    
    /**
     * Splits text into individual sentences.
     * @param text String holding the text.
     * @return Array list of string - each holding an individual sentence.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<StringBuilder> split(String text) throws Exception {
        return this.splitter.split(new StringBuilder(text));
    }
    
    /**
     * Splits a sentence into individual tokens. This method receives output from the {@link #split(java.lang.String)} method.
     * @param sentence String builder holding a sentence.
     * @return Array list containing strings - each representing a token.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<String> tokenize(StringBuilder sentence) throws Exception {
        if (this.tokenizer == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.TOKENIZE.name() + ".");
        return this.tokenizer.getTokens(sentence);
    }

    /**
     * Splits text into individual sentences, and then splits each sentence into individual tokens.
     * @param text String holding the text.
     * @return Array list containing strings - each representing a token.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<ArrayList<String>> tokenize(String text) throws Exception {
        if (this.tokenizer == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.TOKENIZE.name() + ".");
        ArrayList<ArrayList<String>> retVal = new ArrayList<>();
        for (StringBuilder sentence : this.split(text)) {
            retVal.add(this.tokenize(sentence));
        }
        return retVal;
    }

    /**
     * Inserts part-of-speech specification and lemmas into tokens.
     * @param tokens Array list containing strings - each representing a token.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Token Token} class representing a sentence.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<Token> lemmatize(ArrayList<String> tokens) throws Exception {
        if (this.lemmatizer == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.LEMMATIZE.name() + ".");
        return this.lemmatizer.getTokens(tokens);
    }
    
    
    /**
     * Splits text into sentences, splits sentences into tokens, and then inserts part-of-speech specification and lemmas into tokens.
     * @param text String holding the text.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Token Token} class representing a sentence.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<ArrayList<Token>> lemmatize(String text) throws Exception {
        if (this.lemmatizer == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.LEMMATIZE.name() + ".");
        ArrayList<ArrayList<Token>> retVal = new ArrayList<>();
        for (ArrayList<String> sentence : this.tokenize(text)) {
            retVal.add(this.lemmatize(sentence));
        }
        return retVal;
    }
    
    /**
     * Given a collection of proprietary Rockwell expressions it inserts identified concepts into sentence by replacing {@link com.itcag.rockwell.lang.Token tokens} identified as concepts with {@link com.itcag.rockwell.lang.Semtoken semtokens}. Tags of the Rockwell expressions are inserted as the role properties of the semtokens.
     * @param tokens Array list containing instances of the {@link com.itcag.rockwell.lang.Token Token} class representing a sentence.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Token Token} class representing a sentence.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<Token> insertConcepts(ArrayList<Token> tokens) throws Exception {
        if (this.semantex == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.INSERT_CONCEPTS.name() + ".");
        if (this.semantex.isNERConfigured())  throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.INSERT_CONCEPTS.name() + ".");
        return this.semantex.insert(tokens);
    }

    /**
     * Given a collection of proprietary Rockwell expressions it inserts identified concepts into sentence by replacing {@link com.itcag.rockwell.lang.Token tokens} identified as concepts with {@link com.itcag.rockwell.lang.Semtoken semtokens}. Tags of the Rockwell expressions are inserted as the role properties of the semtokens.
     * This method splits text into sentences, splits sentences into tokens, inserts part-of-speech specification and lemmas into tokens, and then converts tokens identified as concepts into instances of the {@link com.itcag.rockwell.lang.Semtoken Semtoken} class.
     * @param text String holding the text.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Token Token} class representing a sentence.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<ArrayList<Token>> insertConcepts(String text) throws Exception {
        if (this.semantex == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.INSERT_CONCEPTS.name() + ".");
        if (this.semantex.isNERConfigured())  throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.INSERT_CONCEPTS.name() + ".");
        ArrayList<ArrayList<Token>> retVal = new ArrayList<>();
        for (ArrayList<Token> sentence : this.lemmatize(text)) {
            retVal.add(this.semantex.insert(sentence));
        }
        return retVal;
    }

    /**
     * Identifies and inserts named entities into sentence by replacing tokens identified as named entities with the instances of the {@link com.itcag.rockwell.lang.Semtoken Semtoken} class that represent these named entities. This method does not return the extracted named entities. If you wish to retrieve the identified named entities, use the {@link #getNamedEntities(java.util.ArrayList)} method.
     * @param tokens Array list containing instances of the {@link com.itcag.rockwell.lang.Token Token} class representing a sentence.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Token Token} class representing a sentence.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<Token> insertNamedEntities(ArrayList<Token> tokens) throws Exception {
        if (this.semantex == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.INSERT_NER.name() + ".");
        if (!this.semantex.isNERConfigured())  throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.INSERT_NER.name() + ".");
        return this.semantex.insertNER(tokens);
    }
    
    /**
     * Splits text into sentences, splits sentences into tokens, inserts part-of-speech specification and lemmas into tokens, and then converts tokens identified as named entities into instances of the {@link com.itcag.rockwell.lang.Semtoken Semtoken} class.  This method does not return the extracted named entities. If you wish to retrieve the identified named entities, use the {@link #getNamedEntities(java.util.ArrayList)} method.
     * @param text String holding the text.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Token Token} class representing a sentence.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<ArrayList<Token>> insertNamedEntities(String text) throws Exception {
        if (this.semantex == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.INSERT_NER.name() + ".");
        if (!this.semantex.isNERConfigured())  throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.INSERT_NER.name() + ".");
        ArrayList<ArrayList<Token>> retVal = new ArrayList<>();
        for (ArrayList<Token> sentence : this.lemmatize(text)) {
            retVal.add(this.semantex.insertNER(sentence));
        }
        return retVal;
    }

    /**
     * Identifies named entities, and return them as instances of the {@link com.itcag.rockwell.lang.Tag Tag} class.
     * @param tokens Array list containing instances of the {@link com.itcag.rockwell.lang.Token Token} class representing a sentence.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Tag Tag} class representing the identified named entities.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<Tag> getNamedEntities(ArrayList<Token> tokens) throws Exception {
        if (this.semantex == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.NER.name() + ".");
        if (!this.semantex.isNERConfigured())  throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.NER.name() + ".");
        return this.semantex.extractNER(tokens);
    }
    
    /**
     * Splits text into sentences, splits sentences into tokens, inserts part-of-speech specification and lemmas into tokens, and then identifies and returns named entities as instances of the {@link com.itcag.rockwell.lang.Tag Tag} class.
     * @param text String holding the text.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Tag Tag} class representing the identified named entities.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<ArrayList<Tag>> getNamedEntities(String text) throws Exception {
        if (this.semantex == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.NER.name() + ".");
        if (!this.semantex.isNERConfigured())  throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.NER.name() + ".");
        ArrayList<ArrayList<Tag>> retVal = new ArrayList<>();
        for (ArrayList<Token> sentence : this.lemmatize(text)) {
            retVal.add(this.semantex.extractNER(sentence));
        }
        return retVal;
    }

    /**
     * Identifies predefined categories in text. 
     * If the NER extractions were included in the processing instructions with which this class was initiated, named entities were identified, and tokens identified as named entities were converted into instances of the {@link com.itcag.rockwell.lang.Semtoken Semtoken} class (the same operation as carried out by the {@link #insertNamedEntities(java.util.ArrayList)} method).
     * @param tokens Array list containing instances of the {@link com.itcag.rockwell.lang.Token Token} class representing a sentence.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Tag Tag class} representing the identified categories.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<Tag> classify(ArrayList<Token> tokens) throws Exception {
        if (this.tagger == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.CLASSIFY.name() + ".");
        if (this.semantex != null) {
            if (this.semantex.isNERConfigured()) {
                return this.tagger.tag(insertNamedEntities(tokens));
            } else {
                return this.tagger.tag(insertConcepts(tokens));
            }
        } else {
            return this.tagger.tag(tokens);
        }
    }
    
    /**
     * Splits text into sentences, splits sentences into tokens, inserts part-of-speech specification and lemmas into tokens, and then identifies predefined categories in text.
     * If the NER extractions were included in the processing instructions with which this class was initiated, named entities were identified, and tokens identified as named entities were converted into instances of the {@link com.itcag.rockwell.lang.Semtoken Semtoken} class (the same operation as carried out by the {@link #insertNamedEntities(java.lang.String)} method).
     * @param text String holding the text.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Tag Tag class} representing the identified categories.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<ArrayList<Tag>> classify(String text) throws Exception {
        if (this.tagger == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.CLASSIFY.name() + ".");
        ArrayList<ArrayList<Tag>> retVal = new ArrayList<>();
        if (this.semantex != null) {
            if (this.semantex.isNERConfigured()) {
                for (ArrayList<Token> sentence : insertNamedEntities(text)) {
                    retVal.add(this.tagger.tag(sentence));
                }
            } else {
                for (ArrayList<Token> sentence : insertConcepts(text)) {
                    retVal.add(this.tagger.tag(sentence));
                }
            }
        } else {
            for (ArrayList<Token> sentence : lemmatize(text)) {
                retVal.add(this.tagger.tag(sentence));
            }
        }
        return retVal;
    }

    /**
     * Extracts data from text.
     * If the NER extractions were included in the processing instructions with which this class was initiated, named entities were identified, and tokens identified as named entities were converted into instances of the {@link com.itcag.rockwell.lang.Semtoken Semtoken} class (the same operation as carried out by the {@link #insertNamedEntities(java.util.ArrayList)} method).
     * @param tokens Array list containing instances of the {@link com.itcag.rockwell.lang.Token Token} class representing a sentence.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Extract Extract} class representing extracted data.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<Extract> extract(ArrayList<Token> tokens) throws Exception {
        if (this.extractor == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.EXTRACT.name() + ".");
        if (this.semantex != null) {
            if (this.semantex.isNERConfigured()) {
                return this.extractor.extract(insertNamedEntities(tokens));
            } else {
                return this.extractor.extract(insertConcepts(tokens));
            }
        } else {
            return this.extractor.extract(tokens);
        }
    }
    
    /**
     * Splits text into sentences, splits sentences into tokens, inserts part-of-speech specification and lemmas into tokens, and then extracts data from text.
     * If the NER extractions were included in the processing instructions with which this class was initiated, named entities were identified, and tokens identified as named entities were converted into instances of the {@link com.itcag.rockwell.lang.Semtoken Semtoken} class (the same operation as carried out by the {@link #insertNamedEntities(java.lang.String)} method).
     * @param text String holding the text.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Extract Extract} class representing extracted data.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<ArrayList<Extract>> extract(String text) throws Exception {
        if (this.extractor == null) throw new IllegalArgumentException("This pipeline was initiated for " + this.currentTask.name() + " and not for " + Tasks.EXTRACT.name() + ".");
        ArrayList<ArrayList<Extract>> retVal = new ArrayList<>();
        if (this.semantex != null) {
            if (this.semantex.isNERConfigured()) {
                for (ArrayList<Token> sentence : insertNamedEntities(text)) {
                    retVal.add(this.extractor.extract(sentence));
                }
            } else {
                for (ArrayList<Token> sentence : insertConcepts(text)) {
                    retVal.add(this.extractor.extract(sentence));
                }
            }
        } else {
            for (ArrayList<Token> sentence : lemmatize(text)) {
                retVal.add(this.extractor.extract(sentence));
            }
        }
        return retVal;
    }
    
}
