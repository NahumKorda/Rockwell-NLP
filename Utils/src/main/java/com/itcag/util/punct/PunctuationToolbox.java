package com.itcag.util.punct;

import com.itcag.util.txt.TextToolbox;

import java.util.HashMap;
import java.util.HashSet;

/**
 * <p>This class provides a collection of methods to handle punctuation.</p>
 */
public final class PunctuationToolbox {
    
    private final static HashSet<String> SIGNS = new HashSet<>();
    private final static HashSet<Character> CHARS = new HashSet<>();
    private final static HashMap<Character, Integer> CHARMAP = new HashMap<>();
    
    static {
        
        for (PunctuationSigns punctuation : PunctuationSigns.values()) {
            SIGNS.add(punctuation.getSign());
            CHARS.add(punctuation.getCharacter());
            CHARMAP.put(punctuation.getCharacter(), punctuation.getPrecedence());
        }
        
    }

    /**
     * Appends a period at the end of the input text if it doesn't end with a punctuation character.
     * @param input String builder holding input text.
     */
    public final static void punctuate(StringBuilder input) {

        TextToolbox.trim(input);
        if (input.length() == 0) return;

        if (!isPunctuation(input.substring(input.length() - 1))) {
            input.append(".");
        }

    }

    /**
     * Evaluates if the input string is a punctuation character.
     * @param input String holding input text.
     * @return Boolean indicating whether input is punctuation.
     */    
    public final static boolean isPunctuation(String input) {

        if (input.length() != 1) return false;
        if (TextToolbox.isEmpty(input)) return false;

        return SIGNS.contains(input);

    }
    
    /**
     * Evaluates if the input character is a punctuation character.
     * @param c Input character.
     * @return Boolean indicating whether input is punctuation.
     */
    public final static boolean isPunctuation(char c) {
        return CHARS.contains(c);
    }

    /**
     * Evaluates whether the input string is a terminal punctuation character. Terminal punctuation characters terminate a sentence (i.e. ".", "!", "?", "…").
     * @param input String holding input text.
     * @return Boolean indicating whether input is terminal punctuation.
     */
    public final static boolean isTerminalPunctuation(String input) {

        if (input.length() != 1) return false;
        if (TextToolbox.isEmpty(input)) return false;

        switch (input) {
            case".":
            case"!":
            case"?":
            case"…":
                return true;
            default:
                return false;
        }

    }
    
    /**
     * Evaluates whether the input string is a non-terminal punctuation character. Non-terminal punctuation characters can be encountered in the middle of a sentence (i.e. ",", ";", ":").
     * @param input String holding input text.
     * @return Boolean indicating whether input is non-terminal punctuation.
     */
    public final static boolean isNonTerminalPunctuation(String input) {

        if (input.length() != 1) return false;
        if (TextToolbox.isEmpty(input)) return false;

        switch (input) {
            case",":
            case":":
            case";":
                return true;
            default:
                return false;
        }

    }
    
    /**
     * Return the precedence of a punctuation character. Precedence determine which character is preserved in the case where multiple consecutive punctuation characters are encountered (e.g., "!?!?!?").
     * @param c Input character.
     * @return Integer indicating precedence of a punctuation character.
     */
    public final static int getPrecedence(char c) {
        return CHARMAP.get(c);
    }

}
