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

import java.security.Key;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>Encrypts/decrypts strings. Primarily designed for encrypting/decrypting authorization data for storage in configuration files and databases.</p>
 */
public final class Encryptor {

    private static final byte[] KEY = "3c526c65e94049ea".getBytes();
    private static final String ALGORITHM = "AES";

    /**     * 
     * @param input String that is to be encrypted.
     * @return Encrypted string.
     * @throws Exception if anything goes wrong.
     */
    public final synchronized static String encrypt(String input) throws Exception {
        
        Key key = generateKey();
        
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        
        byte[] encValue = cipher.doFinal(input.getBytes());

        Base64.Encoder encoder = Base64.getEncoder();
        String retVal = encoder.encodeToString(encValue);
        
        return retVal;
    
    }

    /**
     * @param input Encrypted string.
     * @return Decrypted string.
     * @throws Exception if anything goes wrong.
     */
    public final static String decrypt(String input) throws Exception {
    
        Key key = generateKey();
        
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedValue = decoder.decode(input);

        byte[] decValue = cipher.doFinal(decodedValue);

        String retVal = new String(decValue);
        return retVal;
    
    }

    private static Key generateKey() throws Exception {
        
        Key key = new SecretKeySpec(KEY, ALGORITHM);
        return key;
    
    }

}
