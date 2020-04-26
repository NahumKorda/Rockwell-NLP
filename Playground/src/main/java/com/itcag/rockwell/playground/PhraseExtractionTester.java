package com.itcag.rockwell.playground;

import com.itcag.rockwell.vocabulator.Exclusions;
import com.itcag.rockwell.vocabulator.PropertyFields;
import com.itcag.rockwell.vocabulator.VocabularyExtractor;
import com.itcag.util.Printer;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;
import java.util.Properties;

public class PhraseExtractionTester {

    public static void main(String[] args) throws Exception {

        String filePath = System.getProperty("user.home") + "/code/Rockwell-NLP/Playground/src/main/resources/data/techcrunch";
        
        int max = 6;
        int min = 0;

        String exclusions = Exclusions.STOPPHRASES.name();
        int threshold = 10;
        int trimThreshold = 2;

        String positive = "investment, invest, funding, funded";
        String negative = null;
        String required = "investment, invest, funding, funded";

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
        
        VocabularyExtractor extractor = new VocabularyExtractor(properties);

        ArrayList<String> lines = TextFileReader.read(filePath);
        for (String line : lines) {
            try {
                extractor.process(line);
            } catch (Exception ex) {
                Printer.print(ex.getMessage());
                Printer.print(line);
            }
        }
        
        extractor.print(false);
        
    }
    
}
