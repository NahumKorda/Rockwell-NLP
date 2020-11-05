package com.itcag.rockwell.split;

import com.itcag.english.LatinUnicodeStandardizer;
import com.itcag.util.Printer;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class SplitterTest {
    
    @Test
    public void testSplit() throws Exception {
        
        String text = "Report: GrubHub nears deal to be acquired by Just Eat Takeway.com.";
        
        Splitter splitter = new Splitter(new LatinUnicodeStandardizer(), false);
        ArrayList<String> sentences = splitter.split(text);
        
        for (String sentence : sentences) {
            Printer.print(sentence);
        }
        
    }
    
}
