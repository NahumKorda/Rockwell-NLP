package com.itcag.rockwell.playground;

import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.pipeline.Pipeline;
import com.itcag.rockwell.pipeline.PropertyFields;
import com.itcag.rockwell.util.TokenPrinter;

import java.util.ArrayList;
import java.util.Properties;

public class InsertionTester {

    public static void main(String[] args) throws Exception {

        String test = "company received by $1.2 million in funding";
        
        /**
         * Additional Rockwell patterns.
         */
        String patterns = "/home/nahum/code/Rockwell-NLP/Playground/src/main/resources/script/patterns";
        
        /**
         * Rockwell expressions used for classification.
         */
        String concepts = "/home/nahum/code/Rockwell-NLP/Playground/src/main/resources/script/concepts";
        
        /**
         * Configuration parameters.
         */
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.INSERT_CONCEPTS.name());
        properties.put(PropertyFields.PATTERNS.getField(), patterns);
        properties.put(PropertyFields.CONCEPTS.getField(), concepts);

        Pipeline pipeline = new Pipeline(properties);

        for (ArrayList<Token> tokens : pipeline.insertConcepts(test)) {
            TokenPrinter.printTokensWithPOSAndRole(tokens);
        }
        
    }
    
}
