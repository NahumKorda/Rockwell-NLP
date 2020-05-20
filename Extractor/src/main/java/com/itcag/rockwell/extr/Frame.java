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

/**
 * <p>This class holds a Rocwkell frame used to extract data from text.</p>
 * <p>To learn more about Rockwell frames see: <a href="https://docs.google.com/document/d/16ehTwHFVetysFeySPHOQ8aue64FrN-F5dwVi2xKFVVc/edit#heading=h.d8ot297jcp4z" target="_blank">Rockwell Frames (User Manual)</a>.</p>
 */
public class Frame {

    /**
     * <p>Rockwell frame is defined by its edges. -- The datum of interest is enclosed within these edges. This class holds such an edge.</p>
     */
    public class Edge {
        
        private final String tag;
        private final boolean included;
        
        private Edge(String tag, boolean included) {
            this.tag = tag;
            this.included = included;
        }
        
        /**
         * @return String holding the tag of a Rockwell frame expression. Frame expressions identify frame edges, and convert them to {@link com.itcag.rockwell.lang.Semtoken semtokens}. The tag of such expressions becomes the value of the {@link com.itcag.rockwell.lang.Semtoken#roles role property} of the corresponding semtoken.
         */
        public String getTag() {
            return this.tag;
        }
        
        /**
         * @return Boolean indicating whether the edge should be included in the extracted datum.
         */
        public boolean isIncluded() {
            return this.included;
        }
        
    }

    private final String script;
    
    private final Edge from;
    
    private final Edge until;
    
    private final String condition;
    
    private final String meaning;

    /**
     * @param script String holding the script that describes a Rockwell frame.
     * @throws Exception if anything goes wrong.
     */
    public Frame(String script) throws Exception {
    
        this.script = script;
        
        this.from = getEdge(script, "from:");
        this.until = getEdge(script, "until:");
        
        this.condition = getValue(script, "if:");
        
        this.meaning = getValue(script, "meaning:");
        if (this.meaning == null) throw new IllegalArgumentException("Invalid script (meaning is missing): " + script);

        if (this.from == null && this.until == null) throw new IllegalArgumentException("Invalid script (borderlines not defined): " + script);
    
    }

    private Edge getEdge(String script, String field) throws Exception {
        
        String[] elts = script.split(",");
        for (String elt : elts) {
            elt = elt.toLowerCase().trim();
            if (elt.startsWith(field)) {
                elt = elt.replace(field, "").trim();
                if (elt.isEmpty()) {
                    return null;
                } else {
                    boolean included = false;
                    if (elt.contains(" incl")) {
                        included = true;
                        elt = elt.replace(" incl", "").trim();
                    }
                    Edge retVal = new Edge(elt, included);
                    return retVal;
                }
            }
        }

        return null;
        
    }

    private String getValue(String script, String field) throws Exception {
        
        String[] elts = script.split(",");
        for (String elt : elts) {
            elt = elt.trim();
            if (elt.startsWith(field)) {
                elt = elt.replace(field, "").trim();
                if (elt.isEmpty()) {
                    return null;
                } else {
                    return elt;
                }
            }
        }

        return null;
        
    }

    /**
     * @return String holding the script that describes a Rockwell frame.
     */
    public String getScript() {
        return this.script;
    }
    
    /**
     * 
     * @return Instance of the {@link Edge} class holding the left edge of the frame.
     */
    public Edge getFrom() {
        return from;
    }

    /**
     * @return Instance of the {@link Edge} class holding the right edge of the frame.
     */
    public Edge getUntil() {
        return until;
    }

    /**
     * @return String holding the frame condition. Condition is an optional frame element that holds a tag of Rockwell expression. The extracted datum is validated if it is identified by the corresponding Rockwell expression.
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @return String holding the meaning of the extracted datum.
     */
    public String getMeaning() {
        return meaning;
    }
    
}
