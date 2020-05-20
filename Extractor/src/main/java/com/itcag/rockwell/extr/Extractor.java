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

import com.itcag.rockwell.lang.Extract;
import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tagger.Tagger;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.debug.DebuggingClients;
import com.itcag.rockwell.util.TokenToolbox;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;

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
        
        this.tagger = new Tagger(expressions, debugger);
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

        correct(tags);
        
        ArrayList<Holder> holders = getHolders(tags);
        if (holders.isEmpty()) return retVal;
        
        for (Holder holder : holders) {
            Extract test = processHolder(holder, tokens);
            if(test != null) retVal.add(test);
        }
        
        return retVal;
        
    }
    
    private void correct(ArrayList<Tag> tags) throws Exception {
        
        for (Tag tag : tags) {
            
            if (this.frames.isEdge(tag)) {
                inspect(tag, tags);
            }
        
        }
        
    }
    
    private void inspect (Tag left, ArrayList<Tag> tags) throws Exception {
        
        for (Tag right : tags) {
            if (right.equals(left)) continue;
            if (right.getStart() <= left.getEnd() && right.getStart() >= left.getStart()) {
                right.setStart(left.getEnd() + 1);
            }
            if (right.getEnd() >= left.getStart() && right.getEnd() <= left.getEnd()) {
                right.setEnd(left.getStart() - 1);
            }
        }
        
    }
    
    
    private ArrayList<Holder> getHolders(ArrayList<Tag> tags) {
        ArrayList<Holder> retVal = new ArrayList<>();
        for (Tag tag : tags) {
            if (this.frames.isEdge(tag)) {
                for (Frame frame : this.frames.getFrames(tag)) {
                    retVal.add(getHolder(frame, tags));
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
            extracted = new ArrayList<>(tokens.subList(holder.getFrom().getEnd(), holder.getUntil().getStart()));
        }
        
        if (!holder.getConditions().isEmpty()) {
            boolean validated = false;
            for (Tag condition : holder.getConditions()) {
                if (holder.isFromInclusive() && holder.isUntilInclusive()) {
                    if (holder.getFrom().getStart() - condition.getStart() == 0 && holder.getUntil().getEnd() - condition.getEnd() == 0) {
                        /**
                         * Only the validated part is extracted.
                         */
                        extracted = new ArrayList<>(tokens.subList(condition.getStart(), condition.getEnd() + 1));
                        validated = true;
                        break;
                    }
                } else if (holder.isFromInclusive()) {
                    if (holder.getFrom().getStart() - condition.getStart() == 0 && holder.getUntil().getStart() - condition.getEnd() == 1) {
                        /**
                         * Only the validated part is extracted.
                         */
                        extracted = new ArrayList<>(tokens.subList(condition.getStart(), condition.getEnd() + 1));
                        validated = true;
                        break;
                    }
                } else if (holder.isUntilInclusive()) {
                    if (holder.getFrom().getEnd() - condition.getStart() == 1 && holder.getUntil().getEnd() - condition.getEnd() == 0) {
                        /**
                         * Only the validated part is extracted.
                         */
                        extracted = new ArrayList<>(tokens.subList(condition.getStart(), condition.getEnd() + 1));
                        validated = true;
                        break;
                    }
                } else {
                    if (holder.getFrom().getEnd() - condition.getStart() == 1 && holder.getUntil().getStart() - condition.getEnd() == 1) {
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
            extracted = new ArrayList<>(tokens.subList(0, holder.getFrom().getStart()));
        } else {
            extracted = new ArrayList<>(tokens.subList(0, holder.getFrom().getEnd()));
        }
        
        if (!holder.getConditions().isEmpty()) {
            boolean validated = false;
            for (Tag condition : holder.getConditions()) {
                if (holder.isFromInclusive()) {
                    if (holder.getFrom().getStart() - condition.getStart() == 0) {
                        /**
                         * Only the validated part is extracted.
                         */
                        extracted = new ArrayList<>(tokens.subList(condition.getStart(), condition.getEnd() + 1));
                        validated = true;
                        break;
                    }
                } else {
                    if (condition.getStart() - holder.getFrom().getEnd() == 1) {
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

}
