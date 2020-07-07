package com.itcag.rockwell.vocabulator.phraser;

import com.itcag.util.io.TextFileReader;
import com.itcag.rockwell.vocabulator.Exclusions;
import com.itcag.rockwell.vocabulator.PropertyFields;
import com.itcag.rockwell.vocabulator.VocabularyExtractor;
import com.itcag.util.Printer;

import java.io.File;

import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class PhraserTest {

    private VocabularyExtractor extractor;
    
    @Test
    public void testProcess() throws Exception {

        String folderPath = System.getProperty("user.home") + "/Desktop/analyzerInput/business/titles";
        String filePath = System.getProperty("user.home") + "/code/Rockwell-NLP/Playground/src/main/resources/data/techcrunch";
        
        int max = 6;
        int min = 0;

//        String exclusions = Exclusions.STOPPHRASES.name() + ", " + Exclusions.ADJECTIVE_PHRASES.name() + ", " + Exclusions.VERB_PHRASES.name();
        String exclusions = Exclusions.STOPPHRASES.name();
        int threshold = 20;
        int trimThreshold = 2;

        
        /*
        String positive = "acquisition, acquired, acquiring, purchas";
        String negative = "talent acquisition, customer acquisition, user acquisition, content acquisition";
        String required = "acquisition, acquired, acquiring, purchas";
        */

        String positive = null;
        String negative = null;
        String required = null;

        /*
        String positive = "ipo,offering";
        String negative = null;
        String required = "ipo,offering";
        */

        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), VocabularyExtractor.Tasks.EXTRACT_PHRASES.name());
        
        properties.put(PropertyFields.MIN_PHRASE_LENGTH.getField(), Integer.toString(min));
        properties.put(PropertyFields.MAX_PHRASE_LENGTH.getField(), Integer.toString(max));
        
        properties.put(PropertyFields.EXCLUSIONS.getField(), exclusions);
        properties.put(PropertyFields.WORD_THRESHOLD.getField(), Integer.toString(threshold));
        properties.put(PropertyFields.TRIM_THRESHOLD.getField(), Integer.toString(trimThreshold));
        
        if (positive != null) {
            properties.put(PropertyFields.POSITIVE_FILTER.getField(), positive);
        }
        
        if (negative != null) {
            properties.put(PropertyFields.NEGATIVE_FILTER.getField(), negative);
        }
        
        if (required != null) {
            properties.put(PropertyFields.REQUIRED_FILTER.getField(), required);
        }
        
        this.extractor = new VocabularyExtractor(properties);

        processFolder(folderPath);
        folderPath = System.getProperty("user.home") + "/Desktop/analyzerInput/business/descriptions";
        processFolder(folderPath);
//        processFile(filePath);
        
        extractor.print(false);
        
    }

    private void processFolder(String folderPath) throws Exception {
        
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isFile()) processFile(file.getPath());
            if (file.isDirectory()) processFolder(file.getPath());
        }

    }
    
    private void processFile(String filePath) throws Exception {

        int count = 0;
        
        ArrayList<String> lines = TextFileReader.read(filePath);
        for (String line : lines) {
            Printer.print(Integer.toString(count++));
            try {
                this.extractor.process(line);
            } catch (Exception ex) {
                Printer.print(ex.getMessage());
                Printer.print(line);
            }
        }

        Printer.print(Integer.toString(this.extractor.count()));
        Printer.print("\t" + Integer.toString(this.extractor.indexSize()));
        this.extractor.trim();
        Printer.print("\t" + Integer.toString(this.extractor.indexSize()));
        Printer.print();
        
    }
    
}
