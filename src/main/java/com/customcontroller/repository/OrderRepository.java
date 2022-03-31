package com.customcontroller.repository;

import com.customcontroller.entity.order.Order;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by SeanCunniffe on 04/Mar/2022
 */
@Stateless
public class OrderRepository extends Repository<Order>{
    @Override
    public Class<Order> getEntityClass() {
        return Order.class;
    }

    public List<Order> getOrdersByUserEmail(String email) {
        return entityManager
                .createQuery("select order from Order order where order.user.email = :email", Order.class)
                .setParameter("email", email)
                .getResultList();
    }

    public Order getOrderById(int number){
        return entityManager.find(Order.class, number);
    }
}
