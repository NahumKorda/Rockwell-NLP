package com.itcag.rockwell.tagger.lang;

import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;

/**
 * <p>This class holds a constituent of a condition. Condition element corresponds to a token. If matched, it creates an instance of the {@link com.itcag.rockwell.tagger.lang.Match Match} class.</p>
 * <p>A condition consists of one or more condition elements. The first condition element is always the accepting element, which validates the match. All others - if any - are rejecting elements. Even if validated, a match is nonetheless rejected if any of the rejecting elements is also satisfied.</p>
 * <p>A condition element consists of one or more {@link com.itcag.rockwell.tagger.lang.MatchingSpecification matching specifications}. A matching specification consists of an aspect/value pair. Aspect specifies which aspect of a token must be matched, and value is a string that must be matched by that aspect.</p>
 * <p>The first matching specification creates an instance of this class. All others - if any - are either inserted as {@link com.itcag.rockwell.tagger.lang.Affix affixes}, or as additional specifications. All affixes and/or additional specifications must be satisfied, in order to validate the match.</p>
 * <p>A condition element can be included in the Kleene closure. Then it is referred to as <i>quodlibet</i>, and it implies that zero, one or more tokens are matched regardless of what they are (equivalent of the so-called <i>Kleene star</i>).</p>
 * <p>To learn more about affixes and how Rockwell implements Kleene closure see: <a href="https://docs.google.com/document/d/1wMYCXAOm0cmJ4z5PHLRK5JyPsBY-p3vEYnupik-QT-A/edit?usp=sharing"  target="_blank">Rockwell Script (User Manual)</a>.</p>
 * <p>This class extends the {@link com.itcag.rockwell.tagger.lang.MatchingSpecification MatchingSpecification} class.</p>
 * <p>Validation of matches is carried out using a finite state automaton implemented as the {@link com.itcag.rockwell.tagger.Processor Processor} class. Therefore, every condition element specifies two states: the initial state, and the state into which the automaton transits if the element was matched.</p>
 */
public final class ConditionElement extends MatchingSpecification {
    
    private String conditionId = null;
    
    private final ArrayList<MatchingSpecification> additionalSpecifications = new ArrayList<>();
    
    private Affix prefix = null;
    private Affix infix = null;
    private Affix suffix = null;
    
    private boolean quodlibet = false;

    private final ArrayList<MatchingSpecification> rejects = new ArrayList<>();
    
    private Integer in = null;
    private Integer out = null;
    
    /**
     * @param aspect Value of the {@link com.itcag.rockwell.tagger.lang.MatchingSpecification.Aspect Aspect} enum that specifies which aspect of a token must be matched.
     * @param value String holding the value that must be matched. This value depends on the aspect.
     */
    public ConditionElement(Aspect aspect, String value) {
        super(aspect, value);
    }

    /**
     * @return String holding the ID of the condition that contains this element.
     */
    public String getConditionId() {
        return conditionId;
    }

    /**
     * @param conditionId String holding the ID of the condition that contains this element.
     */
    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    /**
     * @return Array list of additional matching specifications that must be satisfied to validate a match.
     */
    public ArrayList<MatchingSpecification> getAdditionalSpecifications() {
        return additionalSpecifications;
    }

    /**
     * @param additionalCondition Instance of the {@link com.itcag.rockwell.tagger.lang.MatchingSpecification MatchingSpecification} class that holds an additional matching specification that must be satisfied to validate a match.
     */
    public void addAdditionalCondition(MatchingSpecification additionalCondition) {
        additionalSpecifications.add(additionalCondition);
    }

    /**
     * @param aspect Value of the {@link com.itcag.rockwell.tagger.lang.MatchingSpecification.Aspect Aspect} enum that specifies which aspect of a token must be matched.
     * @param value String holding the value that must be matched. This value depends on the aspect.
     */
    public void addAdditionalCondition(Aspect aspect, String value) {
        additionalSpecifications.add(new MatchingSpecification(aspect, value));
    }

    /**
     * @return Instance of the {@link com.itcag.rockwell.tagger.lang.Affix Affix} class that holds a prefix.
     */
    public Affix getPrefix() {
        return prefix;
    }

    /**
     * @param prefix Instance of the {@link com.itcag.rockwell.tagger.lang.Affix Affix} class that holds a prefix.
     */
    public void setPrefix(Affix prefix) {
        this.prefix = prefix;
    }

    /**
     * @return Instance of the {@link com.itcag.rockwell.tagger.lang.Affix Affix} class that holds an infix.
     */
    public Affix getInfix() {
        return infix;
    }

    /**
     * @param infix Instance of the {@link com.itcag.rockwell.tagger.lang.Affix Affix} class that holds an infix.
     */
    public void setInfix(Affix infix) {
        this.infix = infix;
    }

    /**
     * @return Instance of the {@link com.itcag.rockwell.tagger.lang.Affix Affix} class that holds a suffix.
     */
    public Affix getSuffix() {
        return suffix;
    }

    /**
     * @param suffix Instance of the {@link com.itcag.rockwell.tagger.lang.Affix Affix} class that holds a suffix.
     */
    public void setSuffix(Affix suffix) {
        this.suffix = suffix;
    }

    /**
     * @return Boolean indication whether this condition element is in the Kleene closure.
     */
    public boolean isQuodlibet() {
        return quodlibet;
    }

    /**
     * @param quodlibet Boolean indication whether this condition element is in the Kleene closure.
     */
    public void setQuodlibet(boolean quodlibet) {
        this.quodlibet = quodlibet;
    }

    /**
     * @return Array list containing rejecting instances of the {@link com.itcag.rockwell.tagger.lang.MatchingSpecification MatchingSpecification} class.
     */
    public ArrayList<MatchingSpecification> getRejects() {
        return this.rejects;
    }
    
    /**
     * @param reject A rejecting instance of the {@link com.itcag.rockwell.tagger.lang.MatchingSpecification MatchingSpecification} class.
     */
    public void addReject(MatchingSpecification reject) {
        this.rejects.add(reject);
    }
    
    /**
     * @return Integer holding the initial finite state automaton state required by this condition element.
     */
    public Integer getIn() {
        return in;
    }

    /**
     * @param in Integer holding the initial finite state automaton state required by this condition element.
     */
    public void setIn(Integer in) {
        this.in = in;
    }

    /**
     * @return Integer holding the state to which the finite state automaton transits if this condition element is matched.
     */
    public Integer getOut() {
        return out;
    }

    /**
     * @param out Integer holding the state to which the finite state automaton transits if this condition element is matched.
     */
    public void setOut(Integer out) {
        this.out = out;
    }

    @Override
    public String toString() {
        return toString(0);
    }
    
    @Override
    public String toString(int indentation) {
        
        StringBuilder retVal = new StringBuilder();
        
        String indent = TextToolbox.repeat(indentation, "\t");
        indentation++;
        
        retVal.append(indent).append(getKey());
        if (this.quodlibet) retVal.append(" (optional)");
        if (this.in != null && this.out != null) retVal.append(" ").append(in).append(" > ").append(out);
        
        if (!this.additionalSpecifications.isEmpty()) {
            retVal.append("\n").append(indent).append("Additional condition elements:");
            for (MatchingSpecification additional : this.additionalSpecifications) {
                retVal.append("\n").append(additional.toString(indentation));
            }
        }
        
        if (this.prefix != null) {
            retVal.append("\n").append(indent).append("Prefix:");
            retVal.append("\n").append(this.prefix.toString(indentation));
        }
        
        if (this.infix != null) {
            retVal.append("\n").append(indent).append("Infix:");
            retVal.append("\n").append(this.infix.toString(indentation));
        }
        
        if (this.suffix != null) {
            retVal.append("\n").append(indent).append("Suffix:");
            retVal.append("\n").append(this.suffix.toString(indentation));
        }
        
        if (this.rejects != null) {
            retVal.append("\n").append(indent).append("Reject elements:");
            for (MatchingSpecification reject : this.rejects) {
                retVal.append("\n").append(reject.toString(indentation));
            }
        }
        
        return retVal.toString();
        
    }
    
}
