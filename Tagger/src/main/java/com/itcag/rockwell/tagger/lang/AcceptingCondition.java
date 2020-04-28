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

package com.itcag.rockwell.tagger.lang;

import java.util.ArrayList;

/**
 * <p>This class holds the component of a Rockwell script instruction that must be satisfied, in order to validate a match. An instruction consists of one or more conditions. The first condition is the accepting condition.</p>
 * <p>This class extends the {@link com.itcag.rockwell.tagger.lang.Condition Condition} class.</p>
 * <p>To learn more about Rockwell script see: <a href="https://docs.google.com/document/d/1wMYCXAOm0cmJ4z5PHLRK5JyPsBY-p3vEYnupik-QT-A/edit?usp=sharing"  target="_blank">Rockwell Script (User Manual)</a>.</p>
 */
public final class AcceptingCondition extends Condition {

    private final ArrayList<RejectingCondition> rejecting = new ArrayList<>();

    private final ArrayList<String> rejectedById = new ArrayList<>();

    private String tag = null;

    /**
     * @param script Rockwell expression formulated in Rockwell script. Kept as reference to the expression from which the condition was created.
     */
    public AcceptingCondition(String script) {
        super(script);
    }

    /**
     * @return Array list containing instances of the {@link com.itcag.rockwell.tagger.lang.RejectingCondition RejectingCondition} class. Even if validated by the accepting condition, a match is nonetheless rejected if any of the rejecting conditions is also satisfied.
     */
    public final ArrayList<RejectingCondition> getRejecting() {
        return this.rejecting;
    }

    /**
     * @param rejecting An instance of the {@link com.itcag.rockwell.tagger.lang.RejectingCondition RejectingCondition} class. Even if validated by the accepting condition, a match is nonetheless rejected if any of the rejecting conditions is also satisfied.
     */
    public final void addRejecting(RejectingCondition rejecting) {
        this.rejecting.add(rejecting);
    }

    /**
     * @return Array list containing IDs of the rejecting conditions.
     */
    public ArrayList<String> getRejectedById() {
        return this.rejectedById;
    }

    /**
     * @param rejectedById String holding ID of a rejecting condition.
     */
    public void addRejectedById(String rejectedById) {
        this.rejectedById.add(rejectedById);
    }

    /**
     * @return String holding the tag component of a Rockwell expression. Rockwell expression consists of two components: instruction, from which the conditions are created, and the tag, which is a string that assign some meaning to the condition.
     */
    public final String getTag() {
        return tag;
    }

    /**
     * @param tag String holding the tag component of a Rockwell expression. Rockwell expression consists of two components: instruction, from which the conditions are created, and the tag, which is a string that assign some meaning to the condition.
     */
    public final void setTag(String tag) {
        this.tag = tag;
    }
    
}
