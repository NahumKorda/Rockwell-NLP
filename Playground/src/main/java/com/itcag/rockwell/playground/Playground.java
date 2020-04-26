package com.itcag.rockwell.playground;

import com.itcag.util.Printer;
import com.itcag.util.io.CSVFileReader;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.io.TextFileWriter;
import java.util.ArrayList;
import java.util.HashSet;

public class Playground {

    public static void main(String[] args) throws Exception {

        String sourcePath = "/home/nahum/Desktop/techcrunch";
        String targetPath = "/home/nahum/code/Rockwell-NLP/Playground/src/main/resources/data/techcrunch";
        
        ArrayList<String> lines = TextFileReader.read(sourcePath);
        
        TextFileWriter writer = new TextFileWriter(targetPath);

        for (String line : lines) {
//            writer.write(line);
        }

        writer.close();
        
    }
    
}
