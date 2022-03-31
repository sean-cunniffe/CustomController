package com.customcontroller.entity.factory;

import com.customcontroller.entity.ROLE;
import com.customcontroller.entity.User;
import com.customcontroller.services.PasswordEncryptUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserFactoryTest {

    UserFactory userFactory;
    PasswordEncryptUtil passwordEncryptUtil;

    @BeforeEach
    public void before(){
        userFactory = new UserFactory();
        passwordEncryptUtil = mock(PasswordEncryptUtil.class);
        userFactory.setPasswordEncryptUtil(passwordEncryptUtil);
    }

    @Test
    void createUser() {
        String hashedPassword = "hashedPassword";
        when(passwordEncryptUtil.hashPassword(anyString())).thenReturn(hashedPassword);
        String email = "Test@gmail.com";
        String firstName = "Sean";
        String lastName = "Cunniffe";
        String password = "Password";
        ROLE role = ROLE.CUSTOMER;
        User user = userFactory.createUser(email, firstName, lastName, password, role);
        assertEquals(hashedPassword, user.getPassword());
        assertEquals(email, user.getEmail());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(role, user.getRole());
    }

}
