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
