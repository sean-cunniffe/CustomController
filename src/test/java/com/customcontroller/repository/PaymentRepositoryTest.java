package com.customcontroller.repository;

import com.customcontroller.entity.order.PaymentDetails;
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


/**
 * Created by SeanCunniffe on 31/Mar/2022
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentRepositoryTest {

    PaymentRepository paymentRepository;
    EntityManagerFactory emf;
    private EntityManager entityManager;
    DBCommandTransactionalExecutor db;

    @BeforeAll
    void beforeAll() {
        emf = Persistence.createEntityManagerFactory("testPuResourceLocal");
        entityManager = emf.createEntityManager();
        db = new DBCommandTransactionalExecutor(entityManager);
        paymentRepository = new PaymentRepository();
        paymentRepository.setEntityManager(entityManager);
    }

    @Test
    void findAllFromTableTest(){
        List<PaymentDetails> all = paymentRepository.findAll();
        System.out.println(all.size());
        assertTrue(all.size() > 0);
    }

    @Test
    void savePaymentTest(){
        PaymentDetails paymentDetails = getPaymentDetails("123", "02/2023", "Sean Cunniffe", "1234567891234567", 1);
        PaymentDetails paymentDetails1 = db.executeCommand(() -> paymentRepository.add(paymentDetails));
        assertNotNull(paymentDetails1.getId());
    }

    @ParameterizedTest
    @MethodSource("provideNullValues")
    void savePaymentWithNullValueTest(String cvv, String expireDate, String nameOnCard, String carNumber, Integer userId){
        PaymentDetails paymentDetails = getPaymentDetails(cvv, expireDate, nameOnCard, carNumber, userId);
        assertThrows(IllegalStateException.class, ()-> db.executeCommand(() -> paymentRepository.add(paymentDetails)));
    }

    public static Stream<Arguments> provideNullValues(){
        return Stream.of(
                Arguments.of(null, "02/2023", "Sean Cunniffe", "1234567891234567", 1),
                Arguments.of("123", null, "Sean Cunniffe", "1234567891234567", 1),
                Arguments.of("123", "02/2023", null, "1234567891234567", 1),
                Arguments.of("123", "02/2023", "Sean Cunniffe", null, 1),
                Arguments.of("123", "02/2023", "Sean Cunniffe", "1234567891234567", null)
        );
    }

    private PaymentDetails getPaymentDetails(String cvv, String expireDate, String nameOnCard, String carNumber, Integer userId) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setCardNumber(carNumber);
        paymentDetails.setCvv(cvv);
        paymentDetails.setExpiryDate(expireDate);
        paymentDetails.setNameOnCard(nameOnCard);
        paymentDetails.setUserId(userId);
        return paymentDetails;
    }

}
