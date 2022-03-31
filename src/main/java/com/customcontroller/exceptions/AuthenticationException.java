package com.customcontroller.exceptions;

import javax.ejb.ApplicationException;

/**
 * Created by SeanCunniffe on 15/Feb/2022
 */

@ApplicationException
public class AuthenticationException extends RuntimeException{

    public AuthenticationException(String message) {
        super(message);
    }
}
