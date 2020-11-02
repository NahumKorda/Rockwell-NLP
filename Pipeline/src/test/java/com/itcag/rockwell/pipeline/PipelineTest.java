package com.itcag.rockwell.pipeline;

import com.itcag.rockwell.lang.Extract;
import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.semantex.ner.NER;
import com.itcag.rockwell.util.TokenPrinter;
import com.itcag.util.Printer;

import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class PipelineTest {
    
    @Test
    public void testTest() throws Exception {
//        split();
//        tokenizeStringBuilder();
//        tokenizeString();
//        lemmatizeArrayList();
        lemmatizeString();
//        insertNERInArrayList();
//        insertNERInString();
//        getNERFromArrayList();
//        getNERFromString();
//        extractString();
//        extractArrayList();
//        classifyString();
//        classifyArrayList();
    }
    
    private void split() throws Exception {
        
        String text = "\"He wants his brother's wives.\" - Said the old man.";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.SPLIT.name());
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<StringBuilder> sentences = pipeline.split(text);
        
        for (StringBuilder sentence : sentences) {
            System.out.println(sentence.toString());
        }
        
    }

    private void tokenizeStringBuilder() throws Exception {
        
        String text = "The quick brown fox jumps in the U.S.A. over the lazy dog. Lazy dog doesn't wake up.";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.TOKENIZE.name());
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<StringBuilder> sentences = pipeline.split(text);

        for (StringBuilder sentence : sentences) {

            ArrayList<String> tokens = pipeline.tokenize(sentence);

            tokens.forEach((token) -> {
                System.out.println(token);
            });

        }

    }

    private void tokenizeString() throws Exception {
        
        String text = "'He wants his brother's wives.' - Said the old man.";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.TOKENIZE.name());
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<ArrayList<String>> tokens = pipeline.tokenize(text);
        
        for (ArrayList<String> sentence : tokens) {
            for (String token : sentence) {
                Printer.print(token);
            }
            Printer.print();
            Printer.print();
        }
        
    }

    public void lemmatizeArrayList() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog. Lazy dog doesn't wake up.";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.LEMMATIZE.name());
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<StringBuilder> sentences = pipeline.split(text);

        for (StringBuilder sentence : sentences) {

            ArrayList<String> tokenStrings = pipeline.tokenize(sentence);
            ArrayList<Token> tokens = pipeline.lemmatize(tokenStrings);

            TokenPrinter.printTokensWithPOS(tokens);

        }

    }

    private void lemmatizeString() throws Exception {
        
        String text = "'He doesn't want his brothers' wives.' - Said the old man.";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.LEMMATIZE.name());
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<ArrayList<Token>> tokens = pipeline.lemmatize(text);
        
        for (ArrayList<Token> sentence : tokens) {
            TokenPrinter.printTokensWithPOS(sentence);
            Printer.print();
        }
        
    }

    private void insertNERInArrayList() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog. Lazy dog doesn't wake up.";
        String instructions = NER.Instructions.PERSONS.name() + ", " + NER.Instructions.ORGANIZATIONS.name() + ", " + NER.Instructions.DATES.name() + ", " + NER.Instructions.CURRENCIES.name();
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.INSERT_NER.name());
        properties.put(PropertyFields.INSTRUCTIONS.getField(), instructions);
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<ArrayList<Token>> sentences = pipeline.lemmatize(text);
        for (ArrayList<Token> sentence : sentences) {
            sentence = pipeline.insertNamedEntities(sentence);
            TokenPrinter.printTokens(sentence);
            Printer.print();
        }

    }

    private void insertNERInString() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog.";
        String instructions = NER.Instructions.PERSONS.name() + ", " + NER.Instructions.ORGANIZATIONS.name() + ", " + NER.Instructions.DATES.name() + ", " + NER.Instructions.CURRENCIES.name();
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.INSERT_NER.name());
        properties.put(PropertyFields.INSTRUCTIONS.getField(), instructions);
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<ArrayList<Token>> sentences = pipeline.insertNamedEntities(text);
        
        for (ArrayList<Token> sentence : sentences) {
            TokenPrinter.printTokens(sentence);
            Printer.print();
        }
        
    }

    private void getNERFromArrayList() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog. Lazy dog doesn't wake up.";
        String instructions = NER.Instructions.PERSONS.name() + ", " + NER.Instructions.ORGANIZATIONS.name() + ", " + NER.Instructions.DATES.name() + ", " + NER.Instructions.CURRENCIES.name();
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.NER.name());
        properties.put(PropertyFields.INSTRUCTIONS.getField(), instructions);
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<ArrayList<Token>> sentences = pipeline.lemmatize(text);

        for (ArrayList<Token> sentence : sentences) {
            for (Tag tag : pipeline.getNamedEntities(sentence)) {
                Printer.print(tag.toString());
            }
            Printer.print();
        }

    }

    private void getNERFromString() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog.";
        String instructions = NER.Instructions.PERSONS.name() + ", " + NER.Instructions.ORGANIZATIONS.name() + ", " + NER.Instructions.DATES.name() + ", " + NER.Instructions.CURRENCIES.name();
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.NER.name());
        properties.put(PropertyFields.INSTRUCTIONS.getField(), instructions);
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<ArrayList<Tag>> sentences = pipeline.getNamedEntities(text);
        
        for (ArrayList<Tag> sentence : sentences) {
            for (Tag tag : sentence) {
                Printer.print(tag.toString());
            }
            Printer.print();
        }
        
    }

    private void classifyArrayList() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog. Lazy dog doesn't wake up.";
        String instructions = NER.Instructions.PERSONS.name() + ", " + NER.Instructions.ORGANIZATIONS.name() + ", " + NER.Instructions.DATES.name() + ", " + NER.Instructions.CURRENCIES.name();
        String selectorPath = "";
        String patternPath = "";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.CLASSIFY.name());
        properties.put(PropertyFields.INSTRUCTIONS.getField(), instructions);
        properties.put(PropertyFields.EXPRESSIONS.getField(), selectorPath);
        properties.put(PropertyFields.PATTERNS.getField(), patternPath);
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<ArrayList<Token>> sentences = pipeline.lemmatize(text);

        for (ArrayList<Token> sentence : sentences) {
            for (Tag tag : pipeline.classify(sentence)) {
                Printer.print(tag.toString());
            }
            Printer.print();
        }

    }

    private void classifyString() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog.";
        String instructions = NER.Instructions.PERSONS.name() + ", " + NER.Instructions.ORGANIZATIONS.name() + ", " + NER.Instructions.DATES.name() + ", " + NER.Instructions.CURRENCIES.name();
        String selectorPath = "";
        String patternPath = "";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.CLASSIFY.name());
        properties.put(PropertyFields.INSTRUCTIONS.getField(), instructions);
        properties.put(PropertyFields.EXPRESSIONS.getField(), selectorPath);
        properties.put(PropertyFields.PATTERNS.getField(), patternPath);
        
        Pipeline pipeline = new Pipeline(properties);
        
        for (ArrayList<Tag> sentence : pipeline.classify(text)) {
            for (Tag tag : sentence) {
                Printer.print(tag.toString());
            }
            Printer.print();
        }
        
    }

    private void extractArrayList() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog. Lazy dog doesn't wake up.";
        String instructions = NER.Instructions.PERSONS.name() + ", " + NER.Instructions.ORGANIZATIONS.name() + ", " + NER.Instructions.DATES.name() + ", " + NER.Instructions.CURRENCIES.name();
        String frameSelectorPath = "";
        String framePath = "";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.CLASSIFY.name());
        properties.put(PropertyFields.INSTRUCTIONS.getField(), instructions);
        properties.put(PropertyFields.FRAME_EXPRESSIONS.getField(), frameSelectorPath);
        properties.put(PropertyFields.FRAMES.getField(), framePath);
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<ArrayList<Token>> sentences = pipeline.lemmatize(text);

        for (ArrayList<Token> sentence : sentences) {
            ArrayList<Extract> extracts = pipeline.extract(sentence);
            for (Extract extract : extracts) {
                Printer.print(extract.toString());
            }
            Printer.print();
        }

    }

    private void extractString() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog.";
        String instructions = NER.Instructions.PERSONS.name() + ", " + NER.Instructions.ORGANIZATIONS.name() + ", " + NER.Instructions.DATES.name() + ", " + NER.Instructions.CURRENCIES.name();
        String frameSelectorPath = "";
        String framePath = "";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.EXTRACT.name());
        properties.put(PropertyFields.INSTRUCTIONS.getField(), instructions);
        properties.put(PropertyFields.FRAME_EXPRESSIONS.getField(), frameSelectorPath);
        properties.put(PropertyFields.FRAMES.getField(), framePath);
        
        Pipeline pipeline = new Pipeline(properties);
        
        for (ArrayList<Extract> extracts : pipeline.extract(text)) {
            for (Extract extract : extracts) {
                Printer.print(extract.toString());
            }
            Printer.print();
        }
        
    }

}
