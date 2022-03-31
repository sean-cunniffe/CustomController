package com.customcontroller.endtoend.steps.login;

import com.customcontroller.endtoend.SeleniumConfig;
import com.customcontroller.entity.ROLE;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by SeanCunniffe on 30/Mar/2022
 */

public class CreateOrderStepDef {

    private final WebDriver driver;

    public CreateOrderStepDef() {
        SeleniumConfig seleniumConfig = new SeleniumConfig();
        driver = seleniumConfig.getDriver();
    }

    @Before
    public void clearLocalStorage(){
        driver.get("http://localhost:8080/custom-controllers");
        ((WebStorage)driver).getLocalStorage().clear();
    }

    @Given("the customer has logged in")
    public void theCustomerHasLoggedIn() {
        driver.get("http://localhost:8080/custom-controllers/#/login");
        driver.manage().timeouts().implicitlyWait(Duration.of(4, ChronoUnit.SECONDS));
        WebElement element = driver.findElement(By.xpath("/html/body/app-root/div/main/app-login/div/div/div/div/div/div[2]/form/div[1]/input"));
        element.sendKeys("john.doe@gmail.com");
        element = driver.findElement(By.xpath("/html/body/app-root/div/main/app-login/div/div/div/div/div/div[2]/form/div[2]/input"));
        element.sendKeys("password");
        element = driver.findElement(By.xpath("/html/body/app-root/div/main/app-login/div/div/div/div/div/div[2]/form/div[3]/button"));
        element.click();
        driver.manage().timeouts().implicitlyWait(Duration.of(4, ChronoUnit.SECONDS));
    }

    @When("they click create order")
    public void theyClickCreateOrder() {
        driver.findElement(By.id("createOrderBtn")).click();
    }

    @Then("they are directed to the order page")
    public void theyAreDirectedToTheOrderPage() {
        boolean endsWith = driver.getCurrentUrl().endsWith("create-order");
        assertTrue(endsWith);
    }

    @Given("the customer is at the create order page")
    public void theCustomerIsAtTheCreateOrderPage() {
        driver.get("http://localhost:8080/custom-controllers");
        LocalStorage localStorage = ((WebStorage) driver).getLocalStorage();
        localStorage.setItem("accessToken", "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiQUNDRVNTIiwiYWxnIjoiSFMyNTYifQ.eyJST0xFIjoiQ1VTVE9NRVIiLCJpc3MiOiJKd3RVdGlsIiwiZXhwIjoxNjQ5MjM5ODQ3LCJpYXQiOjE2NDg2MzUwNDcsImVtYWlsIjoiam9obi5kb2VAZ21haWwuY29tIn0.obGbk5pFw4qEOODp_OMmnJ86ec5NnihYilALp5ZFa20");
        localStorage.setItem("refreshToken", "eyJ0eXAiOiJKV1QiLCJ0eXBlIjoiUkVGUkVTSCIsImFsZyI6IkhTMjU2In0.eyJST0xFIjoiQ1VTVE9NRVIiLCJpc3MiOiJKd3RVdGlsIiwiZXhwIjoxNjUzODE5MDQ3LCJpYXQiOjE2NDg2MzUwNDcsImVtYWlsIjoiam9obi5kb2VAZ21haWwuY29tIn0.crsWPXsTy1NiJS5jC4cjHZ-NfY5r2NIuHbQSNOSjxNc");
        localStorage.setItem("user", "{\"id\":2,\"email\":\"john.doe@gmail.com\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"role\":\"CUSTOMER\"}");
        localStorage.setItem("controllers", "[]");
        driver.get("http://localhost:8080/custom-controllers/#/create-order");
    }

    @When("they add a controller to their cart")
    public void theyAddAControllerToTheirCart() {
        driver.manage().timeouts().implicitlyWait(Duration.of(4, ChronoUnit.SECONDS));
        WebElement firstControllerOption = driver.findElements(By.className("btn-add-controller")).get(0);
        firstControllerOption.click();
    }

    @Then("the controller is added to the cart")
    public void theControllerIsAddedToTheCart() {
        WebElement webElement = driver.findElement(By.id("viewCartButton"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement until = wait.until(ExpectedConditions.visibilityOf(webElement));
        assertNotNull(until);

    }

    @And("they remove a controller from their cart")
    public void theyRemoveAControllerFromTheirCart() {
        driver.manage().timeouts().implicitlyWait(Duration.of(4, ChronoUnit.SECONDS));
        WebElement firstController = driver.findElements(By.className("btn-remove-controller")).get(0);
        firstController.click();
    }

    @Then("their cart is empty")
    public void theirCartIsEmpty() {
        WebElement webElement = driver.findElement(By.id("viewCartButton"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        Boolean invisible = wait.until(ExpectedConditions.invisibilityOf(webElement));
        assertTrue(invisible);
    }

    @And("the customer click view cart")
    public void theCustomerClickViewCart() {
        driver.findElement(By.id("viewCartButton")).click();
    }

    @And("the customer click Complete Order")
    public void theCustomerClickCompleteOrder() {
        driver.findElement(By.id("completeOrderBtn")).click();
    }

    @And("the customer input their payment details")
    public void theCustomerInputTheirPaymentDetails() {
        driver.findElement(By.id("cardOwnerInput")).sendKeys("John Doe");
        driver.findElement(By.id("cardNumberInput")).sendKeys("1234567899876543");
        driver.findElement(By.id("expireMonthInput")).sendKeys("01");
        driver.findElement(By.id("expireYearInput")).sendKeys("2024");
        driver.findElement(By.id("cvvInput")).sendKeys("123");

    }

    @And("the customer input their delivery Details")
    public void theCustomerInputTheirDeliveryDetails() {
        driver.findElement(By.id("nameInput")).sendKeys("John Doe");
        driver.findElement(By.id("inputAddress")).sendKeys("234 Marys St");
        driver.findElement(By.id("inputAddress2")).sendKeys("Killimor");
        driver.findElement(By.id("inputCity")).sendKeys("Ballinalsoe");
        driver.findElement(By.id("dropdownMenuButton")).click();
        WebElement dropDownMenu = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.id("countyDropDownMenu")));
        List<WebElement> el = dropDownMenu.findElements(By.className("dropdown-item"));
        el.get(0).click();
        driver.findElement(By.id("inputZip")).sendKeys("A12B345");
    }

    @And("the customer clicks Submit order")
    public void theCustomerClicksSubmitOrder() {
        driver.findElement(By.id("submitOrderBtn")).click();
    }

    @Then("the customer is shown the receipt for their order")
    public void theCustomerIsShownTheReceiptForTheirOrder() {
        Boolean receipt = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.urlContains("receipt"));
        assertTrue(receipt);
    }

    @Then("the customer is shown an error message for their delivery details")
    public void theCustomerIsShownAnErrorMessageForTheirDeliveryDetails() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
        WebElement elem1 = webDriverWait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(By.id("nameInputErrorMsg"), By.tagName("span")));
        assertNotNull(elem1);

    }

    @Then("the customer is shown an error message for the payment details")
    public void theCustomerIsShownAnErrorMessageForThePaymentDetails() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(4));
        WebElement elem1 = webDriverWait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(By.id("cardOwnerErrorMsg"), By.tagName("span")));
        assertNotNull(elem1);
    }

    @When("they navigate to the cart page")
    public void theyNavigateToTheCartPage() {
        driver.get("http://localhost:8080/custom-controllers/#/cart");
    }

    @Then("they cannot confirm order")
    public void theyCannotConfirmOrder() {
        int completeOrderBtnPresence = driver.findElements(By.id("completeOrderBtn")).size();
        assertEquals(0, completeOrderBtnPresence);
    }
}
