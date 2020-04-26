package com.itcag.rockwell.playground;

import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tokenizer.Lemmatizer;
import com.itcag.rockwell.tokenizer.Tokenizer;
import com.itcag.rockwell.util.TokenPrinter;
import com.itcag.util.Printer;
import java.util.ArrayList;

public class LemmatizerTester {

    public static void main(String[] args) throws Exception {

        String test = "raised $1.2 million in seed funding";
        
        Tokenizer tokenizer = new Tokenizer();
        ArrayList<String> stringTokens = tokenizer.getTokens(new StringBuilder(test));
        
        Lemmatizer lemmatizer = new Lemmatizer();
        ArrayList<Token> tokens = lemmatizer.getTokens(stringTokens);

        TokenPrinter.printTokensWithPOS(tokens);
        
    }
    
}
