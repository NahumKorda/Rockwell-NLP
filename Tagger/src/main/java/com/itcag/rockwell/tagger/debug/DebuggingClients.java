package com.itcag.rockwell.tagger.debug;

/**
 * <p>Enumerates all possible clients that use the {@link com.itcag.rockwell.tagger.Tagger Tagger} class.</p>
 * <p>This class is used only for debugging.</p>
 */
public enum DebuggingClients {

    TESTING(2),
    PATTERNS(4),
    NER(8),
    NOMINALS(16),
    SHAYENNE_REVIEWER(32),
    PIPELINE(64),

    ;
    
    private final long instruction;
    
    private DebuggingClients(long instruction) {
        this.instruction = instruction;
    }
    
    /**
     * @return Long numerical that can be used for the bitwise comparison in the {@link com.itcag.rockwell.tagger.debug.DebuggingLedger DebuggingLedger} class.
     */
    public final long getInstruction() {
        return this.instruction;
    }
    
}

