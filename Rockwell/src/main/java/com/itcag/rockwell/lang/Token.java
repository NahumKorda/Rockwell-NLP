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

package com.itcag.rockwell.lang;

import com.itcag.rockwell.POSTag;
import com.itcag.rockwell.POSType;
import com.itcag.util.txt.Stemmer;

import java.util.ArrayList;

/**
 * <p>Token is the most elementary constituent of text in Rockwell.</p>
 * <p>Token roughly corresponds to a single word, but parentheses, punctuation marks, and symbols are also converted to tokens. On the other hand, compound words containing hyphens and slashes (e.g., "able-bodied", "I/O") are not separated, and are treated as single tokens.</p>
 * <p>Rockwell is unique in that it does not determine categorically part of speech for every token, as other NLP platforms do. In the case that the part of speech for a token is ambiguous, all alternative interpretations are included in the corresponding token. The correct alternative is determined only when the token is matched by a Rockwell expression that identifies the specific word construct in which the ambiguity is eliminated.</p>
 * <p>This class is extended by the {@link com.itcag.rockwell.lang.Semtoken Semtoken} class.</p>
 */
public class Token {

    protected Integer index = null;
    
    protected final String word;
    protected final String cain;
    protected final POSType type;
    protected final POSTag pos;
    protected final String lemma;

    private final ArrayList<Token> alternatives = new ArrayList<>();
    
    /**
     * Used for words in a sentence (contains index indicating its position in the sentence).
     * @param word String holding the original text.
     * @param pos Value in the {@link com.itcag.rockwell.POSTag POSTag} enum specifying the part of speech.
     * @param lemma String holding the canonical (lexicon) representation of the original text. 
     * @param index Integer specifying the index position of the token in a sentence.
     */
    public Token(String word, POSTag pos, String lemma, Integer index) {
        this(word, pos, lemma);
        this.index = index;
    }
    
    /**
     * Used for the lexicon items (contains frequency of occurrence).
     * @param word String holding the original text.
     * @param pos Value in the {@link com.itcag.rockwell.POSTag POSTag} enum specifying the part of speech.
     * @param lemma String holding the canonical (lexicon) representation of the original text. 
     */
    public Token(String word, POSTag pos, String lemma) {
        this.word= word;
        this.cain = word.toLowerCase();
        if (pos != null) {
            this.type = pos.getType();
        } else {
            this.type = null;
        }
        this.pos = pos;
        this.lemma = lemma;
    }

    /**
     * @return Integer specifying the index position of the token in a sentence.
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Resets the original index position of the token in a sentence with a new position. This is necessary when multiple tokens are converted into a {@link com.itcag.rockwell.lang.Semtoken semtoken}, since the tokens after the semtoken are shifted to the left.
     * @param index Integer specifying the new index position of the token in a sentence.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return String holding the original text.
     */
    public String getWord() {
        return word;
    }

    /**
     * @return String holding the lower case version of the original text.
     */
    public String getCain() {
        return cain;
    }

    /**
     * @return Value in the {@link com.itcag.rockwell.POSTag POSTag} enum specifying the part of speech.
     */
    public POSTag getPos() {
        return pos;
    }

    /**
     * Types group parts of speech that belong to the same word class. For example, all adjectives (AJ0, AJC, AJS) are grouped under the same type (AJ).
     * @return Value in the {@link com.itcag.rockwell.POSType POSType} enum specifying the part-of-speech type.
     */
    public POSType getType() {
        return type;
    }

    /**
     * @return String holding the canonical (lexicon) representation of the original text. 
     */
    public String getLemma() {
        return lemma;
    }

    /**
     * Rockwell is unique in that it does not determine categorically part of speech for every token, as other NLP platforms do. In the case that the part of speech for a token is ambiguous, all alternative interpretations are included in the corresponding token.
     * @return Array list containing tokens representing alternative part-of-speech interpretations.
     */
    public ArrayList<Token> getAlternatives() {
        return this.alternatives;
    }

    /**
     * Rockwell is unique in that it does not determine categorically part of speech for every token, as other NLP platforms do. In the case that the part of speech for a token is ambiguous, all alternative interpretations are included in the corresponding token.
     * @param token Instance oof this class representing an alternative part-of-speech interpretation.
     */
    public void addAlternative(Token token) {
        token.setIndex(this.index);
        this.alternatives.add(token);
    }
    
    /**
     * @return String holding the stem of the original text (stem is generated using the Porter's stemming algorithm for English).
     */
    public String getStem() {
        Stemmer stemmer = new Stemmer();
        if (lemma != null) stemmer.add(lemma.toCharArray());
        stemmer.stem();
        return stemmer.toString();
    }
    
    @Override
    public String toString() {
        return this.word;
    }
    
    public String toStringWithPOS() {
        
        StringBuilder retVal = new StringBuilder();
        
        if (pos != null) retVal.append("[").append(pos).append("]");
        retVal.append(word);
        
        return retVal.toString();
        
    }
    
}
