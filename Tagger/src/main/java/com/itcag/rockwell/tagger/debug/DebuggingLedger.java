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
 * <p>Keeps specifications which clients using the {@link com.itcag.rockwell.tagger.Tagger Tagger} class print debugging data.</p>
 * <p>Applies bitwise comparison to decide whether a client prints debugging data or not.</p>
 * <p>This class is used only for debugging.</p>
 */
public final class DebuggingLedger {

    public final static long DEBUGGING_LEVEL = 0;
//    public final static long DEBUGGING_LEVEL = DebuggingClients.PIPELINE.getInstruction() | DebuggingClients.PATTERNS.getInstruction();
    
    /** Prints conditions (extraction rules) when they are loaded into processor. */
    public final static long PRINT_LEVEL = 0;
    
    public final static boolean isIncluded(DebuggingClients client) {
        return (DEBUGGING_LEVEL & client.getInstruction()) == client.getInstruction();
    }
    
    public final static boolean printConditions(DebuggingClients client) {
        if (!isIncluded(client)) return false;
        return (PRINT_LEVEL & client.getInstruction()) == client.getInstruction();
    }
    
}

