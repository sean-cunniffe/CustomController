package com.customcontroller.endtoend;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * Created by SeanCunniffe on 29/Mar/2022
 */

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/feature/", glue={"com.customcontroller.endtoend.steps"})
public class CucumberIntegrationTests {


    @BeforeClass
    public static void before(){
        File war = WarUtil.createWar();
        WarUtil.deployWar(war);
    }

    @AfterClass
    public static void afterAll(){
        SeleniumConfig.driver.close();
        WarUtil.undeploy("custom-controllers.war");
        WarUtil.terminateServer();
    }

}
