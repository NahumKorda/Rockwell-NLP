package com.itcag.rockwell.split;

import com.itcag.util.txt.TextToolbox;

import java.util.EnumSet;

import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;

/**
 * <p>This class "locks" dual purpose characters wherever they are not used as punctuation. Dual purpose characters are characters that can be used as punctuation, but also in numbers, URLs, acronyms, etc. Locking ensures that the splitting will not erroneously split text in wrong places.</p>
 */
public final class Locker {

    /**
     * @param input String builder holding the original text.
     * @throws Exception if anything goes wrong.
     */
    public final synchronized void lock(StringBuilder input) throws Exception {
        
        lockURL(input);
        
        lockPeriods(input);
        lockColons(input);
        lockCommas(input);
        
        input.append(" ");
        
    }

    private void lockPeriods(StringBuilder input) {
        int start = input.indexOf(".");
        while (start > -1 && start < input.length() - 1) {
            char c = input.charAt(start + 1);
            if (Character.isDigit(c)) {
                input.replace(start, start + 1, Characters.PERIOD.getReplacement());
            } else if (Character.isLetter(c)) {
                lockAcronym(input, start);
            }
            start = input.indexOf(".", start + 1);
        }
    }
    
    private void lockColons(StringBuilder input) {
        /**
         * This is to preserve the time.
         */
        int start = input.indexOf(":");
        while (start > -1 && start < input.length() - 1) {
            char c = input.charAt(start + 1);
            if (Character.isDigit(c)) {
                input.replace(start, start + 1, Characters.COLON.getReplacement());
            }
            start = input.indexOf(".", start + 1);
        }
    }
    
    private void lockCommas(StringBuilder input) {
        int start = input.indexOf(",");
        while (start > -1 && start < input.length() - 1) {
            char c = input.charAt(start + 1);
            if (Character.isDigit(c)) input.replace(start, start + 1, Characters.COMMA.getReplacement());
            start = input.indexOf(",", start + 1);
        }
    }
    
    private void lockURL(StringBuilder input) {

        LinkExtractor linkExtractor = LinkExtractor.builder().linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW, LinkType.EMAIL)).build();
        Iterable<LinkSpan> links = linkExtractor.extractLinks(input);
        while (links.iterator().hasNext()) {
            LinkSpan link = links.iterator().next();
            String original = input.substring(link.getBeginIndex(), link.getEndIndex());
            if (original.endsWith(".")) {
                original = original.substring(0, original.length() - 2);
            }
            String replacement = encode(original);
            TextToolbox.replace(input, original, replacement);
        }

    }
    
    private void lockAcronym(StringBuilder input, int start) {
        
        int dot = start;
        int letter = start - 1;
        for (int i = start; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == 46) {
                if (i == letter + 1) {
                    dot = i;
                    if (i == (input.length() - 1) && i >= start + 2) {
                        TextToolbox.replaceWithin(input, input.substring(start, i + 1), ".", Characters.PERIOD.getReplacement());
                        input.append(".");
                    }
                } else {
                    break;
                }
            } else if (Character.isLetter(c)) {
                if (i == dot + 1) {
                    letter = i;
                } else {
                    break;
                }
            } else {
                if (i > start + 2) {
                    TextToolbox.replaceWithin(input, input.substring(start, i), ".", Characters.PERIOD.getReplacement());
                }
                break;
            }
        }
        
    }
    
    public final void unlock(StringBuilder input) throws Exception {
        decode(input);
    }
    
    private String encode(String input) {

        String retVal = input;

        retVal = TextToolbox.replace(retVal, ".", Characters.PERIOD.getReplacement());
        retVal = TextToolbox.replace(retVal, "!", Characters.EXCLAMATION.getReplacement());
        retVal = TextToolbox.replace(retVal, "?", Characters.QUESTION.getReplacement());
        retVal = TextToolbox.replace(retVal, "-", Characters.HYPHEN.getReplacement());
        retVal = TextToolbox.replace(retVal, ":", Characters.COLON.getReplacement());
        retVal = TextToolbox.replace(retVal, ";", Characters.SEMICOLON.getReplacement());
        retVal = TextToolbox.replace(retVal, "/", Characters.SLASH.getReplacement());

        return retVal;

    }
    
    private void decode(StringBuilder input) {
        
        TextToolbox.replace(input, Characters.PERIOD.getReplacement(), ".");
        TextToolbox.replace(input, Characters.EXCLAMATION.getReplacement(), "!");
        TextToolbox.replace(input, Characters.QUESTION.getReplacement(), "?");
        TextToolbox.replace(input, Characters.COLON.getReplacement(), ":");
        TextToolbox.replace(input, Characters.SEMICOLON.getReplacement(), ";");
        TextToolbox.replace(input, Characters.COMMA.getReplacement(), ",");
        TextToolbox.replace(input, Characters.SLASH.getReplacement(), "/");
        TextToolbox.replace(input, Characters.HYPHEN.getReplacement(), "-");
        
    }
    
}
