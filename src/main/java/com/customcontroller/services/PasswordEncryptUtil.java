package com.customcontroller.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.customcontroller.exceptions.PasswordEncryptException;

import javax.enterprise.context.ApplicationScoped;

/**
 * Created by SeanCunniffe on 07/Feb/2022
 */
@ApplicationScoped
public class PasswordEncryptUtil {

    private final BCrypt.Hasher hasher;

    public PasswordEncryptUtil() {
        hasher = BCrypt.withDefaults();
    }

    public String hashPassword(String plainTextPassword) {
        if (verifyPasswordIsHashed(plainTextPassword))
            throw new PasswordEncryptException("Password is already hashed");

        byte[] hash = hasher.hash(12, plainTextPassword.toCharArray());
        return new String(hash);
    }

    public boolean verifyPassword(String expectedPassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(expectedPassword.toCharArray(), hashedPassword.toCharArray());
        return result.verified;
    }

    public boolean verifyPasswordIsHashed(String hashedPassword) {
        String prefix = "$2";
        return hashedPassword.startsWith(prefix);
    }

}
