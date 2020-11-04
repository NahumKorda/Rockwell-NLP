package com.itcag.util.punct;

import com.itcag.util.txt.TextToolbox;

import java.util.EnumSet;

import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;

public class URL {

    private final LinkExtractor urlDetector = LinkExtractor.builder().linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW)).build();

    public URL() {
        
    }
    
    public void lock(StringBuilder input) {

        Iterable<LinkSpan> links = this.urlDetector.extractLinks(input.toString());
        for (LinkSpan link : links) {
            String original = input.substring(link.getBeginIndex(), link.getEndIndex());
            if (original.endsWith(".")) {
                original = original.substring(0, original.length() - 2);
            }
            StringBuilder replacement = new StringBuilder(original);
            encode(replacement);
            TextToolbox.replace(input, original, replacement.toString());
        }

    }
    
    public String lock(String input) {
        StringBuilder retVal = new StringBuilder(input);
        this.lock(retVal);
        return retVal.toString();
    }
    
    private void encode(StringBuilder input) {
        TextToolbox.replace(input, ".", Characters.URL_PERIOD.getReplacement());
        TextToolbox.replace(input, "!", Characters.URL_EXCLAMATION.getReplacement());
        TextToolbox.replace(input, "?", Characters.URL_QUESTION.getReplacement());
        TextToolbox.replace(input, ":", Characters.URL_COLON.getReplacement());
        TextToolbox.replace(input, ";", Characters.URL_SEMICOLON.getReplacement());
        TextToolbox.replace(input, ";", Characters.URL_COMMA.getReplacement());
        TextToolbox.replace(input, "/", Characters.URL_SLASH.getReplacement());
        TextToolbox.replace(input, "-", Characters.URL_HYPHEN.getReplacement());
    }

    public void unlock(StringBuilder input) {
        decode(input);
    }
    
    public String unlock(String input) {
        StringBuilder retVal = new StringBuilder(input);
        decode(retVal);
        return retVal.toString();
    }
    
    private void decode(StringBuilder input) {
        TextToolbox.replace(input, Characters.URL_PERIOD.getReplacement(), ".");
        TextToolbox.replace(input, Characters.URL_EXCLAMATION.getReplacement(), "!");
        TextToolbox.replace(input, Characters.URL_QUESTION.getReplacement(), "?");
        TextToolbox.replace(input, Characters.URL_COLON.getReplacement(), ":");
        TextToolbox.replace(input, Characters.URL_SEMICOLON.getReplacement(), ";");
        TextToolbox.replace(input, Characters.URL_COMMA.getReplacement(), ";");
        TextToolbox.replace(input, Characters.URL_SLASH.getReplacement(), "/");
        TextToolbox.replace(input, Characters.URL_HYPHEN.getReplacement(), "-");
    }

    public boolean isThisTokenLockedURL(String token) {
        
        if (token.contains(Characters.URL_PERIOD.getReplacement())) return true;
        if (token.contains(Characters.URL_EXCLAMATION.getReplacement())) return true;
        if (token.contains(Characters.URL_QUESTION.getReplacement())) return true;
        if (token.contains(Characters.URL_COLON.getReplacement())) return true;
        if (token.contains(Characters.URL_SEMICOLON.getReplacement())) return true;
        if (token.contains(Characters.URL_COMMA.getReplacement())) return true;
        if (token.contains(Characters.URL_SLASH.getReplacement())) return true;
        if (token.contains(Characters.URL_HYPHEN.getReplacement())) return true;
        
        return false;
        
    }
    
}
