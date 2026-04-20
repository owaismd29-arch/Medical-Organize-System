package com.example.medicalorganizer.bdd.steps;

import static com.example.medicalorganizer.bdd.steps.DatabaseSteps.COLLECTION_NAME;
import static com.example.medicalorganizer.bdd.steps.DatabaseSteps.DB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;

import com.example.medicalorganizer.bdd.MedicalSwingAppBDD;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class MedicalSwingAppSteps {

    private FrameFixture window;

    @After
    public void tearDown() {
        if (window != null)
            window.cleanUp();
    }

    @When("The Medical View is shown")
    public void the_Medical_View_is_shown() {
        application("com.example.medicalorganizer.app.swing.MedicalSwingApp")
            .withArgs(
                "--mongo-port=" + MedicalSwingAppBDD.mongoPort,
                "--db-name=" + DB_NAME,
                "--db-collection=" + COLLECTION_NAME
            )
            .start();
        window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
            @Override
            protected boolean isMatching(JFrame frame) {
                return "Medical Organize System".equals(frame.getTitle()) && frame.isShowing();
            }
        }).using(BasicRobot.robotWithCurrentAwtHierarchy());
    }

    @Then("The list contains elements with the following values")
    public void the_list_contains_elements_with_the_following_values(List<List<String>> values) {
        values.forEach(
            v -> assertThat(window.list().contents())
                .anySatisfy(e -> assertThat(e).contains(v.get(0), v.get(1), v.get(2), v.get(3), v.get(4)))
        );
    }

    @When("The user enters the following values in the text fields")
    public void the_user_enters_the_following_values_in_the_text_fields(List<Map<String, String>> values) {
        values
            .stream()
            .flatMap(m -> m.entrySet().stream())
            .forEach(
                e -> window
                    .textBox(e.getKey() + "TextBox")
                    .enterText(e.getValue())
            );
    }

    @When("The user clicks the {string} button")
    public void the_user_clicks_the_button(String buttonText) {
        window.button(JButtonMatcher.withText(buttonText)).click();
    }

    @Then("An error is shown containing the following values")
    public void an_error_is_shown_containing_the_following_values(List<List<String>> values) {
        assertThat(window.label("errorMessageLabel").text())
            .contains(values.get(0));
    }

    @Given("The user provides patient data in the text fields")
    public void the_user_provides_patient_data_in_the_text_fields() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("nameTextBox").enterText("Owais");
        window.textBox("ageTextBox").enterText("30");
        window.textBox("diagnosisTextBox").enterText("Flu");
        window.textBox("doctorNameTextBox").enterText("Dr. Ahmed");
    }

    @Then("The list contains the new patient")
    public void the_list_contains_the_new_patient() {
        assertThat(window.list().contents())
            .anySatisfy(e -> assertThat(e).contains("1", "Owais", "30", "Flu", "Dr. Ahmed"));
    }

    @Given("The user provides patient data in the text fields, specifying an existing id")
    public void the_user_provides_patient_data_in_the_text_fields_specifying_an_existing_id() {
        window.textBox("idTextBox").enterText(DatabaseSteps.PATIENT_FIXTURE_1_ID);
        window.textBox("nameTextBox").enterText("New Patient");
        window.textBox("ageTextBox").enterText("22");
        window.textBox("diagnosisTextBox").enterText("Malaria");
        window.textBox("doctorNameTextBox").enterText("Dr. New");
    }

    @Then("An error is shown containing the name of the existing patient")
    public void an_error_is_shown_containing_the_name_of_the_existing_patient() {
        assertThat(window.label("errorMessageLabel").text())
            .contains(DatabaseSteps.PATIENT_FIXTURE_1_NAME);
    }

    @Given("The user selects a patient from the list")
    public void the_user_selects_a_patient_from_the_list() {
        window.list("patientList")
            .selectItem(Pattern.compile(".*" + DatabaseSteps.PATIENT_FIXTURE_1_NAME + ".*"));
    }

    @Then("The patient is removed from the list")
    public void the_patient_is_removed_from_the_list() {
        assertThat(window.list().contents())
            .noneMatch(e -> e.contains(".*" + DatabaseSteps.PATIENT_FIXTURE_1_NAME + ".*"));
    }

    @Then("An error is shown containing the name of the selected patient")
    public void an_error_is_shown_containing_the_name_of_the_selected_patient() {
        assertThat(window.label("errorMessageLabel").text())
            .contains(DatabaseSteps.PATIENT_FIXTURE_1_NAME);
    }

    @Then("The list contains the new medical patient")
    public void the_list_contains_the_new_medical_patient() {
        assertThat(window.list().contents())
            .anySatisfy(e -> assertThat(e).contains("1", "Owais", "30", "Flu", "Dr. Ahmed"));
    }

    @Given("The user selects the patient with id {string} from the list")
    public void the_user_selects_the_patient_with_id_from_the_list(String patientId) {
        window.list("patientList")
            .selectItem(Pattern.compile(".*" + patientId + ".*"));
    }

    @When("The user updates the patient details with the following values")
    public void the_user_updates_the_patient_details_with_the_following_values(List<Map<String, String>> values) {
        Map<String, String> patientDetails = values.get(0);

        window.textBox("nameTextBox").setText(patientDetails.get("name"));
        window.textBox("ageTextBox").setText(patientDetails.get("age"));
        window.textBox("diagnosisTextBox").setText(patientDetails.get("diagnosis"));
        window.textBox("doctorNameTextBox").setText(patientDetails.get("doctorName"));

        window.button(JButtonMatcher.withText("Update Selected")).click();
    }

    @Then("The list reflects the updated details for the patient with id {string}")
    public void the_list_reflects_the_updated_details_for_the_patient_with_id(String patientId) {
        assertThat(window.list().contents())
            .anySatisfy(e -> assertThat(e).contains(patientId, "Owais Updated", "35", "Typhoid", "Dr. New"));
    }
}
