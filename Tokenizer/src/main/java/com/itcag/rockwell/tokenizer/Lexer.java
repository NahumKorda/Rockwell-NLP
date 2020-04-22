package com.itcag.rockwell.tokenizer;

import com.itcag.rockwell.tokenizer.res.Lexicon;
import com.itcag.rockwell.POSTag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tokenizer.res.LexicalResources;

/**
 * <p>This class converts a single string representing a token into an instance of the {@link com.itcag.rockwell.lang.Token Token} class.</p>
 */
public final class Lexer {
    
    private final Lexicon lexicon;
    private final LexicalResources lexicalResources;
    
    public Lexer() throws Exception {
        this.lexicon = Lexicon.getInstance();
        this.lexicalResources = LexicalResources.getInstance();
    }

    /**
     * @param word String representing a token,
     * @param index Integer holding the index position of the token in a sentence.
     * @return Instance of the {@link com.itcag.rockwell.lang.Token Token} class.
     */
    public final Token getToken(String word, int index) {
        
        String cain = word.toLowerCase();
        
        /**
         * Capitalized IT could be an acronym for "Information Technologies".
         */
        if (cain.equals("it") && word.equals(word.toUpperCase())) {
            Token retVal = new Token(word, null, null, index);
            Token alternative = lexicon.getAlternatives(cain).getAlternatives().get(0);
            retVal.addAlternative(alternative);
            alternative = new Token(word, POSTag.ACR, word);
            retVal.addAlternative(alternative);
            return retVal;
        } else if (lexicon.getAlternatives(cain).getAlternatives().size() == 1) {
            Token alternative = lexicon.getAlternatives(cain).getAlternatives().get(0);
            if (NumberDetector.getDigits(alternative.getLemma()) != null) {
                Token retVal = new Token(word, null, null, index);
                retVal.addAlternative(alternative);
                retVal.addAlternative(new Token(word, POSTag.CRD, alternative.getLemma(), index));
                return retVal;
            } else if (this.lexicalResources.isCurrencyCode(cain)) {
                Token retVal = new Token(word, null, null, index);
                retVal.addAlternative(alternative);
                retVal.addAlternative(new Token(word, POSTag.XZ2, alternative.getLemma(), index));
                return retVal;
            } else {
                Token retVal = new Token(word, alternative.getPos(), alternative.getLemma(), index);
                return retVal;
            }
        } else {
            Token retVal = new Token(word, null, null, index);
            lexicon.getAlternatives(cain).getAlternatives().stream().map((alternative) -> {
                retVal.addAlternative(alternative);
                return alternative;
            }).forEachOrdered((alternative) -> {
                if (NumberDetector.getDigits(alternative.getLemma()) != null) {
                    retVal.addAlternative(new Token(word, POSTag.CRD, alternative.getLemma(), index));
                } else if (this.lexicalResources.isCurrencyCode(cain)) {
                    retVal.addAlternative(new Token(word, POSTag.XZ2, alternative.getLemma(), index));
                }
            });
            return retVal;
        }
        
    }

}