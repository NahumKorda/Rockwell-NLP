package com.itcag.rockwell.playground;

import com.itcag.rockwell.vocabulator.Exclusions;
import com.itcag.rockwell.vocabulator.PropertyFields;
import com.itcag.rockwell.vocabulator.VocabularyExtractor;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;
import java.util.Properties;

public class SingleWordExtractionTester {

    public static void main(String[] args) throws Exception {
        
        String filePath = System.getProperty("user.home") + "/code/Rockwell-NLP/Playground/src/main/resources/data/techcrunch";
        
        int threshold = 1000;
        String exclusions = Exclusions.STOPWORDS.name() + "," + Exclusions.CONTRACTIONS.name() + "," + Exclusions.SYMBOLS.name() + "," + Exclusions.DIGITS.name();
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), VocabularyExtractor.Tasks.EXTRACT_SINGLE_WORDS.name());
        properties.put(PropertyFields.EXCLUSIONS.getField(), exclusions);
        properties.put(PropertyFields.THRESHOLD.getField(), Integer.toString(threshold));
        
        VocabularyExtractor extractor = new VocabularyExtractor(properties);
        
        ArrayList<String> lines = TextFileReader.read(filePath);
        for (String line : lines) {
            extractor.process(line);
        }
        
        extractor.print(false);
        
    }
    
}
