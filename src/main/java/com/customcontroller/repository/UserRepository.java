package com.customcontroller.repository;

import com.customcontroller.entity.ROLE;
import com.customcontroller.entity.User;
import com.customcontroller.entity.factory.UserFactory;
import com.customcontroller.exceptions.AuthenticationException;
import com.customcontroller.services.PasswordEncryptUtil;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.List;


/**
 * Created by SeanCunniffe on 15/Feb/2022
 */
@Stateless
public class UserRepository extends Repository<User> {

    @Inject
    private UserFactory userFactory;

    @Inject
    private PasswordEncryptUtil passwordEncryptUtil;

    @PostConstruct
    public void init() {
        String adminEmail = "johnmartin@gmail.com";
        if (getUserByEmail(adminEmail) == null) {
            User user = userFactory.createUser(adminEmail, "john", "Martin", "password", ROLE.STAFF);
            entityManager.persist(user);
        }

    }

    public List<User> getUsersByRole() {
        return entityManager.createQuery("SELECT u from User u where u.role = :role", User.class)
                .setParameter("role", ROLE.STAFF)
                .getResultList();
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    public User getUserByEmail(String email) {
        try {
            return entityManager
                    .createQuery("SELECT u from User u where u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException ignore) {
        }
        return null;
    }

    public User getUserByEmailAndPassword(String email, String password) {
        User userByEmail = getUserByEmail(email);
        if (userByEmail == null)
            throw new AuthenticationException("User not found");

        boolean verifyPassword = passwordEncryptUtil.verifyPassword(password, userByEmail.getPassword());
        if (!verifyPassword)
            throw new AuthenticationException("User not found");
        else
            return userByEmail;
    }


    public void setPasswordEncryptUtil(PasswordEncryptUtil passwordEncryptUtil) {
        this.passwordEncryptUtil = passwordEncryptUtil;
    }

    public void setUserFactory(UserFactory userFactory) {
        this.userFactory = userFactory;
    }
}
