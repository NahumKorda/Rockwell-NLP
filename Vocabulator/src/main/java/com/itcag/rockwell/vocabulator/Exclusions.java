package com.itcag.rockwell.vocabulator;

/**
 * <p>Enumerates all possible exclusions that can be specified by the {@link com.itcag.rockwell.vocabulator.PropertyFields#EXCLUSIONS EXCLUSIONS} field.</p>
 * <p>Every value has a corresponding instruction used in bitwise comparison to determine which exclusions are specified.</p>
 */
public enum Exclusions {

    /** Used by both single-word and lemma extraction. */
    STOPWORDS(2),
    /** Used by both single-word and lemma extraction. */
    CONTRACTIONS(4),
    /** Used by both single-word and lemma extraction. */
    SYMBOLS(8),
    /** Used by both single-word and lemma extraction. */
    DIGITS(16),
    
    /** All parts of speech are excluded, except nouns. Specific to lemma extraction. */
    ALL_EXCEPT_NOUNS(32),
    /** All parts of speech are excluded, except verbs. Specific to lemma extraction. */
    ALL_EXCEPT_VERBS(64),
    /** All parts of speech are excluded, except adjectives. Specific to lemma extraction. */
    ALL_EXCEPT_ADJECTIVES(128),

    /** Specific to phrase extraction. */
    STOPPHRASES(256),
    /** Excludes noun phrases. Specific to phrase extraction. */
    NOUN_PHRASES(512),
    /** Excludes verb phrases. Specific to phrase extraction. */
    ADJECTIVE_PHRASES(1024),
    /** Excludes adjective phrases. Specific to phrase extraction. */
    VERB_PHRASES(2048),
    
    ;

    private final long instruction;

    private Exclusions(long instruction) {
        this.instruction = instruction;
    }

    /**
     * @return Long number used in bitwise comparison, in order to determine whether to extract a particular entity or not. The {@link com.itcag.rockwell.semantex.ner.NER.Instructions Instructions} enum provides the {@link com.itcag.rockwell.semantex.ner.NER.Instructions#getInstruction() getInstruction()} method to access the instruction for every available named entity. Instructions must be combined using the bitwise operator OR ("|").
     */
    public long getInstruction() {
        return this.instruction;
    }

}
