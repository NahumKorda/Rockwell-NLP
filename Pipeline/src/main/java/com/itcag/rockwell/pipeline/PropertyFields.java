package com.itcag.rockwell.pipeline;

/**
 * <p>Enumerates processing instructions passed as an argument to the constructor of the {@link Pipeline} class.</p>
 * <p>Since the instructions are passed as an instance of the Java {@link java.util.Properties Properties} class, each value in this enum can potentially create a field in the instance of that class.</p>
 */
public enum PropertyFields {
    
    /** Instructs the pipeline which task it should prepare to execute. */
    TASK("task"),
    /** Local path to a text file containing Rockwell expressions used for classifying. */
    EXPRESSIONS("expressions"),
    /** Local path to a text file containing Rockwell expressions for proprietary patterns that are added to the generic Rockwell patterns. */
    PATTERNS("patterns"),
    /** Named entity extraction instructions specifying which named entities are to be extracted. */
    INSTRUCTIONS("instructions"),
    /** Local path to a text file containing Rockwell expressions that identify concepts that are inserted as {@link com.itcag.rockwell.lang.Semtoken semtokens} into the array list of {@link com.itcag.rockwell.lang.Token tokens} representing a sentence. */
    CONCEPTS("concepts"),
    /** Local path to a text file containing script describing Rockwell frame expressions. */
    FRAME_EXPRESSIONS("frameExpressions"),
    /** Local path to a text file containing Rockwell frames. */
    FRAMES("frames"),

    ;

    private final String field;

    private PropertyFields(String field) {
        this.field = field;
    }

    public String getField() {
        return this.field;
    }

}
