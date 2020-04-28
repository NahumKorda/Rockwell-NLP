package com.itcag.rockwell.playground;

import com.itcag.rockwell.lang.Extract;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.pipeline.Pipeline;
import com.itcag.rockwell.pipeline.PropertyFields;
import com.itcag.util.Printer;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;
import java.util.Properties;

public class ExtractorTester {

    public static void main(String[] args) throws Exception {
        
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
         * Rockwell expressions used for extraction.
         */
        String expressions = "/home/nahum/code/Rockwell-NLP/Playground/src/main/resources/script/frameExpressions";
        
        /**
         * Rockwell expressions used for extraction.
         */
        String frames = "/home/nahum/code/Rockwell-NLP/Playground/src/main/resources/script/frames";
        
        /**
         * Configuration parameters.
         */
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.EXTRACT.name());
        properties.put(PropertyFields.PATTERNS.getField(), patterns);
        properties.put(PropertyFields.CONCEPTS.getField(), concepts);
        properties.put(PropertyFields.FRAME_EXPRESSIONS.getField(), expressions);
        properties.put(PropertyFields.FRAMES.getField(), frames);
        
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
                        
                        ArrayList<Token> tokens = pipeline.lemmatize(pipeline.tokenize(sentence));
                        ArrayList<Extract> extracts = pipeline.extract(tokens);
                        extracts.forEach((extract) -> {
                            Printer.print(extract.toString() + "\t" + sentence);
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
