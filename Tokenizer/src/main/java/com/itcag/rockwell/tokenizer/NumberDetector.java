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

import com.itcag.util.Converter;
import com.itcag.rockwell.POSTag;
import com.itcag.rockwell.lang.Token;

/**
 * <p>This class identifies a number in text. Number can be expressed as digits; it can be a decimal number composed of digits; it can be expressed with words as cardinal or ordinal number; it can be a fraction.</p>
 */
public final class NumberDetector {

    public final static synchronized Token identify(String word, int index) {
        
        if (word.contains("-")) {
            return checkCompoundNumber(word, index);
        }
        
        if (word.contains("/")) {
            return checkFractions(word, index);
        }
        
        if (word.endsWith("st")) {
            if (word.length() < 3) return null;
            if ("1".equals(word.substring(word.length() - 3, word.length() - 2))) {
                return new Token(word, POSTag.ORD, word, index);
            }
        } else if (word.endsWith("nd")) {
            if (word.length() < 3) return null;
            if ("2".equals(word.substring(word.length() - 3, word.length() - 2))) {
                return new Token(word, POSTag.ORD, word, index);
            }
        } else if (word.endsWith("rd")) {
            if (word.length() < 3) return null;
            if ("3".equals(word.substring(word.length() - 3, word.length() - 2))) {
                return new Token(word, POSTag.ORD, word, index);
            }
        } else if (word.endsWith("th")) {
            if (word.length() < 3) return null;
            Integer test = Converter.convertStringToInteger(word.substring(0, word.length() - 2));
            if (test != null) {
                return new Token(word, POSTag.ORD, word, index);
            }
        }
        
        {
            Double test = Converter.convertStringToDouble(word);
            if (test != null) {
                return new Token(word, POSTag.CRD, word, index);
            }
        }

        {
            Long test = getDigits(word);
            if (test != null) {
                return new Token(word, POSTag.CRD, word, index);
            }
        }
        
        if (isOrdinal(word)) {
            return new Token(word, POSTag.ORD, word, index);
        }
        
        if (isProportion(word)) {
            return new Token(word, POSTag.CRD, word, index);
        }
        
        return null;
        
    }
    
    private static Token checkCompoundNumber(String word, int index) {
        
        String[] elts = word.split("-");
        
        {
            int count = 0;
            for (String elt : elts) {
                Double test = Converter.convertStringToDouble(elt.trim());
                if (test != null) count++;
            }
            if (count == elts.length) {
                return new Token(word, POSTag.CRD, word, index);
            }
        }
        
        {
            int count = 0;
            long sum = -1;
            for (String elt : elts) {
                Long test = getDigits(elt.trim());
                if (test != null) {
                    sum += test;
                    count++;
                }
            }
            if (count == elts.length) {
                return new Token(word, POSTag.CRD, word, index);
            } else if (count == elts.length - 1) {
                /**
                 * Check if the last element is an ordinal number.
                 */
                if (isOrdinal(elts[elts.length - 1])) {
                    return new Token(word, POSTag.ORD, word, index);
                }
            }
        }
    
        {
            if (elts.length != 2) return null;
            Long test = getDigits(elts[0].trim());
            if (test == null) return null;
            if (isProportion(elts[1].trim())) {
                return new Token(word, POSTag.CRD, word, index);
            }
        }
        
        return null;
        
    }
    
    private static Token checkFractions(String word, int index) {
        
        String[] elts = word.split("/");
        /**
         * Fraction or a date (e.g. dd/MM/yyyy).
         */
        if (elts.length != 3) return null;
        
        int count = 0;
        for (String elt : elts) {
            Double test = Converter.convertStringToDouble(elt.trim());
            if (test != null) count++;
        }
        if (count == elts.length) {
            return new Token(word, POSTag.CRD, word, index);
        }

        return null;
        
    }
    
    /**
     * @param word String holding a word.
     * @return Long number if the word represents a number, or null if it doesn't.
     */
    public static synchronized Long getDigits(String word) {
        switch (word.toLowerCase()) {
            case "zero":
                return 0l;
            case "one":
                return 1l;
            case "two":
                return 2l;
            case "three":
                return 3l;
            case "four":
                return 4l;
            case "five":
                return 5l;
            case "six":
                return 6l;
            case "seven":
                return 7l;
            case "eight":
                return 8l;
            case "nine":
                return 9l;
            case "ten":
                return 10l;
            case "eleven":
                return 11l;
            case "twelve":
                return 12l;
            case "thirteen":
                return 13l;
            case "fourteen":
                return 14l;
            case "fifteen":
                return 15l;
            case "sixteen":
                return 16l;
            case "seventeen":
                return 17l;
            case "eigthteen":
                return 18l;
            case "nineteen":
                return 19l;
            case "twenty":
                return 20l;
            case "thirty":
                return 30l;
            case "fourty":
                return 40l;
            case "fifty":
                return 50l;
            case "sixty":
                return 60l;
            case "seventy":
                return 70l;
            case "eighty":
                return 80l;
            case "ninety":
                return 90l;
            case "hunderd":
                return 100l;
            case "hundred":
                return 100l;
            case "thousand":
                return 1000l;
            case "million":
            case "mil":
            case "mln":
                return 1000000l;
            case "billion":
                return 1000000000l;
            case "trillion":
                return 1000000000000l;
            case "crore":
            case "karor":
            case "koti":
                return 10000000l;
            case "lakh":
            case "lac":
            case "lacs":
                return 100000l;
            default:
                return null;
        }
    }

    private static boolean isProportion(String word) {
        switch (word.toLowerCase()) {
            case "half":
            case "halves":
            case "third":
            case "thirds":
            case "quarter":
            case "quarters":
            case "fifths":
            case "sixths":
            case "sevenths":
            case "eighths":
            case "ninths":
            case "tenths":
            case "elevenths":
            case "twelfths":
            case "thirteenths":
            case "fourteenths":
            case "fifteenths":
            case "sixteenths":
            case "seventeenths":
            case "eighteenths":
            case "nineteenths":
            case "twentieths":
            case "thirtieths":
            case "fourtieths":
            case "fiftieths":
            case "sixtieths":
            case "seventieths":
            case "eightieths":
            case "ninetieths":
            case "hunderdths":
            case "hundredths":
            case "thousandths":
            case "millionths":
            case "billionths":
            case "trillionths":
               return true;
            default:
                return isOrdinal(word);
        }
    }
    
    private static boolean isOrdinal(String word) {
        switch (word.toLowerCase()) {
            /**
             * "First" is also an adverb, and must be disambiguated.
             */
//            case "first":
            /**
             * "Second" is also a noun, and must be disambiguated.
             */
//            case "second":
            case "fifth":
            case "sixth":
            case "seventh":
            case "eighth":
            case "ninth":
            case "tenth":
            case "eleventh":
            case "twelfth":
            case "thirteenth":
            case "fourteenth":
            case "fifteenth":
            case "sixteenth":
            case "seventeenth":
            case "eighteenth":
            case "nineteenth":
            case "twentieth":
            case "thirtieth":
            case "fourtieth":
            case "fiftieth":
            case "sixtieth":
            case "seventieth":
            case "eightieth":
            case "ninetieth":
            case "hunderdth":
            case "hundredth":
            case "thousandth":
            case "millionth":
            case "billionth":
            case "trillionth":
                return true;
            default:
                return false;
        }
    }
    
}
