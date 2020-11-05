package com.itcag.rockwell.tokenizer;

import com.itcag.english.EnglishLexicon;
import com.itcag.english.EnglishMisspellings;
import com.itcag.english.EnglishNumericalExpressionDetector;
import com.itcag.english.EnglishToklex;
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
        
        Tokenizer tokenizer = new Tokenizer(EnglishToklex.getInstance(), EnglishMisspellings.getInstance());
        
        Lemmatizer lemmatizer = new Lemmatizer(EnglishLexicon.getInstance(), EnglishNumericalExpressionDetector.class);

        for (String test : tests) {
            ArrayList<String> stringTokens = tokenizer.tokenize(test);
            ArrayList<Token> tokens = lemmatizer.lemmatize(stringTokens);
            TokenPrinter.printTokensWithPOS(tokens);
            Printer.print();
        }
        
    }
    
}
