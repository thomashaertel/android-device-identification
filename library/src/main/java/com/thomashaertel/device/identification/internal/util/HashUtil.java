/*
 * Copyright (c) 2015. Thomas Haertel
 *
 * Licensed under Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.thomashaertel.device.identification.internal.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Internal Utility Class for computing hash values (MD5 and SHA-1)
 */
public final class HashUtil {
    private HashUtil() {
    }

    /**
     * Returns a hash value for a string using MD5 algorithm
     *
     * @param text a string to compute a hash value for
     * @return an uppercase hash value for the given text
     * @throws NoSuchAlgorithmException if hash algorithm is not available
     */
    public static String computeHashMD5(final String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(text.getBytes(), 0, text.length());

        // get md5 bytes
        byte hashData[] = md.digest();

        // create a hex string
        StringBuilder sb = new StringBuilder(hashData.length * 2);

        for (int i = 0; i < hashData.length; i++) {
            int b = (0xFF & hashData[i]);
            // if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF) sb.append('0');
            // add number to string
            sb.append(Integer.toHexString(b));
        }

        // hex string to uppercase
        return sb.toString().toUpperCase();
    }

    /**
     * Returns a hash value for a string using SHA-1 algorithm
     *
     * @param text a string to compute a hash value for
     * @return a uppercase hash value for the given text
     * @throws NoSuchAlgorithmException if hash algorithm is not available
     */
    public static String computeHashSHA1(final String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes(), 0, text.length());

        // get sha-1 bytes
        byte hashData[] = md.digest();

        // create a hex string
        StringBuilder sb = new StringBuilder(hashData.length * 2);

        for (int i = 0; i < hashData.length; i++) {
            int b = (0xFF & hashData[i]);
            // if it is a single digit, make sure it have 0 in front (proper padding)
            if (b <= 0xF) sb.append('0');
            // add number to string
            sb.append(Integer.toHexString(b));
        }

        // hex string to uppercase
        return sb.toString().toUpperCase();
    }
}
