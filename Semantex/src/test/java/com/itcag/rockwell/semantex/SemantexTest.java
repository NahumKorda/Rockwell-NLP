package com.itcag.rockwell.semantex;

import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.semantex.ner.NER;
import com.itcag.rockwell.tokenizer.Lemmatizer;
import com.itcag.rockwell.tokenizer.Tokenizer;
import com.itcag.rockwell.util.TokenPrinter;
import com.itcag.rockwell.util.TokenToolbox;
import com.itcag.rockwell.split.Splitter;
import com.itcag.util.Printer;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class SemantexTest {
    
    private final Splitter splitter;

    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;

    public SemantexTest() throws Exception {
        
        this.splitter = new Splitter();
        
        this.tokenizer = new Tokenizer();
        this.lemmatizer = new Lemmatizer();

    }
    
    @Test
    public void testProcess() throws Exception {

        ArrayList<String> tests = new ArrayList<>();
        tests.add("Dr Alicja J Gruzdz was born on June 26, 1982 in Warsaw.");
//        tests.add("Dr Alicja J Gruzdz was born on 26th June 1982 in Warsaw.");
//        tests.add("Dr Alicja J Gruzdz was born on 26 June 1982 in Warsaw.");
//        tests.add("Dr Alicja J Gruzdz was born on the 26th day of June 1982 in Warsaw.");
//        tests.add("Dr Alicja J Gruzdz was born on the 26 day of June 1982 in Warsaw.");
//        tests.add("Dr Gruzdz is the owner of IT Consulting AG Ltd.");

        long instructions = NER.Instructions.PERSONS.getInstruction() | NER.Instructions.ORGANIZATIONS.getInstruction() | NER.Instructions.DATES.getInstruction() | NER.Instructions.CURRENCIES.getInstruction();
        Semantex semantex = new Semantex(instructions);

        for (String test : tests) {
            
            for (StringBuilder sentence : splitter.split(new StringBuilder(test))) {

                ArrayList<String> words = tokenizer.getTokens(new StringBuilder(sentence));
                ArrayList<Token> tokens = lemmatizer.getTokens(words);

                Printer.print();
                Printer.print("-------------------------------------------------------------");
                Printer.print(test);
                Printer.print(TokenToolbox.getStringWithPOSFromTokens(tokens));
                Printer.print();
   
                TokenPrinter.printTokensWithPOSAndRole(semantex.insertNER(tokens));

                Printer.print();
                Printer.print("-------------------------------------------------------------");
                Printer.print();

            }

        }
        
    }
    
}
