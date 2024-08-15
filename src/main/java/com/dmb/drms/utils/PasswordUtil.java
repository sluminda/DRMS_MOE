package com.dmb.drms.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.SecureRandom;

public class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SALT_LENGTH = 32;

    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return Base64.encodeBase64String(salt);
    }

    public static String hashPassword(String password, String salt) {
        String combined = password + salt;
        return DigestUtils.sha256Hex(combined);
    }

    public static boolean verifyPassword(String password, String passwordHash, String salt) {
        String hashedPassword = hashPassword(password, salt);
        return hashedPassword.equals(passwordHash);
    }
}
