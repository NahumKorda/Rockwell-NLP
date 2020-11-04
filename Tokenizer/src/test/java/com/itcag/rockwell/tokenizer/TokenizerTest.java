package com.itcag.rockwell.tokenizer;

import com.itcag.util.Printer;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class TokenizerTest {
    
    @Test
    public void testGetTokens() throws Exception {
        
        ArrayList<String> tests = new ArrayList<>();
        tests.add("This is a test.");
        tests.add("(This is a test.)");
        tests.add("(This is a test).");
        tests.add("This is (maybe) a test.");
        
        Tokenizer tokenizer = new Tokenizer();
        
        for (String test : tests) {
            for (String token : tokenizer.tokenize(test)) {
                Printer.print(token);
            }
            Printer.print();
            
        }
        
    }

}
