package com.itcag.rockwell.tokenizer.res;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * <p>This class loads and stores a list of English contractions and their complete forms.</p>
 * <p>This class is implemented as singleton to avoid reloading lexical resources.</p>
 */
public final class Toklex {
    
    private static volatile Toklex instance = null;
    
    /**
     * This method is designed to throw an exception if lexical resources cannot be loaded, and therefore the double checked locking is necessary.
     * @return Instance of this class.
     * @throws Exception If anything goes wrong.
     */
    public static synchronized Toklex getInstance() throws Exception {
        if (instance == null) {
            synchronized(Toklex.class) {
                if (instance == null) {
                    instance = new Toklex();
                }
            }
        }
        return instance;
    } 
    
    private final HashMap<String, ArrayList<String>> index = new HashMap<>();
    
    private Toklex() throws Exception {

        Loader loader = new Loader();
        ArrayList<String> items = loader.load("toklex");

        items.stream().forEach((item) -> {
            String[] elts = item.split("\t");
            String[] subelts = elts[1].trim().split(" ");
            ArrayList<String> tmp = new ArrayList<>(Arrays.asList(subelts));
            index.put(elts[0].trim(), tmp);
        });

    }
   
    /**
     * @param word String holding a word.
     * @return Boolean indicating whether the word is recognized as a contraction.
     */
    public final synchronized boolean isRecognized(String word) {
        return (index.containsKey(word)); 
    }
    
    /**
     * @param word String holding a word.
     * @return Array list containing alternative complete forms for the word, or null if the word is not recognized.
     */
    public final synchronized ArrayList<String> getReplacement(String word) {
        return index.get(word);
    }
    
}
