package com.itcag.rockwell.extr;

import com.itcag.rockwell.lang.Tag;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * <p>This class holds a collection of Rockwell frames.</p>
 * <p>To learn more about Rockwell frames see: <a href="https://docs.google.com/document/d/16ehTwHFVetysFeySPHOQ8aue64FrN-F5dwVi2xKFVVc/edit#heading=h.d8ot297jcp4z" target="_blank">Rockwell Frames (User Manual)</a>.</p>
 */
public class Frames {
    
    private final ArrayList<Frame> frames = new ArrayList<>();
    private final HashMap<String, ArrayList<Frame>> edges = new HashMap<>(); 

    /**
     * @param framePath String holding a local path to a text file that contains script describing Rockwell frames.
     * @throws Exception if anything goes wrong.
     */
    public Frames(String framePath) throws Exception {
        
        ArrayList<String> items = new ArrayList<>();
        ArrayList<String> lines = TextFileReader.read(framePath);
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("#")) continue;
            items.add(line);
        }

        for (String item : items) {
            
            Frame frame = new Frame(item);
            frames.add(frame);
            
            if (frame.getFrom() != null) {
                if (edges.containsKey(frame.getFrom().getTag())) {
                    edges.get(frame.getFrom().getTag()).add(frame);
                } else {
                    edges.put(frame.getFrom().getTag(), new ArrayList<>(Arrays.asList(frame)));
                }
            } else if (frame.getUntil() != null) {
                if (edges.containsKey(frame.getUntil().getTag())) {
                    edges.get(frame.getUntil().getTag()).add(frame);
                } else {
                    edges.put(frame.getUntil().getTag(), new ArrayList<>(Arrays.asList(frame)));
                }
            }
            
        }
        
    }

    /**
     * @return Array list contaning all Rockwell frames.
     */
    public ArrayList<Frame> getFrames() {
        return this.frames;
    }

    /**
     * @param tag Instance of the class {@link com.itcag.rockwell.lang.Tag Tag} class that is potentially an edge of one or more Rockwell frames.
     * @return Array list containing Rockwell frames with one of the edges identical to the input tag.
     */
    public ArrayList<Frame> getFrames(Tag tag) {
        return this.edges.get(tag.getTag());
    }
    
    /**
     * @param tag Instance of the class {@link com.itcag.rockwell.lang.Tag Tag} class that is potentially an edge of one or more Rockwell frames.
     * @return Boolean indicating whether the input tag is en edge of one or more Rockwell frames. 
     */
    public boolean isEdge(Tag tag) {
        return this.edges.containsKey(tag.getTag());
    }
    
}
