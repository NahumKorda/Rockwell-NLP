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

package com.itcag.rockwell.extr;

import com.itcag.rockwell.POSTag;
import com.itcag.rockwell.lang.Extract;
import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tagger.EnclosedTagModes;
import com.itcag.rockwell.tagger.Tagger;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.debug.DebuggingClients;
import com.itcag.rockwell.util.TokenToolbox;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * <p>This class extracts data from text using Rockwell frames.</p>
 * <p>To learn more about Rockwell frames see: <a href="https://docs.google.com/document/d/16ehTwHFVetysFeySPHOQ8aue64FrN-F5dwVi2xKFVVc/edit#heading=h.d8ot297jcp4z" target="_blank">Rockwell Frames (User Manual)</a>.</p>
 * <p>Rockwell frames work in conjunction with frame expressions. Frame expressions identify potential frame edges in text, and the frames extract the data enclosed within these edges.</p>
 */
public class Extractor {

    private class Holder {

        private final Frame frame;
        
        private Tag from = null;
        private boolean fromInclusive = false;
        
        private Tag until = null;
        private boolean untilInclusive = false;
        
        private final ArrayList<Tag> conditions = new ArrayList<>();
        
        private Holder(Frame frame) {
            this.frame = frame;
        }
        
        private Frame getFrame() {
            return frame;
        }
        
        private Tag getFrom() {
            return from;
        }

        private void setFrom(Tag from) {
            this.from = from;
        }

        private boolean isFromInclusive() {
            return fromInclusive;
        }
        
        private void setFromInclusive(boolean inclusive) {
            fromInclusive = inclusive;
        }
        
        private Tag getUntil() {
            return until;
        }

        private void setUntil(Tag until) {
            this.until = until;
        }

        private boolean isUntilInclusive() {
            return untilInclusive;
        }
        
        private void setUntilInclusive(boolean inclusive) {
            untilInclusive = inclusive;
        }
        
        private ArrayList<Tag> getConditions() {
            return conditions;
        }

        private void addCondition(Tag condition) {
            this.conditions.add(condition);
        }
        
    }
    
    private final Tagger tagger;

    private final Frames frames;
    
    /**
     * 
     * @param frameExpressionPath String holding a local path to a text file containing Rockwell script describing frame expressions.
     * @param framePath String holding a local path to a text file containing script describing Rockwell frames.
     * @throws Exception if anything goes wrong.
     */
    public Extractor(String frameExpressionPath, String framePath) throws Exception {
        
        ArrayList<String> expressions = new ArrayList<>();
        ArrayList<String> lines = TextFileReader.read(frameExpressionPath);
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("#")) continue;
            expressions.add(line);
        }

        Debugger debugger = new Debugger(DebuggingClients.EXTRACTOR, 0);
        
        this.tagger = new Tagger(expressions, EnclosedTagModes.ALL, debugger);
        this.frames = new Frames(framePath);
        
    }
    
    /**
     * This method extracts data from text using Rockwell frames. Extracted data are represented bi the instances of the {@link com.itcag.rockwell.lang.Extract Extract} class.
     * @param tokens Array list of tokens representing a sentence.
     * @return Array list of instances of the {@link com.itcag.rockwell.lang.Extract Extract} class representing the extracted data.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<Extract> extract(ArrayList<Token> tokens) throws Exception {
        
        ArrayList<Extract> retVal = new ArrayList<>();
        
        ArrayList<Tag> tags = this.tagger.tag(tokens);
        if (tags.isEmpty()) return retVal;

        ArrayList<Tag> edges = new ArrayList<>();
        Iterator<Tag> tagIterator = tags.iterator();
        while (tagIterator.hasNext()) {
            Tag tag = tagIterator.next();
            if (this.frames.isEdge(tag)) {
                edges.add(tag);
                tagIterator.remove();
            }
        }
        
        compactConditions(tags);
        for (Tag edge : edges) {
            correct(edge, tags, tokens);
        }
        
        tags.addAll(edges);
        removeErroneous(tags);

        ArrayList<Holder> holders = getHolders(tags, tokens);
        if (!holders.isEmpty()) {
            for (Holder holder : holders) {
                Extract test = processHolder(holder, tokens);
                if(test != null) retVal.add(test);
            }
        }
        
        
        
        return retVal;
        
    }
    
    private void compactConditions(ArrayList<Tag> tags) {

        /**
         * Remove conditions that are included in other conditions.
         */
        Iterator<Tag> tagIterator = tags.iterator();
        while (tagIterator.hasNext()) {
            Tag tag = tagIterator.next();
            boolean remove = false;
            for (Tag other : tags) {
                if (other.equals(tag)) continue;
                if (other.getStart() <= tag.getStart() && other.getEnd() >= tag.getEnd()) {
                    if (!tag.getTag().equals(other.getTag())) continue;
                    remove = true;
                    break;
                }
            }
            if (remove) tagIterator.remove();
        }

    }
    
    private void correct (Tag edge, ArrayList<Tag> tags, ArrayList<Token> tokens) throws Exception {
        
        /**
         * Correct conditions that overlap with edges.
         * Overlapping condition will be contracted or split to exclude the edge.
         */
        ListIterator<Tag> tagIterator = tags.listIterator();
        while (tagIterator.hasNext()) {
            Tag condition = tagIterator.next();
            if (condition.getStart() < edge.getStart() && condition.getEnd() > edge.getEnd()) {
                /**
                 * Edge is completely enclosed in a conditions
                 * (there is at least one token before the edge,
                 * and there is at least one token after the edge).
                 * Create two tags from the tokens before and after.
                 * One replaces the existing tag, the other is added.
                 * For example, the sentence: "Facebook buys Giphy".
                 * The condition is a noun phrase: "Facebook buys Giphy".
                 * Edge is "buys". We want two new tags: "Facebook" and "Giphy".
                 */
                Tag before = new Tag(condition.getTag(), condition.getScript(), condition.getStart(), edge.getStart() - 1);
                evaluateLast(before, tokens);
                Tag after = new Tag(condition.getTag(), condition.getScript(), edge.getEnd() + 1, condition.getEnd());
                tagIterator.set(before);
                tagIterator.add(after);
            } else if (condition.getStart() < edge.getStart() && condition.getEnd() >= edge.getStart()) {
                /**
                 * Edge overlaps the end of the condition.
                 * Contract condition to exclude the edge.
                 */
                condition.setEnd(edge.getStart() - 1);
                evaluateLast(condition, tokens);
            } else if (condition.getStart() <= edge.getEnd() && condition.getEnd() > edge.getEnd()) {
                /**
                 * Edge overlaps the beginning of the condition.
                 * Contract condition to exclude the edge.
                 */
                condition.setStart(edge.getEnd() + 1);
            }
        }
        
    }
    
    private void evaluateLast(Tag tag, ArrayList<Token> tokens) {
        
        /**
         * Ensure that the last token in condition is not punctuation
         */
        while (tag.getEnd() - tag.getStart() > 0) {
            Token token = tokens.get(tag.getEnd());
            if (token.getPos() != null && POSTag.PC1.equals(token.getPos())) {
                tag.setEnd(tag.getEnd() - 1);
            } else {
                return;
            }
        }
        
    }
    
    private void removeErroneous(ArrayList<Tag> tags) {

        /**
         * Remove tags that have end > start.
         * This could be potentially caused by the corrections.
         */
        Iterator<Tag> tagIterator = tags.iterator();
        while (tagIterator.hasNext()) {
            Tag tag = tagIterator.next();
            if (tag.getStart() > tag.getEnd()) tagIterator.remove();
        }

    }
    
    private ArrayList<Holder> getHolders(ArrayList<Tag> tags, ArrayList<Token> tokens) {
        ArrayList<Holder> retVal = new ArrayList<>();
        ArrayList<String> control = new ArrayList<>();
        for (Tag tag : tags) {
            if (this.frames.isEdge(tag)) {
                for (Frame frame : this.frames.getFrames(tag)) {
                    
                    if (control.contains(frame.getScript())) continue;
                    control.add(frame.getScript());
                    
                    Holder test = getHolder(frame, tags);
                    if (test == null) continue;
                    retVal.add(test);
                
                }
            }
        }
        return retVal;
    }
    
    private Holder getHolder(Frame frame, ArrayList<Tag> tags) {
        Holder retVal = new Holder(frame);
        for (Tag tag : tags) {
            if (frame.getFrom() != null && frame.getFrom().getTag().equals(tag.getTag())) {
                retVal.setFrom(tag);
                retVal.setFromInclusive(frame.getFrom().isIncluded());
            } else if (frame.getUntil() != null && frame.getUntil().getTag().equals(tag.getTag())) {
                retVal.setUntil(tag);
                retVal.setUntilInclusive(frame.getUntil().isIncluded());
            } else if (frame.getCondition() != null && frame.getCondition().equals(tag.getTag())) {
                retVal.addCondition(tag);
            }
        }
        if (!isValid(retVal, frame)) return null;
        return retVal;
    }
    
    private boolean isValid(Holder holder, Frame frame) {
        if (frame.getFrom() != null && holder.getFrom() == null) return false;
        if (frame.getUntil() != null && holder.getUntil() == null) return false;
        if (frame.getCondition() != null && holder.getConditions().isEmpty()) return false;
        return true;
    }
    
    private Extract processHolder(Holder holder, ArrayList<Token> tokens) {
        if (holder.getFrom() != null && holder.getUntil() != null) {
            return between(holder, tokens);
        } else if (holder.getUntil() != null) {
            return left(holder, tokens);
        } else if (holder.getFrom() != null) {
            return right(holder, tokens);
        }
        return null;
    }
    
    private Extract between(Holder holder, ArrayList<Token> tokens) {
        
        ArrayList<Token> extracted;
        
        if (holder.isFromInclusive() && holder.isUntilInclusive()) {
            extracted = new ArrayList<>(tokens.subList(holder.getFrom().getStart(), holder.getUntil().getEnd()));
        } else if (holder.isFromInclusive()) {
            extracted = new ArrayList<>(tokens.subList(holder.getFrom().getStart(), holder.getUntil().getStart()));
        } else if (holder.isUntilInclusive()) {
            extracted = new ArrayList<>(tokens.subList(holder.getFrom().getEnd(), holder.getUntil().getStart()));
        } else {
            extracted = new ArrayList<>(tokens.subList(holder.getFrom().getEnd() + 1, holder.getUntil().getStart()));
        }
        
        if (!holder.getConditions().isEmpty()) {
            boolean validated = false;
            for (Tag condition : holder.getConditions()) {
                if (holder.isFromInclusive() && holder.isUntilInclusive()) {
                    if (condition.getStart() - holder.getFrom().getStart() == 0 && holder.getUntil().getEnd() - condition.getEnd() == 0) {
                        /**
                         * Only the validated part is extracted.
                         */
                        extracted = new ArrayList<>(tokens.subList(condition.getStart(), condition.getEnd() + 1));
                        validated = true;
                        break;
                    }
                } else if (holder.isFromInclusive()) {
                    if (condition.getStart() - holder.getFrom().getStart() == 0 && holder.getUntil().getStart() - condition.getEnd() == 1) {
                        /**
                         * Only the validated part is extracted.
                         */
                        extracted = new ArrayList<>(tokens.subList(condition.getStart(), condition.getEnd() + 1));
                        validated = true;
                        break;
                    }
                } else if (holder.isUntilInclusive()) {
                    if (condition.getStart() - holder.getFrom().getEnd() == 1 && holder.getUntil().getEnd() - condition.getEnd() == 0) {
                        /**
                         * Only the validated part is extracted.
                         */
                        extracted = new ArrayList<>(tokens.subList(condition.getStart(), condition.getEnd() + 1));
                        validated = true;
                        break;
                    }
                } else {
                    if (condition.getStart() - holder.getFrom().getEnd() == 1 && holder.getUntil().getStart() - condition.getEnd() == 1) {
                        /**
                         * Only the validated part is extracted.
                         */
                        extracted = new ArrayList<>(tokens.subList(condition.getStart(), condition.getEnd() + 1));
                        validated = true;
                        break;
                    }
                }
            }
            if (!validated) return null;
        }
        
        String value = TokenToolbox.getStringFromTokens(extracted);
        return new Extract(holder.getFrame().getScript(), holder.getFrame().getMeaning(), value);

    }
    
    private Extract left(Holder holder, ArrayList<Token> tokens) {
        
        ArrayList<Token> extracted;
        
        if (holder.isUntilInclusive()) {
            extracted = new ArrayList<>(tokens.subList(0, holder.getUntil().getEnd()));
        } else {
            extracted = new ArrayList<>(tokens.subList(0, holder.getUntil().getStart()));
        }
        
        if (!holder.getConditions().isEmpty()) {
            boolean validated = false;
            for (Tag condition : holder.getConditions()) {
                if (holder.isUntilInclusive()) {
                    if (holder.getUntil().getEnd() - condition.getEnd() == 0) {
                        /**
                         * Only the validated part is extracted.
                         */
                        extracted = new ArrayList<>(tokens.subList(condition.getStart(), condition.getEnd() + 1));
                        validated = true;
                        break;
                    }

                } else {
                    if (holder.getUntil().getStart() - condition.getEnd() == 1) {
                        /**
                         * Only the validated part is extracted.
                         */
                        extracted = new ArrayList<>(tokens.subList(condition.getStart(), condition.getEnd() + 1));
                        validated = true;
                        break;
                    }
                }
            }
            if (!validated) return null;
        }
        
        String value = TokenToolbox.getStringFromTokens(extracted);
        return new Extract(holder.getFrame().getScript(), holder.getFrame().getMeaning(), value);

    }
    
    private Extract right(Holder holder, ArrayList<Token> tokens) {
        
        ArrayList<Token> extracted;
        
        if (holder.isFromInclusive()) {
            extracted = new ArrayList<>(tokens.subList(holder.getFrom().getStart(), tokens.size()));
        } else {
            extracted = new ArrayList<>(tokens.subList(holder.getFrom().getEnd() + 1, tokens.size()));
        }

        if (!holder.getConditions().isEmpty()) {
            for (Tag condition : holder.getConditions()) {
                if (holder.isFromInclusive()) {
                    if (holder.getFrom().getStart() - condition.getStart() == 0) {
                        /**
                         * Only the validated part is extracted.
                         */
                        extracted = new ArrayList<>(tokens.subList(condition.getStart(), condition.getEnd() + 1));
                        String value = TokenToolbox.getStringFromTokens(extracted);
                        return new Extract(holder.getFrame().getScript(), holder.getFrame().getMeaning(), value);
                    }
                } else {
                    if (condition.getStart() - holder.getFrom().getEnd() == 1) {
                        /**
                         * Only the validated part is extracted.
                         */
                        extracted = new ArrayList<>(tokens.subList(condition.getStart(), condition.getEnd() + 1));
                        String value = TokenToolbox.getStringFromTokens(extracted);
                        return new Extract(holder.getFrame().getScript(), holder.getFrame().getMeaning(), value);
                    }
                }
            }
        }

        String value = TokenToolbox.getStringFromTokens(extracted);
        return new Extract(holder.getFrame().getScript(), holder.getFrame().getMeaning(), value);

    }

}
