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
 * <p>Tag holds the coordinates (i.e. index of the first and the last token) of a Rockwell expression identified in a sentence.</p>
 * <p>Rockwell expression consists of an instruction and a tag. Tag is just a string indicating the meaning of the identified expression. For example, if an expression identifies a date, its tag could be "date". In addition to the coordinates of the identified expression, this class holds also its tag.</p>
 */
public final class Tag {
    
    private final String tag;

    private final String script;

    private String sentenceId = null;
    private final int start;
    private final int end;

    /**
     * @param tag String indicating the meaning of the expression identified in a sentence. For example, if the expression identifies a date, its tag could be "date".
     * @param script The complete expression formatted in the Rockwell script.
     * @param start Index of the first token identified by the expression.
     * @param end Index of the last token identified by the expression.
     */
    public Tag(String tag, String script, int start, int end) {
        this.tag = tag;
        this.script = script;
        this.start = start;
        this.end = end;
    }
    
    /**
     * @return String indicating the meaning of the expression identified in a sentence. For example, if the expression identifies a date, its tag could be "date".
     */
    public final String getTag() {
        return tag;
    }
    
    /**
     * @return The complete expression formatted in the Rockwell script.
     */
    public final String getScript() {
        return script;
    }

    /**
     * @return String holding the ID of the sentence in which this tag was identified.
     */
    public String getSentenceId() {
        return sentenceId;
    }

    /**
     * @param sentenceId String holding the ID of the sentence in which this tag was identified.
     */
    public void setSentenceId(String sentenceId) {
        this.sentenceId = sentenceId;
    }
    
    /**
     * @return Index of the first token identified by the expression.
     */
    public final int getStart() {
        return start;
    }
    
    /**
     * @return Index of the last token identified by the expression.
     */
    public final int getEnd() {
        return end;
    }
    
    @Override
    public final String toString() {
        return script + " (" + start + " -> " + end + ")";
    }

}
