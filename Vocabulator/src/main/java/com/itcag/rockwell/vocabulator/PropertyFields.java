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

package com.itcag.rockwell.vocabulator;


/**
 * <p>Enumerates processing instructions passed as an argument to the constructor of the {@link VocabularyExtractor} class.</p>
 * <p>Since the instructions are passed as an instance of the Java {@link java.util.Properties Properties} class, each value in this enum can potentially create a field in the instance of that class.</p>
 */
public enum PropertyFields {

    /** Specifies what should be extracted. Tasks are enumerated in the {@link com.itcag.rockwell.vocabulator.VocabularyExtractor.Tasks  Tasks} enum. */
    TASK("task"),
    /** Specifies the lower boundary that the frequency of occurrence must exceed, in order not to be removed from the results when trimming intermediary results. */
    TRIM_THRESHOLD("trimThreshold"),
    /** Specifies the lower boundary that the frequency of occurrence of a single word or lemma must exceed, in order to be included in the results. */
    WORD_THRESHOLD("wordThreshold"),
    /** Specifies the lower boundary that the frequency of occurrence of a phrase must exceed, in order to be included in the results. */
    PHRASE_THRESHOLD("phraseThreshold"),
    /** Specifies the minimum number of words allowed in an extracted phrase. his number cannot be less than 2. It applies only to the phrase extraction. */
    MIN_PHRASE_LENGTH("minPhraseLength"),
    /** specifies the maximum number of words allowed in an extracted phrase. It applies only to the phrase extraction. */
    MAX_PHRASE_LENGTH("maxPhraseLength"),
    /** Specifies which word classes must be ignored (for example, stop words). */
    EXCLUSIONS("exclusions"),
    /** Specifies terms that must occur on a sentence, in order to extract phrases from it. If not specified, every sentence accepted. It applies only to the phrase extraction. */
    POSITIVE_FILTER("positiveFilter"),
    /** Specifies terms that if cause a sentence to be ignored if they occur in it. It applies only to the phrase extraction. */
    NEGATIVE_FILTER("negativeFilter"),
    /** Specifies terms that must occur in an extracted phrase, in order to include the phrase in the results. It applies only to the phrase extraction. */
    REQUIRED_FILTER("requiredFilter"),
    /** Specifies path to a file holding words that are ignored if extracted. */
    STOPWORDS("stopwords"),
    /** Specifies path to a file holding phrases that are ignored if extracted. */
    STOP_PHRASES("stopPhrases"),
    /** Specifies path to a file holding an accepted label for alternative single-word expressions. */
    WORD_SYNONYMS("wordSynonyms"),
    /** Specifies path to a file holding an accepted label for alternative phrases. */
    PHRASE_SYNONYMS("phraseSynonyms"),
    
    ;
    
    private final String field;
    
    private PropertyFields(String field) {
        this.field = field;
    }
    
    public String getField() {
        return this.field;
    }
    
}
