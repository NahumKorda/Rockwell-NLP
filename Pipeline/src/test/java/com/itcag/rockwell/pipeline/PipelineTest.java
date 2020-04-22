package com.itcag.rockwell.pipeline;

import com.itcag.rockwell.lang.Extract;
import com.itcag.rockwell.lang.Semtoken;
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
    public void testSplit() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog. Lazy dog doesn't wake up.";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.SPLIT.name());
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<StringBuilder> sentences = pipeline.split(text);
        
        for (StringBuilder sentence : sentences) {
            System.out.println(sentence.toString());
        }
        
    }

    @Test
    public void testTokenize_StringBuilder() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog. Lazy dog doesn't wake up.";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.TOKENIZE.name());
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<StringBuilder> sentences = pipeline.split(text);

        for (StringBuilder sentence : sentences) {

            ArrayList<String> tokens = pipeline.tokenize(sentence);

            for (String token : tokens) {
                System.out.println(token);
            }

        }

    }

    @Test
    public void testTokenize_String() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog.";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.TOKENIZE.name());
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<ArrayList<String>> tokens = pipeline.tokenize(text);
        
        for (ArrayList<String> sentence : tokens) {
            for (String token : sentence) {
                Printer.print(token);
            }
            Printer.print();
        }
        
    }

    @Test
    public void testLemmatize_ArrayList() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog. Lazy dog doesn't wake up.";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.TOKENIZE.name());
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<StringBuilder> sentences = pipeline.split(text);

        for (StringBuilder sentence : sentences) {

            ArrayList<String> tokenStrings = pipeline.tokenize(sentence);
            ArrayList<Token> tokens = pipeline.lemmatize(tokenStrings);

            TokenPrinter.printTokensWithPOS(tokens);

        }

    }

    @Test
    public void testLemmatize_String() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog.";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.LEMMATIZE.name());
        
        Pipeline pipeline = new Pipeline(properties);
        ArrayList<ArrayList<Token>> tokens = pipeline.lemmatize(text);
        
        for (ArrayList<Token> sentence : tokens) {
            TokenPrinter.printTokensWithPOS(sentence);
            Printer.print();
        }
        
    }

    @Test
    public void testInsertNamedEntities_ArrayList() throws Exception {
        
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

    @Test
    public void testInsertNamedEntities_String() throws Exception {
        
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

    @Test
    public void testGetNamedEntities_ArrayList() throws Exception {
        
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

    @Test
    public void testGetNamedEntities_String() throws Exception {
        
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

    @Test
    public void testClassify_ArrayList() throws Exception {
        
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

    @Test
    public void testClassify_String() throws Exception {
        
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

    @Test
    public void testExtract_ArrayList() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog. Lazy dog doesn't wake up.";
        String instructions = NER.Instructions.PERSONS.name() + ", " + NER.Instructions.ORGANIZATIONS.name() + ", " + NER.Instructions.DATES.name() + ", " + NER.Instructions.CURRENCIES.name();
        String frameSelectorPath = "";
        String framePath = "";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.CLASSIFY.name());
        properties.put(PropertyFields.INSTRUCTIONS.getField(), instructions);
        properties.put(PropertyFields.FRAME_EXPRESSION_PATH.getField(), frameSelectorPath);
        properties.put(PropertyFields.FRAME_PATH.getField(), framePath);
        
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

    @Test
    public void testExtract_String() throws Exception {
        
        String text = "The quick brown fox jumps over the lazy dog.";
        String instructions = NER.Instructions.PERSONS.name() + ", " + NER.Instructions.ORGANIZATIONS.name() + ", " + NER.Instructions.DATES.name() + ", " + NER.Instructions.CURRENCIES.name();
        String frameSelectorPath = "";
        String framePath = "";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.EXTRACT.name());
        properties.put(PropertyFields.INSTRUCTIONS.getField(), instructions);
        properties.put(PropertyFields.FRAME_EXPRESSION_PATH.getField(), frameSelectorPath);
        properties.put(PropertyFields.FRAME_PATH.getField(), framePath);
        
        Pipeline pipeline = new Pipeline(properties);
        
        for (ArrayList<Extract> extracts : pipeline.extract(text)) {
            for (Extract extract : extracts) {
                Printer.print(extract.toString());
            }
            Printer.print();
        }
        
    }
    
}
