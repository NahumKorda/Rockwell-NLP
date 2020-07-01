package com.itcag.rockwell.vocabulator.word;

import com.itcag.rockwell.vocabulator.PropertyFields;
import com.itcag.util.io.TextFileReader;
import com.itcag.rockwell.vocabulator.Exclusions;
import com.itcag.rockwell.vocabulator.VocabularyExtractor;
import com.itcag.util.Printer;

import java.io.File;
import java.io.FileFilter;

import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class SingleWordExtractorTest {

    private VocabularyExtractor extractor;
    
    @Test
    public void testProcess() throws Exception {
        
        String folderPath = System.getProperty("user.home") + "/Desktop/analyzerInput/business/titles";
        String filePath = System.getProperty("user.home") + "/code/Rockwell-NLP/Playground/src/main/resources/data/techcrunch";
        
        int threshold = 100;
        String exclusions = Exclusions.STOPWORDS.name() + "," + Exclusions.CONTRACTIONS.name() + "," + Exclusions.SYMBOLS.name() + "," + Exclusions.DIGITS.name();
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), VocabularyExtractor.Tasks.EXTRACT_SINGLE_WORDS.name());
        properties.put(PropertyFields.EXCLUSIONS.getField(), exclusions);
        properties.put(PropertyFields.THRESHOLD.getField(), Integer.toString(threshold));
        
        this.extractor = new VocabularyExtractor(properties);
        
        processFolder(folderPath);
        folderPath = System.getProperty("user.home") + "/Desktop/analyzerInput/business/descriptions";
        processFolder(folderPath);
//        processFile(filePath);
        
        Printer.print();
        Printer.print("---------------------------------------------------");
        Printer.print();
        this.extractor.print(false);
        
    }

    public void processFolder(String folderPath) throws Exception {
        
        int count = 0;
        
        File folder = new File(folderPath);
        FileFilter filter = (File file) -> { return file.isFile(); };
        int total = folder.listFiles(filter).length;
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) continue;
            Printer.print(Integer.toString(++count) + "/" + total + "\t" + file.getName()+ "\t" + this.extractor.indexSize());
            processFile(file.getPath());
        }
        
    }
    
    public void processFile(String filePath) throws Exception {
        
        ArrayList<String> lines = TextFileReader.read(filePath);
        for (String line : lines) {
            try {
                this.extractor.process(line);
            } catch (Exception ex) {
                Printer.print(ex.getMessage());
                Printer.print(line);
            }
        }
        
        this.extractor.trim();
        
    }

}
