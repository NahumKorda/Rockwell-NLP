package com.itcag.rockwell.util;

import com.itcag.rockwell.lang.Token;

import java.util.ArrayList;

/**
 * This class provides a collection of static methods for printing an array list of tokens.
 */
public final class TokenPrinter {
    
    /**
     * This method prints the sentence reconstructed from the tokens as close to the original as possible.
     * @param tokens Array list containing tokens representing a sentence.
     */
    public final static void printTokens(ArrayList<Token> tokens) {
        System.out.println(TokenToolbox.getStringFromTokens(tokens));
    }
    
    /**
     * This method prints string reconstructed from tokens that contains roles of the {@link com.itcag.rockwell.lang.Semtoken semtokens}.
     * @param tokens Array list containing tokens representing a sentence.
     */
    public final static void printTokensWithRoles(ArrayList<Token> tokens) {
        System.out.println(TokenToolbox.getStringWithRolesFromTokens(tokens));
    }
    
    /**
     * This method prints string reconstructed from tokens that contains part-of-speech specifications of the unambiguous tokens.
     * @param tokens Array list containing tokens representing a sentence.
     */
    public final static void printTokensWithPOS(ArrayList<Token> tokens) {
        System.out.println(TokenToolbox.getStringWithPOSFromTokens(tokens));
    }
    
    /**
     * This method prints string reconstructed from tokens that contains both roles of the {@link com.itcag.rockwell.lang.Semtoken semtokens} and part-of-speech specifications of the unambiguous tokens.
     * @param tokens Array list containing tokens representing a sentence.
     */
    public final static void printTokensWithPOSAndRole(ArrayList<Token> tokens) {
        System.out.println(TokenToolbox.getStringWithPOSAndRolesFromTokens(tokens));
    }
    
}
