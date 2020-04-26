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

package com.itcag.rockwell.tokenizer.res;

import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;

/**
 * <p>This class reads a text file from the local <i>Other Resources</i> folder, and returns an array list containing lines in the file.</p>
 */
public final class Loader {

    private final String basePath;
    
    public Loader() throws Exception {
        String path2JAR = Loader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String location = path2JAR.substring(0, path2JAR.lastIndexOf("/"));
        if (!location.endsWith("/")) location += "/";
        basePath = location;
    }
    
    /**
     * @param resource String holding the name of the local text file.
     * @return Array list containing lines in that file.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<String> load(String resource) throws Exception {
        
        String filePath = basePath + resource;

        ArrayList<String> retVal = new ArrayList<>();

        ArrayList<String> lines = TextFileReader.read(filePath);
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("#")) continue;
            retVal.add(line);
        }
    
        return retVal;
    
    }
    
}
