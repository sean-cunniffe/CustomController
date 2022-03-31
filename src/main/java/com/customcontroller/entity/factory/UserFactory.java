package com.customcontroller.entity.factory;

import com.customcontroller.entity.ROLE;
import com.customcontroller.entity.User;
import com.customcontroller.services.PasswordEncryptUtil;

import javax.inject.Inject;

/**
 * Created by SeanCunniffe on 15/Feb/2022
 */

public class UserFactory {

    @Inject
    PasswordEncryptUtil passwordEncryptUtil;

    public User createUser(String email, String firstname, String lastName, String password, ROLE role){
        String hashPassword = passwordEncryptUtil.hashPassword(password);
        return new User(firstname, lastName, role, email, hashPassword);
    }

    public void setPasswordEncryptUtil(PasswordEncryptUtil passwordEncryptUtil) {
        this.passwordEncryptUtil = passwordEncryptUtil;
    }
}
