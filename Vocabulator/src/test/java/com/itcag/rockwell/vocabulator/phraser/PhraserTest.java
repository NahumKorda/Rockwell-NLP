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

        String folderPath = "/home/nahum/Desktop/reviews/";
        
        int max = 6;
        int min = 0;

//        String exclusions = Exclusions.STOPPHRASES.name() + ", " + Exclusions.ADJECTIVE_PHRASES.name() + ", " + Exclusions.VERB_PHRASES.name();
        String exclusions = Exclusions.STOPPHRASES.name();
        int threshold = 10;
        int trimThreshold = 2;
        
        String positive = null;
        String negative = null;
        String required = null;
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), VocabularyExtractor.Tasks.EXTRACT_PHRASES.name());
        
        properties.put(PropertyFields.MIN_PHRASE_LENGTH.getField(), Integer.toString(min));
        properties.put(PropertyFields.MAX_PHRASE_LENGTH.getField(), Integer.toString(max));
        
        properties.put(PropertyFields.EXCLUSIONS.getField(), exclusions);
        properties.put(PropertyFields.THRESHOLD.getField(), Integer.toString(threshold));
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

//        processFolder(folderPath);
        processFile("/home/nahum/Desktop/negative statements");
        
        extractor.print(true);
        
    }

    private void processFolder(String folderPath) throws Exception {
        
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isFile()) processFile(file.getPath());
            if (file.isDirectory()) processFolder(file.getPath());
        }

    }
    
    private void processFile(String filePath) throws Exception {

        ArrayList<String> lines = TextFileReader.read(filePath);
        for (String line : lines) {
            this.extractor.process(line);
        }

        Printer.print(Integer.toString(this.extractor.count()));
        Printer.print("\t" + Integer.toString(this.extractor.indexSize()));
        this.extractor.trim();
        Printer.print("\t" + Integer.toString(this.extractor.indexSize()));
        Printer.print();
        
    }
    
}
