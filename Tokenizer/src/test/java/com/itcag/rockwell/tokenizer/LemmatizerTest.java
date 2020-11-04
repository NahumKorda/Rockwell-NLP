package com.itcag.rockwell.tokenizer;

import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.util.TokenPrinter;
import com.itcag.util.Printer;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class LemmatizerTest {
    
    @Test
    public void testLemmatize() throws Exception {

        ArrayList<String> tests = new ArrayList<>();
        tests.add("A CMMC Approach to Address Firmware Vulnerabilities and Ensure Device Integrity");
        
        Tokenizer tokenizer = new Tokenizer();
        
        Lemmatizer lemmatizer = new Lemmatizer();

        for (String test : tests) {
            ArrayList<String> stringTokens = tokenizer.tokenize(test);
            ArrayList<Token> tokens = lemmatizer.lemmatize(stringTokens);
            TokenPrinter.printTokensWithPOS(tokens);
            Printer.print();
        }
        
    }
    
}
