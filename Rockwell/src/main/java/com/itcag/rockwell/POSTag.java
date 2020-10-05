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
 * <p>Complete list of parts of speech supported by Rockwell.</p>
 * <p>Each part of speech is also assigned a {@link com.itcag.rockwell.POSType type}. Types group parts of speech that belong to the same word class. For example, all adjectives (AJ0, AJC, AJS) are grouped under the same type (AJ).</p>
 */
public enum POSTag {

    /** Abbreviation. */
    ABB(POSType.NN),
    /** Acronym. */
    ACR(POSType.NN),
    /** Adjective (general or positive) (e.g., "good", "old", "beautiful"). */
    AJ0(POSType.AJ),
    /** Comparative adjective (e.g., "better", "older"). */
    AJC(POSType.AJ),
    /** Superlative adjective (e.g., "best", "oldest"). */
    AJS(POSType.AJ),
    /** General adverb: an adverb not sub-classified as AVP or AVQ (e.g., "often", "well", "longer", "furthest". */
    AV0(POSType.AV),
    /** Wh-adverb (e.g., "when", "where", "how", "why", "wherever"). */
    AVQ(POSType.CJ),
    /** Coordinating conjunction (e.g., "and", "or", "but"). */
    CJC(POSType.CJ),
    /** Subordinating conjunction (e.g., "although", "when"). */
    CJS(POSType.CJ),
    /** The subordinating conjunction "that". */
    CJT(POSType.CJ),
    /** Cardinal number (e.g., "one", "3", "fifty-five", "3609"). */
    CRD(POSType.NM),
    /** General determiner. */
    DT0(POSType.DT),
    /** Existential "there", i.e. there occurring in the "there is..." or "there are..." construction. */
    EX0(POSType.EX),
    /** Interjection or other isolate (e.g., "oh", "yes", "mhm", "wow"). */
    ITJ(POSType.IT),
    /** Common noun, neutral for number (e.g., "aircraft", "data", "committee"). */
    NN0(POSType.NN),
    /** Singular common noun (e.g., "pencil", "goose", "time", "revelation"). */
    NN1(POSType.NN),
    /** Plural common noun (e.g., "pencils", "geese", "times", "revelations"). */
    NN2(POSType.NN),
    /** Proper noun (e.g., "London", "Michael", "Mars", "IBM"). */
    NP0(POSType.NN),
    /** Ordinal numeral (e.g., "first", "sixth", "77th", "last"). */
    ORD(POSType.NM),
    /** Indefinite pronoun (e.g., "none", "everything", "one", "nobody"). */
    PNI(POSType.PN),
    /** Personal pronoun (e.g., "I", "you", "them", "ours"). */
    PNP(POSType.PN),
    /** Wh-pronoun (e.g., "who", "whoever", "whom"). */
    PNQ(POSType.PN),
    /** Possessive determiner-pronoun (e.g., "your", "their", "his"). */
    PNS(POSType.PN),
    /** Reflexive pronoun (e.g., "myself", "yourself", "itself", "ourselves"). */
    PNX(POSType.PN),
    /** The possessive or genitive marker "'s" or "'" (the Saxon genitive). */
    POS(POSType.PO),
    /** Preposition (e.g., "about", "at", "in", "on", "on behalf of", "with"). */
    PRP(POSType.PR),
    /** Infinitive marker "to". */
    TO0(POSType.TO),
    /** The present tense forms of the verb BE (e.g., "am", "are", "'m", "'re" and subjunctive or imperative "be"). */
    VBB(POSType.VA),
    /** The past tense forms of the verb BE: "was" and "were". */
    VBD(POSType.VA),
    /** The -ing form of the verb BE: "being". */
    VBG(POSType.VA),
    /** The infinitive form of the verb BE: "be". */
    VBI(POSType.VA),
    /** The past participle form of the verb BE: "been". */
    VBN(POSType.VA),
    /** The -s form of the verb BE: "is", "'s". */
    VBZ(POSType.VA),
    /** The finite base form of the verb DO: "do". */
    VDB(POSType.VA),
    /** The past tense form of the verb DO: "did". */
    VDD(POSType.VA),
    /** The -ing form of the verb DO: "doing". */
    VDG(POSType.VA),
    /** The infinitive form of the verb DO: "do". */
    VDI(POSType.VA),
    /** The past participle form of the verb DO: "done". */
    VDN(POSType.VA),
    /** The -s form of the verb DO: "does". */
    VDZ(POSType.VA),
    /** The finite base form of the verb HAVE: "have", "'ve". */
    VHB(POSType.VA),
    /** The past tense form of the verb HAVE: "had", "'d". */
    VHD(POSType.VA),
    /** The -ing form of the verb HAVE: "having". */
    VHG(POSType.VA),
    /** The infinitive form of the verb HAVE: "have". */
    VHI(POSType.VA),
    /** The past participle form of the verb HAVE: "had". */
    VHN(POSType.VA),
    /** The -s form of the verb HAVE: "has", "'s". */
    VHZ(POSType.VA),
    /** Modal auxiliary verb (e.g., "would", "can", "could", "'d"). */
    VM0(POSType.VM),
    /** Future auxiliaries: "will", "shall", "'ll". */
    VM1(POSType.VM),
    /** The finite base form of lexical verbs (e.g., "forget", "send", "live", "return"); includes the imperative and present subjunctive. */
    VVB(POSType.VV),
    /** The past tense form of lexical verbs (e.g., "forgot", "sent", "lived", "returned"). */
    VVD(POSType.VV),
    /** The -ing form of lexical verbs (e.g., "forgetting", "sending", "living", "returning"). */
    VVG(POSType.VV),
    /** The infinitive form of lexical verbs (e.g., "forget", "send", "live", "return"). */
    VVI(POSType.VV),
    /** The past participle form of lexical verbs (e.g., "forgotten", "sent", "lived", "returned"). */
    VVN(POSType.VV),
    /** The -s form of lexical verbs (e.g., "forgets", "sends", "lives", "returns"). */
    VVZ(POSType.VV),
    /** Negation. */
    XX0(POSType.NG),

    /** Sentence terminating punctuation.*/
    PC0(POSType.PC),
    /** Sentence non-terminating punctuation.*/
    PC1(POSType.PC),
    /** Opening parenthesis.*/
    PC2(POSType.PC),
    /** Closing parenthesis.*/
    PC3(POSType.PC),
    
    /** Unrecognized word.*/
    XXX(POSType.NN),
    /** %, ‰*/
    XZ1(POSType.XZ),
    /** Currency.*/
    XZ2(POSType.XZ),
    /** Measuring unit.*/
    XZ3(POSType.XZ),
    /** Hyphen */
    XZ4(POSType.XZ),

    ;
    
    private final POSType type;
    
    private POSTag(POSType type) {
        this.type = type;
    }
    
    public POSType getType() {
        return type;
    }
    
}
