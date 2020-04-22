package com.itcag.rockwell.semantex.ner;

import com.itcag.rockwell.lang.Semtoken;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tokenizer.Lemmatizer;
import com.itcag.rockwell.tokenizer.Tokenizer;
import com.itcag.rockwell.util.TokenToolbox;
import com.itcag.rockwell.split.Splitter;
import com.itcag.util.Printer;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class NominalsTest {
    
    @Test
    public void testIdentify() throws Exception {
        
        ArrayList<String> tests = new ArrayList<>();
//        tests.add("Today is Sunday, March 1, 2020 and it is raining.");
//        tests.add("She works for Google Inc.");
//        tests.add("Dr Alicja Gruzdz.");
//        tests.add("Dr. Alicja Gruzdz.");
        tests.add("USD 14 million");

        Splitter splitter = new Splitter();
        Tokenizer tokenizer = new Tokenizer();
        Lemmatizer lemmatizer = new Lemmatizer();

        for (String test : tests) {
            
            for (StringBuilder sentence : splitter.split(new StringBuilder(test))) {

                ArrayList<String> words = tokenizer.getTokens(new StringBuilder(sentence));
                ArrayList<Token> tokens = lemmatizer.getTokens(words);

                Printer.print();
                Printer.print("-------------------------------------------------------------");
                Printer.print(test);
                Printer.print(TokenToolbox.getStringWithPOSAndRolesFromTokens(tokens));
                Printer.print();
                
                Nominals nominals = Nominals.getInstance();
                for (Token token : nominals.identify(tokens)) {
                    if (token instanceof Semtoken) {
                        Semtoken tmp = (Semtoken) token;
                        Printer.print(tmp.toString());
                    } else {
                        Printer.print(token.toString());
                    }
                }

                Printer.print();
                Printer.print("-------------------------------------------------------------");
                Printer.print();

            }

        }
        
    }
    
}
