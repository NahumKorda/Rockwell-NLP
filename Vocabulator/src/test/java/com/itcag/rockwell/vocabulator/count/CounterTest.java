package com.itcag.rockwell.vocabulator.count;

import com.itcag.rockwell.vocabulator.PropertyFields;
import com.itcag.rockwell.vocabulator.VocabularyExtractor;
import com.itcag.util.io.TextFileReader;

import java.io.File;

import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class CounterTest {
    
    private Counter counter;

    @Test
    public void testProcess() throws Exception {

        String folderPath = "/home/nahum/Desktop/reviews/";

        String positive = "smell, scent, fragrance";

        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), VocabularyExtractor.Tasks.EXTRACT_PHRASES.name());

        properties.put(PropertyFields.POSITIVE_FILTER.getField(), positive);

        this.counter = new Counter(properties);
        
        processFolder(folderPath);
        
        this.counter.print(true);
        
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
            this.counter.process(line);
        }

    }
    
}
