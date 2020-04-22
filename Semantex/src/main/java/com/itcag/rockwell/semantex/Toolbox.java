package com.itcag.rockwell.semantex;

import com.itcag.rockwell.lang.Tag;

import java.util.ArrayList;

public final class Toolbox {

    public final ArrayList<Tag> consolidate(ArrayList<Tag> tags) {
        
        if (tags.isEmpty()) return tags;
        if (tags.size() == 1) return tags;

        ArrayList<Tag> retVal = new ArrayList<>();
        
        while (!tags.isEmpty()) {
            
            Tag test = tags.get(0);
            tags.remove(test);

            for (Tag tag : tags) {
                if (test.getStart() >= tag.getStart() && test.getEnd() < tag.getEnd()) {
                    test = null;
                    break;
                } else if (test.getStart() > tag.getStart() && test.getEnd() <= tag.getEnd()) {
                    test = null;
                    break;
                }
            }
            
            if (test != null) retVal.add(test);
            
        }
        
        return retVal;
        
    }
    
}
