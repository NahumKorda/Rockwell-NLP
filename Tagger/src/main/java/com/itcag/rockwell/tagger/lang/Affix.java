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
 * <p>This class holds a Rockwell affix. A Rockwell affix is any substring of the original string delimited in respect to one or two tokens called anchors. If a Rockwell affix is delimited in respect to a single anchor, then it can be either a prefix (containing all tokens preceding the anchor) or a suffix (containing all tokens following the anchor). If delimited by two anchors, the affix is an infix (contains all tokens between the anchors). Affixes can be empty strings. Affixes may or may not contain anchors.</p>
 * <p>To learn more about affixes see: <a href="https://docs.google.com/document/d/1wMYCXAOm0cmJ4z5PHLRK5JyPsBY-p3vEYnupik-QT-A/edit?usp=sharing"  target="_blank">Rockwell Script (User Manual)</a>.</p>
 */
public class Affix extends MatchingSpecification {
    
    private boolean inclusive = false;
    public boolean complete = false;
    public boolean optional = false;

    /**
     * @param aspect Value of the {@link com.itcag.rockwell.tagger.lang.MatchingSpecification.Aspect Aspect} enum that specifies which apsect of a token must be matched.
     * @param value String holding the value that must be matched. This value depends on the aspect.
     */
    public Affix(Aspect aspect, String value) {
        super(aspect, value);
    }
    
    /**
     * @return Boolean indicating whether the anchor token should be included in the affix.
     */
    public boolean isInclusive() {
        return this.inclusive;
    }
    
    /**
     * @param inclusive Boolean indicating whether the anchor token should be included in the affix.
     */
    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }

    /**
     * @return Boolean indicating whether the entire affix should be matched (or only the part adjacent to the anchor).
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * @param complete Boolean indicating whether the entire affix should be matched (or only the part adjacent to the anchor).
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * @return Boolean indicating whether an empty affix should be considered a valid match.
     */
    public boolean isOptional() {
        return optional;
    }

    /**
     * @param optional Boolean indicating whether an empty affix should be considered a valid match.
     */
    public void setOptional(boolean optional) {
        this.optional = optional;
    }
    
}
