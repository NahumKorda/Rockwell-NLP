package com.itcag.rockwell.semantex;

public class Test {

    private final String text;
    private final String rule;
    private final int start;
    private final int end;
    
    public Test(String text, String rule, int start, int end) {
        this.text = text;
        this.rule = rule;
        this.start = start;
        this.end = end;
    }

    public String getText() {
        return text;
    }

    public String getRule() {
        return rule;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
    
}
