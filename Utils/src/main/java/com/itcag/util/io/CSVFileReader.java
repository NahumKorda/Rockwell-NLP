package com.itcag.util.io;

import com.opencsv.CSVReader;

import java.io.FileReader;

import java.util.ArrayList;

public class CSVFileReader {

    public final static ArrayList<String[]> read(String filePath) throws Exception {
        
        ArrayList<String[]> retVal = new ArrayList<>();

        CSVReader reader = new CSVReader(new FileReader(filePath));
        
        String[] line;
        while ((line = reader.readNext()) != null) {
            retVal.add(line);
        }
        
        return retVal;

    }
    
}
