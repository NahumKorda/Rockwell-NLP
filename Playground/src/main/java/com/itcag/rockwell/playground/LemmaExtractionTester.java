package com.itcag.rockwell.playground;

import com.itcag.rockwell.vocabulator.Exclusions;
import com.itcag.rockwell.vocabulator.PropertyFields;
import com.itcag.rockwell.vocabulator.VocabularyExtractor;
import com.itcag.util.Printer;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;
import java.util.Properties;

public class LemmaExtractionTester {

    public static void main(String[] args) throws Exception {
        
        String folderPath = System.getProperty("user.home") + "/Desktop/reviews/";
        String filePath = System.getProperty("user.home") + "/code/Rockwell-NLP/Playground/src/main/resources/data/techcrunch";
        
        String exclusions = Exclusions.STOPWORDS.name() + "," + Exclusions.CONTRACTIONS.name() + "," + Exclusions.SYMBOLS.name() + "," + Exclusions.DIGITS.name();
        int threshold = 100;

        String positive = "fund,funding,invest,investment";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), VocabularyExtractor.Tasks.EXTRACT_LEMMAS.name());
        if (positive != null) {
            properties.put(PropertyFields.POSITIVE_FILTER.getField(), positive);
        }
        properties.put(PropertyFields.EXCLUSIONS.getField(), exclusions);
        properties.put(PropertyFields.THRESHOLD.getField(), Integer.toString(threshold));
        
        VocabularyExtractor extractor = new VocabularyExtractor(properties);
        
        ArrayList<String> lines = TextFileReader.read(filePath);
        for (String line : lines) {
            try {
                extractor.process(line);
            } catch (Exception ex) {
                Printer.print(ex.getMessage());
            }
        }
        
        extractor.print(false);
        
    }
    
}
