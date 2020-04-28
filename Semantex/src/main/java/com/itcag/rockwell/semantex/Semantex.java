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

package com.itcag.rockwell.semantex;

import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.semantex.adhoc.Interpreter;
import com.itcag.rockwell.semantex.ner.NER;

import java.util.ArrayList;

/**
 * <p>This class provides either named entity extraction, or identification of concepts using proprietary Rockwell script.</p>
 * <p>Name entity identification uses builtin Rockwell expressions that identify a predefined set of named entities. These named entities undergo internal validation.</p>
 * <p>Alternatively, proprietary Rockwell expressions can be used to identify any desired set of concepts. The validation must be accordingly carried out externally.</p>
 */
public final class Semantex {

    private final Interpreter interpreter;
    private final NER ner;

    /**
     * This constructor is used to provide concept identification using any proprietary collection of Rockwell expressions.
     * @param expressionPath String holding a local path to a text file containing Rockwell expressions.
     * @throws Exception if anything goes wrong.
     */
    public Semantex(String expressionPath) throws Exception {
        this.interpreter = new Interpreter(expressionPath);
        this.ner = null;
    }

    /**
     * This constructor is used to provide concept identification using any proprietary collection of Rockwell expressions.
     * @param expressionPath String holding a local path to a text file containing Rockwell expressions.
     * @param patternPath String holding the local path to a text file containing additional pattern expressions.
     * @throws Exception if anything goes wrong.
     */
    public Semantex(String expressionPath, String patternPath) throws Exception {
        this.interpreter = new Interpreter(expressionPath, patternPath);
        this.ner = null;
    }

    /**
     * This constructor is used to only to provide named entity recognition.
     * @param instructions Long number used in bitwise comparison, in order to determine which named entities are to be extracted. The {@link com.itcag.rockwell.semantex.ner.NER.Instructions Instructions} enum provides the {@link com.itcag.rockwell.semantex.ner.NER.Instructions#getInstruction() getInstruction()} method to access the instruction for every available named entity. Instructions must be combined using the bitwise operator OR ("|").
     * @throws Exception if anything goes wrong.
     */
    public Semantex(long instructions) throws Exception {
        this.interpreter = null;
        this.ner = new NER(instructions);
    }

    /**
     * @return Boolean indicating whether the class is configured for the named entity extraction or not.
     */
    public final boolean isNERConfigured() {
        return this.ner != null;
    }
    
    /**
     * This method replaces the original {@link com.itcag.rockwell.lang.Token tokens} with the {@link com.itcag.rockwell.lang.Semtoken semtokens} representing the identified concepts.
     * @param tokens Array list of {@link com.itcag.rockwell.lang.Token tokens} representing a sentence.
     * @return Array list of {@link com.itcag.rockwell.lang.Token tokens} with tokens identified as concepts replaced by {@link com.itcag.rockwell.lang.Semtoken semtokens}.
     * @throws Exception if anything goes wrong.
     */
    public final ArrayList<Token> insert(ArrayList<Token> tokens) throws Exception {
        if (this.interpreter == null) throw new IllegalArgumentException("This class was not initialized for named entity recognition.");
        return this.interpreter.insert(tokens);
    }
    
    /**
     * This method returns the identified concepts.
     * @param tokens Array list of {@link com.itcag.rockwell.lang.Token tokens} representing a sentence.
     * @return Array list of {@link com.itcag.rockwell.lang.Tag tags} representing the identified concepts.
     * @throws Exception if anything goes wrong.
     */
    public final ArrayList<Tag> extract(ArrayList<Token> tokens) throws Exception {
        if (this.interpreter == null) throw new IllegalArgumentException("This class was not initialized for named entity recognition.");
        return this.interpreter.extract(tokens);
    }
    
    /**
     * This method replaces the original {@link com.itcag.rockwell.lang.Token tokens} with the {@link com.itcag.rockwell.lang.Semtoken semtokens} representing the identified named entities.
     * @param tokens Array list of tokens representing a sentence.
     * @return Array list containing semtokens that replaced the original tokens.
     * @throws Exception if anything goes wrong.
     */
    public final ArrayList<Token> insertNER(ArrayList<Token> tokens) throws Exception {
        if (this.ner == null) throw new IllegalArgumentException("This class was not initialized for named entity recognition.");
        return this.ner.insert(tokens);
    }

    /**
     * This method extracts named entities.
     * @param tokens Array list of tokens representing a sentence.
     * @return Array list of instances of the {@link com.itcag.rockwell.lang.Tag Tag} class representing the extracted named entities.
     * @throws Exception if anything goes wrong.
     */
    public final ArrayList<Tag> extractNER(ArrayList<Token> tokens) throws Exception {
        if (this.ner == null) throw new IllegalArgumentException("This class was not initialized for named entity recognition.");
        return this.ner.extract(tokens);
    }

}
