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

package com.itcag.util.punct;

/**
 * <p>Enumerates punctuation characters.</p>
 * <p>Precedence determine which character is preserved in the case where multiple consecutive punctuation characters are encountered (e.g., "!?!?!?").</p>
 */
public enum PunctuationSigns {

    QUESTION("?", (char) 63, 0),
    EXCLAMATION("!", (char) 33, 1),
    PERIOD(".", (char) 46, 2),
    COLON(":", (char) 58, 3),
    SEMICOLON(";", (char) 59, 4),
    COMMA(",", (char) 44, 5),
    ELLIPSIS("…", (char) 8230, 6),

    ;

    private final String sign;
    private final char character;
    private final int precedence;

    private PunctuationSigns(String sign, char character, int precedence) {
        this.sign = sign;
        this.character = character;
        this.precedence = precedence;
    }

    public String getSign() {
        return sign;
    }

    public char getCharacter() {
        return character;
    }

    public int getPrecedence() {
        return precedence;
    }
        
}
