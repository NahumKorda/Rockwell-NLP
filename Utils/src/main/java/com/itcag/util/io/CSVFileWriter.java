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

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class CSVFileWriter {

    public final static void append(String filePath, String[] line) throws Exception {
        
        File file = new File(filePath);
        if (!file.exists()) file.createNewFile();
        
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        try (CSVWriter csv = new CSVWriter(bufferedWriter)) {
            csv.writeNext(line);
            csv.flush();
        }
        
    }

    public final static void reset(String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) file.delete();
        file.createNewFile();
    }
    
}
