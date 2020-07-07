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

import com.itcag.rockwell.POSType;
import com.itcag.rockwell.lang.Token;
import com.itcag.util.Converter;
import com.itcag.rockwell.vocabulator.Exclusions;
import com.itcag.rockwell.vocabulator.PropertyFields;
import com.itcag.rockwell.vocabulator.res.Stopwords;
import com.itcag.util.txt.TextToolbox;

import java.util.HashSet;
import java.util.Properties;

/**
 * <p>This class validates extracted words against specified {@link com.itcag.rockwell.vocabulator.Exclusions exclusions}.</p>
 */
public class Validator {

    private final long exclusions; 
    
    private final Stopwords stopwords;
    
    public Validator(Properties config) throws Exception {
        
        long tmp = 0;
        String test = config.getProperty(PropertyFields.EXCLUSIONS.getField(), null);
        if (test != null) {
            String[] elts = test.split(",");
            for (String elt : elts) {
                elt = elt.trim().toUpperCase();
                Exclusions instruction = Exclusions.valueOf(elt);
                tmp = tmp | instruction.getInstruction();
            }
        }

        this.exclusions = tmp;
        
        this.stopwords = new Stopwords(config.getProperty(PropertyFields.STOPWORDS.getField()));
        
    }
    
    /**
     * This method validates an extract word against {@link com.itcag.rockwell.vocabulator.Exclusions#STOPWORDS stop words}, {@link com.itcag.rockwell.vocabulator.Exclusions#CONTRACTIONS contractions}, {@link com.itcag.rockwell.vocabulator.Exclusions#DIGITS digits} and {@link com.itcag.rockwell.vocabulator.Exclusions#SYMBOLS symbols}.
     * @param word String holding the word to be validated.
     * @return Boolean indicating whether the word is validated or not.
     * @throws Exception if anything goes wrong.
     */
    public boolean isValidWord(String word) throws Exception {
        
        if (TextToolbox.isReallyEmpty(word)) return false;
        
        if ((this.exclusions & Exclusions.CONTRACTIONS.getInstruction()) == Exclusions.CONTRACTIONS.getInstruction()) {
            if (word.startsWith("'")) return false;
        }
        
        if ((this.exclusions & Exclusions.SYMBOLS.getInstruction()) == Exclusions.SYMBOLS.getInstruction()) {
            if (word.length() == 1 && !Character.isLetterOrDigit(word.charAt(0))) return false;
        }
        
        if ((this.exclusions & Exclusions.DIGITS.getInstruction()) == Exclusions.DIGITS.getInstruction()) {
            if (Character.isDigit(word.charAt(0)) && Character.isDigit(word.charAt(word.length() - 1))) {
                if (Converter.convertStringToLong(word) != null) return false;
                if (Converter.convertStringToDouble(word) != null) return false;
            }
        }
        
        if ((this.exclusions & Exclusions.STOPWORDS.getInstruction()) == Exclusions.STOPWORDS.getInstruction()) {
            if (this.stopwords.isStopword(word)) return false;
        }
        
        return true;
        
    }

    /**
     * This method validates an extract word against {@link com.itcag.rockwell.vocabulator.Exclusions#ALL_EXCEPT_NOUNS all but nouns}, {@link com.itcag.rockwell.vocabulator.Exclusions#ALL_EXCEPT_VERBS all but verbs} and {@link com.itcag.rockwell.vocabulator.Exclusions#ALL_EXCEPT_ADJECTIVES all but adjectives} exclusions.
     * @param word String holding the word to be validated.
     * @return Boolean indicating whether the word is validated or not.
     * @throws Exception if anything goes wrong.
     */
    public HashSet<String> getValidLemmas(Token word) throws Exception {
        
        HashSet<String> retVal = new HashSet<>();
        
        if ((exclusions & Exclusions.ALL_EXCEPT_NOUNS.getInstruction()) == Exclusions.ALL_EXCEPT_NOUNS.getInstruction()) {
            if (word.getType() != null) {
                if (POSType.NN.equals(word.getType())) retVal.add(word.getLemma());
            } else {
                for (Token alternative : word.getAlternatives()) {
                    if (POSType.NN.equals(alternative.getType())) retVal.add(alternative.getLemma());
                }
            }
        } else if ((exclusions & Exclusions.ALL_EXCEPT_VERBS.getInstruction()) == Exclusions.ALL_EXCEPT_VERBS.getInstruction()) {
            if (word.getType() != null) {
                if (POSType.VV.equals(word.getType())) retVal.add(word.getLemma());
            } else {
                for (Token alternative : word.getAlternatives()) {
                    if (POSType.VV.equals(alternative.getType())) retVal.add(alternative.getLemma());
                }
            }
        } else if ((exclusions & Exclusions.ALL_EXCEPT_ADJECTIVES.getInstruction()) == Exclusions.ALL_EXCEPT_ADJECTIVES.getInstruction()) {
            if (word.getType() != null) {
                if (POSType.AJ.equals(word.getType())) retVal.add(word.getLemma());
            } else {
                for (Token alternative : word.getAlternatives()) {
                    if (POSType.AJ.equals(alternative.getType())) retVal.add(alternative.getLemma());
                }
            }
        } else {
            if (word.getType() != null) {
                retVal.add(word.getLemma());
            } else {
                for (Token alternative : word.getAlternatives()) {
                    retVal.add(alternative.getLemma());
                }
            }
        }

        return retVal;
        
    }
        
}
