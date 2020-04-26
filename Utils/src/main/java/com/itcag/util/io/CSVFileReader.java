/*
 *
 * Copyright 2020 IT Consulting AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
