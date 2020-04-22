package com.itcag.util;

/**
 * <p>Generic exception that replaces all component-specific exceptions.</p>
 * <p>Component-specific exceptions are handled internally by various class, and are not exposed to external classes. Instead, one simple generic exception is used.</p>
 * <p>UtilException is expected to be caught and handled differently than other exceptions. Its messages are purposely formulated in a way that allows them to be displayed to the end user.</p>
 */
public final class UtilException extends Exception {
    
    /**
     * This constructor is provided only for the compatibility with the superclass.
     */
    public UtilException() {}
    
    /**
     * This constructor is provided only for the compatibility with the superclass.
     */
    public UtilException(Exception ex) {
        super(ex);
    }
    
    /**
     * UtilException is always assumed to feature a message that can be displayed to the end user.
     * @param msg String containing easily understood explanation of the problem that caused this exception.
     */
    public UtilException(String msg) {
        super(msg);
    }
    
}
