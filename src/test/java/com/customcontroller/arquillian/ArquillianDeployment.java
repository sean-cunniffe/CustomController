package com.customcontroller.arquillian;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

/**
 * Created by SeanCunniffe on 11/Mar/2022
 */
@ArquillianSuiteDeployment
public class ArquillianDeployment {

    @org.jboss.arquillian.container.test.api.Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "com/customcontroller")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsResource("META-INF/testing-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/populate-tables-data.sql", "META-INF/populate-tables-data.sql")
                .addAsResource("META-INF/create-DB-test.sql", "META-INF/create-DB-test.sql")
                .addAsResource("META-INF/drop-DB-test.sql", "META-INF/drop-DB-test.sql")
                .addAsLibraries(
                        Maven.resolver()
                                .loadPomFromFile("pom.xml")
                                .resolve("com.auth0:java-jwt:3.18.2",
                                        "at.favre.lib:bcrypt:0.9.0",
                                        "mysql:mysql-connector-java:8.0.28",
                                        "io.cucumber:cucumber-java:7.2.3")
                                .withTransitivity().asFile());
        System.out.println(war.toString(true));
        return war;
    }
}
