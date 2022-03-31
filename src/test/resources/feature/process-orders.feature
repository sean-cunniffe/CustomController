Feature: As a staff member, I want to process orders, so that I can get the order to the customer

  Scenario: Staff are shown orders to be processed
    Given the staff is at the dashboard
    Then they are shown the pending orders

  Scenario: Staff changes order status
    Given the staff is at the dashboard
    When they click to change order status
    Then they can change the order status

  Scenario: Orders are in order of processing status
    Given the staff is at the dashboard
    Then show the orders in order of processing status
