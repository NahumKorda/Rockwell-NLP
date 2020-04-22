package com.itcag.util.txt;

import com.google.common.base.Optional;

import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

import java.util.List;

/**
 * <p>This class attempts to estimate language of the input text.</p>
 * <p>This class is implemented as singleton.</p>
 */
public final class Langdetor {

    private static volatile Langdetor instance = new Langdetor();
    
    /**
     * @return Instance of this class.
     */
    public final static Langdetor getInstance() {
        return instance;
    }
    
    private final LanguageDetector languageDetector;
    private final TextObjectFactory textObjectFactory;

    private Langdetor() {
        
        try {
            
            List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

            languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
            .withProfiles(languageProfiles)
            .build();

            textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

        } catch (Exception ex) {
            
            throw new RuntimeException(ex);
        }
    
    }

    /**
     * Estimates whether the input string is in English.
     * @param text String holding input text.
     * @return Boolean indicating whether the input text is in English.
     */
    public final synchronized boolean isEnglish(String text) {
        
        TextObject textObject = textObjectFactory.forText(text);
        Optional<LdLocale> lang = languageDetector.detect(textObject);

        if (!lang.isPresent()) return false;
        return lang.get().getLanguage().equalsIgnoreCase("en");
        
    }

    /**
     * Estimates the language of the input text.
     * @param text String holding input text.
     * @return String holding the estimated language two-letter code (IETF BCP 47 tag).
     * @see com.optimaize.langdetect.i18n.LdLocale
     */
    public final synchronized String getLanguage(String text) {

        TextObject textObject = textObjectFactory.forText(text);
        Optional<LdLocale> lang = languageDetector.detect(textObject);

        if (!lang.isPresent()) return null;

        return lang.get().getLanguage();

    }

}