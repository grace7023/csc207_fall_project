package fall2018.csc2017.game_centre;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Data saving + loading + encryption + HashCheck playground
 * TO BE OF USE FOR PHASE 2
 * Idea: complicated passwords, SHA-512,
 */
class Addons {

    /**
     * Hash string to SHA256 for security
     *
     * @param input string that is to be hashed
     * @return hash of string
     * <p>
     * Inspired from https://stackoverflow.com/questions/3103652/hash-string-via-sha-256-in-java
     */
    static String stringToSHA256(String input) {
        String toReturn = null;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(input.getBytes(StandardCharsets.UTF_8));
            toReturn = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    /**
     * check if string returns the same hash as the hashed password
     *
     * @param input    string to be hashed and compared
     * @param password hashed string
     * @return true if match
     */
    static boolean checkString(String input, String password) {
        return stringToSHA256(input).equals(password);
    }
}
