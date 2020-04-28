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

package com.itcag.rockwell;

/**
 * <p>Complete list of all part of speech types supported by Rockwell.</p>
 * <p>Types group parts of speech that belong to the same word class. For example, all adjectives (AJ0, AJC, AJS) are grouped under the same type (AJ).</p>
 */
public enum POSType {

    /** Adjectives. */
    AJ,
    /** Adverbs. */
    AV,
    /** Conjunctions. */
    CJ,
    /** Determiners. */
    DT,
    /** Existential "there". */
    EX,
    /** Interjections. */
    IT,
    /** Negations. */
    NG,
    /** Nouns. */
    NN,
    /** Numbers. */
    NM,
    /** Punctuation characters. */
    PC,
    /** Pronouns. */
    PN,
    /** The possessive or genitive marker "'s" or "'" (the Saxon genitive). */
    PO,
    /** Prepositions. */
    PR,
    /** Infinitive marker "to". */
    TO,
    /** Auxiliary verbs. */
    VA,
    /** Modal verbs. */
    VM,
    /** Verbs. */
    VV,
    /** Unrecognized. */
    XX,
    /** Symbols. */
    XZ,

}

