package com.itcag.rockwell.extr;

import com.itcag.english.EnglishLexicon;
import com.itcag.english.EnglishMisspellings;
import com.itcag.english.EnglishNumericalExpressionDetector;
import com.itcag.english.EnglishToklex;
import com.itcag.english.LatinUnicodeStandardizer;
import com.itcag.rockwell.lang.Extract;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.semantex.Semantex;
import com.itcag.rockwell.semantex.ner.NER;
import com.itcag.rockwell.split.Splitter;
import com.itcag.rockwell.tokenizer.Lemmatizer;
import com.itcag.rockwell.tokenizer.Tokenizer;
import com.itcag.rockwell.util.TokenToolbox;
import com.itcag.util.Printer;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class ExtractorTest {
    
    private final Splitter splitter;

    private final Tokenizer tokenizer;
    private final Lemmatizer lemmatizer;

    public ExtractorTest() throws Exception {
        this.splitter = new Splitter(new LatinUnicodeStandardizer(), false);
        this.tokenizer = new Tokenizer(EnglishToklex.getInstance(), EnglishMisspellings.getInstance());
        this.lemmatizer = new Lemmatizer(EnglishLexicon.getInstance(), EnglishNumericalExpressionDetector.class);
    }
    
    @Test
    public void testExtract() throws Exception {
        
//        String frameRulePath = "/home/nahum/code/Rockwell-NLP/Extractor/src/main/resources/testExtractionSelectors";
//        String framePath = "/home/nahum/code/Rockwell-NLP/Extractor/src/main/resources/testExtractionFrames";
        String frameRulePath = "/home/nahum/code/News-Analyzer/Analyzer/src/main/resources/events/scripts";
        String framePath = "/home/nahum/code/News-Analyzer/Analyzer/src/main/resources/frames/frames";
        
        ArrayList<String> tests = new ArrayList<>();
//        tests.add("Apple acquired NextVR, suggesting it still harbors VR ambitions");
//        tests.add("Blue Ocean Robotics Acquires Beam Telepresence Robot From Suitable Technologies");
//        tests.add("Uber Said to Be in Talks to Acquire Grubhub");
//        tests.add("Uber reportedly offered to acquire Grubhub");
//        tests.add("Happiest Minds looks to acquire small firms for faster revenue growth");
//        tests.add("Metro West plan revealed: Rydalmere station scrapped, 150 properties to be acquired");
        tests.add("Walmart CEO Doug McMillon: Jet.com acquisition was worthwhile");
//        tests.add("");

        long instructions = NER.Instructions.DATES.getInstruction() | NER.Instructions.CURRENCIES.getInstruction();
        Semantex semantex = new Semantex(instructions);

        for (String test : tests) {
            
            for (StringBuilder sentence : splitter.splitInPipeline(test)) {

                ArrayList<String> words = tokenizer.tokenize(sentence.toString());
                ArrayList<Token> tokens = lemmatizer.lemmatize(words);

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
