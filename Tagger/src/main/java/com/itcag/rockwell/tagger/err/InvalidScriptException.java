package com.itcag.rockwell.tagger.err;

/**
 * <p>Exception signaling a problem with the Rockwell script.</p>
 * <p>To learn more about Rockwell script see: <a href="https://docs.google.com/document/d/1wMYCXAOm0cmJ4z5PHLRK5JyPsBY-p3vEYnupik-QT-A/edit?usp=sharing"  target="_blank">Rockwell Script (User Manual)</a>.</p>
 */
public final class InvalidScriptException extends Exception {
    
    public InvalidScriptException() {
        super();
    }
    
    public InvalidScriptException(String msg) {
        super(msg);
    }
    
    public InvalidScriptException(Exception ex) {
        super(ex);
    }
    
}
