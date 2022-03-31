package com.customcontroller.dto;

import com.customcontroller.entity.User;

/**
 * Created by SeanCunniffe on 15/Feb/2022
 */

public class AuthenticationResponse {

    private User user;
    private String token;

    public AuthenticationResponse() {
        /*
        // Empty for jax-rs
         */
    }

    public AuthenticationResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
