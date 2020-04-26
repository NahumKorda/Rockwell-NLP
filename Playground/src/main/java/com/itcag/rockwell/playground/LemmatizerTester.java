package com.itcag.rockwell.playground;

import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.pipeline.Pipeline;
import com.itcag.rockwell.pipeline.PropertyFields;
import com.itcag.rockwell.tokenizer.Lemmatizer;
import com.itcag.rockwell.tokenizer.Tokenizer;
import com.itcag.rockwell.util.TokenPrinter;
import com.itcag.util.Printer;
import java.util.ArrayList;
import java.util.Properties;

public class LemmatizerTester {

    public static void main(String[] args) throws Exception {

        String test = "company raised $1.2 million in seed funding";
        
        Properties properties = new Properties();
        properties.put(PropertyFields.TASK.getField(), Pipeline.Tasks.LEMMATIZE.name());

        Pipeline pipeline = new Pipeline(properties);
        
        ArrayList<Token> tokens = pipeline.lemmatize(pipeline.tokenize(new StringBuilder(test)));

        TokenPrinter.printTokensWithPOS(tokens);
        
    }
    
}
