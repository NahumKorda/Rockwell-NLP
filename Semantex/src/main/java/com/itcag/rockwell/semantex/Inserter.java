package com.itcag.rockwell.semantex;

import com.itcag.rockwell.lang.Semtoken;
import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.util.TokenToolbox;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <p>This class replaces the original {@link com.itcag.rockwell.lang.Token tokens} with the {@link com.itcag.rockwell.lang.Semtoken semtokens} representing the identified nominals, named entities or concepts.</p>
 */
public class Inserter {

    /**
     * This method replaces the original {@link com.itcag.rockwell.lang.Token tokens} with the {@link com.itcag.rockwell.lang.Semtoken semtokens} representing the identified nominals, named entities or concepts.
     * @param tags Array list containing instances of the {@link com.itcag.rockwell.lang.Tag Tag} class representing the identified nominals, named entities or concepts.
     * @param tokens Array list of {@link com.itcag.rockwell.lang.Token tokens} representing a sentence.
     * @return Array list containing {@link com.itcag.rockwell.lang.Semtoken semtokens} that replaced the original {@link com.itcag.rockwell.lang.Token tokens}.
     */
    public static ArrayList<Token> insertSemtokens(ArrayList<Tag> tags, ArrayList<Token> tokens) {
        
        if (tags.isEmpty()) return tokens;
        
        HashMap<Integer, Semtoken> semtokens = getSemtokens(tags, tokens);
        if (semtokens.isEmpty()) return tokens;
        
        return replaceTokensWithSemtokens(semtokens, tokens);
        
    }
    
    private static HashMap<Integer, Semtoken> getSemtokens(ArrayList<Tag> tags, ArrayList<Token> tokens) {
        
        HashMap<Integer, Semtoken> retVal = new HashMap<>(); 
        
        for (Tag tag : tags) {
            ArrayList<Token> tmp = new ArrayList<>();
            for (Token token : tokens) {
                if (token.getIndex() >= tag.getStart() && token.getIndex() <= tag.getEnd()) {
                    tmp.add(token);
                }
            }
            if (retVal.containsKey(tag.getStart())) {
               retVal.get(tag.getStart()).addRole(tag.getTag());
            } else {
                Semtoken semtoken = new Semtoken(TokenToolbox.getStringFromTokens(tmp), null, null, tag.getStart(), tmp);
                semtoken.addRole(tag.getTag());
                retVal.put(tag.getStart(), semtoken);
            }
        }
        
        return retVal;
        
    }
    
    private static ArrayList<Token> replaceTokensWithSemtokens(HashMap<Integer, Semtoken> semtokens, ArrayList<Token> tokens) {

        ArrayList<Token> retVal = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {
            
            Token token = tokens.get(i);
            
            if (semtokens.containsKey(token.getIndex())) {
                Semtoken semtoken = semtokens.get(token.getIndex());
                semtoken.setIndex(retVal.size());
                retVal.add(semtoken);
                i = semtoken.getTokens().get(semtoken.getTokens().size() - 1).getIndex();
            } else {
                token.setIndex(retVal.size());
                retVal.add(token);
            }
        }

        return retVal;

    }

}
