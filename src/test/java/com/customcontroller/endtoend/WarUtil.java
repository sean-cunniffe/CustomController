package com.customcontroller.endtoend;

import com.customcontroller.MyApp;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Created by SeanCunniffe on 31/Mar/2022
 */

public class WarUtil {

    static Process serverProcess;

    public static File createWar(){
        WebArchive war = ShrinkWrap.create(WebArchive.class, "custom-controllers")
                .addPackages(true,
                        "com/customcontroller/controller",
                        "com/customcontroller/dto",
                        "com/customcontroller/entity",
                        "com/customcontroller/exceptions",
                        "com/customcontroller/repository",
                        "com/customcontroller/services")
                .addClass(MyApp.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsResource("META-INF/populate-tables-data.sql", "META-INF/populate-tables-data.sql")
                .addAsResource("META-INF/create-DB-test.sql", "META-INF/create-DB-test.sql")
                .addAsResource("META-INF/drop-DB-test.sql", "META-INF/drop-DB-test.sql")
                .addAsResource("META-INF/testing-persistence.xml", "META-INF/persistence.xml")
                .addAsLibraries(
                        Maven.resolver()
                                .loadPomFromFile("pom.xml")
                                .resolve("com.auth0:java-jwt:3.18.2",
                                        "at.favre.lib:bcrypt:0.9.0",
                                        "io.cucumber:cucumber-java:7.2.3")
                                .withTransitivity().asFile());
        String webapp="src/main/webApp";

        war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class)
                .importDirectory(webapp).as(GenericArchive.class), "/", Filters.includeAll());
        File target = new File("custom-controllers.war");
        war.as(ZipExporter.class).exportTo(target, true);
        System.out.println(war.toString(true));
        return target;
    }

    /**
     * Returns the process the server is running on
     * @param warFile
     * @return
     */
    public static Process deployWar(File warFile) {
        try {
            serverProcess = Runtime.getRuntime().exec("cmd /c start C:\\wildfly-13.0.0.Final\\bin\\standalone.bat");
            Object o = new Object();
            synchronized (o){
                o.wait(15000);
            }
            Process exec = Runtime.getRuntime().exec(
                    "cmd /c start C:\\wildfly-13.0.0.Final\\bin\\jboss-cli.bat --connect --command=\"deploy --force " + warFile.getAbsolutePath() + "\"");
            synchronized (o){
                o.wait(15000);
            }
            closeProcess(exec);
        }catch(IOException | InterruptedException e){
            e.printStackTrace();

        }
        return serverProcess;
    }

    private static void closeProcess(Process exec) {
        try {
            exec.getOutputStream().flush();
            exec.getOutputStream().close();
            exec.getInputStream().close();
            exec.getErrorStream().close();
            exec.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void undeploy(String warName) {
        try {
            Object o = new Object();
            synchronized (o) {
                o.wait(15000);
            }
            Process exec = Runtime.getRuntime().exec(
                    "cmd /c start C:\\wildfly-13.0.0.Final\\bin\\jboss-cli.bat --connect --command=\"undeploy " + warName + "\"");
            synchronized (o) {
                o.wait(15000);
            }
            closeProcess(exec);
        }catch(IOException | InterruptedException ignored){}
    }

    public static void terminateServer(){
        try {
            Process exec = Runtime.getRuntime().exec(
                    "cmd /c start C:\\wildfly-13.0.0.Final\\bin\\jboss-cli.bat --connect --command=shutdown");
            Object o = new Object();
            synchronized (o){
                o.wait(15000);
            }
            closeProcess(exec);
            closeProcess(serverProcess);
            Runtime.getRuntime().exec("taskkill /f /IM cmd.exe");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
