package com.itcag.util.punct;

import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;

/**
 * <p>This class loads and stores a list of most frequently encountered acronyms.</p>
 * <p>This class is implemented as singleton to avoid reloading lexical resources.</p>
 */
public class Acronyms {
    
    private static volatile Acronyms instance = null;
    
    /**
     * This method is designed to throw an exception if lexical resources cannot be loaded, and therefore the double checked locking is necessary.
     * @return Instance of this class.
     * @throws Exception If anything goes wrong.
     */
    public static Acronyms getInstance() throws Exception {
        if (instance == null) {
            synchronized(Acronyms.class) {
                if (instance == null) {
                    instance = new Acronyms();
                }
            }
        }
        return instance;
    } 
    
    private final ArrayList<String> acronyms;
    
    private Acronyms() throws Exception {
        Loader loader = new Loader();
        this.acronyms = loader.load("acronyms");
    }
    
    /**
     * This method "locks" acronyms by replacing period characters in it with non-printable characters. This ensures that the acronyms will be treated as single words during splitting and tokenizing. 
     * @param input String builder holding text.
     */
    public final synchronized void lock(StringBuilder input) {
        
        input.append(" ");
        
        int end = input.indexOf(". ");
        if (end == -1) return;

        while (end > -1) {
            
            int start = input.lastIndexOf(" ", end);
            if (start == -1) start = 0;

            String test = input.substring(start, end + 1).trim();

            if (this.acronyms.contains(test.toLowerCase())) {
                replace(input, start, end);
            }
            
            end = input.indexOf(". ", end + 1);

        }
        
    }
    
    private void replace(StringBuilder input, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (input.charAt(i) == 46) {
                input.replace(i, i + 1, Punctuation.ACRONYM_PERIOD);
            }
        }
    }
    
    /**
     * This method "unlocks" locked text by replacing the inserted non-printable characters with the periods.
     * @param input String builder holding text.
     */
    public final synchronized void unlock(StringBuilder input) {
        TextToolbox.replaceCaIn(input, Punctuation.ACRONYM_PERIOD, ".");
    }
    
    /**
     * This method "unlocks" locked text by replacing the inserted non-printable characters with the periods.
     * @param input String holding text.
     * @return Unlocked text.
     */
    public final synchronized String unlock(String input) {
        return TextToolbox.replaceCaIn(input, Punctuation.ACRONYM_PERIOD, ".");
    }
    
}
