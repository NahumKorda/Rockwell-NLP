package com.itcag.rockwell.tokenizer.res;

import com.itcag.rockwell.POSTag;
import com.itcag.rockwell.lang.Alternatives;
import com.itcag.rockwell.lang.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * <p>This class loads and stores an English lexicon.</p>
 * <p>This class is implemented as singleton to avoid reloading lexical resources.</p>
 */
public final class Lexicon {
    
    private static volatile Lexicon instance = null;
    
    /**
     * This method is designed to throw an exception if lexical resources cannot be loaded, and therefore the double checked locking is necessary.
     * @return Instance of this class.
     * @throws Exception If anything goes wrong.
     */
    public static synchronized Lexicon getInstance() throws Exception {
        if (instance == null) {
            synchronized(Lexicon.class) {
                if (instance == null) {
                    instance = new Lexicon();
                }
            }
        }
        return instance;
    }
    
    /**
     * Key is word and value is a list of tokens holding alternative part of speech and lemma pairs for the word.
     */
    private final HashMap<String, ArrayList<Token>> index = new HashMap<>();
    
    private Lexicon() throws Exception {
        
        Loader loader = new Loader();
        ArrayList<String> items = loader.load("lexicon");

        for (String item : items) {
            /**
             * word|POS|lemma
             */
            String[] elts = item.split("\\|");
            String word = elts[0].trim();
            String pos = elts[1].trim();
            String lemma = elts[2].trim();
            if (index.containsKey(word)) {
                index.get(word).add(new Token(word, POSTag.valueOf(pos), lemma));
            } else {
                index.put(word, new ArrayList<>(Arrays.asList(new Token(word, POSTag.valueOf(pos), lemma))));
            }
        }

    }
    
    /**
     * @return Hash map containing all words from the lexicon and their alternative part-of-speech interpretations.
     */
    public final synchronized HashMap<String, ArrayList<Token>> getIndex() {
        return index;
    }
    
    /**
     * @param word String holding a word.
     * @return Boolean indication whether the word is in the lexicon or not.
     */
    public final synchronized boolean isKnown(String word) {
        return index.containsKey(word);
    }
    
    /**
     * @param word String holding a word.
     * @return Instance of the {@link com.itcag.rockwell.lang.Alternatives Alternatives} class containing alternative part-of-speech interpretations for the word, or null if the word is not recognized.
     */
    public final synchronized Alternatives getAlternatives(String word) {
        return new Alternatives(index.get(word));
    }

}
