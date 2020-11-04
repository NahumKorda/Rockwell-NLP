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

import com.itcag.util.txt.TextToolbox;

/**
 * <p>This class "locks" dual purpose characters wherever they are not used as punctuation. Dual purpose characters are characters that can be used as punctuation, but also in numbers, URLs, acronyms, etc. Locking ensures that the splitting will not erroneously split text in wrong places.</p>
 */
public final class Locker {

    private final Abbreviations abbrevations;
    private final Acronyms acronyms;
    private final Domains domains;
    
    private final URL urlDetector;
    private final Email emailDetector;

    public Locker() throws Exception {
        this.abbrevations = Abbreviations.getInstance();
        this.acronyms = Acronyms.getInstance();
        this.domains = Domains.getInstance();
        this.urlDetector = new URL();
        this.emailDetector = new Email();
    }
    
    /**
     * @param input String builder holding the original text.
     * @throws Exception if anything goes wrong.
     */
    public final synchronized void lock(StringBuilder input) throws Exception {
        
        this.urlDetector.lock(input);
        this.emailDetector.lock(input);
        
        this.abbrevations.lock(input);
        this.acronyms.lock(input);
        this.domains.lock(input);
        
        lockPeriods(input);
        lockColons(input);
        lockCommas(input);
        
        input.append(" ");
        
    }

    public final synchronized String lock(String input) throws Exception {
        StringBuilder retVal = new StringBuilder(input);
        this.lock(retVal);
        return retVal.toString();
    }
    
    private void lockPeriods(StringBuilder input) {
        int start = input.indexOf(".");
        while (start > -1 && start < input.length() - 1) {
            char c = input.charAt(start + 1);
            if (Character.isDigit(c)) {
                input.replace(start, start + 1, Characters.PERIOD.getReplacement());
            } else if (Character.isLetter(c)) {
                lockAcronym(input, start);
            }
            start = input.indexOf(".", start + 1);
        }
    }
    
    private void lockAcronym(StringBuilder input, int start) {
        
        int dot = start;
        int letter = start - 1;
        for (int i = start; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == 46) {
                if (i == letter + 1) {
                    dot = i;
                    if (i == (input.length() - 1) && i >= start + 2) {
                        TextToolbox.replaceWithin(input, input.substring(start, i + 1), ".", Characters.ACRONYM.getReplacement());
                        input.append(".");
                    }
                } else {
                    break;
                }
            } else if (Character.isLetter(c)) {
                if (i == dot + 1) {
                    letter = i;
                } else {
                    break;
                }
            } else {
                if (i > start + 2) {
                    TextToolbox.replaceWithin(input, input.substring(start, i), ".", Characters.ACRONYM.getReplacement());
                }
                break;
            }
        }
        
    }
    
    private void lockColons(StringBuilder input) {
        /**
         * This is to preserve the time.
         */
        int start = input.indexOf(":");
        while (start > -1 && start < input.length() - 1) {
            char c = input.charAt(start + 1);
            if (Character.isDigit(c)) {
                input.replace(start, start + 1, Characters.COLON.getReplacement());
            }
            start = input.indexOf(".", start + 1);
        }
    }
    
    private void lockCommas(StringBuilder input) {
        int start = input.indexOf(",");
        while (start > -1 && start < input.length() - 1) {
            char c = input.charAt(start + 1);
            if (Character.isDigit(c)) input.replace(start, start + 1, Characters.COMMA.getReplacement());
            start = input.indexOf(",", start + 1);
        }
    }
    
    public final void unlockEverything(StringBuilder input) throws Exception {
        TextToolbox.replace(input, Characters.ABBREVIATION.getReplacement(), ".");
        TextToolbox.replace(input, Characters.ACRONYM.getReplacement(), ".");
        TextToolbox.replace(input, Characters.DOMAIN.getReplacement(), ".");
        this.urlDetector.unlock(input);
        this.emailDetector.unlock(input);
        decode(input);
    }
    
    public final String unlockEverything(String input) throws Exception {
        StringBuilder retVal = new StringBuilder(input);
        this.unlockEverything(retVal);
        return retVal.toString();
    }
    
    public final void unlockPunctuationOnly(StringBuilder input) throws Exception {
        decode(input);
    }
    
    public final String unlockPunctuationOnly(String input) throws Exception {
        StringBuilder retVal = new StringBuilder(input);
        decode(retVal);
        return retVal.toString();
    }
    
    private void decode(StringBuilder input) {
        TextToolbox.replace(input, Characters.PERIOD.getReplacement(), ".");
        TextToolbox.replace(input, Characters.EXCLAMATION.getReplacement(), "!");
        TextToolbox.replace(input, Characters.QUESTION.getReplacement(), "?");
        TextToolbox.replace(input, Characters.COLON.getReplacement(), ":");
        TextToolbox.replace(input, Characters.SEMICOLON.getReplacement(), ";");
        TextToolbox.replace(input, Characters.COMMA.getReplacement(), ",");
        TextToolbox.replace(input, Characters.SLASH.getReplacement(), "/");
        TextToolbox.replace(input, Characters.HYPHEN.getReplacement(), "-");
    }
    
    public final synchronized boolean isURL(String input) {
        return this.urlDetector.isThisTokenLockedURL(input);
    }
    
    public final synchronized String unlockURL(String input) {
        return this.urlDetector.unlock(input);
    }
    
    public final synchronized boolean isEmail(String input) {
        return this.emailDetector.isThisTokenLockedEmail(input);
    }
    
    public final synchronized String unlockEmail(String input) {
        return this.emailDetector.unlock(input);
    }
    
    /**
     * This method "unlocks" locked text by replacing the inserted non-printable characters with the periods.
     * @param input String holding text.
     * @return Unlocked text.
     */
    public final synchronized String unlockAbbreviation(String input) {
        return TextToolbox.replaceCaIn(input, Characters.ABBREVIATION.getReplacement(), ".");
    }
    
    /**
     * This method "unlocks" locked text by replacing the inserted non-printable characters with the periods.
     * @param input String holding text.
     * @return Unlocked text.
     */
    public final synchronized String unlockAcronym(String input) {
        return TextToolbox.replaceCaIn(input, Characters.ACRONYM.getReplacement(), ".");
    }
    
    /**
     * This method "unlocks" locked text by replacing the inserted non-printable characters with the periods.
     * @param input String holding text.
     * @return Unlocked text.
     */
    public final synchronized String unlockDomain(String input) {
        return TextToolbox.replaceCaIn(input, Characters.DOMAIN.getReplacement(), ".");
    }
    
}
