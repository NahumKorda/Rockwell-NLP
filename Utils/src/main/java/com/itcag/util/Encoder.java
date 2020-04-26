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

import com.itcag.util.txt.TextToolbox;
import java.net.URLEncoder;
import java.net.URLDecoder;

import java.io.UnsupportedEncodingException;


/**
 * <p>This class provides URL encoding/decoding.</p>
 */
public final class Encoder {

    /**
     * Performs URL encoding.
     * @param text String to be encoded.
     * @return Encoded string.
     * @throws UtilException if encoding fails for any reason.
     */
    public final static String encodeText(String text) throws UtilException {
        
        if (TextToolbox.isEmpty(text)) return text;

        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new UtilException(ex);
        }

    }
    /**
     * Decodes a URL encoded string.
     * @param text String to be decoded.
     * @return Decoded string.
     * @throws UtilException if decoding fails for any reason.
     */
    public final static String decodeText(String text) throws UtilException {
        
        if (TextToolbox.isEmpty(text)) return text;

        try {
            return URLDecoder.decode(text, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new UtilException(ex);
        }

    }
    
}
