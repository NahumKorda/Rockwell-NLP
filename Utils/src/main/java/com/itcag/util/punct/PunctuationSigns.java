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
