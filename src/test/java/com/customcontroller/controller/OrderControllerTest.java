package com.customcontroller.controller;

import com.customcontroller.entity.ROLE;
import com.customcontroller.entity.User;
import com.customcontroller.entity.order.Order;
import com.customcontroller.entity.order.PaymentDetails;
import com.customcontroller.entity.order.Shipping;
import com.customcontroller.entity.order.Status;
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
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class OrderControllerTest {

    @Inject
    OrderController orderController;

    @Inject
    UserRepository userRepository;

    @Before
    public void before() {
//        prepareData.setupDataFromFile();

    }


    @Test
    public void getAllTests() {
        Response response = orderController.getAllOrders();
        List<Order> orders = (List<Order>) response.getEntity();
        assertEquals(200, response.getStatus());
        assertTrue(orders.size() > 0);
    }

    @Test
    public void getOrderByEmail() {
        SecurityContext securityContext = new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> "john.doe@gmail.com";
            }

            @Override
            public boolean isUserInRole(String role) {
                return ROLE.valueOf(role).equals(ROLE.CUSTOMER);
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
        Response response = orderController.getOrdersForUser("john.doe@gmail.com", securityContext);
        List<Order> orders = (List<Order>) response.getEntity();
        assertEquals(200, response.getStatus());
        assertTrue(orders.size() > 0);
    }

    @Test
    public void createAndDeleteOrderTest() {
        User user = userRepository.getUserByEmail("john.doe@gmail.com");
        Order order = new Order();
        order.setUser(user);
        PaymentDetails paymentDetails = createPayment();
        order.setPaymentDetails(paymentDetails);
        Shipping shipping = createShipping();
        order.setShipping(shipping);
        Response response = orderController.createOrder(order);
        Order newOrder = (Order) response.getEntity();
        assertNotNull(newOrder.getId());
        SecurityContext securityContext = new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> "john.doe@gmail.com";
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
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
        Response deleteOrder = orderController.deleteOrder(order.getId(), securityContext);
        assertEquals(200, deleteOrder.getStatus());
    }

    @Test
    public void testTryDeleteOrderUnAuth() {

        SecurityContext securityContext = new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> "not.email@gmail.com";
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
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
        EJBException ejbException = assertThrows(EJBException.class, () -> orderController.deleteOrder(1, securityContext));
        WebApplicationException wae = (WebApplicationException) ejbException.getCausedByException();
        assertTrue(wae.getMessage().startsWith("Order does not belong to email "));
    }


    private Shipping createShipping() {
        Shipping shipping = new Shipping();
        shipping.setStreet("Main St");
        shipping.setStreet2("Killimor Rd");
        shipping.setCity("Ballinalsoe");
        shipping.setCounty("Galway");
        shipping.setCountry("Ireland");
        shipping.setZip("ABCD123");
        shipping.setDeliverTo("John Doe");
        return shipping;
    }

    private PaymentDetails createPayment() {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setNameOnCard("John Doe");
        paymentDetails.setCardNumber("1234567891011121");
        paymentDetails.setCvv("123");
        paymentDetails.setExpiryDate("01/2023");
        return paymentDetails;
    }

    @Test
    public void updateOrderStatus() {
        Response response = orderController.updateStatus(1, Status.COMPLETE);
        Order entity = (Order) response.getEntity();
        assertEquals(Status.COMPLETE, entity.getStatus());
    }


}
