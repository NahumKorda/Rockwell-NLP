package com.itcag.util.punct;

import com.itcag.util.txt.TextToolbox;

/**
 * <p>This class normalizes punctuation to ensure that it complies with the standard English orthography.</p>
 */
public final class Punctuation {
    
    public final static String ABBREVIATION_PERIOD = Character.toString((char) 1);
    public final static String ACRONYM_PERIOD = Character.toString((char) 2);

    /**
     * @param input string builder holding text to be normalized. 
     */
    public final static synchronized void normalize(StringBuilder input) {
        
        if (TextToolbox.isEmpty(input)) return;

        removeSpaceBefore(input);
        if (TextToolbox.isEmpty(input)) return;

        resolveEllipses(input);
        if (TextToolbox.isEmpty(input)) return;
        
        removePunctuationAtBeginning(input);
        if (TextToolbox.isEmpty(input)) return;
        
        resolveMultiplePunctuation(input);
        if (TextToolbox.isEmpty(input)) return;

        resolveMultipleHyphens(input);
        if (TextToolbox.isEmpty(input)) return;
        
        TextToolbox.fixEmptySpaces(input);
        if (TextToolbox.isEmpty(input)) return;

        for (PunctuationSigns sign : PunctuationSigns.values()) {
            insertEmptySpaceAfterPunctuation(input, sign.getSign());
        }

    }

    private static void resolveEllipses(StringBuilder input) {
        
        /*
         * Replace multiple periods with ellipsis.
         */
        while (input.indexOf("....") != -1) {
            TextToolbox.replace(input, "....", "...");
        }

        /*
         * Replace ellipsis with a single character.
         */
        while (input.indexOf("...") != -1) {
            TextToolbox.replace(input, "...", "…");
        }

        /*
         * Replace multiple ellipses with a single (in case that there was original ellipsis sign preceded or followed by multiple periods).
         */
        while (input.indexOf("……") != -1) {
            TextToolbox.replace(input, "……", "…");
        }

        /*
         * Replace two periods with a single.
         */
        while (input.indexOf("..") != -1) {
            TextToolbox.replace(input, "..", ".");
        }

    }

    private static void removeSpaceBefore(StringBuilder input) {
        for (PunctuationSigns sign : PunctuationSigns.values()) {
            String test = " " + sign.getSign();
            while (input.indexOf(test) != -1) {
                TextToolbox.replace(input, test, sign.getSign());
            }
        }
    }

    public static void removePunctuationAtBeginning(StringBuilder input) {
        TextToolbox.trim(input);
        if (TextToolbox.isEmpty(input)) return;
        while (PunctuationToolbox.isPunctuation(input.charAt(0))) {
            input.deleteCharAt(0);
            TextToolbox.trim(input);
            if (TextToolbox.isEmpty(input)) return;
        }
    }
    
    private static void resolveMultiplePunctuation(StringBuilder input) {

        int i = 0;
        while (i < input.length()) {

            char c = input.charAt(i);

            if (PunctuationToolbox.isPunctuation(c)) {
                if (i > 0) {
                    char p = input.charAt(i - 1);
                    if (PunctuationToolbox.isPunctuation(p)) {
                        if (PunctuationToolbox.getPrecedence(p) <= PunctuationToolbox.getPrecedence(c)) {
                            input.deleteCharAt(i);
                            continue;
                        }
                    }
                }
            }

            i++;

        }

    }
    
    private static void resolveMultipleHyphens(StringBuilder input) {
        while (input.indexOf("--") != -1) {
            TextToolbox.replace(input, "--", "-");
        }
    }
    
    private static void insertEmptySpaceAfterPunctuation(StringBuilder input, String punctuation) {
        
        int pos = input.indexOf(punctuation);
        while (pos > -1) {

            /*
             * Ensure that we can peek one character - check if this is the period that ends the entire text.
             */
            if (input.length() > pos + 1) {

                /*
                 * Check whether the following character is empty space.
                 */
                if (input.charAt(pos + 1) != 32) {

                    /*
                     * Check if we can peek yet another character.
                     */
                    if (input.length() > pos + 2) {

                        /*
                         * Check whether the following character is a period (making it an acronym).
                         */
                        if (input.charAt(pos + 2) != 46) {
                        
                            /*
                             * if not, insert an empty space after the period.
                             */
                            input.insert(pos + 1, (char) 32);
                        
                        }
                        
                    }
                    
                }

            }
            
            pos = input.indexOf(punctuation, pos + 1);

        }
        
    }
    
}
