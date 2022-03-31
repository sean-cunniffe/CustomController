package com.customcontroller.repository;

import com.customcontroller.entity.order.Shipping;

import javax.ejb.Stateless;

/**
 * Created by SeanCunniffe on 08/Mar/2022
 */
@Stateless
public class ShipmentRepository extends Repository<Shipping>{

    @Override
    public Class<Shipping> getEntityClass() {
        return Shipping.class;
    }
}
