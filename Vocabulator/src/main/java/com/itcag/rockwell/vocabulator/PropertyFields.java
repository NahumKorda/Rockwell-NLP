package com.itcag.rockwell.vocabulator;

/**
 * <p>Enumerates processing instructions passed as an argument to the constructor of the {@link VocabularyExtractor} class.</p>
 * <p>Since the instructions are passed as an instance of the Java {@link java.util.Properties Properties} class, each value in this enum can potentially create a field in the instance of that class.</p>
 */
public enum PropertyFields {

    /** Specifies what should be extracted. Tasks are enumerated in the {@link com.itcag.rockwell.vocabulator.VocabularyExtractor.Tasks  Tasks} enum. */
    TASK("task"),
    /** Specifies the lower boundary that the frequency of occurrence must exceed, in order to be included in the results. */
    THRESHOLD("threshold"),
    /** Specifies the lower boundary that the frequency of occurrence must exceed, in order not to be removed from the results when trimming intermediary results. */
    TRIM_THRESHOLD("trimThreshold"),
    /** Specifies which word classes must be ignored (for example, stop words). */
    EXCLUSIONS("exclusions"),
    /** Specifies the minimum number of words allowed in an extracted phrase. his number cannot be less than 2. It applies only to the phrase extraction. */
    MIN_PHRASE_LENGTH("min"),
    /** specifies the maximum number of words allowed in an extracted phrase. It applies only to the phrase extraction. */
    MAX_PHRASE_LENGTH("max"),
    /** Specifies terms that must occur on a sentence, in order to extract phrases from it. If not specified, every sentence accepted. It applies only to the phrase extraction. */
    POSITIVE_FILTER("positive"),
    /** Specifies terms that if cause a sentence to be ignored if they occur in it. It applies only to the phrase extraction. */
    NEGATIVE_FILTER("negative"),
    /** Specifies terms that must occur in an extracted phrase, in order to include the phrase in the results. It applies only to the phrase extraction. */
    REQUIRED_FILTER("required"),
    
    ;
    
    private final String field;
    
    private PropertyFields(String field) {
        this.field = field;
    }
    
    public String getField() {
        return this.field;
    }
    
}
