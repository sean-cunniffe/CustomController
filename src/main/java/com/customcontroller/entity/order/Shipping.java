package com.customcontroller.entity.order;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by SeanCunniffe on 04/Mar/2022
 */

@Entity
@Table(name = "shipping_details")
public class Shipping {

    @Id
    @Column(name = "shipping_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer shippingId;

    @Column(name = "deliver_to")
    @NotNull
    private String deliverTo;

    @Column
    @NotNull
    private String street;
    @Column
    @NotNull
    private String street2;

    @Column
    @NotNull
    private String city;

    @Column
    @NotNull
    private String county;

    @Column
    @NotNull
    private String country;
    @Column
    @NotNull
    private String zip;

    @Column(name = "user_id")
    @NotNull
    private Integer userId;


    public Integer getShippingId() {
        return shippingId;
    }

    public void setShippingId(Integer shippingId) {
        this.shippingId = shippingId;
    }

    public String getDeliverTo() {
        return deliverTo;
    }

    public void setDeliverTo(String deliverTo) {
        this.deliverTo = deliverTo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public void setUserId(Integer id) {
        this.userId = id;
    }
}
