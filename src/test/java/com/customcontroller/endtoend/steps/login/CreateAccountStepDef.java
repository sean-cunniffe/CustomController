package com.customcontroller.endtoend.steps.login;

import com.customcontroller.MyApp;
import com.customcontroller.endtoend.SeleniumConfig;
import com.customcontroller.endtoend.WarUtil;
import com.customcontroller.entity.ROLE;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertNotNull;

/**
 * Created by SeanCunniffe on 29/Mar/2022
 */

public class CreateAccountStepDef {

    private final WebDriver driver;

    public CreateAccountStepDef() {
        SeleniumConfig seleniumConfig = new SeleniumConfig();
        driver = seleniumConfig.getDriver();
    }

    @Before
    public void clearLocalStorage(){
        driver.get("http://localhost:8080/custom-controllers");
        ((WebStorage)driver).getLocalStorage().clear();
    }

    @When("they navigate to the login page")
    public void theyNavigateToTheLoginPage(){
        driver.get("http://localhost:8080/custom-controllers/#/login");
        driver.manage().timeouts().implicitlyWait(Duration.of(4, ChronoUnit.SECONDS));
    }

    @Then("they have the option to create an account")
    public void theyHaveTheOptionToCreateAnAccount() {
        WebElement element = driver.findElement(By.xpath("/html/body/app-root/div/main/app-login/div/div/div/div/div/div[2]/form/p/a"));
        assertNotNull(element);
    }

    @Given("they navigate to the create account page")
    public void theyNavigateToTheCreateAccountPage() {
        driver.get("http://localhost:8080/custom-controllers/#/create-account");
        driver.manage().timeouts().implicitlyWait(Duration.of(4, ChronoUnit.SECONDS));
    }

    @When("they enter their email")
    public void theyEnterTheirEmail() {
        driver.findElement(By.id("emailInput")).sendKeys("Test123@gmail.com");
    }

    @When("they enter an already existing email")
    public void theyEnterAnAlreadyExistingEmail() {
        driver.findElement(By.id("emailInput")).sendKeys("johnmartin@gmail.com");
    }

    @And("they enter their first name")
    public void theyEnterTheirFirstName() {
        driver.findElement(By.id("firstName")).sendKeys("First Name");
    }

    @And("they enter their last name")
    public void theyEnterTheirLastName() {
        driver.findElement(By.id("lastNameInput")).sendKeys("Last Name");
    }

    @And("they enter their password")
    public void theyEnterTheirPassword() {
        driver.findElement(By.id("passwordInput")).sendKeys("password");
    }

    @And("they enter their confirm password")
    public void theyEnterTheirConfirmPassword() {
        driver.findElement(By.id("confirmPasswordInput")).sendKeys("password");
    }
    @And("they enter their confirm password that does not match password")
    public void theyEnterTheirConfirmPasswordThatDoesNotMatchPassword() {
        driver.findElement(By.id("confirmPasswordInput")).sendKeys("password123");
    }

    @And("they click create account")
    public void theyClickCreateAccount() {
        driver.findElement(By.id("createAccountBtn")).click();
    }

    @Then("they are redirected to the dashboard logged in")
    public void theyAreNavigatedToTheAdminDashboard() {
        driver.manage().timeouts().implicitlyWait(Duration.of(4, ChronoUnit.SECONDS));
        WebElement element = driver.findElement(By.xpath("/html/body/app-root/div/main/app-dashboard/div/h2/span"));
        assertNotNull(element);
    }

    @Then("they are shown an error message that they left a field blank")
    public void theyAreShownAnErrorMessageThatTheyLeftAFieldBlank() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By xpath = By.xpath("/html/body/app-root/div/main/app-create-account/div/div/div/div/div/div/form/label[3]/span");
        WebElement elem1 = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(xpath));
        assertNotNull(elem1);
    }

    @Then("they are shown an error message that the email is in use")
    public void theyAreShownAnErrorMessageThatTheEmailIsInUse() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By xpath = By.xpath("/html/body/app-root/div/main/app-create-account/div/div/div/div/div/div/form/label[1]/span");
        WebElement elem1 = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(xpath));
        assertNotNull(elem1);
    }

    @Then("they are shown an error message that the password fields do not match")
    public void theyAreShownAnErrorMessageThatThePasswordFieldsDoNotMatch() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By xpath = By.xpath("/html/body/app-root/div/main/app-create-account/div/div/div/div/div/div/form/label[5]/span");
        WebElement elem1 = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(xpath));
        assertNotNull(elem1);
    }
}
