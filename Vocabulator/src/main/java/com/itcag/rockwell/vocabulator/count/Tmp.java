package com.itcag.rockwell.vocabulator.count;

import com.itcag.util.io.TextFileReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Tmp {

    private static String sourceFolder = "/home/nahum/Desktop/reviews/";
    private static String targetFolder = "/home/nahum/Desktop/new reviews/";
    
    private static int cur = 0;

    public static void main(String[] args) throws Exception {

        processFolder(sourceFolder);
        
    }
    
    public static void processFolder(String folderPath) throws Exception {
        
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isFile()) processFile(file.getPath());
            if (file.isDirectory()) processFolder(file.getPath());
        }

    }
    
    public static void processFile(String filePath) throws Exception {

        ArrayList<String> lines = TextFileReader.read(filePath);
        for (String line : lines) {
            line = line + "\n";
            try (OutputStream os = new FileOutputStream(new File(targetFolder + Integer.toString(cur) + ".txt"), true)) {
                os.write(line.getBytes(), 0, line.length());
            }
            cur++;
            if (cur >= 10000) cur = 0;
        }

    }
    
}
