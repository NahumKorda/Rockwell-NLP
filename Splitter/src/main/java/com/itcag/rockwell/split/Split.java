package com.itcag.rockwell.split;

import com.itcag.util.punct.PunctuationToolbox;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;

/**
 * <p>This class splits text into individual sentences, after the use of all dual purpose characters was "locked".</p>
 */
public final class Split {
    
    /**
     * @param input String builder holding the original text.
     * @return Array list containing string builders holding individual sentences.
     */
    public final ArrayList<StringBuilder> split(StringBuilder input) {

        TextToolbox.trim(input);
        
        if (TextToolbox.isEmpty(input)) return new ArrayList<>();

        ArrayList<StringBuilder> retVal = new ArrayList<>();

        StringBuilder buffer = new StringBuilder();
        StringBuilder lastWord = new StringBuilder();
        
        char p;
        char n;
        
        int i = 0;
        while (i < input.length()) {

            char c = input.charAt(i);

            if (i > 0) {
                p = input.charAt(i - 1);
            } else {
                p = 0;
            }

            if ((i + 1) < input.length()) {
                n = input.charAt(i + 1);
            } else {
                n = 0;
            }

            switch (c) {
                case 32:
                    
                    /*
                     * First character is empty space - ignore it.
                     */
                    if (p == 0) continue;
                    
                    /*
                     * Last character is empty space - ignore it.
                     * If buffer is not empty,
                     * it will be inserted into the return list by default.
                     */
                    if (n == 0) continue;
                    
                    if (isEndOfSentence(p, lastWord) && buffer.length() > 0) {
                        retVal.add(buffer);
                        buffer = new StringBuilder();
                    } else {
                        buffer.append(c);
                    }
                    
                    lastWord = new StringBuilder();

                    break;

                case 9:
                case 10:
                case 13:
                    /*
                     * Split on line break.
                     */
                    if (buffer.length() > 0) {
                        retVal.add(buffer);
                        buffer = new StringBuilder();
                        lastWord = new StringBuilder();
                    }
                    break;
                default:
                    buffer.append(c);
                    lastWord.append(c);
                    break;
            }
             
            i++;
            
        }

        if (buffer.length() > 0) retVal.add(buffer);
        
        return retVal;
    
    }
    
    private boolean isEndOfSentence(char p, StringBuilder lastWord) {

        /**
         * Do not split after a single letter word.
         */
        if (lastWord.length() == 1) return false;
        
        if (!PunctuationToolbox.isTerminalPunctuation(Character.toString(p))) return false;
        
        return true;
        
    }
    
}
