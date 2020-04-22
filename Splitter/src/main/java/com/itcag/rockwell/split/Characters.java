package com.itcag.rockwell.split;

/**
 * <p>Enumerates dual purpose characters: characters that can be used as punctuation, and in numbers, URLs, acronyms, etc.</p>
 * <p>For each such character a non-printable character replacement is provided. These replacements are used in the {@link com.itcag.rockwell.split.Locker Locker} class to "lock" these characters wherever they are <b>not</b> used as punctuation. This ensures that the splitting will not erroneously split text in wrong places.</p>
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
    SPACE((char) 11),

    ;

    private final char replacement;
    
    private Characters(char replacement) {
        this.replacement = replacement;
    }
    
    public String getReplacement() {
        return Character.toString(this.replacement);
    }
    
}
