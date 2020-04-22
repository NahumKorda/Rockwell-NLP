package com.itcag.rockwell.tokenizer;

import com.itcag.rockwell.tokenizer.res.Lexicon;
import com.itcag.rockwell.tokenizer.res.LexicalResources;
import com.itcag.util.punct.PunctuationToolbox;
import com.itcag.rockwell.POSTag;
import com.itcag.rockwell.lang.Token;
import com.itcag.util.Converter;
import com.itcag.util.punct.Abbreviations;
import com.itcag.util.punct.Acronyms;
import com.itcag.util.punct.Punctuation;

import java.util.ArrayList;

/**
 * <p>Converts tokens provided as strings into instances of the {@link com.itcag.rockwell.lang.Token Token} class.</p>
 */
public final class Lemmatizer {
    
    private final Abbreviations abbrevations;
    private final Acronyms acronyms;
    
    private final Lexicon lexicon;
    private final Lexer lexer;
    
    private final LexicalResources lexicalResources;

    public Lemmatizer() throws Exception {

        this.abbrevations = Abbreviations.getInstance();
        this.acronyms = Acronyms.getInstance();

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
            } else if (word.contains(Punctuation.ABBREVIATION_PERIOD)) {
                word = this.abbrevations.unlock(word);
                retVal.add(new Token(word, POSTag.ABB, word, retVal.size()));
            } else if (cain.contains(Punctuation.ACRONYM_PERIOD)) {
                word = this.acronyms.unlock(word);
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
                if (token == null) token = new Token(word, POSTag.XXX, word, retVal.size());
                retVal.add(token);
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
    
}
