package com.customcontroller.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testTwoUsersEqual(){
        User user = new User("John", "Doe", ROLE.CUSTOMER, "johndoe@gmail.com","password");
        User user2 = new User("John", "Doe", ROLE.CUSTOMER, "johndoe@gmail.com","password");
        assertEquals(user, user2);
    }

    @Test
    void testTwoUsersNotEqual(){
        User user = new User("Mary", "pie", ROLE.CUSTOMER, "mary@gmail.com","password");
        User user2 = new User("John", "Doe", ROLE.CUSTOMER, "johndoe@gmail.com","password");
        assertNotEquals(user, user2);
    }

    @Test
    void testTwoUsersHashCodesEqual(){
        User user = new User("John", "Doe", ROLE.CUSTOMER, "johndoe@gmail.com","password");
        User user2 = new User("John", "Doe", ROLE.CUSTOMER, "johndoe@gmail.com","password");
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void testTwoUsersHashCodesNotEqual(){
        User user = new User("Mary", "pie", ROLE.CUSTOMER, "mary@gmail.com","password");
        User user2 = new User("John", "Doe", ROLE.CUSTOMER, "johndoe@gmail.com","password");
        assertNotEquals(user.hashCode(), user2.hashCode());
    }

}
