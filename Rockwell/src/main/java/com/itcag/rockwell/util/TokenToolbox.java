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

package com.itcag.rockwell.util;

import com.itcag.rockwell.lang.Semtoken;
import com.itcag.rockwell.lang.Token;
import com.itcag.util.punct.PunctuationToolbox;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;

/**
 * <p>This class provides a collection of static methods that convert an array list of tokens into a string.</p>
 */
public class TokenToolbox {

    /**
     * This method reconstructs the original sentence as close as possible.
     * @param tokens Array list containing tokens representing a sentence.
     * @return String construed from the tokens.
     */
    public static String getStringFromTokens(ArrayList<Token> tokens) {
        StringBuilder retVal = new StringBuilder();
        tokens.forEach((token) -> {
            if (token instanceof Semtoken) {
                Semtoken tmp = (Semtoken) token;
                retVal.append(" ").append(tmp.toString());
            } else if (token.getWord().startsWith("'")) {
                retVal.append(token.getWord());
            } else if ("n't".equals(token.getCain())) {
                retVal.append(token.getWord());
            } else if (PunctuationToolbox.isPunctuation(token.getWord())) {
                retVal.append(token.getWord());
            } else {
                retVal.append(" ").append(token.getWord());
            }
        });
        TextToolbox.trim(retVal);
        return retVal.toString();
    }

    /**
     * This method converts tokens into a string, and inserts roles of the {@link com.itcag.rockwell.lang.Semtoken semtokens} into it.
     * @param tokens Array list containing tokens representing a sentence.
     * @return String construed from the tokens.
     */
    public static String getStringWithRolesFromTokens(ArrayList<Token> tokens) {
        StringBuilder retVal = new StringBuilder();
        tokens.forEach((token) -> {
            if (token instanceof Semtoken) {
                Semtoken tmp = (Semtoken) token;
                retVal.append(" ").append(tmp.toStringWithRoles());
            } else if (token.getWord().startsWith("'")) {
                retVal.append(token.getWord());
            } else if ("n't".equals(token.getCain())) {
                retVal.append(token.getWord());
            } else if (PunctuationToolbox.isPunctuation(token.getWord())) {
                retVal.append(token.getWord());
            } else {
                retVal.append(" ").append(token.getWord());
            }
        });
        TextToolbox.trim(retVal);
        return retVal.toString();
    }

    /**
     * This method converts tokens into string, and inserts part-of-speech specifications of the unambiguous tokens into it.
     * @param tokens Array list containing tokens representing a sentence.
     * @return String construed from the tokens.
     */
    public static String getStringWithPOSFromTokens(ArrayList<Token> tokens) {
        StringBuilder retVal = new StringBuilder();
        tokens.forEach((token) -> {
            if (token instanceof Semtoken) {
                Semtoken tmp = (Semtoken) token;
                retVal.append(" ").append(tmp.toStringWithPOS());
            } else {
                retVal.append(" ").append(token.toStringWithPOS());
            }
        });
        TextToolbox.trim(retVal);
        return retVal.toString();
    }

    /**
     * This method converts tokens into string, and inserts both roles of the {@link com.itcag.rockwell.lang.Semtoken semtokens} and part-of-speech specifications of the unambiguous tokens into it.
     * @param tokens Array list containing tokens representing a sentence.
     * @return String construed from the tokens.
     */
    public static String getStringWithPOSAndRolesFromTokens(ArrayList<Token> tokens) {
        StringBuilder retVal = new StringBuilder();
        tokens.forEach((token) -> {
            if (token instanceof Semtoken) {
                Semtoken tmp = (Semtoken) token;
                retVal.append(" (").append(tmp.toStringWithPOSAndRoles()).append(")");
            } else {
                retVal.append(" ").append(token.toStringWithPOS());
            }
        });
        TextToolbox.trim(retVal);
        return retVal.toString();
    }

}
