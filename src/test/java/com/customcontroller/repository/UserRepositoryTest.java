package com.customcontroller.repository;

import com.customcontroller.entity.ROLE;
import com.customcontroller.entity.User;
import com.customcontroller.entity.factory.UserFactory;
import com.customcontroller.exceptions.AuthenticationException;
import com.customcontroller.services.PasswordEncryptUtil;
import com.customcontroller.testutil.DBCommandTransactionalExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    UserRepository userRepository;
    EntityManagerFactory emf;
    private EntityManager entityManager;
    DBCommandTransactionalExecutor db;

    @BeforeAll
    void beforeAll() {
        emf = Persistence.createEntityManagerFactory("testPuResourceLocal");
        entityManager = emf.createEntityManager();
        db = new DBCommandTransactionalExecutor(entityManager);
        userRepository = new UserRepository();
        userRepository.setEntityManager(entityManager);
        userRepository.setPasswordEncryptUtil(new PasswordEncryptUtil());
    }

    @Test
    public void findAllFromTable() {
        addUser(new User("John", "Doe", ROLE.STAFF, "findAllFromTable@gmail.com", "password"));
        List<User> all = userRepository.findAll();
        assertTrue(all.size() > 0);
    }

    @Test
    public void getUserByEmail() {
        String email = "johnDoe@gmail.com";
        User expectedUser = new User("John", "Doe", ROLE.STAFF, email, "password");
        expectedUser = addUser(expectedUser);
        User actualUser = userRepository.getUserByEmail(email);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testCreateUser() {
        UserFactory userFactory = new UserFactory();
        userFactory.setPasswordEncryptUtil(new PasswordEncryptUtil());
        User user = userFactory.createUser(
                "sean.cunniffe927@gmail.com",
                "Sean",
                "Cunniffe",
                "password",
                ROLE.CUSTOMER);
        User user1 = addUser(user);
        assertNotNull(user1.getId());
    }

    @ParameterizedTest
    @MethodSource("nullValueUsers")
    public void testCreateUserWithNullValues(String firstName, String lastName, ROLE role, String email, String password) {
        User user = new User(firstName, lastName, role, email, password);
        assertThrows(IllegalStateException.class, ()->db.executeCommand(()->userRepository.add(user)));
    }

    public static Stream<Arguments> nullValueUsers(){
        return Stream.of(
                Arguments.of(null, "cunniffe", ROLE.STAFF, "sean.cunniffe@gmail.com","password"),
                Arguments.of("sean", null, ROLE.STAFF, "sean.cunniffe@gmail.com","password"),
                Arguments.of("sean", "cunniffe", null, "sean.cunniffe@gmail.com","password"),
                Arguments.of("sean", "cunniffe", ROLE.STAFF, null,"password"),
                Arguments.of("sean", "cunniffe", ROLE.STAFF, "sean.cunniffe@gmail.com",null)
        );
    }

    private User addUser(User user) {
        entityManager.getTransaction().begin();
        user = userRepository.add(user);
        entityManager.getTransaction().commit();
        return user;
    }

    @Test
    public void testGetUserByEmailAndPassword() {
        User user = userRepository.getUserByEmailAndPassword("john.doe@gmail.com", "password");
        assertEquals("john.doe@gmail.com", user.getEmail());
    }

    @Test
    public void testGetUserByEmailAndPasswordNotExist() {
        assertThrows(AuthenticationException.class, () -> userRepository.getUserByEmailAndPassword("john@gmail.com", "password"));

    }

    @Test
    public void testGetUserByRole() {
        List<User> user = userRepository.getUsersByRole();
        assertTrue(user.size() > 0);
    }


}
