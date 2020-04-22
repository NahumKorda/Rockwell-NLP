package com.itcag.rockwell.lang;

import java.util.ArrayList;

/**
 * <p>Rockwell is unique in that it does not determine categorically part of speech for every token, as other NLP platforms do. In the case that the part of speech for a token is ambiguous, all alternative interpretations are included in the corresponding token. The correct alternative is determined only when the token is matched by a Rockwell expression that identifies the specific word construct in which the ambiguity is eliminated.</p>
 * <p>This class holds alternative interpretations of a particular word in the lexicon.</p>
 */
public class Alternatives {
    
    private final ArrayList<Token> alternatives;
    
    /**
     * @param alternatives Array list containing alternative interpretations (part of speech and lemma pairs) of a word.
     */
    public Alternatives(ArrayList<Token> alternatives) {
        this.alternatives = alternatives;
    }

    /**
     * 
     * @return Array list containing alternative interpretations (part of speech and lemma pairs) of a word.
     */
    public ArrayList<Token> getAlternatives() {
        return this.alternatives;
    }
    
    @Override
    public String toString() {
        
        if (alternatives == null) return "";
        
        StringBuilder retVal = new StringBuilder();
        
        alternatives.stream().forEach((alternative) -> {
            retVal.append(alternative.toString()).append("\n");
        });
        
        return retVal.toString();
        
    }
    
}
