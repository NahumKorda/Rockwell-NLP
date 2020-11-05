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

package com.itcag.multilingual;

public interface NumericalExpressionDetector {

    /**
     * Enumerates types of recognized numerical expressions.
     */
    public enum Type {
        /** Only number - expressed as digits, fraction or with words. */
        NUMBER,
        /** Currency sign appended to a number. */
        CURRENCY,
        /** Percent or per mille sign appended to a number. */
        PERCENTAGE,
        /** Measuring unit appended to a number. */
        QUANTITY,
    }
    
    public boolean split(char[] chars);
    public Type getType();
    public Number getNumber();
    public String getNumberAsString();
    public String getPrefix();
    public String getSuffix();
    public boolean getCca();
    public boolean getPlusMinus();
    
}
