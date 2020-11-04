package com.itcag.rockwell.semantex.ner;

import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tokenizer.Lemmatizer;
import com.itcag.rockwell.tokenizer.Tokenizer;
import com.itcag.rockwell.util.TokenToolbox;
import com.itcag.rockwell.semantex.Test;
import com.itcag.rockwell.semantex.Tests;
import com.itcag.rockwell.split.Splitter;
import com.itcag.rockwell.util.TokenPrinter;
import com.itcag.util.Printer;

import java.util.ArrayList;

public class NERTest {
    
    private final Splitter splitter;

    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;

    public NERTest() throws Exception {
        this.splitter = new Splitter();
        this.tokenizer = new Tokenizer();
        this.lemmatizer = new Lemmatizer();
    }
    
    @org.junit.jupiter.api.Test
    public void testExtract() throws Exception {

//        ArrayList<Test> tests = Tests.getNERDateTests();
//        ArrayList<Test> tests = Tests.getNERPersonTests();
        ArrayList<Test> tests = Tests.getNERCorporationTests();
//        ArrayList<Test> tests = Tests.getNERCurrencyTests();

        NER ner = new NER();

        for (Test test : tests) {
            
            for (StringBuilder sentence : splitter.splitInPipeline(test.getText())) {

                ArrayList<String> words = tokenizer.tokenize(sentence.toString());
                ArrayList<Token> tokens = lemmatizer.lemmatize(words);

                Printer.print();
                Printer.print("-------------------------------------------------------------");
                Printer.print(test.getText());
                Printer.print(TokenToolbox.getStringWithPOSAndRolesFromTokens(tokens));
                
                ArrayList<Tag> tags = ner.extract(tokens);
                
                if (isSuccessful(test, tags)) {
                    Printer.print("SUCCESS!");
                } else {
                    Printer.print("FAILURE!");
                    for (Tag tag : tags) {
                        Printer.print("\t" + tag.toString());
                    }
                }

                Printer.print();
                Printer.print("-------------------------------------------------------------");
                Printer.print();

            }

        }
        
    }
    
    private boolean isSuccessful(Test test, ArrayList<Tag> tags) {
        for (Tag tag : tags) {
            if (tag.getStart() == test.getStart() && tag.getEnd() == test.getEnd()) {
                Printer.print(tag.toString());
                return true;
            }
        }
        return false;
    }
    
    @org.junit.jupiter.api.Test
    public void testInsert() throws Exception {

//        ArrayList<Test> tests = Tests.getNERDateTests();
//        ArrayList<Test> tests = Tests.getNERPersonTests();
        ArrayList<Test> tests = Tests.getNERCorporationTests();
//        ArrayList<Test> tests = Tests.getNERCurrencyTests();

        NER ner = new NER();

        for (Test test : tests) {
            
            for (StringBuilder sentence : splitter.splitInPipeline(test.getText())) {

                ArrayList<String> words = tokenizer.tokenize(sentence.toString());
                ArrayList<Token> tokens = lemmatizer.lemmatize(words);

                Printer.print();
                Printer.print("-------------------------------------------------------------");
                Printer.print(test.getText());
                TokenPrinter.printTokensWithPOS(tokens);
                
                tokens = ner.insert(tokens);
                TokenPrinter.printTokensWithRoles(tokens);
                
                Printer.print();
                Printer.print("-------------------------------------------------------------");
                Printer.print();

            }

        }
        
    }
    
}
