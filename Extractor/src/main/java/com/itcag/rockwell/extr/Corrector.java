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

package com.itcag.rockwell.extr;

import com.itcag.rockwell.lang.Tag;

import java.util.ArrayList;

/**
 * <p>This handles the case where an edge and a tag overlap.</p>
 * <p>For example: "Happiest Minds looks to acquire small firms".</p>
 * <p>Given that the "looks to acquire" is identified as an edge, and that it is expected to be preceded by a noun phrase, "Happiest Minds looks" will be extracted, since "looks" can also be a noun. Nonetheless, "looks" is removed, since it is identified as a constituent of the edge.</p>
 */
public class Corrector {

    private final Frames frames;

    /**
     * 
     * @param frames Instance of the {@link Frames} class used to identify edges. 
     */
    public Corrector(Frames frames) {
        this.frames = frames;
    }

    /**
     * 
     * @param tags List of extracted tags.
     * @throws Exception if anything goes wrong.
     */
    public void correct(ArrayList<Tag> tags) throws Exception {
        
        for (Tag tag : tags) {
            
            if (this.frames.isEdge(tag)) {
                inspect(tag, tags);
            }
        
        }
        
    }
    
    private void inspect (Tag left, ArrayList<Tag> tags) throws Exception {
        
        for (Tag right : tags) {
            if (right.equals(left)) continue;
            if (right.getStart() <= left.getEnd() && right.getStart() >= left.getStart()) right.setStart(left.getEnd() + 1);
            if (right.getEnd() >= left.getStart() && right.getEnd() <= left.getEnd()) right.setEnd(left.getStart() - 1);
        }
        
    }
    
    
}
