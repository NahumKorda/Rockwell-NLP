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

package com.itcag.rockwell.semantex;

import com.itcag.rockwell.lang.Tag;

import java.util.ArrayList;

public final class Toolbox {

    public final ArrayList<Tag> consolidate(ArrayList<Tag> tags) {
        
        if (tags.isEmpty()) return tags;
        if (tags.size() == 1) return tags;

        ArrayList<Tag> retVal = new ArrayList<>();
        
        while (!tags.isEmpty()) {
            
            Tag test = tags.get(0);
            tags.remove(test);

            for (Tag tag : tags) {
                if (test.getStart() >= tag.getStart() && test.getEnd() < tag.getEnd()) {
                    test = null;
                    break;
                } else if (test.getStart() > tag.getStart() && test.getEnd() <= tag.getEnd()) {
                    test = null;
                    break;
                }
            }
            
            if (test != null) retVal.add(test);
            
        }
        
        return retVal;
        
    }
    
}
