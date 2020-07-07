package com.itcag.rockwell.vocabulator.res;

import com.itcag.rockwell.vocabulator.PropertyFields;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class Synonyms {

    private final HashMap<String, String> wordSynonyms = new HashMap<>();
    private final HashMap<String, String> phraseSynonyms = new HashMap<>();
    
    public Synonyms(Properties config) throws Exception {
        
        if (config.containsKey(PropertyFields.WORD_SYNONYMS.getField())) {
            loadWordSynonyms(config.getProperty(PropertyFields.WORD_SYNONYMS.getField()));
        }
        
        if (config.containsKey(PropertyFields.PHRASE_SYNONYMS.getField())) {
            loadPhraseSynonyms(config.getProperty(PropertyFields.PHRASE_SYNONYMS.getField()));
        }
    
    }
    
    private void loadWordSynonyms(String sourcePath) throws Exception {
        
        ArrayList<String> lines = TextFileReader.read(sourcePath);
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                String[] elts = line.split("\\|");
                String label = elts[0].trim();
                String[] subelts = elts[1].trim().split(",");
                for (String subelt : subelts) {
                    this.wordSynonyms.put(subelt.trim(), label);
                }
            }
        }

    }
    
    private void loadPhraseSynonyms(String sourcePath) throws Exception {
        
        ArrayList<String> lines = TextFileReader.read(sourcePath);
        for (String line : lines) {
            line = line.trim();
            if (!line.isEmpty()) {
                String[] elts = line.split("\\|");
                String label = elts[0].trim();
                String[] subelts = elts[1].trim().split(",");
                for (String subelt : subelts) {
                    this.phraseSynonyms.put(subelt.trim(), label);
                }
            }
        }

    }

    public String getWordSynonym(String word) {
        
        if (TextToolbox.isReallyEmpty(word)) return word;
        
        word = word.trim();
        
        if (this.wordSynonyms.containsKey(word)) {
            return this.wordSynonyms.get(word);
        } else {
            return word;
        }
    
    }
    
    public String getPhraseSynonym(String phrase) {
        
        if (TextToolbox.isReallyEmpty(phrase)) return phrase;
        
        phrase = phrase.trim();
        
        if (this.phraseSynonyms.containsKey(phrase)) {
            return this.phraseSynonyms.get(phrase);
        } else {
            return phrase;
        }
    
    }
    
}
