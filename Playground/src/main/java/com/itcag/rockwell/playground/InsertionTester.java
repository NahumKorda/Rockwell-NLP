package com.itcag.rockwell.playground;

import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.pipeline.Pipeline;
import com.itcag.rockwell.pipeline.PropertyFields;
import com.itcag.rockwell.util.TokenPrinter;
import java.util.ArrayList;
import java.util.Properties;

public class InsertionTester {

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
         * Rockwell expressions used for classification.
         */
        String expressions = "/home/nahum/code/Rockwell-NLP/Playground/src/main/resources/script/expressions";
        
        /**
         * Configuration parameters.
         */
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.INSERT_CONCEPTS.name());
        properties.put(PropertyFields.PATTERNS.getField(), patterns);
        properties.put(PropertyFields.CONCEPTS.getField(), concepts);
        properties.put(PropertyFields.EXPRESSIONS.getField(), expressions);

        Pipeline pipeline = new Pipeline(properties);

        for (ArrayList<Token> tokens : pipeline.insertConcepts("Announced a merger.")) {
            TokenPrinter.printTokensWithPOSAndRole(tokens);
        }
        
    }
    
}
