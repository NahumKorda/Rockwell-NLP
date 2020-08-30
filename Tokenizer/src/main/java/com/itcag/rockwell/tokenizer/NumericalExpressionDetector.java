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

package com.itcag.rockwell.tokenizer;

import com.itcag.rockwell.tokenizer.res.LexicalResources;
import com.itcag.util.Converter;
import com.itcag.util.txt.TextToolbox;

/**
 * <p>Analyzes a single token (passed as an array of characters), and identifies:</p>
 * <ul>
 * <li>{@link Type#PERCENTAGE percentage} (percent/per mille sign + number),</li>
 * <li>{@link Type#QUANTITY quantity} (measuring unit + number),</li>
 * <li>{@link Type#CURRENCY currency} (currency symbol + number),</li>
 * <li>{@link Type#NUMBER number} (only number),</li>
 * </ul>
 * <p>If identified, currency, quantity and percentage are split into separate tokens (number and symbols as two separate tokens).</p>
 * <p>This class operates in the following manner:</p>
 * <ol>
 * <li>A token is converted to an array consisting of characters.</li>
 * <li>The array is passed to the {@link #split(char[])} method.</li>
 * <li>If a numerical expression was identified, this method returns TRUE, or FALSE otherwise.</li>
 * <li>If this method returns TRUE, the {@link #getNumber()}, {@link #getNumberAsString()}, {@link #getRest()} and {@link #getType()} methods are called, in order to retrieve the components of the numerical expression.</li>
 * </ol>
 */
public final class NumericalExpressionDetector {
    
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
    
    private final LexicalResources lexicalResources;

    private Type type = null;
    
    private Boolean negative = null;
    private Double number = null;
    
    private final StringBuilder digits = new StringBuilder();
    private final StringBuilder prefix = new StringBuilder();
    private final StringBuilder suffix = new StringBuilder();
    
    public NumericalExpressionDetector() throws Exception {
        this.lexicalResources = LexicalResources.getInstance();
    }
    
    /**
     * This method analyzes an array of characters representing a token, identifies numerical expressions, and splits them if required into a number and symbols.
     * @param chars Array of characters to be analyzed.
     * @return Boolean indicating whether one or more numerical expressions were identified.
     */
    public final boolean split(char[] chars) {
        
        if (chars.length == 0) return false;

        if (Character.isLetter(chars[0])) return false;

        for (int i = 0; i < chars.length; i++) {

            if (chars[i] == 43) {
                /**
                 * Plus sign.
                 */
                if (i == 0) {
                    this.negative = false;
                } else {
                    return false;
                }
            } else if (chars[i] == 45) {
                /**
                 * Minus sign.
                 */
                if (i == 0) {
                    this.negative = false;
                } else {
                    return false;
                }
            } else if (chars[i] == 44 || chars[i] == 46) {
                /**
                 * Comma and period.
                 */
                if (this.digits.length() == 0) return false;
                this.digits.append(chars[i]);
            } else if (Character.isDigit(chars[i])) {
                if (this.suffix.length() > 0) return false;
                this.digits.append(chars[i]);
            } else {
                if (digits.length() == 0) {
                    this.prefix.append(chars[i]);
                } else {
                    this.suffix.append(chars[i]);
                }
            }
            
        }
        
        /**
         * Ensure that the entire char array was analyzed.
         */
        int length = this.prefix.length() + this.digits.length() + this.suffix.length();
        if (this.negative != null) length++;
        if (length != chars.length) return false;
        
        if (!extractNumber()) return false;
        if (!resolvePrefix()) return false;
        if (!resolveSuffix()) return false;

        if (this.type == null) type = Type.NUMBER;

        return true;
        
    }

    private boolean extractNumber() {
        if (this.digits.indexOf(",") > -1) TextToolbox.replace(digits, ",", "");
        Double test = Converter.convertStringToDouble(this.digits.toString());
        if (test != null) {
            if (this.negative != null && this.negative) {
                this.number = -test;
            } else {
                this.number = test;
            }
            return true;
        } else {
            return false;
        }
    }
    
    private boolean resolvePrefix() {
        
        if (this.prefix.length() == 0) return true;

        if (this.lexicalResources.isCurrencySymbol(this.prefix.toString().toLowerCase())) {
            type = Type.CURRENCY;
            return true;
        }

        return false;
        
    }
    
    private boolean resolveSuffix() {

        if (this.suffix.length() == 0) return true;

        if ("k".equalsIgnoreCase(this.suffix.toString())) {
            this.number = this.number * 1000;
            return true;
        } else if ("m".equalsIgnoreCase(this.suffix.toString())) {
            this.number = this.number * 1000000;
            return true;
        } else if ("%".equalsIgnoreCase(this.suffix.toString())) {
            type = Type.PERCENTAGE;
            return true;
        } else if ("‰".equalsIgnoreCase(this.suffix.toString())) {
            type = Type.PERCENTAGE;
            return true;
        } else if (this.lexicalResources.isMeasuringUnit(this.suffix.toString().toLowerCase())) {
            type = Type.QUANTITY;
            return true;
        }

        return false;

    }
    
    /**
     * Used if the {@link #split(char[])} method returned TRUE.
     * @return Number holding the number part pf the token.
     */
    public Number getNumber() {
        return number;
    }

    /**
     * Used if the {@link #split(char[])} method returned TRUE.
     * @return String holding the number part of the token.
     */
    public String getNumberAsString() {
        return Converter.formatDouble(number);
    }
    
    /**
     * Used if the {@link #split(char[])} method returned TRUE.
     * @return String holding the part of the token that is not a number and precedes it (i.e. a currency sign), null otherwise.
     */
    public String getPrefix() {
        return this.prefix.toString();
    }

    /**
     * Used if the {@link #split(char[])} method returned TRUE.
     * @return String holding the part of the token that is not a number and succeeds it (i.e. the percent/per mille sign or a measuring unit), null otherwise.
     */
    public String getSuffix() {
        return this.suffix.toString();
    }

    /**
     * Used if the {@link #split(char[])} method returned TRUE.
     * @return Value of the {@link Type} enum indicating which numerical expression was identified.
     */
    public Type getType() {
        return type;
    }
    
}
