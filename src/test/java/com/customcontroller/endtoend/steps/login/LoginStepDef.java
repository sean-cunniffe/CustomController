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
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.*;

/**
 * Created by SeanCunniffe on 29/Mar/2022
 */

public class LoginStepDef {


    @ParameterType("staff|customer")
    public ROLE role(String role) {
        role = role.toUpperCase();
        return ROLE.valueOf(role);
    }

    @Before
    public void clearLocalStorage(){
        driver.get("http://localhost:8080/custom-controllers");
        ((WebStorage)driver).getLocalStorage().clear();
    }

    private final WebDriver driver;

    public LoginStepDef() {
        SeleniumConfig seleniumConfig = new SeleniumConfig();
        driver = seleniumConfig.getDriver();
    }

    @When("{role} enter correct username")
    public void theyEnterCorrectUsername(ROLE role) {
        WebElement element = driver.findElement(By.xpath("/html/body/app-root/div/main/app-login/div/div/div/div/div/div[2]/form/div[1]/input"));
        if (role.equals(ROLE.CUSTOMER))
            element.sendKeys("john.doe@gmail.com");
        else
            element.sendKeys("johnmartin@gmail.com");
    }

    @When("{role} enter incorrect username")
    public void enterIncorrectUsername(ROLE role) {
        WebElement element = driver.findElement(By.xpath("/html/body/app-root/div/main/app-login/div/div/div/div/div/div[2]/form/div[1]/input"));
        element.sendKeys("notauser@gmail.com");
    }

    @And("{role} enter correct password")
    public void theyEnterCorrectPassword(ROLE role) {
        String password = "password";
        enterPassword(password);
    }

    private void enterPassword(String password) {
        WebElement element = driver.findElement(By.xpath("/html/body/app-root/div/main/app-login/div/div/div/div/div/div[2]/form/div[2]/input"));
        element.sendKeys(password);
    }

    @And("they click login")
    public void theyClickLogin() {
        WebElement element = driver.findElement(By.xpath("/html/body/app-root/div/main/app-login/div/div/div/div/div/div[2]/form/div[3]/button"));
        element.click();
    }

    @Then("{role} are navigated to the {role} dashboard")
    public void theyAreNavigatedToTheAdminDashboard(ROLE role, ROLE dashboardRole) {
        driver.manage().timeouts().implicitlyWait(Duration.of(4, ChronoUnit.SECONDS));
        WebElement element;
        if (role.equals(ROLE.CUSTOMER)) {
            element = driver.findElement(By.xpath("/html/body/app-root/div/main/app-dashboard/div/h2/span"));
        } else {
            element = driver.findElement(By.xpath("/html/body/app-root/div/main/app-dashboard/div/div[1]/h2/span"));
        }
        assertNotNull(element);
    }

    @And("{role} enter incorrect password")
    public void theyEnterIncorrectPassword(ROLE role) {
        enterPassword("NOTTHEPASSWORD");
    }

    @Given("{role} navigate to the login page")
    public void theyNavigateToTheLoginPage(ROLE role) {
        driver.get("http://localhost:8080/custom-controllers/#/login");
        driver.manage().timeouts().implicitlyWait(Duration.of(4, ChronoUnit.SECONDS));
    }

    @Then("{role} is shown an error message")
    public void isShownAnErrorMessage(ROLE role) {
        WebElement webElement = driver.findElement(By.className("alert"));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement until = wait.until(ExpectedConditions.visibilityOf(webElement));
        assertNotNull(until);
    }

    @When("{role} successfully logs in")
    public void customerSuccessfullyLogsIn(ROLE role) {
        theyNavigateToTheLoginPage(role);
        theyEnterCorrectUsername(role);
        theyEnterCorrectPassword(role);
        theyClickLogin();
    }


    @And("they click logout")
    public void theyClickLogout() {
        WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/app-root/div/div/nav/li/a")));
        elem.click();
        WebElement elem1 = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/app-root/div/div/nav/li/div/a")));
        elem1.click();
    }

    @Then("navigate back to the login page")
    public void navigateBackToTheLoginPage() {
        boolean login = driver.getCurrentUrl().endsWith("login");
        assertTrue(login);
    }
}
