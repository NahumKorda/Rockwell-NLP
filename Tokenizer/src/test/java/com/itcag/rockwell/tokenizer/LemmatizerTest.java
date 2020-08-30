package com.itcag.rockwell.tokenizer;

import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.util.TokenPrinter;
import com.itcag.util.Printer;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class LemmatizerTest {
    
    @Test
    public void testGetTokens() throws Exception {

        ArrayList<String> tests = new ArrayList<>();
        tests.add("Telecom Argentina Has Tuesday Deadline to Pay $7.5m Ransom");
        
        Tokenizer tokenizer = new Tokenizer();
        
        Lemmatizer lemmatizer = new Lemmatizer();

        for (String test : tests) {
            ArrayList<String> stringTokens = tokenizer.getTokens(new StringBuilder(test));
            ArrayList<Token> tokens = lemmatizer.getTokens(stringTokens);
            TokenPrinter.printTokensWithPOS(tokens);
            Printer.print();
        }
        
    }
    
}
