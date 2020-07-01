package com.itcag.rockwell.split;

import com.itcag.util.Printer;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class SplitterTest {
    
    @Test
    public void testSplit() throws Exception {
        
        StringBuilder text = new StringBuilder("Report: GrubHub nears deal to be acquired by Just Eat Takeway.com...");
        
        Splitter splitter = new Splitter();
        ArrayList<StringBuilder> sentences = splitter.split(text);
        
        for (StringBuilder sentence : sentences) {
            Printer.print(sentence);
        }
        
    }
    
}
