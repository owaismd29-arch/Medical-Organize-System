// End-to-end tests for Medical Organize System
package com.example.medicalorganizer.view.swing;

import javax.swing.JFrame;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.*;

import java.util.regex.Pattern;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;


@RunWith(GUITestRunner.class)
public class PatientSwingAppE2E extends AssertJSwingJUnitTestCase {

    @ClassRule
    public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

    private static final String DB_NAME = "test-db";
    private static final String COLLECTION_NAME = "test-collection";

    private static final String PATIENT_FIXTURE_1_ID = "1";
    private static final String PATIENT_FIXTURE_1_NAME = "Owais";
    private static final int PATIENT_FIXTURE_1_AGE = 30;
    private static final String PATIENT_FIXTURE_1_DIAGNOSIS = "Flu";
    private static final String PATIENT_FIXTURE_1_DOCTOR = "Dr. Ahmed";

    private static final String PATIENT_FIXTURE_2_ID = "2";
    private static final String PATIENT_FIXTURE_2_NAME = "Sara Malik";
    private static final int PATIENT_FIXTURE_2_AGE = 25;
    private static final String PATIENT_FIXTURE_2_DIAGNOSIS = "Cold";
    private static final String PATIENT_FIXTURE_2_DOCTOR = "Dr. Sara";

    private MongoClient mongoClient;
    private FrameFixture window;

    @Override
    protected void onSetUp() throws Exception {
        String containerIpAddress = mongo.getContainerIpAddress();
        Integer mappedPort = mongo.getFirstMappedPort();
        mongoClient = new MongoClient(containerIpAddress, mappedPort);
        mongoClient.getDatabase(DB_NAME).drop();
        addTestPatientToDatabase(PATIENT_FIXTURE_1_ID, PATIENT_FIXTURE_1_NAME,
                PATIENT_FIXTURE_1_AGE, PATIENT_FIXTURE_1_DIAGNOSIS, PATIENT_FIXTURE_1_DOCTOR);
        addTestPatientToDatabase(PATIENT_FIXTURE_2_ID, PATIENT_FIXTURE_2_NAME,
                PATIENT_FIXTURE_2_AGE, PATIENT_FIXTURE_2_DIAGNOSIS, PATIENT_FIXTURE_2_DOCTOR);
        application("com.example.medicalorganizer.app.swing.MedicalSwingApp")
                .withArgs("--mongo-host=" + containerIpAddress, "--mongo-port=" + mappedPort.toString(),
                        "--db-name=" + DB_NAME, "--db-collection=" + COLLECTION_NAME)
                .start();
        window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
            @Override
            protected boolean isMatching(JFrame frame) {
                return "Medical Organize System".equals(frame.getTitle()) && frame.isShowing();
            }
        }).using(robot());
    }

    @Override
    protected void onTearDown() {
        mongoClient.close();
    }

    @Test @GUITest
    public void testOnStartAllDatabaseElementsAreShown() {
        assertThat(window.list().contents())
            .anySatisfy(e -> assertThat(e).contains(PATIENT_FIXTURE_1_ID, PATIENT_FIXTURE_1_NAME,
                    String.valueOf(PATIENT_FIXTURE_1_AGE), PATIENT_FIXTURE_1_DIAGNOSIS, PATIENT_FIXTURE_1_DOCTOR))
            .anySatisfy(e -> assertThat(e).contains(PATIENT_FIXTURE_2_ID, PATIENT_FIXTURE_2_NAME,
                    String.valueOf(PATIENT_FIXTURE_2_AGE), PATIENT_FIXTURE_2_DIAGNOSIS, PATIENT_FIXTURE_2_DOCTOR));
    }

    @Test @GUITest
    public void testAddButtonSuccess() {
        window.textBox("idTextBox").enterText("10");
        window.textBox("nameTextBox").enterText("Usman Ali");
        window.textBox("ageTextBox").enterText("40");
        window.textBox("diagnosisTextBox").enterText("Diabetes");
        window.textBox("doctorNameTextBox").enterText("Dr. Usman");
        window.button(JButtonMatcher.withText("Add Patient")).click();
        assertThat(window.list().contents())
            .anySatisfy(e -> assertThat(e).contains("10", "Usman Ali", "40", "Diabetes", "Dr. Usman"));
    }

    @Test @GUITest
    public void testAddButtonError() {
        window.textBox("idTextBox").enterText(PATIENT_FIXTURE_1_ID);
        window.textBox("nameTextBox").enterText("New Patient");
        window.textBox("ageTextBox").enterText("22");
        window.textBox("diagnosisTextBox").enterText("Malaria");
        window.textBox("doctorNameTextBox").enterText("Dr. New");
        window.button(JButtonMatcher.withText("Add Patient")).click();
        assertThat(window.label("errorMessageLabel").text())
            .contains(PATIENT_FIXTURE_1_ID, PATIENT_FIXTURE_1_NAME,
                    String.valueOf(PATIENT_FIXTURE_1_AGE), PATIENT_FIXTURE_1_DIAGNOSIS, PATIENT_FIXTURE_1_DOCTOR);
    }

    @Test @GUITest
    public void testDeleteButtonSuccess() {
        window.list("patientList")
            .selectItem(Pattern.compile(".*" + PATIENT_FIXTURE_1_NAME + ".*"));
        window.button(JButtonMatcher.withText("Delete Selected")).click();
        assertThat(window.list().contents())
            .noneMatch(e -> e.contains(".*" + PATIENT_FIXTURE_1_NAME + ".*"));
    }

    @Test @GUITest
    public void testDeleteButtonError() {
        window.list("patientList")
            .selectItem(Pattern.compile(".*" + PATIENT_FIXTURE_1_NAME + ".*"));
        removeTestPatientFromDatabase(PATIENT_FIXTURE_1_ID);
        window.button(JButtonMatcher.withText("Delete Selected")).click();
        assertThat(window.label("errorMessageLabel").text())
            .contains(PATIENT_FIXTURE_1_ID, PATIENT_FIXTURE_1_NAME,
                    String.valueOf(PATIENT_FIXTURE_1_AGE), PATIENT_FIXTURE_1_DIAGNOSIS, PATIENT_FIXTURE_1_DOCTOR);
    }

    @Test @GUITest
    public void testUpdateButtonSuccess() {
        window.list("patientList")
            .selectItem(Pattern.compile(".*" + PATIENT_FIXTURE_1_NAME + ".*"));

        window.textBox("nameTextBox").setText("Owais Updated");
        window.textBox("ageTextBox").setText("35");
        window.textBox("diagnosisTextBox").setText("Typhoid");
        window.textBox("doctorNameTextBox").setText("Dr. New");

        window.button(JButtonMatcher.withText("Update Selected")).click();

        assertThat(window.list().contents())
            .anySatisfy(e -> assertThat(e).contains("Owais Updated", "35", "Typhoid", "Dr. New"));
    }

    private void addTestPatientToDatabase(String id, String name, int age, String diagnosis, String doctorName) {
        mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME).insertOne(new Document()
            .append("id", id).append("name", name).append("age", age)
            .append("diagnosis", diagnosis).append("doctorName", doctorName));
    }

    private void removeTestPatientFromDatabase(String id) {
        mongoClient
            .getDatabase(DB_NAME)
            .getCollection(COLLECTION_NAME)
            .deleteOne(Filters.eq("id", id));
    }
}
