package com.itcag.rockwell.tagger.patterns;

import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tagger.Tagger;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.debug.DebuggingClients;
import com.itcag.rockwell.tagger.lang.Affix;
import com.itcag.rockwell.tagger.lang.Conditions;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>This class loads and stores the so-called <i>patterns</i>.</p>
 * <p>Patterns are Rockwell expressions that are applied only to {@link com.itcag.rockwell.tagger.lang.Affix affixes}. They are therefore stored in a separate file, and loaded here on demand.</p>
 * <p>Patterns are typically generic, designed to identify syntactic constituents, such as noun phrases, adjective phrases, adverbial phrases, etc.</p>
 * <p>To learn more about Rockwell expressions and patterns see: <a href="https://docs.google.com/document/d/1wMYCXAOm0cmJ4z5PHLRK5JyPsBY-p3vEYnupik-QT-A/edit?usp=sharing"  target="_blank">Rockwell Script (User Manual)</a>.</p>
 */
public class Patterns {

    private final Conditions conditions;

    /**
     * @param scripts Array list containing Rockwell expressions for patterns.
     * @throws Exception if anything goes wrong.
     */
    public Patterns(ArrayList<String> scripts) throws Exception {
        this.conditions = new Conditions(scripts);
    }

    /**
     * Identifies a prefix pattern in an array of {@link com.itcag.rockwell.lang.Token tokens}.
     * @param tokens Array list containing prefix tokens.
     * @param prefix Instance of the {@link com.itcag.rockwell.tagger.lang.Affix Affix} class that is to be validated.
     * @param debugger Instance of the {@link com.itcag.rockwell.tagger.debug.Debugger Debugger} class use for debugging only.
     * @return Instance of the {@link com.itcag.rockwell.lang.Tag Tag} class if the prefix was successfully matched, or null otherwise.
     * @throws Exception if anything goes wrong.
     */
    public final Tag getPrefix(ArrayList<? extends Token> tokens, Affix prefix, Debugger debugger) throws Exception {

        Debugger newDebugger = new Debugger(DebuggingClients.PATTERNS, debugger.depth() + 1);
        Tagger tagger = new Tagger(this.conditions, this, Tagger.Client.PATTERN_PREFIX, newDebugger);
        if (prefix.isComplete()) {
            for (Tag tag : tagger.tag(tokens, prefix.getValue())) {
                if (tag.getStart() == tokens.get(0).getIndex() && tag.getEnd() == tokens.get(tokens.size() - 1).getIndex()) {
                    return tag;
                }
            }
        } else {
            
            Tag longest = null;
            for (Tag tag : tagger.tag(tokens, prefix.getValue())) {
                if (longest == null) {
                    longest = tag;
                } else if (tag.getStart() < longest.getStart() || tag.getEnd() > longest.getEnd()) {
                    longest = tag;
                }
            }
            
            /**
             * The last identified token of a prefix
             * must be also the last token in the token sequence.
             */
            if (longest != null && longest.getEnd() == tokens.get(tokens.size() - 1).getIndex()) {
                return longest;
            }

        }

        return null;

    }

    /**
     * Identifies an infix pattern in an array of {@link com.itcag.rockwell.lang.Token tokens}.
     * @param tokens Array list containing infix tokens.
     * @param infix Instance of the {@link com.itcag.rockwell.tagger.lang.Affix Affix} class that is to be validated.
     * @param debugger Instance of the {@link com.itcag.rockwell.tagger.debug.Debugger Debugger} class use for debugging only.
     * @return Instance of the {@link com.itcag.rockwell.lang.Tag Tag} class if the infix was successfully matched, or null otherwise.
     * @throws Exception if anything goes wrong.
     */
    public final Tag getInfix(ArrayList<? extends Token> tokens, Affix infix, Debugger debugger) throws Exception {
        Debugger newDebugger = new Debugger(DebuggingClients.PATTERNS, debugger.depth() + 1);
        Tagger tagger = new Tagger(this.conditions, this, Tagger.Client.PATTERN_INFIX, newDebugger);
        for (Tag tag : tagger.tag(tokens, infix.getValue())) {
            /**
             * The first identified token of an infix
             * must be also the first token in the token sequence,
             * and the last identified token of an infix
             * must be also the last token of the token sequence.
             */
            if (tag.getStart() == tokens.get(0).getIndex() && tag.getEnd() == tokens.get(tokens.size() - 1).getIndex()) {
                return tag;
            }
        }
        return null;
    }

    /**
     * Identifies a suffix pattern in an array of {@link com.itcag.rockwell.lang.Token tokens}.
     * @param tokens Array list containing suffix tokens.
     * @param suffix Instance of the {@link com.itcag.rockwell.tagger.lang.Affix Affix} class that is to be validated.
     * @param debugger Instance of the {@link com.itcag.rockwell.tagger.debug.Debugger Debugger} class use for debugging only.
     * @return Instance of the {@link com.itcag.rockwell.lang.Tag Tag} class if the suffix was successfully matched, or null otherwise.
     * @throws Exception if anything goes wrong.
     */
    public final Tag getSuffix(ArrayList<? extends Token> tokens, Affix suffix, Debugger debugger) throws Exception {

        Debugger newDebugger = new Debugger(DebuggingClients.PATTERNS, debugger.depth() + 1);
        Tagger tagger = new Tagger(this.conditions, this, Tagger.Client.PATTERN_SUFFIX, newDebugger);
        if (suffix.isComplete()) {
            for (Tag tag : tagger.tag(tokens, suffix.getValue())) {
                if (tag.getStart() == tokens.get(0).getIndex() && tag.getEnd() == tokens.get(tokens.size() - 1).getIndex()) {
                    return tag;
                }
            }
        } else {

            ArrayList<Tag> buffer = new ArrayList<>();
            
            for (Tag tag : tagger.tag(tokens, suffix.getValue())) {
                Iterator<Tag> bufferIterator = buffer.iterator();
                while (bufferIterator.hasNext()) {
                    Tag existing = bufferIterator.next();
                    if (tag.getStart() < existing.getStart() && tag.getEnd() >= existing.getEnd()) {
                        bufferIterator.remove();
                    } else if (tag.getEnd() > existing.getEnd() && tag.getStart() <= existing.getStart()) {
                        bufferIterator.remove();
                    } else if (tag.getStart() >= existing.getStart() && tag.getEnd() <= existing.getEnd()) {
                        tag = null;
                        break;
                    }
                }
                if (tag != null) buffer.add(tag);
            }

            /**
             * The first identified token of a suffix
             * must be also the first token in the token sequence.
             */
            for (Tag tag : buffer) {
                if (tag.getStart() == tokens.get(0).getIndex()) {
                    return tag;
                }
            }
        
        }
        
        return null;
    
    }
    
    public void print() {
        this.conditions.print();
    }
    
}
