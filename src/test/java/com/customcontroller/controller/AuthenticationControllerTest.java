package com.customcontroller.controller;

import com.customcontroller.dto.AuthenticationRequest;
import com.customcontroller.dto.AuthenticationResponse;
import com.customcontroller.entity.ROLE;
import com.customcontroller.entity.User;
import com.customcontroller.entity.factory.UserFactory;
import com.customcontroller.repository.UserRepository;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

/**
 * Created by SeanCunniffe on 09/Mar/2022
 */

@RunWith(Arquillian.class)
public class AuthenticationControllerTest{

    @Inject
    AuthenticationController ac;

    @Inject
    UserRepository userRepository;

    @Inject
    UserFactory uf;

    private User user;
    private String password123;

    @Before
    public void init(){
        String email = "test12345@gmail.com";
        user = userRepository.getUserByEmail(email);
        password123 = "password123";
        if(user == null) {
            user = uf.createUser(email, "TEST", "TSET", password123, ROLE.STAFF);
            user = userRepository.add(user);
        }
    }

    @Test
    public void testGetRefreshTokenThenAccess(){
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(user.getEmail());
        authenticationRequest.setPassword(password123);
        Response response = ac.getRefreshToken(authenticationRequest);
        AuthenticationResponse ar = (AuthenticationResponse) response.getEntity();
        assertEquals(201, response.getStatus());
        Response response1 = ac.getAccessToken(ar.getToken());
        assertEquals(201, response1.getStatus());
    }

    @Test
    public void testTryGetRefreshTokenWithWrongPassword(){
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(user.getEmail());
        authenticationRequest.setPassword("NOTTHEPASSWORD");
        EJBException ejbException = assertThrows(EJBException.class, () -> ac.getRefreshToken(authenticationRequest));
        WebApplicationException e = (WebApplicationException) ejbException.getCausedByException();
        assertTrue(e.getMessage().startsWith("User does not exist"));
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), e.getResponse().getStatus());
    }

    @Test
    public void testTryGetRefreshTokenWithWrongEmail(){
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("Nottheemail@gmail.com");
        authenticationRequest.setPassword(user.getPassword());
        EJBException ejbException = assertThrows(EJBException.class, () -> ac.getRefreshToken(authenticationRequest));
        WebApplicationException e = (WebApplicationException) ejbException.getCausedByException();
        assertTrue(e.getMessage().startsWith("User does not exist"));
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), e.getResponse().getStatus());
    }

    @Test
    public void testTryGetRefreshTokenWithBlankEmail(){
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("");
        authenticationRequest.setPassword(user.getPassword());
        EJBException ejbException = assertThrows(EJBException.class, () -> ac.getRefreshToken(authenticationRequest));
        ConstraintViolationException e = (ConstraintViolationException) ejbException.getCausedByException();

    }

    @Test
    public void testTryGetRefreshTokenWithBlankPassword(){
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(user.getEmail());
        authenticationRequest.setPassword("");
        EJBException ejbException = assertThrows(EJBException.class, () -> ac.getRefreshToken(authenticationRequest));
        ConstraintViolationException e = (ConstraintViolationException) ejbException.getCausedByException();

    }

    @Test
    public void testGetAccessTokenWithEmptyRefreshToken(){
        EJBException ejbException = assertThrows(EJBException.class, () -> ac.getAccessToken(""));
        WebApplicationException e = (WebApplicationException) ejbException.getCausedByException();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), e.getResponse().getStatus());
    }

}
