Feature: As a customer, I want to create an account, so that I can access services available to me

  Scenario: customer wants to create an account
    When they navigate to the login page
    Then they have the option to create an account

  Scenario: customer successfully creates an account
    Given they navigate to the create account page
    When they enter their email
    And they enter their first name
    And they enter their last name
    And they enter their password
    And they enter their confirm password
    And they click create account
    Then they are redirected to the dashboard logged in

    Scenario: customer unsuccessfully creates an account because they left a field blank
      Given they navigate to the create account page
      When they enter their email
      And they enter their first name
      And they enter their password
      And they enter their confirm password
      And they click create account
      Then they are shown an error message that they left a field blank

  Scenario: customer unsuccessfully creates an account because they try using an existing email
    Given they navigate to the create account page
    When they enter an already existing email
    And they enter their first name
    And they enter their last name
    And they enter their password
    And they enter their confirm password
    And they click create account
    Then they are shown an error message that the email is in use

  Scenario: customer unsuccessfully creates an account because the password fields do not match
    Given they navigate to the create account page
    When they enter their email
    And they enter their first name
    And they enter their last name
    And they enter their password
    And they enter their confirm password that does not match password
    And they click create account
    Then they are shown an error message that the password fields do not match


