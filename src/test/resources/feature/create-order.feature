Feature: As a customer, I want to create an order, so that I can purchase a controller of my choice

  Scenario: Customer navigates to create order
    Given the customer has logged in
    When they click create order
    Then they are directed to the order page

    Scenario: Customer adds a controller to their cart
      Given the customer is at the create order page
      When they add a controller to their cart
      Then the controller is added to the cart

  Scenario: Customer removes a controller to their cart
    Given the customer is at the create order page
    When they add a controller to their cart
    And they remove a controller from their cart
    Then their cart is empty

  Scenario: Customer successfully create an order
    Given the customer is at the create order page
    When they add a controller to their cart
    And the customer click view cart
    And the customer click Complete Order
    And the customer input their payment details
    And the customer input their delivery Details
    And the customer clicks Submit order
    Then the customer is shown the receipt for their order

  Scenario: Customer enters invalid payment details
    Given the customer is at the create order page
    When they add a controller to their cart
    And the customer click view cart
    And the customer click Complete Order
    And the customer input their delivery Details
    And the customer clicks Submit order
    Then the customer is shown an error message for the payment details

  Scenario: Customer enters invalid delivery details
    Given the customer is at the create order page
    When they add a controller to their cart
    And the customer click view cart
    And the customer click Complete Order
    And the customer input their payment details
    And the customer clicks Submit order
    Then the customer is shown an error message for their delivery details

    Scenario: Customer cannot create order with no controllers
      Given the customer is at the create order page
      And their cart is empty
      When they navigate to the cart page
      Then they cannot confirm order
