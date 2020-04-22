package com.itcag.rockwell;

/**
 * <p>Complete list of all part of speech types supported by Rockwell.</p>
 * <p>Types group parts of speech that belong to the same word class. For example, all adjectives (AJ0, AJC, AJS) are grouped under the same type (AJ).</p>
 */
public enum POSType {

    /** Adjectives. */
    AJ,
    /** Adverbs. */
    AV,
    /** Conjunctions. */
    CJ,
    /** Determiners. */
    DT,
    /** Existential "there". */
    EX,
    /** Interjections. */
    IT,
    /** Negations. */
    NG,
    /** Nouns. */
    NN,
    /** Numbers. */
    NM,
    /** Punctuation characters. */
    PC,
    /** Pronouns. */
    PN,
    /** The possessive or genitive marker "'s" or "'" (the Saxon genitive). */
    PO,
    /** Prepositions. */
    PR,
    /** Infinitive marker "to". */
    TO,
    /** Auxiliary verbs. */
    VA,
    /** Modal verbs. */
    VM,
    /** Verbs. */
    VV,
    /** Unrecognized. */
    XX,
    /** Symbols. */
    XZ,

}

