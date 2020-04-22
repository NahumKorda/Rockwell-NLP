package com.itcag.rockwell.vocabulator.res;

import java.util.HashSet;

/**
 * <p>This class loads and stores stop words. Word extraction can be instructed to ignore these words.</p>
 * <p>This class is implemented as singleton to avoid reloading lexical resources.</p>
 */
public class Stopwords {

    private static volatile Stopwords instance = null;
    
    /**
     * This method is designed to throw an exception if lexical resources cannot be loaded, and therefore the double checked locking is necessary.
     * @return Instance of this class.
     * @throws Exception If anything goes wrong.
     */
    public static Stopwords getInstance() throws Exception {
        if (instance == null) instance = new Stopwords();
        return instance;
    }
    
    private final HashSet<String> index = new HashSet<>();
    
    private Stopwords() throws Exception {
        
        Loader loader = new Loader();
        for (String stopword : loader.load("stopwords")) {
            this.index.add(stopword);
        }
        
    }
    
    /**
     * Evaluates whether a word is a stop word.
     * @param word String holding the word to be evaluated.
     * @return Boolean indicating whether the word is a stop word or not.
     */
    public boolean isStopword(String word) {
        return this.index.contains(word);
    }
    
}
