package com.itcag.rockwell.tagger;

import com.itcag.rockwell.tagger.lang.Conditions;
import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.patterns.Patterns;
import com.itcag.rockwell.tagger.lang.State;
import com.itcag.rockwell.tagger.patterns.Loader;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;
import java.util.UUID;

/**
 * <p>This class carries out tagging of the text by applying the selected Rockwell expressions and patterns.</p>
 * <p>Text is provided as an array list of {@link com.itcag.rockwell.lang.Token tokens}. Rockwell expressions are provided as an instance of the {@link com.itcag.rockwell.tagger.lang.Conditions Conditions} class, and patterns as an instance of the {@link com.itcag.rockwell.tagger.patterns.Patterns Patterns} class.</p>
 * <p>Tagging is carried out by an instance of the {@link com.itcag.rockwell.tagger.Processor Processor} class.</p>
 */
public class Tagger {
   
    /**
     * Enumerates all possible clients that use this class.
     */
    public enum Client {
        MAIN, PATTERN_PREFIX, PATTERN_INFIX, PATTERN_SUFFIX, ;
    }
    
    private final Conditions conditions;
    private final Patterns patterns;
    
    /**
     * Signals if Tagger is used to inspect a suffix.
     */
    private final Client client;
    
    private final Debugger debugger;

    /**
     * This constructor is used when only the generic Rockwell patterns need to be loaded. 
     * @param scripts Selected Rockwell expressions that are to be applied to text.
     * @param debugger Instance of the {@link com.itcag.rockwell.tagger.debug.Debugger Debugger} class use for debugging only.
     * @throws Exception if anything goes wrong.
     */
    public Tagger(ArrayList<String> scripts, Debugger debugger) throws Exception {
        this.conditions = new Conditions(scripts);
        Loader loader = new Loader();
        this.patterns = new Patterns(loader.load("patterns"));
        this.client = Client.MAIN;
        this.debugger = debugger;
    }

    /**
     * This constructor is used when additional, proprietary patterns need to be loaded together with the generic Rockwell patterns.
     * @param scripts Selected Rockwell expressions that are to be applied to text.
     * @param proprietaryPatternPath String holding the local path to a text file containing additional pattern expressions.
     * @param debugger Instance of the {@link com.itcag.rockwell.tagger.debug.Debugger Debugger} class use for debugging only.
     * @throws Exception if anything goes wrong.
     */
    public Tagger(ArrayList<String> scripts, String proprietaryPatternPath, Debugger debugger) throws Exception {

        this.conditions = new Conditions(scripts);

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
        
        this.client = Client.MAIN;
        this.debugger = debugger;

    }
    
    /**
     * This constructor is used only by the {@link com.itcag.rockwell.tagger.patterns.Patterns Patterns} class for affix validation against applicable patterns.
     * @param conditions Instance of the {@link com.itcag.rockwell.tagger.lang.Conditions Conditions} class containing the applicable Rockwell expressions.
     * @param patterns Instance of the {@link com.itcag.rockwell.tagger.patterns.Patterns Patterns} class containing the applicable patterns.
     * @param client Value of the {@link Client} enum signaling which affix is being evaluated (used only for debugging).
     * @param debugger Instance of the {@link com.itcag.rockwell.tagger.debug.Debugger Debugger} class use for debugging only.
     * @throws Exception if anything goes wrong.
     */
    public Tagger(Conditions conditions, Patterns patterns, Client client, Debugger debugger) throws Exception {
        /**
         * Used only by Patterns. -- No need to reload patterns once they are loaded by the main client.
         */
        this.conditions = conditions;
        this.patterns = patterns;
        this.client = client;
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
        
        String sentenceID = UUID.randomUUID().toString().replace("-", "").trim();
        
        TokenAnalyzer analyzer = new TokenAnalyzer(processor);
        
        for (int i = 0; i < tokens.size(); i++) {
            
            Token token = tokens.get(i);
            analyzer.analyze(token);

            if (Client.MAIN.equals(this.client)) {
                Integer next = getNewNext(i, analyzer);
                if (next != null) i = next;
            }
            
        }

        return getTags(analyzer.getMatches(), sentenceID);
        
    }
    
    private Integer getNewNext(int i, TokenAnalyzer analyzer) {
        
        /**
         * If a suffix was successfully identified,
         * there is no sense in inspecting the identified tokens again.
         * Inspection of tokens can be moved forward
         * to the first non-identified token.
         * Unless...
         */
        int next = i;
        for (State match : analyzer.getMatches()) {
            
            /**
             * (1) Rejecting conditions should not move the cursor.
             */
            if (match.getIdToBeRejected() != null) continue;
            
            /**
             * (2) If the accepting condition contains a suffix condition element,
             * and it features rejecting conditions,
             * the following situation may occur:
             * the suffix completes the validation of the accepting condition,
             * but the rejecting condition was still not completed,
             * because it requires inspection of the tokens
             * that were already validated by the suffix validation;
             * consequently, the rejecting condition would never be validated,
             * if the cursor is moved beyond the end of the suffix.
             * For example:
             * "irritation at all"
             * @pos+prefix+suffix :PRP+noun+noun / @cain :at ; @cain :all | valid_noun_phrase
             * when "at" is checked both prefix and suffix get validated,
             * so that the accepting condition is completed.
             * Nonetheless, the rejecting condition is not yet validated,
             * since "all" is not reached yet.
             */
            if (match.getEnd() > next) {
                if (!match.getRejectedById().isEmpty() && !analyzer.getCurrentStates().isEmpty()) {
                    for (State state : analyzer.getCurrentStates()) {
                        if (match.getConditionId().equals(state.getIdToBeRejected())) {
                            return null;
                        }
                    }
                }
        
                next = match.getEnd();
            
            }
        
        }
        
        if (next != i) return next;
        return null;

    }
    
    private ArrayList<Tag> getTags(ArrayList<State> matches, String sentenceID) {
        
        ArrayList<Tag> retVal = new ArrayList<>();

        for (State match : matches) {
            if (match.getIdToBeRejected() == null) {
                if (isToBeRejected(match, matches)) continue;
                Tag tag = new Tag(match.getTag(), match.getScript(), match.getFirstMatch(), match.getLastMatch());
                tag.setSentenceId(sentenceID);
                retVal.add(tag);
            }
        }

        return retVal;
        
    }

    private boolean isToBeRejected(State state, ArrayList<State> matches) {
        for (State match : matches) {
            if (state.equals(match)) continue;
            if (match.getIdToBeRejected() == null) continue;
            if (state.getConditionId().equals(match.getIdToBeRejected())) {
                if (state.getStart() <= match.getStart() && state.getEnd() >= match.getEnd()) return true;
            }
        }
        return false;
    }
    
}
