package com.customcontroller.exceptions;

/**
 * Created by SeanCunniffe on 07/Feb/2022
 */

public class PasswordEncryptException extends RuntimeException{

    public PasswordEncryptException(String message) {
        super(message);
    }
}
