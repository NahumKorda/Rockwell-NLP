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

package com.itcag.rockwell.tagger;

import com.itcag.rockwell.tagger.lang.Conditions;
import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.debug.DebuggingClients;
import com.itcag.rockwell.tagger.patterns.Patterns;
import com.itcag.rockwell.tagger.patterns.Loader;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;

/**
 * <p>This class carries out tagging of the text by applying the selected Rockwell expressions and patterns.</p>
 * <p>Text is provided as an array list of {@link com.itcag.rockwell.lang.Token tokens}. Rockwell expressions are provided as an instance of the {@link com.itcag.rockwell.tagger.lang.Conditions Conditions} class, and patterns as an instance of the {@link com.itcag.rockwell.tagger.patterns.Patterns Patterns} class.</p>
 * <p>Tagging is carried out by an instance of the {@link com.itcag.rockwell.tagger.Processor Processor} class.</p>
 */
public class Tagger {
   
    private final Conditions conditions;
    private final Patterns patterns;
    
    private final EnclosedTagModes enclosedTagMode;

    private final Debugger debugger;

    /**
     * This constructor is used when only the generic Rockwell patterns need to be loaded. 
     * @param scripts Selected Rockwell expressions that are to be applied to text.
     * @param enclosedTagMode
     * @param debugger Instance of the {@link com.itcag.rockwell.tagger.debug.Debugger Debugger} class use for debugging only.
     * @throws Exception if anything goes wrong.
     */
    public Tagger(ArrayList<String> scripts, EnclosedTagModes enclosedTagMode, Debugger debugger) throws Exception {
        this.conditions = new Conditions(scripts);
        this.enclosedTagMode = enclosedTagMode;
        Loader loader = new Loader();
        this.patterns = new Patterns(loader.load("patterns"));
        this.debugger = debugger;
    }

    /**
     * This constructor is used when additional, proprietary patterns need to be loaded together with the generic Rockwell patterns.
     * @param scripts Selected Rockwell expressions that are to be applied to text.
     * @param enclosedTagMode
     * @param proprietaryPatternPath String holding the local path to a text file containing additional pattern expressions.
     * @param debugger Instance of the {@link com.itcag.rockwell.tagger.debug.Debugger Debugger} class use for debugging only.
     * @throws Exception if anything goes wrong.
     */
    public Tagger(ArrayList<String> scripts, EnclosedTagModes enclosedTagMode, String proprietaryPatternPath, Debugger debugger) throws Exception {

        this.conditions = new Conditions(scripts);

        this.enclosedTagMode = enclosedTagMode;

        Loader loader = new Loader();
        ArrayList<String> patternScripts = loader.load("patterns");

        ArrayList<String> lines = TextFileReader.read(proprietaryPatternPath);
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("#")) continue;
            patternScripts.add(line);
        }

        this.patterns = new Patterns(patternScripts);
        
        this.debugger = debugger;

    }
    
    /**
     * This constructor is used only by the {@link com.itcag.rockwell.tagger.patterns.Patterns Patterns} class for affix validation against applicable patterns.
     * @param conditions Instance of the {@link com.itcag.rockwell.tagger.lang.Conditions Conditions} class containing the applicable Rockwell expressions.
     * @param enclosedTagMode
     * @param patterns Instance of the {@link com.itcag.rockwell.tagger.patterns.Patterns Patterns} class containing the applicable patterns.
     * @param debugger Instance of the {@link com.itcag.rockwell.tagger.debug.Debugger Debugger} class use for debugging only.
     * @throws Exception if anything goes wrong.
     */
    public Tagger(Conditions conditions, EnclosedTagModes enclosedTagMode, Patterns patterns, Debugger debugger) throws Exception {
        /**
         * Used only by Patterns. -- No need to reload patterns once they are loaded by the main client.
         */
        this.conditions = conditions;
        this.enclosedTagMode = enclosedTagMode;
        this.patterns = patterns;
        this.debugger = debugger;
    }
    
    /**
     * This method is used when the main input text is being validated (and not the affixes).
     * @param tokens Array list of instances of the {@link com.itcag.rockwell.lang.Token Token} class representing text to be processed.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Tag Tag} class representing tags of the matched Rockwell expressions.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<Tag> tag(ArrayList<? extends Token> tokens) throws Exception {
        Processor processor = new Processor(this.conditions, this.patterns, tokens, null, this.debugger);
        return run(tokens, processor);
    }
    
    /**
     * This method is used only for affix validation.
     * @param tokens Array list of instances of the {@link com.itcag.rockwell.lang.Token Token} class representing text to be processed.
     * @param expectedValue String holding the tag of a targeted pattern.
     * @return Array list containing instances of the {@link com.itcag.rockwell.lang.Tag Tag} class representing the matched Rockwell expressions.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<Tag> tag(ArrayList<? extends Token> tokens, String expectedValue) throws Exception {
        /**
         * Used only by Patterns.
         * No need to reload patterns once they are loaded by the main client.
         */
        Processor processor = new Processor(this.conditions, this.patterns, tokens, expectedValue, this.debugger);
        return run(tokens, processor);
    }
    
    private ArrayList<Tag> run(ArrayList<? extends Token> tokens, Processor processor) throws Exception {
        
        TokenAnalyzer analyzer = new TokenAnalyzer(processor, this.enclosedTagMode, this.debugger);
        
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            analyzer.analyze(token);
        }

        return analyzer.getTags();
        
    }
    
}
