package com.itcag.rockwell.vocabulator.word;

import com.itcag.rockwell.vocabulator.PropertyFields;
import com.itcag.util.io.TextFileReader;
import com.itcag.rockwell.vocabulator.Exclusions;
import com.itcag.rockwell.vocabulator.VocabularyExtractor;
import com.itcag.util.Printer;

import java.io.File;

import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class LemmaExtractorTest {

    private VocabularyExtractor extractor;
    
    @Test
    public void testProcess() throws Exception {
        
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
        
        this.extractor = new VocabularyExtractor(properties);
        
//        processFolder(folderPath);
        processFile(filePath);
        
        this.extractor.print(true);
        
    }

    public void processFolder(String folderPath) throws Exception {
        
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isFile()) processFile(file.getPath());
            if (file.isDirectory()) processFolder(file.getPath());
        }

    }
    
    public void processFile(String filePath) throws Exception {

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
