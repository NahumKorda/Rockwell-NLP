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
