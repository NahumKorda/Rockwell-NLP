package com.itcag.rockwell.tagger.lang;

import java.util.ArrayList;
import java.util.UUID;

/**
 * <p>This class holds a component of a Rockwell script instruction. An instruction consists of one or more conditions. The first condition is the accepting condition, which if satisfied validates the match. All others - if any - are rejecting conditions. Even if validated, a match is nonetheless rejected if any of the rejecting conditions is also satisfied.</p>
 * <p>This class is extended by the {@link com.itcag.rockwell.tagger.lang.AcceptingCondition AcceptingCondition} and {@link com.itcag.rockwell.tagger.lang.RejectingCondition RejectingCondition} classes.</p>
 * <p>Every condition consists of one or more condition elements.</p>
 * <p>To learn more about Rockwell script see: <a href="https://docs.google.com/document/d/1wMYCXAOm0cmJ4z5PHLRK5JyPsBY-p3vEYnupik-QT-A/edit?usp=sharing"  target="_blank">Rockwell Script (User Manual)</a>.</p>
 */
public class Condition {

    private final String id = UUID.randomUUID().toString().replace("-", "");
    
    private final String script;
    
    private final ArrayList<ConditionElement> conditionElements = new ArrayList<>();
    
    /**
     * @param script Rockwell expression formulated in Rockwell script. Kept as reference to the expression from which the condition was created.
     */
    public Condition(String script) {
        this.script = script;
    }
    
    /**
     * @return String holding UUID. It is for internal use only.
     */
    public String getId() {
        return id;
    }

    /**
     * @return Rockwell expression formulated in Rockwell script. Kept as reference to the expression from which the condition was created.
     */
    public String getScript() {
        return script;
    }

    /**
     * @return Array list containing the constituent elements.
     */
    public ArrayList<ConditionElement> getConditionElements() {
        return conditionElements;
    }
    
    /**
     * @param conditionElement Instance of the {@link com.itcag.rockwell.tagger.lang.ConditionElement ConditionElement} class holding a constituent of the condition.
     */
    public void addConditionElement(ConditionElement conditionElement) {
        conditionElements.add(conditionElement);
    }

    @Override
    public String toString() {
        return script;
    }
    
}
