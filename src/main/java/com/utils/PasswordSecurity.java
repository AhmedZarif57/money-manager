package com.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Secure password hashing utility using PBKDF2 with SHA-256
 * Provides industry-standard password security with salt and iterations
 */
public class PasswordSecurity {
    
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 65536; // 65,536 iterations (recommended by OWASP)
    private static final int KEY_LENGTH = 256; // 256-bit key
    private static final int SALT_LENGTH = 32; // 32 bytes salt
    
    /**
     * Hashes a password with a randomly generated salt
     * @param password The plain text password
     * @return Base64 encoded string containing salt and hash separated by ":"
     */
    public static String hashPassword(String password) {
        try {
            // Generate random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Hash the password
            byte[] hash = hashPassword(password.toCharArray(), salt);
            
            // Return salt:hash as Base64 encoded string
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verifies a password against a stored hash
     * @param password The plain text password to verify
     * @param storedHash The stored hash (salt:hash format)
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            // Split stored hash into salt and hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);
            
            // Hash the input password with the same salt
            byte[] testHash = hashPassword(password.toCharArray(), salt);
            
            // Compare hashes using constant-time comparison to prevent timing attacks
            return constantTimeEquals(hash, testHash);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Hashes a password with a given salt using PBKDF2
     */
    private static byte[] hashPassword(char[] password, byte[] salt) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        return factory.generateSecret(spec).getEncoded();
    }
    
    /**
     * Constant-time comparison to prevent timing attacks
     */
    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
    
    /**
     * Checks if a password hash is in the new secure format
     * @param passwordField The password field from storage
     * @return true if it's already hashed, false if it's plain text
     */
    public static boolean isHashed(String passwordField) {
        // Hashed passwords contain ":" separator and are Base64 encoded
        if (passwordField == null || !passwordField.contains(":")) {
            return false;
        }
        
        String[] parts = passwordField.split(":");
        if (parts.length != 2) {
            return false;
        }
        
        try {
            // Try to decode both parts as Base64
            Base64.getDecoder().decode(parts[0]);
            Base64.getDecoder().decode(parts[1]);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
