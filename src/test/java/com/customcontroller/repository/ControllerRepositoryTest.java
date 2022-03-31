package com.customcontroller.repository;

import com.customcontroller.entity.order.Controller;
import com.customcontroller.entity.order.ControllerType;
import com.customcontroller.testutil.DBCommandTransactionalExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by SeanCunniffe on 08/Mar/2022
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControllerRepositoryTest {

    ControllerRepository controllerRepository;
    EntityManagerFactory emf;
    DBCommandTransactionalExecutor executor;

    @BeforeAll
    void beforeAll() {
        emf = Persistence.createEntityManagerFactory("testPuResourceLocal");
        EntityManager entityManager = emf.createEntityManager();
        controllerRepository = new ControllerRepository();
        controllerRepository.setEntityManager(entityManager);
//        PrepareData.readSetupScript(entityManager);
        executor = new DBCommandTransactionalExecutor(entityManager);

    }


    @Test
    public void testRandomControllers(){
        List<Controller> randomControllers = controllerRepository.getRandomControllers(5);
        assertEquals(5, randomControllers.size());
    }

    @Test
    public void getAllControllerTypes(){
        List<ControllerType> controllerTypes = controllerRepository.getAllControllerTypes();
        assertEquals(4, controllerTypes.size());
    }
}
