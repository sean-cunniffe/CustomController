Feature: As a staff member, I want to delete orders, so that I can remove them from the queue if a customer requests a refund.

  Scenario: Staff Member wants the option to delete the order
    Given the staff is at the dashboard
    Then show an option to delete an order

  Scenario: Staff member cancels order deletion
    Given the staff is at the dashboard
    When staff click cancel order
    Then staff click cancel order and order isn't deleted

  Scenario: Staff wants to delete the order
    Given the staff is at the dashboard
    When staff click cancel order
    Then staff click confirm and the order is deleted

