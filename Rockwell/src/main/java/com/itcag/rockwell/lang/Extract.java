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

package com.itcag.rockwell.lang;

/**
 * <p>This class holds a datum extracted from a sentence by a Rockwell frame.</p>
 */
public class Extract {

    private final String script;
    
    private final String meaning;
    
    private String value;
    private String sentenceId = null;
    
    /**
     * @param script String holding the Rockwell frame that extracted the datum.
     * @param meaning String holding the meaning value of the Rockwell frame. The meaning indicates what the extracted datum is. 
     * @param value String holding the extracted datum.
     */
    public Extract(String script, String meaning, String value) {
        this.script = script;
        this.meaning = meaning;
        this.value = value;
    }

    /**
     * @return String holding the Rockwell frame that extracted the datum.
     */
    public String getScript() {
        return this.script;
    }
    
    /**
     * @return String holding the meaning value of the Rockwell frame. The meaning indicates what the extracted datum is. 
     */
    public String getMeaning() {
        return meaning;
    }

    /**
     * @return String holding the extracted datum.
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    /**
     * @return String holding the ID of the sentence from which the datum was extracted.
     */
    public String getSentenceId() {
        return this.sentenceId;
    }
    
    /**
     * @param sentenceId String holding the ID of the sentence from which the datum was extracted.
     */
    public void setSentenceId(String sentenceId) {
        this.sentenceId = sentenceId;
    }
    
    @Override
    public String toString() {
        return this.value + " [" + this.meaning + "]";
    }
    
}
