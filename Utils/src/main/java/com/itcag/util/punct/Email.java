package com.itcag.util.punct;

import com.itcag.util.txt.TextToolbox;

import java.util.EnumSet;

import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;

public class Email {

    private final LinkExtractor emailDetector = LinkExtractor.builder().linkTypes(EnumSet.of( LinkType.EMAIL)).build();

    public Email() {
        
    }
    
    public void lock(StringBuilder input) {

        Iterable<LinkSpan> links = this.emailDetector.extractLinks(input.toString());
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
        StringBuilder retVal = new StringBuilder();
        this.lock(retVal);
        return retVal.toString();
    }
    
    private void encode(StringBuilder input) {
        TextToolbox.replace(input, ".", Characters.EMAIL_PERIOD.getReplacement());
        TextToolbox.replace(input, "!", Characters.EMAIL_EXCLAMATION.getReplacement());
        TextToolbox.replace(input, "?", Characters.EMAIL_QUESTION.getReplacement());
        TextToolbox.replace(input, ":", Characters.EMAIL_COLON.getReplacement());
        TextToolbox.replace(input, ";", Characters.EMAIL_SEMICOLON.getReplacement());
        TextToolbox.replace(input, ";", Characters.EMAIL_COMMA.getReplacement());
        TextToolbox.replace(input, "/", Characters.EMAIL_SLASH.getReplacement());
        TextToolbox.replace(input, "-", Characters.EMAIL_HYPHEN.getReplacement());
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
        TextToolbox.replace(input, Characters.EMAIL_PERIOD.getReplacement(), ".");
        TextToolbox.replace(input, Characters.EMAIL_EXCLAMATION.getReplacement(), "!");
        TextToolbox.replace(input, Characters.EMAIL_QUESTION.getReplacement(), "?");
        TextToolbox.replace(input, Characters.EMAIL_COLON.getReplacement(), ":");
        TextToolbox.replace(input, Characters.EMAIL_SEMICOLON.getReplacement(), ";");
        TextToolbox.replace(input, Characters.EMAIL_COMMA.getReplacement(), ";");
        TextToolbox.replace(input, Characters.EMAIL_SLASH.getReplacement(), "/");
        TextToolbox.replace(input, Characters.EMAIL_HYPHEN.getReplacement(), "-");
    }

    public boolean isThisTokenLockedEmail(String token) {
        
        if (token.contains(Characters.EMAIL_PERIOD.getReplacement())) return true;
        if (token.contains(Characters.EMAIL_EXCLAMATION.getReplacement())) return true;
        if (token.contains(Characters.EMAIL_QUESTION.getReplacement())) return true;
        if (token.contains(Characters.EMAIL_COLON.getReplacement())) return true;
        if (token.contains(Characters.EMAIL_SEMICOLON.getReplacement())) return true;
        if (token.contains(Characters.EMAIL_COMMA.getReplacement())) return true;
        if (token.contains(Characters.EMAIL_SLASH.getReplacement())) return true;
        if (token.contains(Characters.EMAIL_HYPHEN.getReplacement())) return true;
        
        return false;
        
    }
    
}
