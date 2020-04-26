package com.itcag.rockwell.extr;

import com.itcag.rockwell.lang.Extract;
import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tagger.Tagger;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.debug.DebuggingClients;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;

/**
 * <p>This class extracts data from text using Rockwell frames.</p>
 * <p>To learn more about Rockwell frames see: <a href="https://docs.google.com/document/d/16ehTwHFVetysFeySPHOQ8aue64FrN-F5dwVi2xKFVVc/edit#heading=h.d8ot297jcp4z" target="_blank">Rockwell Frames (User Manual)</a>.</p>
 * <p>Rockwell frames work in conjunction with frame expressions. Frame expressions identify potential frame edges in text, and the frames extract the data enclosed within these edges.</p>
 */
public class Extractor {

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

        for (Tag tag : tags) {
            
            if (this.frames.isEdge(tag)) {
                
                for (Frame frame : this.frames.getFrames(tag)) {
        
                    if (frame.getFrom() != null && frame.getUntil() != null) {
                
                        Tag until = getUntilTag(frame.getUntil().getTag(), tags);
                        if (until == null) continue;
                        
                        ArrayList<Token> extracted = extractValue(tag, until, frame, tokens);
                        if (frame.getCondition() != null) extracted = applyCondition(frame.getCondition(), tags, extracted);
                        if (extracted.isEmpty()) continue;
                        
                        String value = getValueFromTokens(extracted);
                        Extract extract = new Extract(frame.getScript(), frame.getMeaning(), value);
                        retVal.add(extract);
                    
                    } else {
                        
                        ArrayList<Token> extracted = extractValue(tag, frame, tokens);
                        if (frame.getCondition() != null) extracted = applyCondition(frame.getCondition(), tags, extracted);
                        if (extracted.isEmpty()) continue;
                        
                        String value = getValueFromTokens(extracted);
                        Extract extract = new Extract(frame.getScript(), frame.getMeaning(), value);
                        retVal.add(extract);
                    
                    }
                
                }
            
            }
        
        }

        return retVal;
        
    }
    
    private Tag getUntilTag(String target, ArrayList<Tag> tags) {
        for (Tag tag : tags) {
            if (tag.getTag().equals(target)) return tag;
        }
        return null;
    }
    
    private ArrayList<Token> extractValue(Tag from, Tag until, Frame frame, ArrayList<Token> tokens) {
        
        ArrayList<Token> retVal = new ArrayList();
        
        for (Token token : tokens) {
            if (frame.getFrom().isIncluded()) {
                if (token.getIndex() >= from.getEnd()) {
                    if (frame.getUntil().isIncluded()) {
                        if (token.getIndex() <= until.getStart()) {
                            retVal.add(token);
                        }
                    } else {
                        if (token.getIndex() < until.getStart()) {
                            retVal.add(token);
                        }
                    }
                }
            } else {
                if (token.getIndex() > from.getEnd()) {
                    if (frame.getUntil().isIncluded()) {
                        if (token.getIndex() <= until.getStart()) {
                            retVal.add(token);
                        }
                    } else {
                        if (token.getIndex() < until.getStart()) {
                            retVal.add(token);
                        }
                    }
                }
            }
        }
        
        return retVal;
        
    }
    
    private ArrayList<Token> extractValue(Tag tag, Frame frame, ArrayList<Token> tokens) {

        ArrayList<Token> retVal = new ArrayList();
        
        if (frame.getFrom() != null && frame.getUntil() == null) {

            for (Token token : tokens) {
                if (frame.getFrom().isIncluded()) {
                    if (token.getIndex() >= tag.getEnd()) {
                        retVal.add(token);
                    }
                } else {
                    if (token.getIndex() > tag.getEnd()) {
                        retVal.add(token);
                    }
                }
            }
            
        } else if (frame.getUntil() != null && frame.getFrom() == null) {

            for (Token token : tokens) {
                if (frame.getUntil().isIncluded()) {
                    if (token.getIndex() <= tag.getStart()) {
                        retVal.add(token);
                    }
                } else {
                    if (token.getIndex() < tag.getStart()) {
                        retVal.add(token);
                    }
                }
            }

        }

        return retVal;
        
    }
    
    private ArrayList<Token> applyCondition(String condition, ArrayList<Tag> tags, ArrayList<Token> extracted) {
        
        ArrayList<Token> retVal = new ArrayList();
        
        for (Tag tag : tags) {
            if (tag.getTag().equals(condition)) {
                
                /**
                 * Check whether the tag is contained within the extracted tokens.
                 */
                if (tag.getStart() < extracted.get(0).getIndex()) continue;
                if (tag.getEnd() > extracted.get(extracted.size() - 1).getIndex()) continue;

                for (Token token : extracted) {
                    if (tag.getStart() >= token.getIndex() && tag.getEnd() <= token.getIndex()) {
                        retVal.add(token);
                    }
                }
                
            }
        }

        return retVal;
        
    }
    
    private String getValueFromTokens(ArrayList<Token> tokens) {
        
        StringBuilder retVal = new StringBuilder();
        
        for (Token token : tokens) {
            if (token.getWord().startsWith("'")) {
                retVal.append(token.getWord());
            } else {
                retVal.append(" ").append(token.getWord());
            }
        }
        
        TextToolbox.trim(retVal);
        
        return retVal.toString();
        
    }
    
}
