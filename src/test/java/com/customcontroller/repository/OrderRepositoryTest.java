package com.customcontroller.repository;

import com.customcontroller.entity.order.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderRepositoryTest {

    OrderRepository orderRepository;
    EntityManagerFactory emf;
    private EntityManager entityManager;

    @BeforeAll
    void beforeAll() {
        emf = Persistence.createEntityManagerFactory("testPuResourceLocal");
        entityManager = emf.createEntityManager();
//        PrepareData.readSetupScript(entityManager);
        orderRepository = new OrderRepository();
        orderRepository.setEntityManager(entityManager);
    }

    @Test
    public void findAllTest(){
        List<Order> all = orderRepository.findAll();
        assertTrue(all.size() > 0);
    }

    @Test
    public void findOrderByEmail(){
        List<Order> ordersByUserEmail = orderRepository.getOrdersByUserEmail("john.doe@gmail.com");
        assertTrue(ordersByUserEmail.size() > 0);
    }

    @Test
    public void findOrderWithEmailThatHasNoOrders(){
        List<Order> ordersByUserEmail = orderRepository.getOrdersByUserEmail("notanemail@gmail.com");
        assertEquals(0, ordersByUserEmail.size());
    }

    @Test
    public void findOrderById(){
        Order orderById = orderRepository.getOrderById(1);
        assertNotNull(orderById);
    }

    @Test
    public void findOrderByIdThatDoesNotExist(){
        Order orderById = orderRepository.getOrderById(123154);
        assertNull(orderById);
    }


}
