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

package com.itcag.rockwell.vocabulator.phraser;

import com.itcag.rockwell.POSType;
import com.itcag.rockwell.lang.Token;

/**
 * <p>This class validates the first and the last token in a phrase. Not every sequence of words is considered to be a phrase. This class ensures that the selected phrases are not meaningless word sequences.</p>
 */
public final class Validator {
    
    /**
     * Validates the first token in a phrase. If not validated, this token is removed, and the next token is validated as first.
     * @param token Instance of the {@link com.itcag.rockwell.lang.Token Token} class.
     * @return Boolean indicating whether the token can start a valid phrase.
     */
    public boolean isValidFirstWord(Token token) {
        
        if (token.getCain().contains("'")) return false;
        
        if (!isValidFirstOrLastWord(token.getCain())) return false;
        
        if (token.getType() != null) {
            return isValidFirstWord(token.getType());
        } else {
            for (Token alternative : token.getAlternatives()) {
                if (!isValidFirstWord(alternative.getType())) return false;
            }
        }
    
        return true;
    
    }
    
    private boolean isValidFirstWord(POSType type) {
        switch (type) {
            case VA:
            case AV:
            case CJ:
            case DT:
            case PN:
            case PO:
            case PR:
            case TO:
            case PC:
                return false;
            default:
                return true;
        }
    }
    
    /**
     * Validates the last token in a phrase. If not validated, this token is removed, and the next token is validated as last.
     * @param token Instance of the {@link com.itcag.rockwell.lang.Token Token} class.
     * @return Boolean indicating whether the token can end a valid phrase.
     */
    public boolean isValidLastWord(Token token) {

        if (!isValidFirstOrLastWord(token.getCain())) return false;

        if (token.getType() != null) {
            return isValidLastWord(token.getType());
        } else {
            for (Token alternative : token.getAlternatives()) {
                if (!isValidLastWord(alternative.getType())) return false;
            }
        }
        
        return true;
    
    }

    private boolean isValidLastWord(POSType type) {
        switch (type) {
            case CJ:
            case DT:
            case EX:
            case NG:
            case NM:
            case PN:
            case PR:
            case TO:
            case PC:
                return false;
            default:
                return true;
        }
    }
    
    private boolean isValidFirstOrLastWord(String word) {
        
        if (word.length() == 1) {
            if (!Character.isLetterOrDigit(word.charAt(0))) return false;
        }

        switch (word) {
            case "will":
                return false;
        }
        
        return true;
        
    }
    
}
