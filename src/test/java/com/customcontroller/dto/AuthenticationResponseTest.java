package com.customcontroller.dto;

import com.customcontroller.entity.ROLE;
import com.customcontroller.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationResponseTest {

    User user = new User("Mary", "pie", ROLE.CUSTOMER, "mary@gmail.com","password");

    @Test
    void testGetAndSetUser() {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setUser(user);
        assertEquals(user, authenticationResponse.getUser());
    }

    @Test
    void testGetAndSetToken() {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        String token = "Token";
        authenticationResponse.setToken(token);
        assertEquals(token, authenticationResponse.getToken());
    }

}
