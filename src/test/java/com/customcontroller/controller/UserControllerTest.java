package com.customcontroller.controller;

import com.customcontroller.entity.ROLE;
import com.customcontroller.entity.User;
import com.customcontroller.repository.UserRepository;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import java.security.Principal;

import static org.junit.Assert.*;

/**
 * Created by SeanCunniffe on 28/Mar/2022
 */
@RunWith(Arquillian.class)
public class UserControllerTest {

    @Inject
    UserController userC;

    @Inject
    UserRepository userRepository;

    User user;
    private String email;
    private final SecurityContext adminSecurityContext = new SecurityContext() {
        @Override
        public Principal getUserPrincipal() {
            return ()->"";
        }

        @Override
        public boolean isUserInRole(String role) {
            return role.equals(ROLE.STAFF.name());
        }

        @Override
        public boolean isSecure() {
            return true;
        }

        @Override
        public String getAuthenticationScheme() {
            return null;
        }
    };

    @Before
    public void init(){
        email = "test345@gmail.com";
        user = userRepository.getUserByEmail(email);
        if(user == null) {
            user = new User("firstTest", "lastTest", ROLE.CUSTOMER, "test345@gmail.com", "password1234");
            user = userRepository.add(user);
        }
    }

    @Test
    public void testGetUserByEmailAsStaff(){
        Response response = userC.getUserByEmail(email, adminSecurityContext);
        User user = (User) response.getEntity();
        assertEquals(200, response.getStatus());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testGetUserByEmailThatDoesntExist(){
        EJBException ejbException = assertThrows(EJBException.class, () -> userC.getUserByEmail("temp@gmail.com", adminSecurityContext));
        WebApplicationException e = (WebApplicationException) ejbException.getCausedByException();
        assertEquals("User not found", e.getMessage());
    }

    @Test
    public void testGetUserByEmailAsTheUser(){
        SecurityContext securityContext = new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return ()->email;
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        };
        Response response = userC.getUserByEmail(email, securityContext);
        User user = (User) response.getEntity();
        assertEquals(200, response.getStatus());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testGetUserUnAuth(){
        SecurityContext securityContext = new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return ()->"NOTTHEEMAIL@gmail.com";
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        };
        EJBException ejbException = assertThrows(EJBException.class, () -> userC.getUserByEmail(email, securityContext));
        WebApplicationException e = (WebApplicationException) ejbException.getCausedByException();
        assertEquals(403, e.getResponse().getStatus());
    }

    @Test
    public void testCreateUser() {
        User user = new User("firstTest", "lastTest", ROLE.CUSTOMER, "testtemp@gmail.com", "password1234");
        Response response = userC.createAccount(user, null);
        user = (User) response.getEntity();
        assertNotNull(user.getId());
    }

    @Test
    public void testCreateExistingUser(){
        User user = new User("firstTest", "lastTest", ROLE.CUSTOMER, "test345@gmail.com", "password1234");
        EJBException ejbException = assertThrows(EJBException.class, () -> userC.createAccount(user, null));
        WebApplicationException e = (WebApplicationException) ejbException.getCausedByException();
        assertEquals("User already exists", e.getMessage());
        assertEquals(Response.Status.CONFLICT.getStatusCode(), e.getResponse().getStatus());
    }

    @Test
    public void testCreateUserAsAdmin(){
        User user = new User("firstTest", "lastTest", ROLE.STAFF, "testtemp2@gmail.com", "password1234");
        Response response = userC.createAccount(user, adminSecurityContext);
        user = (User) response.getEntity();
        assertNotNull(user.getId());
        assertEquals(201, response.getStatus());
    }
}
