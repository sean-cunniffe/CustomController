package com.customcontroller.entity.order;

import com.customcontroller.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by SeanCunniffe on 04/Mar/2022
 */

@Entity
@Table(name = "customer_order")
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "order_controllers",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "controller_id"))
    List<Controller> controllers;

    @Column(name = "date_ordered")
    LocalDateTime dateOrdered;

    @ManyToOne
    @JoinColumn(name = "shipping_id")
    Shipping shipping;

    @Enumerated(EnumType.STRING)
    Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id")
    PaymentDetails paymentDetails;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<Controller> getControllers() {
        return controllers;
    }

    public void setControllers(List<Controller> controllers) {
        this.controllers = controllers;
    }

    public LocalDateTime getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(LocalDateTime dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @JsonIgnore
    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    @JsonProperty
    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", controllers=" + controllers +
                ", dateOrdered=" + dateOrdered +
                ", shipping=" + shipping +
                ", status=" + status +
                ", paymentDetails=" + paymentDetails +
                '}';
    }
}
