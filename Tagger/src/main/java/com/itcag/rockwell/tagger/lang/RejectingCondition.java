package com.itcag.rockwell.tagger.lang;

/**
 * <p>This class holds a component of a Rockwell script instruction that rejects a match validated by an accepting condition.</p>
 * <p>This class extends the {@link com.itcag.rockwell.tagger.lang.Condition Condition} class.</p>
 * <p>To learn more about Rockwell script see: <a href="https://docs.google.com/document/d/1wMYCXAOm0cmJ4z5PHLRK5JyPsBY-p3vEYnupik-QT-A/edit?usp=sharing"  target="_blank">Rockwell Script (User Manual)</a>.</p>
 */
public class RejectingCondition extends Condition {
    
    private final String idToBeRejected;
    
    /**
     * @param script Rockwell expression formulated in Rockwell script. Kept as reference to the expression from which the condition was created.
     * @param idToBeRejected String holding the ID of the corresponding accepting condition.
     */
    public RejectingCondition(String script, String idToBeRejected) {
        super(script);
        this.idToBeRejected = idToBeRejected;
    }
    
    /**
     * @return String holding the ID of the corresponding accepting condition.
     */
    public String getIdToBeRejected() {
        return this.idToBeRejected;
    }
    
}
