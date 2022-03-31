package com.customcontroller.repository;

import com.customcontroller.entity.order.Controller;
import com.customcontroller.entity.order.ControllerType;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SeanCunniffe on 08/Mar/2022
 */
@Stateless
public class ControllerRepository extends Repository<Controller> {


    public List<Controller> getRandomControllers(int number) {
        List<Controller> controllers = new ArrayList<>();
        List<Controller> allControllers = entityManager.createQuery("select c from Controller c", Controller.class).getResultList();
        int count = allControllers.size();
        number = Math.min(number, count);
        for (int i = 0; i < number; i++) {
            int random = (int) Math.floor(Math.random() * (count - i));
            Controller controller = allControllers.get(random);
            controllers.add(controller);
            allControllers.remove(controller);

        }
        return controllers;
    }

    @Override
    public Class<Controller> getEntityClass() {
        return Controller.class;
    }

    public List<ControllerType> getAllControllerTypes() {
        return entityManager.createQuery("select distinct controller.type from Controller controller", ControllerType.class).getResultList();
    }
}
