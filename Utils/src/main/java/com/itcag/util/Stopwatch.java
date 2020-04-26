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

package com.itcag.util;

public class Stopwatch {

    private final Long start;
    private Long end = null;
    
    public Stopwatch() {
        this.start = System.currentTimeMillis();
    }
    
    public void stop() {
        end = System.currentTimeMillis();
    }
    
    public long check() {
        return System.currentTimeMillis() - this.start;
    }
    
    public String checkFormatted() {
        return Converter.formatLongTime(System.currentTimeMillis() - this.start);
    }
    
    public long duration() throws Exception {
        if (this.end == null) throw new IllegalStateException("Stopwatch was not stopped. You can get duration only after stopping the stopwatch. Use check to read intermediary durations.");
        return this.end - this.start;
    }
    
    public String durationFormatted() {
        return Converter.formatLongTime(this.end - this.start);
    }
    
}
