Feature: As a customer, I want to cancel an order, so that the order does not get processed and I don’t get charged

  Scenario: Customer cancels deletion of order
    Given the customer is at the dashboard
    When customer click cancel order
    Then customer click cancel order and order isn't deleted

  Scenario: Customer wants to cancel an order
    Given the customer is at the dashboard
    When customer click cancel order
    Then customer click confirm and the order is deleted


