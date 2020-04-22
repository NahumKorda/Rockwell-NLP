package com.itcag.rockwell.tagger.debug;

import com.itcag.rockwell.lang.Token;
import com.itcag.util.Printer;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;

/**
 * <p>This class systemouts debugging data.</p>
 * <p>This class is used only for debugging.</p>
 */
public class Debugger {
    
    private final DebuggingClients client;
    
    private final int depth;

    /**
     * @param client Value of the {@link com.itcag.rockwell.tagger.debug.DebuggingClients DebuggingClients} enum identifying the current client.
     * @param depth Integer specifying the required indentation.
     */
    public Debugger(DebuggingClients client, int depth) {
        this.client = client;
        this.depth = depth;
    }

    /**
     * @return Value of the {@link com.itcag.rockwell.tagger.debug.DebuggingClients DebuggingClients} enum identifying the current client.
     */
    public DebuggingClients client() {
        return this.client;
    }

    /**
     * @return Integer specifying the required indentation.
     */
    public int depth() {
        return this.depth;
    }

    /**
     * @return The deep clone of the current instance of this class.
     */
    public Debugger getCopy() {
        return new Debugger(this.client, this.depth + 1);
    }

    /**
     * @param msg String holding the message to be systemouted.
     */
    public void print(String msg) {
        
        if (!DebuggingLedger.isIncluded(this.client)) return;
        
        if (TextToolbox.isReallyEmpty(msg)) {
            Printer.print();
        } else {
            String indent = TextToolbox.repeat(depth, "\t");
            Printer.print(indent + this.client.name() + "\t" + msg);
        }
    
    }

    /**
     * @param tokens Array list of instances of the {@link com.itcag.rockwell.lang.Token Token} class that are to be systemouted.
     */
    public void print(ArrayList<? extends Token> tokens) {
        
        if (!DebuggingLedger.isIncluded(this.client)) return;
        
        String indent = TextToolbox.repeat(depth, "\t");

        StringBuilder retVal = new StringBuilder();
        tokens.forEach((token) -> {
            if (retVal.length() > 0) retVal.append(" ");
            retVal.append(token.getWord());
        });
        Printer.print(indent + this.client.name() + "\t" + retVal.toString());
        
    }

    /**
     * @param msg String holding the message to be systemouted.
     * @param tokens Array list of instances of the {@link com.itcag.rockwell.lang.Token Token} class that are to be systemouted.
     */
    public void print(String msg, ArrayList<? extends Token> tokens) {
        
        if (!DebuggingLedger.isIncluded(this.client)) return;
        
        String indent = TextToolbox.repeat(depth, "\t");

        StringBuilder retVal = new StringBuilder();
        tokens.forEach((token) -> {
            if (retVal.length() > 0) retVal.append(" ");
            retVal.append(token.getWord());
        });
        Printer.print(indent + this.client.name() + "\t" + msg + ": " + retVal.toString());
        
    }
    
}
