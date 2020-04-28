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

package com.itcag.rockwell.tagger.debug;

/**
 * <p>Enumerates all possible clients that use the {@link com.itcag.rockwell.tagger.Tagger Tagger} class.</p>
 * <p>This class is used only for debugging.</p>
 */
public enum DebuggingClients {

    TESTING(2),
    PATTERNS(4),
    NER(8),
    NOMINALS(16),
    INTERPRETER(32),
    EXTRACTOR(64),
    PIPELINE(128),

    ;
    
    private final long instruction;
    
    private DebuggingClients(long instruction) {
        this.instruction = instruction;
    }
    
    /**
     * @return Long numerical that can be used for the bitwise comparison in the {@link com.itcag.rockwell.tagger.debug.DebuggingLedger DebuggingLedger} class.
     */
    public final long getInstruction() {
        return this.instruction;
    }
    
}

