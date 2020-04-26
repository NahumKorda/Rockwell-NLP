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

/**
 * <p>Generic exception that replaces all component-specific exceptions.</p>
 * <p>Component-specific exceptions are handled internally by various class, and are not exposed to external classes. Instead, one simple generic exception is used.</p>
 * <p>UtilException is expected to be caught and handled differently than other exceptions. Its messages are purposely formulated in a way that allows them to be displayed to the end user.</p>
 */
public final class UtilException extends Exception {
    
    /**
     * This constructor is provided only for the compatibility with the superclass.
     */
    public UtilException() {}
    
    /**
     * This constructor is provided only for the compatibility with the superclass.
     */
    public UtilException(Exception ex) {
        super(ex);
    }
    
    /**
     * UtilException is always assumed to feature a message that can be displayed to the end user.
     * @param msg String containing easily understood explanation of the problem that caused this exception.
     */
    public UtilException(String msg) {
        super(msg);
    }
    
}
