package com.itcag.rockwell.tokenizer.res;

import java.util.HashSet;

/**
 * <p>This class loads and stores three lexical resources:</p>
 * <ul>
 * <li>Currency codes is a list of all world's three-letter currency codes.</li>
 * <li>Currency symbols is a list of all world's currency symbols (e.g., "$").</li>
 * <li>Measuring units is a list of the basic and the most frequently used measuring units.</li>
 * </ul>
 * <p>This class is implemented as singleton to avoid reloading lexical resources.</p>
 */
public final class LexicalResources {

    private static volatile LexicalResources instance = null;
    
    /**
     * This method is designed to throw an exception if lexical resources cannot be loaded, and therefore the double checked locking is necessary.
     * @return Instance of this class.
     * @throws Exception If anything goes wrong.
     */
    public static synchronized LexicalResources getInstance() throws Exception {
        if (instance == null) {
            synchronized(LexicalResources.class) {
                if (instance == null) {
                    instance = new LexicalResources();
                }
            }
        }
        return instance;
    }
    
    private final HashSet<String> currencyCodes;
    private final HashSet<String> currencySymbols;
    private final HashSet<String> measuringUnits;
    
    private LexicalResources() throws Exception {
        Loader loader = new Loader();
        this.currencyCodes = new HashSet<>(loader.load("currencyCodes"));
        this.currencySymbols = new HashSet<>(loader.load("currencySymbols"));
        this.measuringUnits = new HashSet<>(loader.load("measuringUnits"));
    }
    
    /**
     * @param word String holding a word.
     * @return Boolean indicating whether the word is recognized as a currency code.
     */
    public final synchronized boolean isCurrencyCode(String word) {
        return this.currencyCodes.contains(word);
    }
    
    /**
     * @param word String holding a word.
     * @return Boolean indicating whether the word is recognized as a currency symbol.
     */
    public final synchronized boolean isCurrencySymbol(String word) {
        return this.currencySymbols.contains(word);
    }
    
    /**
     * @param word String holding a word.
     * @return Boolean indicating whether the word is recognized as a measuring unit.
     */
    public final synchronized boolean isMeasuringUnit(String word) {
        return this.measuringUnits.contains(word);
    }
    
}
