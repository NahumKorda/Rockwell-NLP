package com.itcag.rockwell.semantex.adhoc;

import com.itcag.rockwell.semantex.Inserter;
import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.semantex.Toolbox;
import com.itcag.rockwell.tagger.Tagger;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.debug.DebuggingClients;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;

/**
 * <p>This class receives expressions that are used to identify concepts in text. The {@link #extract(java.util.ArrayList)} method provides the same functionality as the {@link com.itcag.rockwell.tagger.Tagger#tag(java.util.ArrayList) tag(java.util.ArrayList)} method in the {@link com.itcag.rockwell.tagger.Tagger Tagger} class, but the {@link #insert(java.util.ArrayList)} method replaces the original {@link com.itcag.rockwell.lang.Token tokens} with the {@link com.itcag.rockwell.lang.Semtoken semtokens} representing the identified concepts.</p> 
 */
public class Interpreter {

    private final Tagger tagger;

    /**
     * @param expressionPath String holding a local path to a text file containing Rockwell expressions that identify concepts.
     * @throws Exception if anything goes wrong.
     */
    public Interpreter(String expressionPath) throws Exception {
        
        ArrayList<String> expressions = new ArrayList<>();
        ArrayList<String> lines = TextFileReader.read(expressionPath);
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("#")) continue;
            expressions.add(line);
        }

        Debugger debugger = new Debugger(DebuggingClients.INTERPRETER, 0);
        
        this.tagger = new Tagger(expressions, debugger);
    
    }

    /**
     * @param expressionPath String holding a local path to a text file containing Rockwell expressions that identify concepts.
     * @param patternPath String holding the local path to a text file containing additional pattern expressions.
     * @throws Exception if anything goes wrong.
     */
    public Interpreter(String expressionPath, String patternPath) throws Exception {
        
        ArrayList<String> expressions = new ArrayList<>();
        ArrayList<String> lines = TextFileReader.read(expressionPath);
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("#")) continue;
            expressions.add(line);
        }

        Debugger debugger = new Debugger(DebuggingClients.INTERPRETER, 0);
        
        this.tagger = new Tagger(expressions, patternPath, debugger);
    
    }

    /**
     * This method replaces the original {@link com.itcag.rockwell.lang.Token tokens} with the {@link com.itcag.rockwell.lang.Semtoken semtokens} representing the identified concepts.
     * @param tokens Array list of {@link com.itcag.rockwell.lang.Token tokens} representing a sentence.
     * @return Array list of {@link com.itcag.rockwell.lang.Token tokens} with tokens identified as concepts replaced by {@link com.itcag.rockwell.lang.Semtoken semtokens}.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<Token> insert(ArrayList<Token> tokens) throws Exception {
        
        ArrayList<Tag> tags = this.tagger.tag(tokens);
        if (tags.isEmpty()) return tokens;
        
        Toolbox toolbox = new Toolbox();
        tags = toolbox.consolidate(tags);
        
        return Inserter.insertSemtokens(tags, tokens);
        
    }

    /**
     * This method returns the identified concepts.
     * @param tokens Array list of {@link com.itcag.rockwell.lang.Token tokens} representing a sentence.
     * @return Array list of {@link com.itcag.rockwell.lang.Tag tags} representing the identified concepts.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<Tag> extract(ArrayList<Token> tokens) throws Exception {
        return this.tagger.tag(tokens);
    }
    
}
