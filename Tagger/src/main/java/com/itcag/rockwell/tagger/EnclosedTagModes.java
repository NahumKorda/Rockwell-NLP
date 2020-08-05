package com.itcag.rockwell.tagger;

/**
 * Instructions determining how tags enclosed within other tags are handled.
 */
public enum EnclosedTagModes {

    /** All enclosed tags are removed and ignored. */
    NONE,

    /** Enclosed tags are removed only if their value is the same as the enclosing tag. */
    NON_IDENTICAL,

    /** All enclosed tags are preserved. */
    ALL
    
}
