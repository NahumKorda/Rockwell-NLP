package com.itcag.util;

public class Stopwatch {

    private final Long start;
    private Long end = null;
    
    public Stopwatch() {
        this.start = System.currentTimeMillis();
    }
    
    public void stop() {
        end = System.currentTimeMillis();
    }
    
    public long check() {
        return System.currentTimeMillis() - this.start;
    }
    
    public String checkFormatted() {
        return Converter.formatLongTime(System.currentTimeMillis() - this.start);
    }
    
    public long duration() throws Exception {
        if (this.end == null) throw new IllegalStateException("Stopwatch was not stopped. You can get duration only after stopping the stopwatch. Use check to read intermediary durations.");
        return this.end - this.start;
    }
    
    public String durationFormatted() {
        return Converter.formatLongTime(this.end - this.start);
    }
    
}
