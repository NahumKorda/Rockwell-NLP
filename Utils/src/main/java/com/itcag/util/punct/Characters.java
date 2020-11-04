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
 * <p>Enumerates dual purpose characters: characters that can be used as punctuation, and in numbers, URLs, acronyms, etc.</p>
 * <p>For each such character a non-printable character replacement is provided. These replacements are used in the {@link com.itcag.util.punct.Locker Locker} class to "lock" these characters wherever they are <b>not</b> used as punctuation. This ensures that the splitting will not erroneously split text in wrong places.</p>
 */
public enum Characters {
    
    PERIOD((char) 1),
    EXCLAMATION((char) 2),
    QUESTION((char) 3),
    COLON((char) 4),
    SEMICOLON((char) 5),
    COMMA((char) 6),
    SLASH((char) 7),
    HYPHEN((char) 8),
    
    SPACE((char) 127),

    ABBREVIATION((char) 11),
    ACRONYM((char) 12),
    DOMAIN((char) 14),

    URL_PERIOD((char) 15),
    URL_EXCLAMATION((char) 16),
    URL_QUESTION((char) 17),
    URL_COLON((char) 18),
    URL_SEMICOLON((char) 19),
    URL_COMMA((char) 20),
    URL_SLASH((char) 21),
    URL_HYPHEN((char) 22),

    EMAIL_PERIOD((char) 23),
    EMAIL_EXCLAMATION((char) 24),
    EMAIL_QUESTION((char) 25),
    EMAIL_COLON((char) 26),
    EMAIL_SEMICOLON((char) 27),
    EMAIL_COMMA((char) 28),
    EMAIL_SLASH((char) 29),
    EMAIL_HYPHEN((char) 30),

    ;

    private final char replacement;
    
    private Characters(char replacement) {
        this.replacement = replacement;
    }
    
    public String getReplacement() {
        return Character.toString(this.replacement);
    }
    
}
