package com.itcag.rockwell.tokenizer;

import com.itcag.rockwell.tokenizer.res.LexicalResources;
import com.itcag.util.Converter;

import java.util.ArrayList;

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

    private boolean negative = false;
    private Double number = null;
    private String rest = null;
    
    private Type type = null;
    
    private final ArrayList<Character> digits = new ArrayList<>();
    private final ArrayList<Character> nondigits = new ArrayList<>();
    
    public NumericalExpressionDetector() throws Exception {
        this.lexicalResources = LexicalResources.getInstance();
    }
    
    /**
     * This method analyzes an array of characters representing a token, identifies numerical expressions, and splits them if required into a number and a symbol.
     * @param chars Array of characters to be analyzed.
     * @return Boolean indicating whether one or more numerical expressions were identified.
     */
    public final boolean split(char[] chars) {
        
        if (chars.length == 0) return false;
        
        /**
         * Plus sign.
         */
        if (chars[0] ==  43 && chars.length > 1) {
            negative = false;
            /**
             * Ignore the first character.
             */
            return extract(1, chars);
        }
            
        /**
         * Minus sign.
         */
        if (chars[0] == 45 && chars.length > 1) {
            /**
             * Ignore the first character.
             */
            negative = true;
            return extract(1, chars);
        }
        
        return extract(0, chars);
        
    }

    private boolean extract(int start, char[] chars) {
        if (Character.isDigit(chars[start])) {
            start = extractDigits(0, chars);
            if (start == chars.length) return evaluate(chars);
            start = extractNonDigits(start, chars);
            if (start == chars.length) return evaluate(chars);
        } else {
            start = extractNonDigits(start, chars);
            if (start == chars.length) return false;
            start = extractDigits(start, chars);
            if (start == chars.length) return evaluate(chars);
        }
        return false;
    } 
    
    private int extractDigits(int start, char[] chars) {
        for (int i = start ; i < chars.length ; i++) {
            if (Character.isDigit(chars[i])) {
                digits.add(chars[i]);
            } else if (chars[i] == 44) {
                /**
                 * Comma.
                 */
                digits.add(chars[i]);
            } else if (chars[i] == 46) {
                /**
                 * Period.
                 */
                digits.add(chars[i]);
            } else {
               return i; 
            }
        }
        return chars.length;
    }
    
    private int extractNonDigits(int start, char[] chars) {
        for (int i = start ; i < chars.length ; i++) {
            if (Character.isDigit(chars[i])) {
               return i; 
            } else {
                nondigits.add(chars[i]);
            }
        }
        return chars.length;
    }
    
    private boolean evaluate(char[] chars) {
        
        /**
         * Ensure that the entire char array was analyzed.
         * If not, abort because this is not a numerical expressions.
         */
        if (negative) {
            if (digits.size() + nondigits.size() + 1 != chars.length) return false;
        } else {
            if (digits.size() + nondigits.size() != chars.length) return false;
        }
        
        /**
         * A numerical expression must include a number.
         * If not, abort because this is not a numerical expressions.
         */
        extractNumber();
        if (number == null) return false;
        
        extractString();
        
        if (number != null) {
            if (getRest() != null) {
                if (this.lexicalResources.isCurrencySymbol(getRest().toLowerCase())) {
                    type = Type.CURRENCY;
                    return true;
                } else if (this.lexicalResources.isMeasuringUnit(getRest().toLowerCase())) {
                    type = Type.QUANTITY;
                    return true;
                } else if ("%".equalsIgnoreCase(getRest())) {
                    type = Type.PERCENTAGE;
                    return true;
                } else if ("‰".equalsIgnoreCase(getRest())) {
                    type = Type.PERCENTAGE;
                    return true;
                }
            } else {
                type = Type.NUMBER;
                return true;
            }
        }
        
        return false;
        
    }
    
    private void extractNumber() {
        String chars = digits.stream().map(e -> e.toString()).reduce((acc, e) -> acc  + e).get();
        Double test = Converter.convertStringToDouble(chars);
        if (test != null) {
            if (negative) {
                number = -test;
            } else {
                number = test;
            }
        }
    }
    
    private void extractString() {
        if (nondigits.isEmpty()) return;
        rest = nondigits.stream().map(e -> e.toString()).reduce((acc, e) -> acc  + e).get();
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
     * @return String holding the part of the token that is not a number (i.e. a currency sign, the percent/per mille sign or a measuring unit), null otherwise.
     */
    public String getRest() {
        return rest;
    }

    /**
     * Used if the {@link #split(char[])} method returned TRUE.
     * @return Value of the {@link Type} enum indicating which numerical expression was identified.
     */
    public Type getType() {
        return type;
    }
    
}
