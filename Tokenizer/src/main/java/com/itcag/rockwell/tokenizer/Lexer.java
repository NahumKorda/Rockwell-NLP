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

import com.itcag.multilingual.LexicalResources;
import com.itcag.multilingual.Lexicon;
import com.itcag.rockwell.POSTag;
import com.itcag.rockwell.lang.Token;

/**
 * <p>This class converts a single string representing a token into an instance of the {@link com.itcag.rockwell.lang.Token Token} class.</p>
 */
public final class Lexer {
    
    private final Lexicon lexicon;
    private final LexicalResources lexicalResources;
    
    public Lexer(Lexicon lexicon) throws Exception {
        this.lexicon = lexicon;
        this.lexicalResources = LexicalResources.getInstance();
    }

    /**
     * @param word String representing a token,
     * @param index Integer holding the index position of the token in a sentence.
     * @return Instance of the {@link com.itcag.rockwell.lang.Token Token} class.
     */
    public final Token getToken(String word, int index) {
        
        String cain = word.toLowerCase();
        
        if (lexicon.getAlternatives(cain).getAlternatives().size() == 1) {
            Token alternative = lexicon.getAlternatives(cain).getAlternatives().get(0);
            if (NumberDetector.getDigits(alternative.getLemma()) != null) {
                Token retVal = new Token(word, null, null, index);
                retVal.addAlternative(alternative);
                retVal.addAlternative(new Token(word, POSTag.CRD, alternative.getLemma(), index));
                return retVal;
            } else if (this.lexicalResources.isCurrencyCode(cain)) {
                Token retVal = new Token(word, null, null, index);
                retVal.addAlternative(alternative);
                retVal.addAlternative(new Token(word, POSTag.XZ2, alternative.getLemma(), index));
                return retVal;
            } else {
                Token retVal = new Token(word, alternative.getPos(), alternative.getLemma(), index);
                return retVal;
            }
        } else {
            Token retVal = new Token(word, null, null, index);
            lexicon.getAlternatives(cain).getAlternatives().stream().map((alternative) -> {
                retVal.addAlternative(alternative);
                return alternative;
            }).forEachOrdered((alternative) -> {
                if (NumberDetector.getDigits(alternative.getLemma()) != null) {
                    retVal.addAlternative(new Token(word, POSTag.CRD, alternative.getLemma(), index));
                } else if (this.lexicalResources.isCurrencyCode(cain)) {
                    retVal.addAlternative(new Token(word, POSTag.XZ2, alternative.getLemma(), index));
                }
            });
            return retVal;
        }
        
    }

}
