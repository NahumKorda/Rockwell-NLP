package com.itcag.rockwell.tagger.lang;

import com.itcag.rockwell.tagger.ConditionFactory;
import com.itcag.util.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * <p>This class holds a collection of {@link com.itcag.rockwell.tagger.lang.Condition conditions} created by a collection of Rockwell expressions. It is used by the finite state automaton implemented in the {@link com.itcag.rockwell.tagger.Processor Processor} class.</p>
 * <p>Rockwell expressions are provided in the Rockwell script, which is parsed by the {@link com.itcag.rockwell.tagger.ConditionFactory ConditionFactory} class.</p>
 * <p>Here the state transitions are defined and assigned to the {@link com.itcag.rockwell.tagger.lang.ConditionElement condition elements}.</p>
 * <p>To learn more about Rockwell script see: <a href="https://docs.google.com/document/d/1wMYCXAOm0cmJ4z5PHLRK5JyPsBY-p3vEYnupik-QT-A/edit?usp=sharing"  target="_blank">Rockwell Script (User Manual)</a>.</p>
 */
public class Conditions {

    /**
     * The initial state for all conditions.
     */
    public final static int INITIAL_STATE_CODE = 0;
    /**
     * The final state for all conditions that signals that all condition elements in the condition were successfully matched.
     */
    public final static int FINAL_STATE_CODE = -1;
    
    /**
     * The preset maximum size of the Kleene closure (the maximum number of the consecutive quodlibet matches), and of the {@link com.itcag.rockwell.tagger.lang.Affix affixes}.
     */
    public final static int MAX_OPTIONAL = 10;
    
    /**
     * The key is the condition ID.
     */
    private final HashMap<String, Condition> conditions = new HashMap<>();
    
    /**
     * Contains the first condition elements in every condition.
     * The main key is a composite of aspect + "|" + value.
     * @see com.itcag.rockwell.tagger.lang.MatchingSpecification#getKey()
     * The value is an array of the corresponding condition elements.
     */
    private final HashMap<String, ArrayList<ConditionElement>> initial = new HashMap<>();
    
    /**
     * Contains all condition elements except the first.
     * The outer key is a composite of aspect + "|" + value.
     * @see com.itcag.rockwell.tagger.lang.MatchingSpecification#getKey()
     * The medial key is the "in" state.
     * The inner key is the "out" state.
     * The value is the corresponding condition element.
     */
    private final HashMap<String, HashMap<Integer, HashMap<Integer, ConditionElement>>> elements = new HashMap<>();

    private int stateCode = INITIAL_STATE_CODE;

    /**
     * @param scripts Array list containing all Rockwell expressions that create conditions stored in this class.
     * @throws Exception if anything goes wrong.
     */
    public Conditions(ArrayList<String> scripts) throws Exception {
        
        ConditionFactory factory = new ConditionFactory();
        
        for (String script : scripts) {
            script = script.trim();
            if (script.isEmpty()) continue;
            if (script.startsWith("#")) continue;
            AcceptingCondition acceptingCondition = factory.get(script);
            addCondition(acceptingCondition);
        }
    
    }
    
    private void addCondition(AcceptingCondition acceptingCondition) throws Exception {
        
        conditions.put(acceptingCondition.getId(), acceptingCondition);
        addConditionElements(acceptingCondition);

        for (RejectingCondition rejectingCondition : acceptingCondition.getRejecting()) {
            conditions.put(rejectingCondition.getId(), rejectingCondition);
            addConditionElements(rejectingCondition);
        }
        
    }
    
    private void addConditionElements(Condition condition) {
        
        for (int i = 0; i < condition.getConditionElements().size(); i++) {
            
            ConditionElement conditionElement = condition.getConditionElements().get(i);

            if (i == 0) {
                conditionElement.setIn(INITIAL_STATE_CODE);
            } else {
                conditionElement.setIn(stateCode);
            }
            
            if (i == condition.getConditionElements().size() - 1) {
                conditionElement.setOut(FINAL_STATE_CODE);
            } else {
                if (conditionElement.isQuodlibet()) {
                    conditionElement.setOut(stateCode);
                } else {
                    conditionElement.setOut(++stateCode);
                }
            }
            
            /**
             * If the first element is optional,
             * the next element must be also added to the initial.
             */
            if (i == 0 || stateCode == 0) {
                addToInitial(conditionElement);
            } else {
                addToElements(conditionElement);
            }
        
        }

    }
    
    private void addToInitial(ConditionElement conditionElement) {
        if (initial.containsKey(conditionElement.getKey())) {
            initial.get(conditionElement.getKey()).add(conditionElement);
        } else {
            initial.put(conditionElement.getKey(), new ArrayList<>(Arrays.asList(conditionElement)));
        }
    }
    
    private void addToElements(ConditionElement conditionElement) {
        if (elements.containsKey(conditionElement.getKey())) {
            HashMap<Integer, HashMap<Integer, ConditionElement>> medial = elements.get(conditionElement.getKey());
            if (medial.containsKey(conditionElement.getIn())) {
                HashMap<Integer, ConditionElement> inner = medial.get(conditionElement.getIn());
                inner.put(conditionElement.getOut(), conditionElement);
            } else {
                HashMap<Integer, ConditionElement> inner = new HashMap<>();
                inner.put(conditionElement.getOut(), conditionElement);
                medial.put(conditionElement.getIn(), inner);
            }
        } else {
            HashMap<Integer, ConditionElement> inner = new HashMap<>();
            inner.put(conditionElement.getOut(), conditionElement);
            HashMap<Integer, HashMap<Integer, ConditionElement>> medial = new HashMap<>();
            medial.put(conditionElement.getIn(), inner);
            elements.put(conditionElement.getKey(), medial);
        }
    }

    /**
     * @return Hash map containing all conditions. The key is the condition ID.
     */
    public HashMap<String, Condition> getConditions() {
        return conditions;
    }

    /**
     * @return Return the hash map of all initial condition elements. The key is a composite of aspect + "|" + value.
     * @see com.itcag.rockwell.tagger.lang.MatchingSpecification#getKey()
     */
    public HashMap<String, ArrayList<ConditionElement>> getInitial() {
        return initial;
    }

    /**
     * @return hash map of all transitional condition elements (all but the first condition element in every condition). The key is a composite of aspect + "|" + value.
     * @see com.itcag.rockwell.tagger.lang.MatchingSpecification#getKey()
     */
    public HashMap<String, HashMap<Integer, HashMap<Integer, ConditionElement>>> getElements() {
        return elements;
    }

    @Override
    public String toString() {

        StringBuilder retVal = new StringBuilder();
        
        this.conditions.values().stream().map((condition) -> {
            retVal.append("\n").append(condition.getId()).append(" ").append(condition.getScript());
            return condition;
        }).map((condition) -> {
            condition.getConditionElements().forEach((element) -> {
                retVal.append("\n").append(element.toString(1));
            });
            return condition;
        }).forEachOrdered((_item) -> {
            retVal.append("\n");
        });
        
        return retVal.toString();
        
    }
    
    public void print() {
        Printer.print();
        Printer.print("CONDITIONS:");
        Printer.print("----------------------------------------------");
        Printer.print(this.toString());
        Printer.print("----------------------------------------------");
        Printer.print();
    }
    
}
