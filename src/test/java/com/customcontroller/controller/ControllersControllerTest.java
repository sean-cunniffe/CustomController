package com.customcontroller.controller;

import com.customcontroller.entity.order.Controller;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Created by SeanCunniffe on 28/Mar/2022
 */

@RunWith(Arquillian.class)
public class ControllersControllerTest {

    @Inject
    ControllersController cc;

    @Test
    public void getPromoControllersTest() {
        Response response = cc.getPromoControllers(5);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void getPromoControllersWhereAmtIsGreaterThanExistingTest() {
        Response response = cc.getPromoControllers(Integer.MAX_VALUE);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void getPromoControllersWhereAmtIsZeroTest() {
        Response response = cc.getPromoControllers(0);
        assertEquals(200, response.getStatus());
        List<Controller> controllerList = (List<Controller>) response.getEntity();
        assertEquals(0, controllerList.size());
    }

    @Test
    public void getPromoControllersWhereAmtIsMinusOneTest() {
        ConstraintViolationException causeException =
                (ConstraintViolationException) assertThrows(EJBException.class, () -> cc.getPromoControllers(-1)).getCausedByException();
    }

    @Test
    public void getPromoControllersWhereAmtIsOneTest() {
        int amt = 1;
        Response response = cc.getPromoControllers(amt);
        assertEquals(200, response.getStatus());
        List<Controller> controllerList = (List<Controller>) response.getEntity();
        assertEquals(amt, controllerList.size());
    }

    @Test
    public void getAllControllerTypesTest() {
        Response response = cc.getAllControllerTypes();
        assertEquals(200, response.getStatus());
    }

    @Test
    public void getAllControllersTest() {
        Response response = cc.getAllControllers();
        assertEquals(200, response.getStatus());
    }
}
