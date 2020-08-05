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

package com.itcag.rockwell.semantex.ner;

import com.itcag.rockwell.semantex.Inserter;
import com.itcag.rockwell.lang.Tag;
import com.itcag.rockwell.lang.Token;
import com.itcag.rockwell.semantex.Toolbox;
import com.itcag.rockwell.tagger.EnclosedTagModes;
import com.itcag.rockwell.tagger.Tagger;
import com.itcag.rockwell.tagger.debug.Debugger;
import com.itcag.rockwell.tagger.debug.DebuggingClients;

import java.util.ArrayList;

/**
 * <p>This class loads and stores Rockwell script that identifies the so-called <i>nominals</i>. Nominals are terms from the lexicon used to identify named entities. For example: names of the months and weekdays used to identify dates, suffixes used to identify corporations, honorary titles used to identify personal names, etc.</p>
 * <p>This class identifies nominals in an array list of {@link com.itcag.rockwell.lang.Token tokens} representing a sentence, and then replaces the original tokens identified as nominals with the {@link com.itcag.rockwell.lang.Semtoken semtokens}.</p>
 * <p>This class is implemented as singleton to avoid reloading lexical resources.</p>
 */
public class Nominals {

    private static volatile Nominals instance = null;
    
    /**
     * This method is designed to throw an exception if lexical resources cannot be loaded, and therefore the double checked locking is necessary.
     * @return Instance of this class.
     * @throws Exception If anything goes wrong.
     */
    public static Nominals getInstance() throws Exception {
        if (instance == null) {
            synchronized(Nominals.class) {
                if (instance == null) {
                    instance = new Nominals();
                }
            }
        }
        return instance;
    }
    
    private final Tagger tagger;

    private Nominals() throws Exception {
        
        Loader loader = new Loader();
        ArrayList<String> rules = loader.load("nominals");

        Debugger debugger = new Debugger(DebuggingClients.NOMINALS, 0);
        
        this.tagger = new Tagger(rules, EnclosedTagModes.NONE, debugger);
    
    }

    /**
     * This method identifies nominals in an array list of {@link com.itcag.rockwell.lang.Token tokens}, and replaces the original tokens with the {@link com.itcag.rockwell.lang.Semtoken semtokens}.
     * @param tokens Array list of {@link com.itcag.rockwell.lang.Token tokens} representing a sentence.
     * @return Array list of tokens with {@link com.itcag.rockwell.lang.Token tokens} identified as nominals replaced by {@link com.itcag.rockwell.lang.Semtoken semtokens}.
     * @throws Exception if anything goes wrong.
     */
    public ArrayList<Token> identify(ArrayList<Token> tokens) throws Exception {
        
        ArrayList<Tag> tags = this.tagger.tag(tokens);
        if (tags.isEmpty()) return tokens;
        
        Toolbox toolbox = new Toolbox();
        tags = toolbox.consolidate(tags);

        return Inserter.insertSemtokens(tags, tokens);
        
    }
    
}
