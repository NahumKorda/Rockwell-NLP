package com.itcag.rockwell.playground;

import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.pipeline.Pipeline;
import com.itcag.rockwell.pipeline.PropertyFields;
import com.itcag.util.Printer;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Demonstrates how to extract samples for the evaluation of classification.
 * Instead of processing entire articles one by one,
 * each article is first split into individual sentences,
 * and each sentence is the processed separately.
 * This enables validating the classification tag
 * against the exact sentence in which it was identified.
 */
public class ClassificationSampler {

    public static void main(String[] args) {
        
        /**
         * File holding TechCrunch articles.
         */
        String filePath = System.getProperty("user.home") + "/code/Rockwell-NLP/Playground/src/main/resources/data/techcrunch";
        
        /**
         * Additional Rockwell patterns.
         */
        String patterns = System.getProperty("user.home") + "/code/Rockwell-NLP/Playground/src/main/resources/script/patterns";
        
        /**
         * Rockwell expressions used for classification.
         */
        String concepts = System.getProperty("user.home") + "/code/Rockwell-NLP/Playground/src/main/resources/script/concepts";
        
        /**
         * Rockwell expressions used for classification.
         */
        String expressions = System.getProperty("user.home") + "/code/Rockwell-NLP/Playground/src/main/resources/script/expressions";
        
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
             * Iterate over articles in the file.
             */
            for (String line : TextFileReader.read(filePath)) {

                try {
                    
                    if (TextToolbox.isEmpty(line)) continue;

                    /**
                     * Split the article into individual sentences.
                     */
                    for (StringBuilder sentence : pipeline.split(line)) {
                        
                        /**
                         * Classification results are returned
                         * as list of tags - separately for
                         * every sentence in the article.
                         */
                        ArrayList<Token> tokens = pipeline.lemmatize(pipeline.tokenize(sentence));
                        ArrayList<Tag> tags = pipeline.classify(tokens);
                        tags.forEach((tag) -> {
                            Printer.print(tag.getTag() + "\t" + sentence);
                        });
                    
                    }

                } catch (Exception ex) {
                    Printer.printException(ex);
                }

            }

        } catch (Exception ex) {
            Printer.printException(ex);
        }
        
    }
    
}
