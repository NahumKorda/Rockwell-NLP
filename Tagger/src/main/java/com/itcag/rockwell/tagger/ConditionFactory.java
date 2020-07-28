/*
 *
 * Copyright 2020 IT Consulting AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.itcag.rockwell.tagger;

import com.itcag.rockwell.tagger.err.InvalidScriptException;
import com.itcag.rockwell.tagger.lang.Affix;
import com.itcag.rockwell.tagger.lang.AcceptingCondition;
import com.itcag.rockwell.tagger.lang.ConditionElement;
import com.itcag.rockwell.tagger.lang.Condition;
import com.itcag.rockwell.tagger.lang.RejectingCondition;
import com.itcag.util.Converter;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

/**
 * <p>This class parses Rockwell script and converts it into instances of the {@link com.itcag.rockwell.tagger.lang.Condition Condition} class.</p>
 * <p>To learn more about Rockwell script see: <a href="https://docs.google.com/document/d/1wMYCXAOm0cmJ4z5PHLRK5JyPsBY-p3vEYnupik-QT-A/edit?usp=sharing"  target="_blank">Rockwell Script (User Manual)</a>.</p>
 */
public class ConditionFactory {

    private class Aspect {
        
        private final ConditionElement.Aspect aspect;
        private final boolean inclusive;
        private final boolean complete;
        private final boolean compulsory;
        
        private Aspect(ConditionElement.Aspect aspect, boolean inclusive, boolean complete, boolean compulsory) {
            this.aspect = aspect;
            this.inclusive = inclusive;
            this.complete = complete;
            this.compulsory = compulsory;
        }

        private ConditionElement.Aspect getAspect() {
            return aspect;
        }

        private boolean isInclusive() {
            return inclusive;
        }

        private boolean isComplete() {
            return complete;
        }

        private boolean isCompulsory() {
            return compulsory;
        }

    }
    
    public static final String KLEENE = "*";

    private final String COMPONENT_DELIMITER = "|";
    private final String CONDITION_DELIMITER = "/";
    private final String CONDITION_ELEMENT_DELIMITER = ";";
    private final String CONDITION_SUBELEMENT_DELIMITER = "\\";
    
    private final String ASPECT_PREFIX = "@";
    private final String VALUE_PREFIX = ":";
    private final String CONJUNCTION = "+";
    
    private final String INCLUSIVE = "x";
    private final String COMPLETE = "t";
    private final String OPTIONAL = "*";
    
    private final String OPTION_LEFT_BRACKET = "[";
    private final String OPTION_RIGHT_BRACKET = "]";

    private final String ESCAPE_CHARACTER = "^";
    private final String ESCAPE_LEFT_BRACKET = "{{{";
    private final String ESCAPE_RIGHT_BRACKET = "}}}";
    private final HashMap<String, String> escaped = new HashMap<>();
    
    /**
     * @param script String holding the Rockwell script.
     * @return Instance of the {@link com.itcag.rockwell.tagger.lang.AcceptingCondition AcceptingCondition} class created from the input.
     * @throws Exception if anything goes wrong.
     */
    public final AcceptingCondition get(String script) throws Exception {

        AcceptingCondition retVal = new AcceptingCondition(script);

        script = encodeEscapedCharacters(script);

        String[] elts = script.split("\\" + COMPONENT_DELIMITER);
        if (elts.length != 2) throw new InvalidScriptException("Script must consist of exactly 2 components delimited by a pipe sign (\"|\").");

        parseConditions(elts[0].trim(), retVal);
        
        retVal.setTag(elts[1].trim());
        
        return retVal;
        
    }

    private String encodeEscapedCharacters(String script) {
        
        while (script.contains(ESCAPE_CHARACTER)) {
            int pos = script.indexOf(ESCAPE_CHARACTER);
            String escapee = script.substring(pos + 1, pos + 2);
            String replacement = UUID.randomUUID().toString().replace("-", "");
            this.escaped.put(replacement, escapee);
            script = TextToolbox.replace(script, ESCAPE_CHARACTER + escapee, ESCAPE_LEFT_BRACKET + replacement + ESCAPE_RIGHT_BRACKET);
        }
        
        return script;

    }

    private String decodeEscapedCharacters(String script) {
        while (script.contains(ESCAPE_LEFT_BRACKET)) {
            int start = script.indexOf(ESCAPE_LEFT_BRACKET);
            int end = script.indexOf(ESCAPE_RIGHT_BRACKET, start + ESCAPE_LEFT_BRACKET.length());
            String replacement = script.substring(start + ESCAPE_LEFT_BRACKET.length(), end);
            String escapee = this.escaped.get(replacement);
            script = script.replace(ESCAPE_LEFT_BRACKET + replacement + ESCAPE_RIGHT_BRACKET, escapee);
        }
        return script;
    }
    
    private void parseConditions(String script, AcceptingCondition acceptingCondition) throws Exception {
        
        if (TextToolbox.isReallyEmpty(script)) throw new InvalidScriptException("No condition was specified.");
        
        String[] elts = script.split(CONDITION_DELIMITER);
        for (int i = 0; i < elts.length; i++) {
            String elt = elts[i].trim();
            if (i == 0) {
                insertConditionElements(elt, acceptingCondition);
            } else {
                RejectingCondition rejecting = new RejectingCondition(elt, acceptingCondition.getId());
                acceptingCondition.addRejectedById(rejecting.getId());
                insertConditionElements(elt, rejecting);
                acceptingCondition.addRejecting(rejecting);
            }
        }
        
    }
    
    private void insertConditionElements(String script, Condition condition) throws Exception {

        LinkedList<ConditionElement> elements = getConditionElements(script);
        
        if (elements.getFirst().isQuodlibet()) throw new IllegalArgumentException("Optional element cannot be the first conditional element: " + script); 
        if (elements.getLast().isQuodlibet()) throw new IllegalArgumentException("Optional element cannot be the last conditional element: " + script); 

        for (ConditionElement element : elements) {
            
            if (element.getInfix() != null) {
                ConditionElement newElement = new ConditionElement(ConditionElement.Aspect.QUODLIBET, KLEENE);
                /**
                 * The inserted quodlibet must be optional,
                 * since some infixes could consist of multiple words,
                 * and some of a single word.
                 * For the single word infixes no quodlibet tokens will be collected.
                 */
                newElement.setQuodlibet(true);
                newElement.setConditionId(condition.getId());
                condition.addConditionElement(newElement);
            }
            
            element.setConditionId(condition.getId());
            condition.addConditionElement(element);
        }

    }
    
    private LinkedList<ConditionElement> getConditionElements(String script) throws Exception {
        
        LinkedList<ConditionElement> retVal = new LinkedList<>();
        
        String[] elts = script.split(CONDITION_ELEMENT_DELIMITER);
        for (String elt : elts) {
            ConditionElement conditionElement = getConditionElement(elt.trim());
            retVal.add(conditionElement);
        }
        
        return retVal;
        
    }
    
    private ConditionElement getConditionElement(String script) throws Exception {

        while (script.contains("  ")) script = script.replace("  ", " ");
        script = script.trim();

        boolean optional = false;
        if (script.startsWith(OPTION_LEFT_BRACKET)) {
            optional = true;
            script =  script.substring(1);
            if (script.endsWith(OPTION_RIGHT_BRACKET)) script = script.substring(0, script.length() - 1);
            script = script.trim();
        }

        ConditionElement retVal = null;
        
        String[] elts = script.split("\\" + CONDITION_SUBELEMENT_DELIMITER);
        for (String elt : elts) {
            if (retVal == null) {
                retVal = getConditionElement(elt.trim(), optional);
            } else {
                ConditionElement reject = getConditionElement(elt.trim(), false);
                retVal.addReject(reject);
            }
        }
        
        return retVal;
        
    }
    
    private ConditionElement getConditionElement(String script, boolean optional) throws Exception {
        
        String[] elts = script.split(" ");
        if (elts.length < 2 || elts.length > 3) throw new InvalidScriptException("Invalid condition element: " + script);

        ArrayList<Aspect> aspects = extractAspects(script, elts);
        ArrayList<String> values = extractValues(script, elts);
        if (aspects.isEmpty()) throw new InvalidScriptException("No aspects specified: " + script);
        if (values.isEmpty()) throw new InvalidScriptException("No strings specified: " + script);
        if (aspects.size() != values.size()) throw new InvalidScriptException("Numbers of aspects and strings are not matching: " + script);
        
        ConditionElement retVal = null;
        for (int i = 0; i < aspects.size(); i++) {
            if (retVal == null) {
                retVal = getConditionElement(aspects.get(i), values.get(i), optional, script);
            } else {
                insertAdditionalConditions(aspects.get(i), values.get(i), retVal);
            }
        }

        return retVal;
        
    }
    
    private ConditionElement getConditionElement(Aspect aspect, String value, boolean optional, String script) throws Exception {

        switch (aspect.getAspect()) {
            case PREFIX:
            case INFIX:
            case SUFFIX:
                throw new InvalidScriptException("Affixes cannot be the leading aspect: " + script);
            case QUODLIBET:
                ConditionElement retVal = new ConditionElement(ConditionElement.Aspect.QUODLIBET, KLEENE);
                retVal.setQuodlibet(true);
                Integer max = Converter.convertStringToInteger(value);
                if (max != null) retVal.setOptionalMax(max);
                return retVal;
            case LEMMA:
                /**
                 * Ensure lower case string.
                 */
                retVal = new ConditionElement(aspect.getAspect(), value.toLowerCase());
                retVal.setQuodlibet(optional);
                return retVal;
            default:
                retVal = new ConditionElement(aspect.getAspect(), value);
                retVal.setQuodlibet(optional);
                return retVal;
        }

    }
    
    private void insertAdditionalConditions(Aspect aspect, String value, ConditionElement conditionElement) {
        
        switch (aspect.getAspect()) {
            case PREFIX:
            {
                Affix affix = new Affix(aspect.getAspect(), value);
                affix.setInclusive(aspect.isInclusive());
                affix.setComplete(aspect.isComplete());
                affix.setOptional(aspect.isCompulsory());
                conditionElement.setPrefix(affix);
                break;
            }
            case INFIX:
            {
                Affix affix = new Affix(aspect.getAspect(), value);
                affix.setInclusive(aspect.isInclusive());
                affix.setComplete(aspect.isComplete());
                affix.setOptional(aspect.isCompulsory());
                conditionElement.setInfix(affix);
                break;
            }
            case SUFFIX:
            {
                Affix affix = new Affix(aspect.getAspect(), value);
                affix.setInclusive(aspect.isInclusive());
                affix.setComplete(aspect.isComplete());
                affix.setOptional(aspect.isCompulsory());
                conditionElement.setSuffix(affix);
                break;
            }
            case LEMMA:
            case ROLE:
            {
                /**
                 * Ensure lower case string.
                 */
                conditionElement.addAdditionalCondition(aspect.getAspect(), value.toLowerCase());
                break;
            }
            default:
            {
                conditionElement.addAdditionalCondition(aspect.getAspect(), value);
            }
        }

    }
    
    private ArrayList<Aspect> extractAspects(String script, String[] elts) throws Exception {

        ArrayList<Aspect> retVal = new ArrayList<>();
        
        String test = getValue(elts, ASPECT_PREFIX);
        if (TextToolbox.isReallyEmpty(test)) throw new InvalidScriptException("Invalid aspect specification: " + script);

        String[] subElts = test.split("\\" + CONJUNCTION);
        for (String subElt : subElts) {
            
            boolean inclusive = false;
            boolean complete = false;
            boolean optional = false;
            if (subElt.contains("{")) {
                String affixProperties = subElt.substring(subElt.indexOf("{"));
                inclusive = TextToolbox.containsCaIn(affixProperties, INCLUSIVE);
                complete = TextToolbox.containsCaIn(affixProperties, COMPLETE);
                optional = TextToolbox.containsCaIn(affixProperties, OPTIONAL);
                subElt = subElt.substring(0, subElt.indexOf("{"));
            }
            ConditionElement.Aspect flag = ConditionElement.Aspect.valueOf(subElt.toUpperCase());
            
            Aspect aspect = new Aspect(flag, inclusive, complete, optional);
            retVal.add(aspect);
            
        }
        
        return retVal;

    }
    
    private ArrayList<String> extractValues(String script, String[] elts) throws Exception {

        ArrayList<String> retVal = new ArrayList<>();
        
        String test = getValue(elts, VALUE_PREFIX);
        if (TextToolbox.isReallyEmpty(test)) throw new InvalidScriptException("Invalid value specification: " + script);

        test = decodeEscapedCharacters(test);
        
        String[] subElts = test.split("\\" + CONJUNCTION);
        retVal.addAll(Arrays.asList(subElts));
        
        return retVal;

    }
    
    private String getValue(String[] elts, String prefix) {

        for (String elt : elts) {
            if (elt.startsWith(prefix)) {
                String retVal = elt.substring(1);
                if (retVal.isEmpty()) return null;
                return retVal;
            }
        }

        return null;

    }
    
}
