package com.itcag.rockwell.playground;

import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.pipeline.Pipeline;
import com.itcag.rockwell.pipeline.PropertyFields;
import com.itcag.util.MathToolbox;
import com.itcag.util.Printer;
import com.itcag.util.Stopwatch;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * Demonstrates the simplest way how to classify texts using Rockwell expressions.
 */
public class ClassificationTester {

    public static void main(String[] args) {
        
        /**
         * File holding TechCrunch articles.
         */
        String filePath = "/home/nahum/code/Rockwell-NLP/Playground/src/main/resources/data/techcrunch";
        
        /**
         * Additional Rockwell patterns.
         */
        String patterns = "/home/nahum/code/Rockwell-NLP/Playground/src/main/resources/script/patterns";
        
        /**
         * Rockwell expressions used for classification.
         */
        String concepts = "/home/nahum/code/Rockwell-NLP/Playground/src/main/resources/script/concepts";
        
        /**
         * Rockwell expressions used for classification.
         */
        String expressions = "/home/nahum/code/Rockwell-NLP/Playground/src/main/resources/script/expressions";
        
        /**
         * Configuration parameters.
         */
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.CLASSIFY.name());
        properties.put(PropertyFields.PATTERNS.getField(), patterns);
        properties.put(PropertyFields.CONCEPTS.getField(), concepts);
        properties.put(PropertyFields.EXPRESSIONS.getField(), expressions);
        
        try {
            
            /**
             * Use the configuration, in order to create a classification pipeline.
             */
            Pipeline pipeline = new Pipeline(properties);
            
            /**
             * Map that counts hits.
             */
            HashMap<String, Integer> counter = new HashMap<>();
            
            /**
             * Some performance data.
             */
            long corpusSize = 0;
            Stopwatch stopwatch = new Stopwatch();
            
            /**
             * Iterate over articles in the file.
             */
            for (String line : TextFileReader.read(filePath)) {

                try {
                    
                    if (TextToolbox.isEmpty(line)) continue;

                    corpusSize += line.getBytes().length;
                    
                    /**
                     * Classification results are returned
                     * as list of tags - separately for
                     * every sentence in the article.
                     */
                    ArrayList<ArrayList<Tag>> sentences = pipeline.classify(line);
                    for (ArrayList<Tag> sentence : sentences) {
                        for (Tag tag : sentence) {
                            if (counter.containsKey(tag.getTag())) {
                                counter.put(tag.getTag(), counter.get(tag.getTag()) + 1);
                            } else {
                                counter.put(tag.getTag(), 1);
                            }
                        }
                    }


                } catch (Exception ex) {
                    Printer.printException(ex);
                }

            }

            /**
             * Systemout the stats.
             */
            stopwatch.stop();
            double durationInSecs = stopwatch.duration() / 1000;
            double average = corpusSize / durationInSecs;
            Printer.print("Processed " + corpusSize + " bytes in " + MathToolbox.roundDouble(durationInSecs, 2) + " seconds (" + MathToolbox.roundDouble(average, 2) + " bytes/sec).");
            Printer.print("Duration: " + stopwatch.durationFormatted() + ".");
            counter.entrySet().forEach((entry) -> {
                Printer.print(entry.getValue() + "\t" + entry.getKey());
            });
            
        } catch (Exception ex) {
            Printer.printException(ex);
        }
        
    }
    
}
