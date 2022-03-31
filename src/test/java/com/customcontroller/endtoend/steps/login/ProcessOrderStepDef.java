package com.customcontroller.endtoend.steps.login;

import com.customcontroller.endtoend.SeleniumConfig;
import com.customcontroller.entity.ROLE;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Created by SeanCunniffe on 30/Mar/2022
 */

public class ProcessOrderStepDef {

    private final WebDriver driver;


//    @ParameterType("staff|customer")
//    public ROLE role(String role) {
//        role = role.toUpperCase();
//        return ROLE.valueOf(role);
//    }

    @Before
    public void clearLocalStorage(){
        driver.get("http://localhost:8080/custom-controllers");
        ((WebStorage)driver).getLocalStorage().clear();
    }

    public ProcessOrderStepDef() {
        SeleniumConfig seleniumConfig = new SeleniumConfig();
        driver = seleniumConfig.getDriver();
    }

    @Given("the {role} is at the dashboard")
    public void theRoleIsAtTheDashboard(ROLE role){
        driver.manage().timeouts().implicitlyWait(Duration.of(4, ChronoUnit.SECONDS));
        driver.get("http://localhost:8080/custom-controllers");
        LocalStorage localStorage = ((WebStorage) driver).getLocalStorage();
        String refreshToken = null;
        String accessToken = null;
        String user = null;
        if(role.equals(ROLE.STAFF)){
            refreshToken = "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiUkVGUkVTSCIsImFsZyI6IkhTMjU2In0.eyJST0xFIjoiU1RBRkYiLCJpc3MiOiJKd3RVdGlsIiwiZXhwIjoxNjUzODQyODEzLCJpYXQiOjE2NDg2NTg4MTMsImVtYWlsIjoiam9obm1hcnRpbkBnbWFpbC5jb20ifQ.1euegkZY-Q6AiP45HiuZdEyCdygAO1yiropzhvRYLOc";
            accessToken = "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiQUNDRVNTIiwiYWxnIjoiSFMyNTYifQ.eyJST0xFIjoiU1RBRkYiLCJpc3MiOiJKd3RVdGlsIiwiZXhwIjoxNjQ5MjYzNjEzLCJpYXQiOjE2NDg2NTg4MTMsImVtYWlsIjoiam9obm1hcnRpbkBnbWFpbC5jb20ifQ.k4IC7DOrBmLwVHbhB4hesjlNct2s-xo6ZVo82NLHHlc";
            user = "{\"id\":3,\"email\":\"johnmartin@gmail.com\",\"firstName\":\"john\",\"lastName\":\"Martin\",\"role\":\"STAFF\"}";
        }else{
            refreshToken = "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiUkVGUkVTSCIsImFsZyI6IkhTMjU2In0.eyJST0xFIjoiQ1VTVE9NRVIiLCJpc3MiOiJKd3RVdGlsIiwiZXhwIjoxNjUzODE5MDQ3LCJpYXQiOjE2NDg2MzUwNDcsImVtYWlsIjoiam9obi5kb2VAZ21haWwuY29tIn0.crsWPXsTy1NiJS5jC4cjHZ-NfY5r2NIuHbQSNOSjxNc";
            accessToken = "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiQUNDRVNTIiwiYWxnIjoiSFMyNTYifQ.eyJST0xFIjoiQ1VTVE9NRVIiLCJpc3MiOiJKd3RVdGlsIiwiZXhwIjoxNjQ5MjM5ODQ3LCJpYXQiOjE2NDg2MzUwNDcsImVtYWlsIjoiam9obi5kb2VAZ21haWwuY29tIn0.obGbk5pFw4qEOODp_OMmnJ86ec5NnihYilALp5ZFa20";
            user = "{\"id\":2,\"email\":\"john.doe@gmail.com\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"role\":\"CUSTOMER\"}";
        }

        localStorage.setItem("refreshToken", refreshToken);
        localStorage.setItem("accessToken", accessToken);
        localStorage.setItem("user", user);

        driver.navigate().refresh();
        driver.get("http://localhost:8080/custom-controllers/#/dashboard");
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.urlContains("dashboard"));
    }

    @Then("they are shown the pending orders")
    public void theyAreShownThePendingOrders() {
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.className("cancel-order-btn")));
        assertNotNull(webElement);
    }

    @When("they click to change order status")
    public void theyClickToChangeOrderStatus() {
        driver.findElements(By.id("dropdownMenuButton")).get(0).click();
    }

    @Then("they can change the order status")
    public void theyCanChangeTheOrderStatus() {
        WebElement until = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.className("status-dropdown-menu")));
        assertNotNull(until);
    }

    @Then("show the orders in order of processing status")
    public void showTheOrdersInOrderOfProcessingStatus() {
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("dropdownMenuButton"))).get(0).click();
        WebElement dropdownMenu = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.className("status-dropdown-menu")));
        List<String> statusList = dropdownMenu.findElements(By.tagName("a")).stream().map(WebElement::getText).collect(Collectors.toList());
        assertTrue(statusList.size() > 0);
    }

    @Then("show an option to delete an order")
    public void showAnOptionToDeleteAnOrder() {
        WebElement webElement = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.className("cancel-order-btn")));
        assertNotNull(webElement);

    }

    @When("{role} click cancel order")
    public void theyClickCancelOrder(ROLE role) {
        String className = "cancel-order-btn";
        WebElement cancelOrderBtn = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.className(className)));
        cancelOrderBtn.click();

    }

    @Then("{role} click confirm and the order is deleted")
    public void theyClickConfirm(ROLE role) {
        int orderAmt = driver.findElements(By.className("cancel-order-btn")).size();
        WebElement confirmCancelBtn = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.className("confirm-cancel-order-btn")));
        confirmCancelBtn.click();
        new WebDriverWait(driver,Duration.ofSeconds(5)).until(webDriver -> driver.findElements(By.className("cancel-order-btn")).size() < orderAmt);
        int newOrderAmt = driver.findElements(By.className("cancel-order-btn")).size();
        assertEquals(orderAmt-1,newOrderAmt);
    }

    @Then("{role} click cancel order and order isn't deleted")
    public void theyClickCancelOrderAndOrderIsnTDeleted(ROLE role) {
        int orderAmt = driver.findElements(By.className("cancel-order-btn")).size();
        WebElement cancelOrderBtn = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.className("cancel-order-btn")));
        cancelOrderBtn.click();
        int newOrderAmt = driver.findElements(By.className("cancel-order-btn")).size();
        assertEquals(orderAmt, newOrderAmt);
    }
}
