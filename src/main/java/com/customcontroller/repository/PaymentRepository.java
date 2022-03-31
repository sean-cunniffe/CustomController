package com.customcontroller.repository;

import com.customcontroller.entity.order.PaymentDetails;

import javax.ejb.Stateless;

/**
 * Created by SeanCunniffe on 08/Mar/2022
 */
@Stateless
public class PaymentRepository extends Repository<PaymentDetails> {
    @Override
    public Class<PaymentDetails> getEntityClass() {
        return PaymentDetails.class;
    }
}
