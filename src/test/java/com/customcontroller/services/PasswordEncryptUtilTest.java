package com.customcontroller.services;

import com.customcontroller.exceptions.PasswordEncryptException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncryptUtilTest {

    PasswordEncryptUtil passwordEncryptUtil;

    @BeforeEach
    void init() {
        passwordEncryptUtil = new PasswordEncryptUtil();
    }

    @Test
    void testCreateAndVerifySuccessfully() {
        String hashPassword = passwordEncryptUtil.hashPassword("password");
        assertTrue(hashPassword.startsWith("$2a"));
        boolean verifyPassword = passwordEncryptUtil.verifyPassword("password", hashPassword);
        assertTrue(verifyPassword);
    }

    @Test
    void testVerifyDifferentHashForSamePassword() {
        String password = "password";
        String hashPassword1 = passwordEncryptUtil.hashPassword(password);
        String hashPassword2 = passwordEncryptUtil.hashPassword(password);
        assertNotEquals(hashPassword2, hashPassword1);
    }

    @Test
    void testExceptionThrowWhenUsingHashedPasswordAsPassword() {
        // hash for password with 12 rounds
        String password = "$2a$12$H83g8ZFGM2ZHR0u70IsLcOOtiCuh/p9UYa5dF6n62GP/gFb6b6jSO";
        Throwable t = assertThrows(PasswordEncryptException.class, () -> passwordEncryptUtil.hashPassword(password));
        assertEquals("Password is already hashed",t.getMessage());
    }

}
