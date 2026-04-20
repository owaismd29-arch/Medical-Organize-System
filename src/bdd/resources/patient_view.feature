Feature: Medical View
  Specifications of the behavior of the Medical View

  Scenario: The initial state of the view
    Given The database contains the patients with the following values
      | id | name      | age | diagnosis | doctorName |
      |  1 | Owais  |  30 | Flu       | Dr. Ahmed  |
      |  2 | Sara Malik|  25 | Cold      | Dr. Sara   |
    When The Medical View is shown
    Then The list contains elements with the following values
      | 1 | Owais   | 30 | Flu  | Dr. Ahmed |
      | 2 | Sara Malik | 25 | Cold | Dr. Sara  |

  Scenario: Add a new patient
    Given The Medical View is shown
    When The user enters the following values in the text fields
      | id | name     | age | diagnosis | doctorName |
      |  1 | Owais |  30 | Flu       | Dr. Ahmed  |
    And The user clicks the "Add Patient" button
    Then The list contains elements with the following values
      | 1 | Owais | 30 | Flu | Dr. Ahmed |

  Scenario: Add a new patient with an existing id
    Given The database contains the patients with the following values
      | id | name     | age | diagnosis | doctorName |
      |  1 | Owais |  30 | Flu       | Dr. Ahmed  |
    And The Medical View is shown
    When The user enters the following values in the text fields
      | id | name        | age | diagnosis | doctorName |
      |  1 | New Patient |  22 | Malaria   | Dr. New    |
    And The user clicks the "Add Patient" button
    Then An error is shown containing the following values
      | 1 | Owais | 30 | Flu | Dr. Ahmed |
