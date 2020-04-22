package com.itcag.rockwell.vocabulator;

import com.itcag.util.Printer;

import java.util.HashSet;

/**
 * <p>This class holds the extracted word or phrase.</p>
 */
public final class Term {

    private String text;
    private int foo = 1;
    
    private final HashSet<String> sentences = new HashSet<>();
    
    /**
     * @param text String holding the extracted word/phrase.
     */
    public Term(String text) {
        this.text = text;
    }

    /**
     * @return String holding the extracted word/phrase.
     */
    public final String getText() {
        return this.text;
    }
    
    /**
     * @param text String holding the extracted word/phrase.
     */
    public final void setText(String text) {
        this.text = text;
    }

    /**
     * @return Integer indicating the frequency of occurrence of the extracted word/phrase.
     */
    public final int getFOO() {
        return this.foo;
    }
    
    /**
     * Increments the frequency of occurrence of the extracted word.phrase by one.
     */
    public final void incrementFOO() {
        this.foo++;
    }
    
    /**
     * @param foo Integer indicating the frequency of occurrence of the extracted word/phrase.
     */
    public final void addFOO(int foo) {
        this.foo += foo;
    }
    
    /**
     * The number of sentences and the frequency of occurrence do not need to be identical, since word/phrase can be extracted from identical sentences, and while this would increment the frequency of occurrence, the sentences are unique, and identical sentences occur always only once.
     * @return Hash set containing all sentences from which the word/phrase was extracted.
     */
    public final HashSet<String> getSentences() {
        return this.sentences;
    }

    /**
     * @param sentence String holding the sentence from which word/phrase was extracted.
     */
    public final void addSentence(String sentence) {
        if (this.sentences.size() == 100) return;
        this.sentences.add(sentence);
    }
    
    @Override
    public String toString() {
        return this.text;
    }
    
    public void print(boolean includeSentences) {
        
        Printer.print("☆ " + foo + "\t" + text);
        if (!includeSentences) return;
        
        this.sentences.forEach((sentence) -> {
            Printer.print("\t" + sentence);
        });
        
    }
    
}
