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

/**
 * <p>This class holds a component of a Rockwell script instruction that rejects a match validated by an accepting condition.</p>
 * <p>This class extends the {@link com.itcag.rockwell.tagger.lang.Condition Condition} class.</p>
 * <p>To learn more about Rockwell script see: <a href="https://docs.google.com/document/d/1wMYCXAOm0cmJ4z5PHLRK5JyPsBY-p3vEYnupik-QT-A/edit?usp=sharing"  target="_blank">Rockwell Script (User Manual)</a>.</p>
 */
public class RejectingCondition extends Condition {
    
    private final String idToBeRejected;
    
    /**
     * @param script Rockwell expression formulated in Rockwell script. Kept as reference to the expression from which the condition was created.
     * @param idToBeRejected String holding the ID of the corresponding accepting condition.
     */
    public RejectingCondition(String script, String idToBeRejected) {
        super(script);
        this.idToBeRejected = idToBeRejected;
    }
    
    /**
     * @return String holding the ID of the corresponding accepting condition.
     */
    public String getIdToBeRejected() {
        return this.idToBeRejected;
    }
    
}
