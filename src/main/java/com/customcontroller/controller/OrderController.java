package com.customcontroller.controller;

import com.customcontroller.entity.ROLE;
import com.customcontroller.entity.User;
import com.customcontroller.entity.order.*;
import com.customcontroller.repository.OrderRepository;
import com.customcontroller.repository.PaymentRepository;
import com.customcontroller.repository.ShipmentRepository;
import com.customcontroller.repository.UserRepository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by SeanCunniffe on 07/Mar/2022
 */

@Stateless
@LocalBean
@Path("/order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    OrderRepository orderRepository;
    @Inject
    PaymentRepository paymentRepository;
    @Inject
    ShipmentRepository shipmentRepository;
    @Inject
    UserRepository userRepository;


    @GET
    public Response getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return Response.status(200).entity(orders).build();
    }

    @GET
    @Path("/search")
    public Response getOrdersForUser(@QueryParam("email") String email, @Context SecurityContext securityContext) {
        boolean isAdmin = securityContext.isUserInRole(ROLE.STAFF.name());
        boolean isTokenEmailEqualRequestingEmail = securityContext.getUserPrincipal().getName().equals(email);
        if(!isTokenEmailEqualRequestingEmail && !isAdmin){
            throw new WebApplicationException("Not authorized", 401);
        }
        List<Order> orders = orderRepository.getOrdersByUserEmail(email);
        return Response.ok().entity(orders).build();
    }

    @POST
    public Response createOrder(Order order) {
        User user = userRepository.getUserByEmail(order.getUser().getEmail());
        order.setUser(user);
        PaymentDetails paymentDetails = order.getPaymentDetails();
        paymentDetails.setUserId(user.getId());
        paymentDetails = paymentRepository.add(paymentDetails);
        order.setPaymentDetails(paymentDetails);
        order.setDateOrdered(LocalDateTime.now());
        order.setStatus(Status.WAITING);
        Shipping shipping = order.getShipping();
        shipping.setUserId(user.getId());
        shipping = shipmentRepository.add(shipping);
        order.setShipping(shipping);

        Order newOrder = orderRepository.add(order);
        return Response.status(201).entity(newOrder).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateStatus(@PathParam("id") Integer orderNumber, @QueryParam("status") Status status) {
        Order order = orderRepository.getOrderById(orderNumber);
        order.setStatus(status);
        order = orderRepository.merge(order);
        return Response.ok().entity(order).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") Integer orderNumber, @Context SecurityContext securityContext) {
        String tokenEmail = securityContext.getUserPrincipal().getName();
        boolean isStaff = securityContext.isUserInRole(ROLE.STAFF.name());
        Order order = orderRepository.getOrderById(orderNumber);
        if (!order.getUser().getEmail().equals(tokenEmail) && !isStaff) {
            throw new WebApplicationException("Order does not belong to email " + tokenEmail, 401);
        }
        orderRepository.delete(order);
        return Response.ok().build();
    }
}
