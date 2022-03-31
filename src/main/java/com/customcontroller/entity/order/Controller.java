package com.customcontroller.entity.order;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by SeanCunniffe on 04/Mar/2022
 */

@Entity
@Table(name = "controller")
public class Controller {

    @Id
    @Column(name = "controller_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "controller_type")
    ControllerType type;

    @Column
    String pattern;

    @Column
    String color;

    @Column(name = "img_url")
    String imgUrl;

    @Column
    Double price;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "order_controllers",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "controller_id"))
    List<Order> orders;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ControllerType getType() {
        return type;
    }

    public void setType(ControllerType type) {
        this.type = type;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Controller{" +
                "id=" + id +
                ", type=" + type +
                ", pattern='" + pattern + '\'' +
                ", color='" + color + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", price=" + price + '\'' +
                '}';
    }
}
