package com.customcontroller.repository;

import com.customcontroller.entity.order.Shipping;
import com.customcontroller.testutil.DBCommandTransactionalExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ShipmentRepositoryTest {

    ShipmentRepository shipmentRepository;
    EntityManagerFactory emf;
    private EntityManager entityManager;
    DBCommandTransactionalExecutor db;

    @BeforeAll
    void beforeAll() {
        emf = Persistence.createEntityManagerFactory("testPuResourceLocal");
        entityManager = emf.createEntityManager();
        db = new DBCommandTransactionalExecutor(entityManager);
//        PrepareData.readSetupScript(entityManager);
        shipmentRepository = new ShipmentRepository();
        shipmentRepository.setEntityManager(entityManager);
    }

    @Test
    void findAllFromTableTest(){
        List<Shipping> all = shipmentRepository.findAll();
        assertTrue(all.size() > 0);
    }

    @Test
    void addEntityTest(){
        Shipping shippingE = createShipping("a34h345", "Ireland", "Galway City", "Mary Rd", "Killimor", "John Doe", "Galway");
        Shipping shipping = db.executeCommand(() -> shipmentRepository.add(shippingE));
        assertNotNull(shipping.getShippingId());
    }

    @ParameterizedTest
    @MethodSource("providerNullValues")
    void addEntityWithNullValueTest(String zip, String country, String city, String street, String street2, String deliverTo, String county){
        Shipping shipping = createShipping(zip, country, city, street, street2, deliverTo, county);
        assertThrows(IllegalStateException.class, () -> db.executeCommand(() -> shipmentRepository.add(shipping)));
    }

    private static Stream<Arguments> providerNullValues(){
        return Stream.of(
                Arguments.of(null, "Ireland", "Galway City", "Mary Rd", "Killimor", "John Doe", "Galway"),
                Arguments.of("a34h345", null, "Galway City", "Mary Rd", "Killimor", "John Doe", "Galway"),
                Arguments.of("a34h345", "Ireland", null, "Mary Rd", "Killimor", "John Doe", "Galway"),
                Arguments.of("a34h345", "Ireland", "Galway City",null, "Killimor", "John Doe", "Galway"),
                Arguments.of("a34h345", "Ireland", "Galway City","Mary Rd", null, "John Doe", "Galway"),
                Arguments.of("a34h345", "Ireland", "Galway City", "Mary Rd", "Killimor", null, "Galway"),
                Arguments.of("a34h345", "Ireland", "Galway City", "Mary Rd", "Killimor", "John Doe", null)
        );
    }


    private Shipping createShipping(String zip, String country, String city, String street, String street2, String deliverTo, String county){
        Shipping entity = new Shipping();
        entity.setZip(zip);
        entity.setCountry(country);
        entity.setCity(city);
        entity.setStreet(street);
        entity.setStreet2(street2);
        entity.setCounty(county);
        entity.setDeliverTo(deliverTo);
        return entity;
    }

}
