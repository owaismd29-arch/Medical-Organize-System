Feature: Medical View High Level
  Specifications of the behavior of the Medical View

  Background: 
    Given The database contains a few patients
    And The Medical View is shown

  Scenario: Add a new patient
    Given The user provides patient data in the text fields
    When The user clicks the "Add Patient" button
    Then The list contains the new medical patient

  Scenario: Add a new patient with an existing id
    Given The user provides patient data in the text fields, specifying an existing id
    When The user clicks the "Add Patient" button
    Then An error is shown containing the name of the existing patient

  Scenario: Update an existing patient
    Given The database contains the patients with the following values
      | id | name     | age | diagnosis | doctorName |
      |  1 | Owais |  30 | Flu       | Dr. Ahmed  |
    And The user selects the patient with id "1" from the list
    When The user updates the patient details with the following values
      | id | name             | age | diagnosis | doctorName |
      |  1 | Owais Updated |  35 | Typhoid   | Dr. New    |
    And The user clicks the "Update Selected" button
    Then The list reflects the updated details for the patient with id "1"

  Scenario: Delete a patient
    Given The user selects a patient from the list
    When The user clicks the "Delete Selected" button
    Then The patient is removed from the list

  Scenario: Delete a not existing patient
    Given The user selects a patient from the list
    But The patient is in the meantime removed from the database
    When The user clicks the "Delete Selected" button
    Then An error is shown containing the name of the selected patient
    And The patient is removed from the list
