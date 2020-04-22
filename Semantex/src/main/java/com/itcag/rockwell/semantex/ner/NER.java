package com.itcag.rockwell.semantex.ner;

import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.semantex.Inserter;
import com.itcag.rockwell.semantex.Toolbox;
import com.itcag.rockwell.tagger.Tagger;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.debug.DebuggingClients;
import com.itcag.util.Converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * <p>This class identifies the following types of named entities:</p>
 * <ul>
 * <li>Persons (personal names),</li>
 * <li>Corporations (corporate names),</li>
 * <li>Dates, and</li>
 * <li>Currency amounts.</li>
 * </ul>
 * <p>Dates are recognized in both numerical and lexical formats.</p>
 * <p>Using the {@link Instructions} enum this class can be instructed which named entities to identify. This enum provides the {@link Instructions#getInstruction() getInstruction()} method to access the instruction for every available named entity. Instructions must be combined using the bitwise operator OR ("|").</p>
 * <p>This class provides the {@link #extract(java.util.ArrayList)} method to to retrieve the identified named entities as an array list of {@link  com.itcag.rockwell.lang.Tag tags}, and the {@link #insert(java.util.ArrayList)} method to replace the original {@link com.itcag.rockwell.lang.Token tokens} with the {@link com.itcag.rockwell.lang.Semtoken semtokens} representing the identified named entities.</p>
 */
public final class NER {

    /**
     * <p>Lists all available named entities.</p>
     * <p>Tag is the expected tag used by the Rockwell expressions that identify these entities.</p>
     * <p>Instruction is an integer used in bitwise comparison, in order to determine whether to extract a particular entity or not.</p>
     */
    public enum Instructions {
        
        PERSONS("person", 2),
        ORGANIZATIONS("organization", 4),
        DATES("date", 8),
        CURRENCIES("currency", 16),
        
        ;
        
        private final String tag;
        private final long instruction;
        
        private Instructions(String tag, long instruction) {
            this.tag = tag;
            this.instruction = instruction;
        }
        
        private String getTag() {
            return this.tag;
        }
        
        public final long getInstruction() {
            return this.instruction;
        }
        
    }
    
    private final Tagger tagger;

    public NER() throws Exception {

        Loader loader = new Loader();
        ArrayList<String> expressions = loader.load("ner");

        Debugger debugger = new Debugger(DebuggingClients.NER, 0);
        
        this.tagger = new Tagger(expressions, debugger);
        
    }

    /**
     * @param instructions Integer used in bitwise comparison, in order to determine whether to extract a particular entity or not.
     * @throws Exception if anything goes wrong.
     */
    public NER(long instructions) throws Exception {

        Loader loader = new Loader();
        ArrayList<String> rules = loader.load("ner");
        if (instructions != 0) {
            Iterator<String> ruleIterator = rules.iterator();
            while (ruleIterator.hasNext()) {
                String rule = ruleIterator.next();
                if ((instructions & Instructions.PERSONS.getInstruction()) != Instructions.PERSONS.getInstruction()) {
                    if (rule.endsWith("| " + Instructions.PERSONS.getTag())) {
                        ruleIterator.remove();
                    }
                } else if ((instructions & Instructions.ORGANIZATIONS.getInstruction()) != Instructions.ORGANIZATIONS.getInstruction()) {
                    if (rule.endsWith("| " + Instructions.ORGANIZATIONS.getTag())) {
                        ruleIterator.remove();
                    }
                } else if ((instructions & Instructions.DATES.getInstruction()) != Instructions.DATES.getInstruction()) {
                    if (rule.endsWith("| " + Instructions.DATES.getTag())) {
                        ruleIterator.remove();
                    }
                } else if ((instructions & Instructions.CURRENCIES.getInstruction()) != Instructions.CURRENCIES.getInstruction()) {
                    if (rule.endsWith("| " + Instructions.CURRENCIES.getTag())) {
                        ruleIterator.remove();
                    }
                }
            }
        }
        
        Debugger debugger = new Debugger(DebuggingClients.NER, 0);
        this.tagger = new Tagger(rules, debugger);
        
    }
    
    /**
     * This method replaces the original {@link com.itcag.rockwell.lang.Token tokens} with the {@link com.itcag.rockwell.lang.Semtoken semtokens} representing the identified named entities.
     * @param tokens Array list of {@link com.itcag.rockwell.lang.Token tokens} representing a sentence.
     * @return Array list containing {@link com.itcag.rockwell.lang.Semtoken semtokens} that replaced the original {@link com.itcag.rockwell.lang.Token tokens}.
     * @throws Exception if anything goes wrong.
     */
    public final ArrayList<Token> insert(ArrayList<Token> tokens) throws Exception {
        ArrayList<Tag> tags = extract(tokens);
        return Inserter.insertSemtokens(tags, tokens);
    }
    
    /**
     * This method extracts named entities.
     * @param tokens Array list of {@link com.itcag.rockwell.lang.Token tokens} representing a sentence.
     * @return Array list of instances of the {@link com.itcag.rockwell.lang.Tag Tag} class representing the extracted named entities.
     * @throws Exception if anything goes wrong.
     */
    public final ArrayList<Tag> extract(ArrayList<Token> tokens) throws Exception {
        
        /**
         * Insert the nominals first.
         */
        Nominals nominals = Nominals.getInstance();
        tokens = nominals.identify(tokens);

        ArrayList<Tag> tags = this.tagger.tag(tokens);
        if (tags.isEmpty()) return tags;

        ListIterator<Tag> tagIterator = tags.listIterator();
        while (tagIterator.hasNext()) {
            Tag tag = tagIterator.next();
            switch (tag.getTag()) {
                case "date":
                {
                    if (tag.getStart() == tag.getEnd()) {
                        if (!isNumericDateFormat(tokens.get(tag.getStart()).getWord())) tagIterator.remove();
                    }
                    break;
                }
                case "person":
                {
                    Tag test = validatePersonalName(tag, tokens);
                    if (test == null) {
                        tagIterator.remove();
                    } else {
                        tagIterator.set(test);
                    }
                    break;
                }
                case "corporation":
                {
                    Tag test = validateCorporateName(tag, tokens);
                    if (test == null) {
                        tagIterator.remove();
                    } else {
                        tagIterator.set(test);
                    }
                    break;
                }
            }
        }

        Toolbox toolbox = new Toolbox();
        tags = toolbox.consolidate(tags);
        
        return tags;
        
    }
    
    public final static boolean isNumericDateFormat(String date) {
        if (Converter.convertStringToDate(date, "yyyy-MM-dd") != null) return true;
        if (Converter.convertStringToDate(date, "yy-MM-dd") != null) return true;
        if (Converter.convertStringToDate(date, "dd-MM-yyyy") != null) return true;
        if (Converter.convertStringToDate(date, "dd-MM-yy") != null) return true;
        if (Converter.convertStringToDate(date, "MM-dd-yyyy") != null) return true;
        if (Converter.convertStringToDate(date, "MM-dd-yy") != null) return true;
        if (Converter.convertStringToDate(date, "dd/MM/yyyy") != null) return true;
        if (Converter.convertStringToDate(date, "dd/MM/yy") != null) return true;
        if (Converter.convertStringToDate(date, "MM/dd/yyyy") != null) return true;
        if (Converter.convertStringToDate(date, "MM/dd/yy") != null) return true;
        return false;
    }
    
    private Tag validatePersonalName(Tag tag, ArrayList<Token> tokens) {

        ArrayList<Token> extracted = new ArrayList<>();
        
        for (Token token : tokens) {
            if (token.getIndex() >= tag.getStart() && token.getIndex() <= tag.getEnd()) {
                if (token.getWord().equals(",")) continue;
                extracted.add(token);
            }
        }
        
        return Validator.validatePersonalName(tag, extracted);
        
    }

    private Tag validateCorporateName(Tag tag, ArrayList<Token> tokens) {

        ArrayList<Token> extracted = new ArrayList<>();
        
        for (Token token : tokens) {
            if (token.getIndex() >= tag.getStart() && token.getIndex() <= tag.getEnd()) {
                if (token.getWord().equals(",")) continue;
                extracted.add(token);
            }
        }
        
        return Validator.validateCorporateName(tag, extracted);
        
    }

}
