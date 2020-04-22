package com.itcag.rockwell.lang;

/**
 * <p>This class holds a datum extracted from a sentence by a Rockwell frame.</p>
 */
public class Extract {

    private final String script;
    
    private final String meaning;
    private final String value;
    
    private String sentenceId = null;
    
    /**
     * @param script String holding the Rockwell frame that extracted the datum.
     * @param meaning String holding the meaning value of the Rockwell frame. The meaning indicates what the extracted datum is. 
     * @param value String holding the extracted datum.
     */
    public Extract(String script, String meaning, String value) {
        this.script = script;
        this.meaning = meaning;
        this.value = value;
    }

    /**
     * @return String holding the Rockwell frame that extracted the datum.
     */
    public String getScript() {
        return this.script;
    }
    
    /**
     * @return String holding the meaning value of the Rockwell frame. The meaning indicates what the extracted datum is. 
     */
    public String getMeaning() {
        return meaning;
    }

    /**
     * @return String holding the extracted datum.
     */
    public String getValue() {
        return value;
    }

    /**
     * @return String holding the ID of the sentence from which the datum was extracted.
     */
    public String getSentenceId() {
        return this.sentenceId;
    }
    
    /**
     * @param sentenceId String holding the ID of the sentence from which the datum was extracted.
     */
    public void setSentenceId(String sentenceId) {
        this.sentenceId = sentenceId;
    }
    
    @Override
    public String toString() {
        return this.value + " [" + this.meaning + "]";
    }
    
}
