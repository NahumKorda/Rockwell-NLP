package com.itcag.rockwell.tagger;

import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tokenizer.Lemmatizer;
import com.itcag.rockwell.tokenizer.Tokenizer;
import com.itcag.rockwell.util.TokenToolbox;
import com.itcag.rockwell.split.Splitter;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.debug.DebuggingClients;
import com.itcag.util.Printer;

import java.util.ArrayList;
import java.util.Arrays;

public class TaggerTest {
    
    private final Splitter splitter;

    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;

    public TaggerTest() throws Exception {
        
        this.splitter = new Splitter();
        
        this.tokenizer = new Tokenizer();
        this.lemmatizer = new Lemmatizer();

    }
    
    @org.junit.jupiter.api.Test
    public void testTag() throws Exception {
        
//        execute(Tests.getAdverbialPhrases());
//        execute(Tests.getAdjectivePhrases());
//        execute(Tests.getNounPhrases());
//        execute(Tests.getAdHocTests());
        
        run();

    }

    private void run() throws Exception {
        
//        StringBuilder text = new StringBuilder("Stakes in Facebook, Boeing, Disney");
//        ArrayList<String> expressions = new ArrayList<>();
//        expressions.add("@cain+suffix{} :in+noun | test");

        StringBuilder text = new StringBuilder("JioMart, the e-commerce venture from India’s richest man, launches in 200 cities and towns");
        ArrayList<String> expressions = new ArrayList<>();
        expressions.add("@type+suffix{x} :NN+noun | test ");
        
        for (StringBuilder sentence : splitter.split(text)) {

            ArrayList<String> words = tokenizer.getTokens(new StringBuilder(sentence));
            ArrayList<Token> tokens = lemmatizer.getTokens(words);

            Printer.print();
            Printer.print("-------------------------------------------------------------");
            Printer.print(text);
            Printer.print(TokenToolbox.getStringWithPOSAndRolesFromTokens(tokens));
            Printer.print();


            Debugger debugger = new Debugger(DebuggingClients.TESTING, 0);

            Tagger tagger = new Tagger(expressions, debugger);
            ArrayList<Tag> tags = tagger.tag(tokens);

            for (Tag tag : tags) {
                Printer.print(tag.toString());
            }

            Printer.print();
            Printer.print("-------------------------------------------------------------");
            Printer.print();

        }

    }
    
    private void execute(ArrayList<Test> tests) throws Exception {
        
        for (Test test : tests) {
            
            for (StringBuilder sentence : splitter.split(new StringBuilder(test.getText()))) {

                ArrayList<String> words = tokenizer.getTokens(new StringBuilder(sentence));
                ArrayList<Token> tokens = lemmatizer.getTokens(words);

                Printer.print();
                Printer.print("-------------------------------------------------------------");
                Printer.print(test.getText());
                Printer.print(TokenToolbox.getStringWithPOSAndRolesFromTokens(tokens));
                Printer.print();
                

                Debugger debugger = new Debugger(DebuggingClients.TESTING, 0);

                Tagger tagger = new Tagger(new ArrayList<>(Arrays.asList(test.getRule())), debugger);
                ArrayList<Tag> tags = tagger.tag(tokens);

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
    
}
