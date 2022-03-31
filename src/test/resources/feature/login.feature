Feature: As a customer, I want to login, so that I can access the services available to me
Feature: As a staff member, I want to login, so that I can access the services available to me.

  Scenario: Staff navigates to the dashboard successfully
    Given staff navigate to the login page
    When staff enter correct username
    And staff enter correct password
    And they click login
    Then staff are navigated to the staff dashboard

  Scenario: Customer navigates to the dashboard successfully
    Given customer navigate to the login page
    When customer enter correct username
    And customer enter correct password
    And they click login
    Then customer are navigated to the customer dashboard

  Scenario: Staff navigates to the dashboard unsuccessfully because they entered wrong password
    Given staff navigate to the login page
    When staff enter correct username
    And staff enter incorrect password
    And they click login
    Then staff is shown an error message

  Scenario: Staff navigates to the dashboard unsuccessfully because they entered wrong username
    Given staff navigate to the login page
    When staff enter incorrect username
    And staff enter correct password
    And they click login
    Then staff is shown an error message

  Scenario: Customer navigates to the dashboard unsuccessfully because they entered wrong password
    Given customer navigate to the login page
    When customer enter correct username
    And customer enter incorrect password
    And they click login
    Then customer is shown an error message

  Scenario: Customer navigates to the dashboard unsuccessfully because they entered wrong username
    Given customer navigate to the login page
    When customer enter incorrect username
    And customer enter correct password
    And they click login
    Then customer is shown an error message

  Scenario: Customer wants to logout
    When customer successfully logs in
    And they click logout
    Then navigate back to the login page
