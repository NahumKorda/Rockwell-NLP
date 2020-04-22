package com.itcag.extr;

import com.itcag.rockwell.extr.Extractor;
import com.itcag.rockwell.lang.Extract;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tokenizer.Lemmatizer;
import com.itcag.rockwell.tokenizer.Tokenizer;
import com.itcag.rockwell.util.TokenToolbox;
import com.itcag.rockwell.semantex.Semantex;
import com.itcag.rockwell.semantex.ner.NER;
import com.itcag.rockwell.split.Splitter;
import com.itcag.util.Printer;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class ExtractorTest {
    
    private final Splitter splitter;

    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;

    public ExtractorTest() throws Exception {
        this.splitter = new Splitter();
        this.tokenizer = new Tokenizer();
        this.lemmatizer = new Lemmatizer();
    }
    
    @Test
    public void testExtract() throws Exception {
        
        String frameRulePath = "/home/nahum/Desktop/code/rockwell/Extractor/src/main/resources/testExtractionSelectors";
        String framePath = "/home/nahum/Desktop/code/rockwell/Extractor/src/main/resources/testExtractionFrames";
        
        ArrayList<String> tests = new ArrayList<>();
        tests.add("Dr Alicja J Gruzdz was born on June 26, 1982 in Warsaw.");
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
                Printer.print(TokenToolbox.getStringWithPOSAndRolesFromTokens(tokens));
                Printer.print();
                
                ArrayList<Token> semtokens = semantex.insertNER(tokens);
                
                Extractor extractor = new Extractor(frameRulePath, framePath);
                for (Extract extract : extractor.extract(semtokens)) {
                    Printer.print(extract.toString());
                }

                Printer.print();
                Printer.print("-------------------------------------------------------------");
                Printer.print();

            }

        }

    }
    
}
