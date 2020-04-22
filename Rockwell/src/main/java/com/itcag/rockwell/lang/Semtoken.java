package com.itcag.rockwell.lang;

import com.itcag.rockwell.POSTag;
import com.itcag.rockwell.util.TokenToolbox;

import java.util.ArrayList;

/**
 * <p>Semtoken is short for "semantic token". It extends the {@link com.itcag.rockwell.lang.Token Token} class.</p>
 * <p>Semtoken features the property named <i>roles</i>, which holds the meaning of the semtoken. Examples, of such meaning are the named entities: person, corporation, date, currency amount...</p>
 * <p>Semtoken can feature multiple roles. For example, one role could be "date", and another "beginning of epidemic outbreak".</p>
 * <p>Semtoken can replace multiple regular tokens. For example, a person's name may consist of the first and the family name, or a date may look like this: "March 11, 2020". In such case, all original tokens are preserved using the array list <i>tokens</i>. Also, the tokens following the semtoken in the sentence are shifted to the left and re-indexed accordingly.</p>
 */
public class Semtoken extends Token {
    
    private final ArrayList<Token> tokens;
    
    private final ArrayList<String> roles = new ArrayList<>();

    /**
     * @param word String holding the original text. If the semtoken replaces multiple tokens, they are combined in a single string.
     * @param pos Value in the {@link com.itcag.rockwell.POSTag POSTag} enum specifying the part of speech.
     * @param lemma String holding the canonical (lexicon) representation of the original text. 
     * @param index Integer specifying the index position of the token in a sentence.
     * @param tokens Array list containing the original tokens that the semtoken replaces.
     */
    public Semtoken(String word, POSTag pos, String lemma, Integer index, ArrayList<Token> tokens) {
        super(word, pos, lemma, index);
        this.tokens = tokens;
    }
    
    /**
     * @return Array list containing one or more original tokens replaced by the semtoken. 
     */
    public ArrayList<Token> getTokens() {
        return tokens;
    }

    /**
     * Semtoken features property roles, which holds the meaning of the semtoken. Examples, of such meaning are the named entities: person, corporation, date, currency amount...
     * @return Array list containing meanings assigned to the semtoken.
     */
    public ArrayList<String> getRoles() {
        return roles;
    }

    /**
     * Semtoken features property roles, which holds the meaning of the semtoken. Examples, of such meaning are the named entities: person, corporation, date, currency amount...
     * @param role String containing a meaning assigned to the semtoken.
     */
    public void addRole(String role) {
        if (!this.roles.contains(role.toLowerCase())) this.roles.add(role.toLowerCase());
    }

    @Override
    public final String toString() {
        return TokenToolbox.getStringFromTokens(this.tokens);
    }
    
    public final String toStringWithRoles() {
        
        StringBuilder retVal = new StringBuilder(this.toString());
        if (!this.roles.isEmpty()) {
            StringBuilder tmp = new StringBuilder();
            this.roles.forEach((role) -> {
                if (tmp.length() > 0) tmp.append("; ");
                tmp.append(role).append(" [").append(this.tokens.get(0).index).append("->").append(this.tokens.get(this.tokens.size() - 1).index).append("]");
            });
            retVal.append(" {").append(tmp).append("}");
        }
        return retVal.toString();
        
    }
    
    @Override
    public final String toStringWithPOS() {
        StringBuilder retVal = new StringBuilder();
        this.tokens.forEach((token) -> {
            if (retVal.length() > 0) retVal.append(" ");
            retVal.append(token.toStringWithPOS());
        });
        return retVal.toString();
    }
    
    public final String toStringWithPOSAndRoles() {
        
        StringBuilder retVal = new StringBuilder();
        this.tokens.forEach((token) -> {
            if (retVal.length() > 0) retVal.append(" ");
            retVal.append(token.toStringWithPOS());
        });
        if (!this.roles.isEmpty()) {
            StringBuilder tmp = new StringBuilder();
            this.roles.forEach((role) -> {
                if (tmp.length() > 0) tmp.append("; ");
                tmp.append(role).append(" [").append(this.tokens.get(0).index).append("->").append(this.tokens.get(this.tokens.size() - 1).index).append("]");
            });
            retVal.append(" {").append(tmp).append("}");
        }
        return retVal.toString();
        
    }
    
}
