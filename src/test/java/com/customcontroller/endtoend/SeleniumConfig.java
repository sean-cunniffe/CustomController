package com.customcontroller.endtoend;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by SeanCunniffe on 11/Mar/2022
 */

public class SeleniumConfig {

    public static WebDriver driver;
    private static String baseUrl;

    static{
        Properties properties = getProperties();
        System.setProperty("webdriver.chrome.driver", properties.getProperty("selenium.driver.location"));
        baseUrl = properties.getProperty("base.url");
        driver = new ChromeDriver();
    }

    public SeleniumConfig() {
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("selenium.properties");
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }


}
