package com.itcag.rockwell.vocabulator.phraser;

import com.itcag.rockwell.vocabulator.Term;
import com.itcag.rockwell.POSType;
import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.pipeline.Pipeline;
import com.itcag.rockwell.vocabulator.Exclusions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>This class validates the entire phrase after it was created. The validations includes the following:</p>
 * <ol>
 * <li>Phrases below the specified threshold are removed.</li>
 * <li>Phrases that do not comply with predefined phrase structures are removed.</li>
 * <li>Phrases that contain tokens with identical lemmas are unified into a single phrase.</li>
 * <li>The phrases are sorted descending by their frequency of occurrence.</li>
 * </ol>
 */
public final class Postprocessor {
    
    private final Pipeline pipeline;
    
    /**
     * @param pipeline Instance of the {@link com.itcag.rockwell.pipeline.Pipeline Pipeline} class used to validate phrases against predefined phrase structures defined as Rockwell expressions.
     */
    public Postprocessor(Pipeline pipeline) {
        this.pipeline = pipeline;
    }
    
    /**
     * 
     * @param index Tree map containing the extracted phrases.
     * @param exclusions Long number holding the instructions what phrases should be excluded.
     * @param threshold Integer specifying the minimum frequency of occurrence that a phrase must feature to be validated.
     * @return Tree map containing validated phrases sorted descending by their frequency of occurrence.
     * @throws Exception if anything goes wrong.
     */
    public final TreeMap<Integer, ArrayList<Term>> sortOut(TreeMap<String, Term> index, long exclusions, int threshold) throws Exception {
        
        TreeMap<Integer, ArrayList<Term>> retVal = new TreeMap<>(Collections.reverseOrder());

        /**
         * Remove phrases with the frequency of occurrence below the threshold.
         */
        trim(index,threshold);

        /**
         * Ensure that phrases comply with the recognized phrase formats.
         */
        validateFormat(index, exclusions);
        
        /**
         * Combine phrases with identical lemmas.
         */
        index = combine(index);
        
        /**
         * Invert index, so that the frequency of occurrence is the map key.
         */
        for (Map.Entry<String, Term> entry : index.entrySet()) {
            if (retVal.containsKey(entry.getValue().getFOO())) {
                retVal.get(entry.getValue().getFOO()).add(entry.getValue());
            } else {
                retVal.put(entry.getValue().getFOO(), new ArrayList<>(Arrays.asList(entry.getValue())));
            }
        }

        return retVal;
        
    }
    
    private void trim(TreeMap<String, Term> index, int threshold) {
        
        Iterator<Map.Entry<String, Term>> indexIterator = index.entrySet().iterator();
        while (indexIterator.hasNext()) {
            Map.Entry<String, Term> entry = indexIterator.next();
            if (entry.getValue().getFOO() < threshold) indexIterator.remove();
        }
        
    }
    
    private void validateFormat(TreeMap<String, Term> index, long exclusions) throws Exception {

        Iterator<Map.Entry<String, Term>> indexIterator = index.entrySet().iterator();
        while (indexIterator.hasNext()) {

            Map.Entry<String, Term> entry = indexIterator.next();

        }

    }
    
    public boolean isValidFormat(Term term, long exclusions) throws Exception {
        
        for (ArrayList<Tag> tags : this.pipeline.classify(term.getText())) {

            /**
             * This phrase does not comply with any of the recognized phrase formats.
             */
            if (tags.isEmpty()) return false;

            /**
             * Inspect tags to see if any covers the entire phrase.
             */
            for (ArrayList<String> tokens : this.pipeline.tokenize(term.getText())) {

                for (Tag tag : tags) {

                    switch (tag.getTag()) {
                        case "valid_noun_phrase":
                            if ((exclusions & Exclusions.NOUN_PHRASES.getInstruction()) != Exclusions.NOUN_PHRASES.getInstruction()) {
                                if (tag.getStart() == 0 && tag.getEnd() == tokens.size() - 1) return true;
                            }
                            break;
                        case "valid_adjective_phrase":
                            if ((exclusions & Exclusions.ADJECTIVE_PHRASES.getInstruction()) != Exclusions.ADJECTIVE_PHRASES.getInstruction()) {
                                if (tag.getStart() == 0 && tag.getEnd() == tokens.size() - 1) return true;
                            }
                            break;
                        case "valid_verb_phrase":
                            if ((exclusions & Exclusions.VERB_PHRASES.getInstruction()) != Exclusions.VERB_PHRASES.getInstruction()) {
                                if (tag.getStart() == 0 && tag.getEnd() == tokens.size() - 1) return true;
                            }
                            break;
                    }

                }

            }

        }

        return false;
        
    }
    
    private TreeMap<String, Term> combine(TreeMap<String, Term> index) throws Exception {

        TreeMap<String, Term> retVal = new TreeMap<>();
        
        while (!index.isEmpty()) {
            Map.Entry<String, Term> entry = index.pollFirstEntry();
            combine(entry, index);
            retVal.put(entry.getKey(), entry.getValue());
        }
        
        return retVal;
        
    }
    
    private void combine(Map.Entry<String, Term> test, TreeMap<String, Term> index) throws Exception {
        
        
        for (ArrayList<Token> left : this.pipeline.lemmatize(test.getValue().getText())) {
            
            Iterator<Map.Entry<String, Term>> indexIterator = index.entrySet().iterator();
            while (indexIterator.hasNext()) {

                Map.Entry<String, Term> entry = indexIterator.next();

                for (ArrayList<Token> right : this.pipeline.lemmatize(entry.getValue().getText())) {
                    
                    if (left.size() != right.size()) continue;

                    StringBuilder newText = new StringBuilder();
                    boolean matched = true;
                    for (int i = 0; i < left.size(); i++) {
                        String tmp = getCommonLemma(left.get(i), right.get(i));
                        if (tmp == null) {
                            matched = false;
                            break;
                        } else {
                            if (newText.length() > 0) newText.append(" ");
                            newText.append(tmp);
                        }
                    }

                    if (matched) {
                        test.getValue().setText(newText.toString());
                        test.getValue().addFOO(entry.getValue().getFOO());
                        indexIterator.remove();
                    }

                }

            }

        }

    }
    
    private String getCommonLemma(Token left, Token right) {
        
        if (left.getLemma() != null) {
            
            if (right.getLemma() != null) {

                if (left.getLemma().equals(right.getLemma())) {
                    if (POSType.PN.equals(left.getType())) {
                        return left.getCain();
                    } else {
                        return left.getLemma();
                    }
                }

            } else {

                for (Token alternative : right.getAlternatives()) {
                    if (left.getLemma().equals(alternative.getLemma())) {
                        if (POSType.PN.equals(left.getType())) {
                            return left.getCain();
                        } else {
                            return left.getLemma();
                        }
                    }
                }

            }
            
        } else {
            
            if (right.getLemma() != null) {

                for (Token alternative : left.getAlternatives()) {
                    if (right.getLemma().equals(alternative.getLemma())) {
                        if (POSType.PN.equals(right.getType())) {
                            return right.getCain();
                        } else {
                            return right.getLemma();
                        }
                    }
                }

            } else {

                for (Token leftAlternative : left.getAlternatives()) {
                    for (Token rightAlternative : right.getAlternatives()) {
                        if (rightAlternative.getLemma().equals(leftAlternative.getLemma())) {
                            if (POSType.PN.equals(leftAlternative.getType())) {
                                return leftAlternative.getCain();
                            } else {
                                return leftAlternative.getLemma();
                            }
                        }
                    }
                }
                
            }

        }

        return null;
        
    }
    
}
