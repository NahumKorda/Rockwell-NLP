package com.itcag.rockwell.tagger.lang;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * <p>This class holds a state of the finite state automaton implemented in the {@link com.itcag.rockwell.tagger.Processor Processor} class.</p>
 * <p>Rockwell uses the non-deterministic finite state automaton, which can simultaneously be in multiple states. However, each state has its own history of transitions. The history of transitions is preserved as an array list of matches that led to the current state.</p>
 * <p>A state can be created by an instance of the {@link com.itcag.rockwell.tagger.lang.AcceptingCondition AcceptingCondition} class or by an instance of the {@link com.itcag.rockwell.tagger.lang.RejectingCondition RejectingCondition} class. Once created the state is updated with every new match.</p>
 */
public final class State {
    
    private final String conditionId;
    private final String script;
    
    private int state;
    
    private String tag = null;

    private final ArrayList<String> rejectedById = new ArrayList<>();
    private String idToBeRejected = null;
    
    private final TreeMap<Integer, Match> matches = new TreeMap<>();

    private int optionalCount = 0;

    /**
     * An instance of this class is created when the first condition element of a condition is matched. Every additional match is added to the condition, and its state is updated with a new value.
     * @param conditionId String holding the ID of the condition that creates an instance of this class.
     * @param script Rockwell script that created this condition.
     * @param state Integer representing the initial state. This value is updated with every new match.
     */
    public State(String conditionId, String script, int state) {
        this.conditionId = conditionId;
        this.script = script;
        this.state = state;
    }

    /**
     * @return String holding the ID of the condition that created this state.
     */
    public String getConditionId() {
        return this.conditionId;
    }

    /**
     * @return Rockwell script that created the condition that created this state.
     */
    public String getScript() {
        return this.script;
    }

    /**
     * @return Integer representing the current state. 
     */
    public int getState() {
        return this.state;
    }

    /**
     * @param state Integer representing the current state. The current state is updated with every new match.
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * @return Tree map containing all matches that led to the current state. The key in the map is the index position of the corresponding token in a sentence.
     */
    public TreeMap<Integer, Match> getMatches() {
        return this.matches;
    }

    /**
     * @param match New match added to the history of the state,
     */
    public void addMatch(Match match) {
        /**
         * It is necessary to prevent modification of a token already
         * identified by the specific condition into a quodlibet token.
         * This would happen if there are two consecutive infixes in a condition.
         * The first infix may be anchored by the first token, but then this token
         * could be modified to quodlibet, and thus included into the next infix,
         * although it was already identified.
         */
        if (this.matches.containsKey(match.getIndex()) && match.isQuodlibet()) return;
        this.matches.put(match.getIndex(), match);
    }

    /**
     * @return Integer representing the index position in a sentence of the first matched token.
     */
    public int getFirstMatch() {
        return this.matches.firstKey();
    }

    /**
     * @return Integer representing the index position in a sentence of the last matched token.
     */
    public int getLastMatch() {
        return this.matches.lastKey();
    }

    /**
     * @return String holding the tag of the corresponding Rockwell expression (used only if the condition that created this state was an instance of the {@link com.itcag.rockwell.tagger.lang.AcceptingCondition AcceptingCondition} class).
     */
    public final String getTag() {
        return this.tag;
    }

    /**
     * @param tag String holding the tag of the corresponding Rockwell expression (used only if the condition that created this state was an instance of the {@link com.itcag.rockwell.tagger.lang.AcceptingCondition AcceptingCondition} class).
     */
    public final void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return Array list containing IDs of the conditions that can reject the condition that created this state (used only if the condition that created this state was an instance of the {@link com.itcag.rockwell.tagger.lang.AcceptingCondition AcceptingCondition} class).
     */
    public ArrayList<String> getRejectedById() {
        return this.rejectedById;
    }

    /**
     * @param rejectedById Array list containing IDs of the conditions that can reject the condition that created this state (used only if the condition that created this state was an instance of the {@link com.itcag.rockwell.tagger.lang.AcceptingCondition AcceptingCondition} class).
     */
    public void setRejectedById(ArrayList<String> rejectedById) {
        this.rejectedById.addAll(rejectedById);
    }

    /**
     * @return String holding the ID of the condition that can be rejected by the condition that created this state (used only if the condition that created this state was an instance of the {@link com.itcag.rockwell.tagger.lang.RejectingCondition RejectingCondition} class).
     */
    public String getIdToBeRejected() {
        return this.idToBeRejected;
    }

    /**
     * @param idToBeRejected String holding the ID of the condition that can be rejected by the condition that created this state (used only if the condition that created this state was an instance of the {@link com.itcag.rockwell.tagger.lang.RejectingCondition RejectingCondition} class).
     */
    public void setIdToBeRejected(String idToBeRejected) {
        this.idToBeRejected = idToBeRejected;
    }

    /**
     * For practical reasons the size of the Kleene closure is restricted by the {@link com.itcag.rockwell.tagger.lang.Conditions#MAX_OPTIONAL preset limit}. Therefore, every state counts consecutive quodlibet matches.
     * @return Integer holding the count of the consecutive quodlibet matches.
     */
    public int getOptionalCount() {
        return this.optionalCount;
    }

    /**
     * @param optionalCount Integer holding the count of the consecutive quodlibet matches.
     */
    public void setOptionalCount(int optionalCount) {
        this.optionalCount = optionalCount;
    }
    
    /**
     * Increments the count of the consecutive quodlibet matches by one.
     */
    public void incrementOptionalCount() {
        this.optionalCount++;
    }
    
    /**
     * Resets the count of the consecutive quodlibet matches to zero.
     */
    public void resetOptionalCount() {
        this.optionalCount = 0;
    }
    
    /**
     * @return Integer holding the index position of the first match.
     */
    public int getStart() {
        return this.matches.firstEntry().getValue().getIndex();
    }
    
    /**
     * @return Integer holding the index position of the last match.
     */
    public int getEnd() {
        return this.matches.lastEntry().getValue().getIndex();
    }

    /**
     * @return The deep clone of the current instance of this class.
     */
    public State getCopy() {
        State retVal = new State(this.conditionId, this.script, this.state);
        retVal.setTag(this.tag);
        retVal.setRejectedById(this.rejectedById);
        retVal.setIdToBeRejected(this.idToBeRejected);
        this.matches.entrySet().forEach((entry) -> {
            retVal.addMatch(entry.getValue());
        });
        retVal.setOptionalCount(this.optionalCount);
        return retVal;
    }
    
    @Override
    public String toString() {
        
        StringBuilder retVal = new StringBuilder();
        retVal.append(this.conditionId).append(" > ").append(this.state);
        this.matches.entrySet().stream().forEach((entry) -> {
            retVal.append("\t").append(entry.getValue().toString());
        });
        retVal.append("\n").append(this.script);
        return retVal.toString();
    }
    
}
