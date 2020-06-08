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

package com.itcag.rockwell.tokenizer;

import com.itcag.rockwell.tokenizer.res.Lexicon;
import com.itcag.rockwell.tokenizer.res.LexicalResources;
import com.itcag.util.punct.PunctuationToolbox;
import com.itcag.rockwell.POSTag;
import com.itcag.rockwell.lang.Alternatives;
import com.itcag.rockwell.lang.Token;
import com.itcag.util.Converter;
import com.itcag.util.punct.Characters;
import com.itcag.util.punct.Locker;

import java.util.ArrayList;

/**
 * <p>Converts tokens provided as strings into instances of the {@link com.itcag.rockwell.lang.Token Token} class.</p>
 */
public final class Lemmatizer {
    
    private final Locker locker;
    
    private final Lexicon lexicon;
    private final Lexer lexer;
    
    private final LexicalResources lexicalResources;

    public Lemmatizer() throws Exception {

        this.locker = new Locker();

        this.lexicon = Lexicon.getInstance();
        this.lexicalResources = LexicalResources.getInstance();

        this.lexer = new Lexer();

    }
    
    /**
     * @param sentence Array list containing strings holding tokens.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Token Token} class.
     * @throws Exception If anything goes wrong.
     */
    public final synchronized ArrayList<Token> getTokens(ArrayList<String> sentence) throws Exception {
        
        ArrayList<Token> retVal = new ArrayList<>();
        
        for (String word : sentence) {
            
            String cain = word.toLowerCase();
            
            if (PunctuationToolbox.isTerminalPunctuation(cain)) {
                retVal.add(new Token(word, POSTag.PC0, word, retVal.size()));
            } else if (PunctuationToolbox.isNonTerminalPunctuation(cain)) {
                retVal.add(new Token(word, POSTag.PC1, word, retVal.size()));
            } else if (word.contains(Characters.ABBREVIATION.getReplacement())) {
                word = this.locker.unlockAbbreviation(word);
                retVal.add(new Token(word, POSTag.ABB, word, retVal.size()));
            } else if (cain.contains(Characters.ACRONYM.getReplacement())) {
                word = this.locker.unlockAcronym(word);
                retVal.add(new Token(word, POSTag.ACR, word, retVal.size()));
            } else if ("(".equals(cain) || "[".equals(cain) || "{".equals(cain)) {
                retVal.add(new Token(word, POSTag.PC2, word, retVal.size()));
            } else if (")".equals(cain) || "]".equals(cain) || "}".equals(cain)) {
                retVal.add(new Token(word, POSTag.PC3, word, retVal.size()));
            } else if ("n't".equals(cain) || "not".equals(cain)) {
                retVal.add(new Token(word, POSTag.XX0, "not", retVal.size()));
            } else if ("never".equals(cain) || "ne'er".equals(cain)) {
                retVal.add(new Token(word, POSTag.XX0, "never", retVal.size()));
            } else if (lexicon.isKnown(cain)) {
                retVal.add(lexer.getToken(word, retVal.size()));
            } else if (NumberDetector.getDigits(cain) != null) {
                retVal.add(new Token(word, POSTag.CRD, cain, retVal.size()));
            } else if ("'".equals(word)) {
                retVal.add(new Token(word, POSTag.POS, "'", retVal.size()));
            } else if (word.startsWith("'") || word.endsWith("'")) {
                retVal.add(new Token(word, POSTag.XXX, word, retVal.size()));
            } else if (word.length() == 1 && ("%".equals(word) || "‰".equals(word))) {
                retVal.add(new Token(word, POSTag.XZ1, word, retVal.size()));
            } else if (this.lexicalResources.isCurrencyCode(cain)) {
                retVal.add(new Token(word, POSTag.XZ2, cain, retVal.size()));
            } else if (this.lexicalResources.isCurrencySymbol(word)) {
                retVal.add(new Token(word, POSTag.XZ2, cain, retVal.size()));
            } else if (this.lexicalResources.isMeasuringUnit(cain)) {
                /**
                 * Many measures are single letters that could also be initials in names.
                 * If the word consists of a single letter, and this letter is capitalized,
                 * assume that this is maybe a measuring unit, or maybe an initial.
                 */
                if (word.length() == 1 && !word.equals(cain)) {
                    Token token = new Token(word, null, null, retVal.size());
                    token.addAlternative(new Token(word, POSTag.XZ3, cain));
                    token.addAlternative(new Token(word, POSTag.XXX, word));
                    retVal.add(token);
                } else {
                    retVal.add(new Token(word, POSTag.XZ3, cain, retVal.size()));
                }
            } else if (isTime(word, retVal)) {
                /**
                 * Do nothing.
                 */
            } else if (isNumericalExpression(word, retVal)) {
                /**
                 * Do nothing.
                 */
            } else {
                Token token = NumberDetector.identify(word, retVal.size());
                if (token == null) {
                    if (!isCompoundWord(word, cain, retVal)) {
                        token = new Token(word, POSTag.XXX, word, retVal.size());
                        retVal.add(token);
                    }
                } else {
                    retVal.add(token);
                }
            }

        }
        
        return retVal;
        
    }

    private static boolean isTime(String word, ArrayList<Token> tokens) {
        
        if (!word.contains(":")) return false;
        
        String[] elts = word.split(":");
        
        int count = 0;
        for (String elt : elts) {
            Double test = Converter.convertStringToDouble(elt.trim());
            if (test == null) return false;
            count++;
        }
        if (count == elts.length) {
            tokens.add(new Token(word, POSTag.CRD, word, tokens.size()));
            return true;
        }
        return false;
        
    }
    
    private boolean isNumericalExpression(String word, ArrayList<Token> tokens) throws Exception {
        
        if (word.contains("'") && word.contains("\"")) return isImperialLength(word, tokens);
        
        NumericalExpressionDetector detector = new NumericalExpressionDetector();
        if (detector.split(word.toCharArray())) {
            switch (detector.getType()) {
                case PERCENTAGE:
                    tokens.add(new Token(detector.getNumberAsString(), POSTag.CRD, detector.getNumberAsString(), tokens.size()));
                    tokens.add(new Token(detector.getRest(), POSTag.XZ1, detector.getRest(), tokens.size()));
                    return true;
                case CURRENCY:
                    tokens.add(new Token(detector.getRest(), POSTag.XZ2, detector.getRest(), tokens.size()));
                    tokens.add(new Token(detector.getNumberAsString(), POSTag.CRD, detector.getNumberAsString(), tokens.size()));
                    return true;
                case QUANTITY:
                    tokens.add(new Token(detector.getNumberAsString(), POSTag.CRD, detector.getNumberAsString(), tokens.size()));
                    tokens.add(new Token(detector.getRest(), POSTag.XZ3, detector.getRest(), tokens.size()));
                    return true;
                case NUMBER:
                    tokens.add(new Token(detector.getNumberAsString(), POSTag.CRD, detector.getNumberAsString(), tokens.size()));
                    return true;
            }
        }
        return false;
    }
    
    private boolean isImperialLength(String word, ArrayList<Token> tokens) {

        /**
         * Handles only foot and inch combinations.
         * For example: 5'7".
         * Since there are two measuring units occurring in the same word,
         * the NumericalExpressionDetector class cannot parse it correctly.
         */
        int foot = word.indexOf("'");
        int inch = word.indexOf("\"");
        if (foot == -1 || inch == -1 || inch < foot) return false;
        
        String feet = word.substring(0, foot);
        String inches = word.substring(foot + 1, word.length() - 1);
        
        tokens.add(new Token(feet, POSTag.CRD, feet, tokens.size()));
        tokens.add(new Token("'", POSTag.XZ3, "'", tokens.size()));
        tokens.add(new Token(inches, POSTag.CRD, inches, tokens.size()));
        tokens.add(new Token("\"", POSTag.XZ3, "\"", tokens.size()));
        
        return true;

    }
    
    private boolean isCompoundWord(String word, String cain, ArrayList<Token> tokens) {

        if (word.contains("-")) {
            String[] elts = cain.split("-");
            String last = elts[elts.length - 1];
            if (this.lexicon.isKnown(last)) {
                Alternatives alternatives = this.lexicon.getAlternatives(last);
                for (Token alternative : alternatives.getAlternatives()) {
                    if (POSTag.VVN.equals(alternative.getPos()) || POSTag.VVG.equals(alternative.getPos())) {
                        Token token = new Token(word, POSTag.AJ0, word, tokens.size());
                        tokens.add(token);
                        return true;
                    }
                }
            }
        }
        
        return false;
        
    }
    
}
