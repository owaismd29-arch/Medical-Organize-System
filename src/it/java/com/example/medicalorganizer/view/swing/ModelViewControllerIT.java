package com.example.medicalorganizer.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.awaitility.Awaitility;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.example.medicalorganizer.repository.mongo.PatientMongoRepository;
import com.example.medicalorganizer.controller.PatientController;
import com.example.medicalorganizer.model.Patient;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@RunWith(GUITestRunner.class)
public class ModelViewControllerIT extends AssertJSwingJUnitTestCase {

    @ClassRule
    public static final MongoDBContainer mongo =
        new MongoDBContainer("mongo:4.4.3");

    private MongoClient mongoClient;

    private FrameFixture window;
    private PatientMongoRepository patientRepository;
    private PatientController patientController;

    public static final String PATIENT_COLLECTION_NAME = "patient";
    public static final String MEDICAL_DB_NAME = "medical";

    @Override
    protected void onSetUp() {
        mongoClient = new MongoClient(
            new ServerAddress(
                mongo.getHost(),
                mongo.getFirstMappedPort()));
        patientRepository = new PatientMongoRepository(mongoClient, MEDICAL_DB_NAME, PATIENT_COLLECTION_NAME);
        for (Patient patient : patientRepository.findAll()) {
            patientRepository.delete(patient.getId());
        }
        window = new FrameFixture(robot(), GuiActionRunner.execute(() -> {
            MedicalSwingView medicalSwingView = new MedicalSwingView();
            patientController = new PatientController(patientRepository, medicalSwingView);
            medicalSwingView.setPatientController(patientController);
            return medicalSwingView;
        }));
        window.show();
    }

    @Override
    protected void onTearDown() {
        mongoClient.close();
    }

    @Test
    public void testAddPatient() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("nameTextBox").enterText("Owais");
        window.textBox("ageTextBox").enterText("30");
        window.textBox("diagnosisTextBox").enterText("Flu");
        window.textBox("doctorNameTextBox").enterText("Dr. Ahmed");
        window.button(JButtonMatcher.withText("Add Patient")).click();
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(patientRepository.findById("1"))
                .isEqualTo(new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed"))
        );
    }

    @Test
    public void testDeletePatient() {
        patientRepository.save(new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed"));
        GuiActionRunner.execute(() -> patientController.getAllPatients());
        window.list().selectItem(0);
        window.button(JButtonMatcher.withText("Delete Selected")).click();
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(patientRepository.findById("1")).isNull()
        );
    }

    @Test
    public void testUpdatePatient() {
        patientRepository.save(new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed"));

        GuiActionRunner.execute(() -> patientController.getAllPatients());

        window.list().selectItem(0);

        window.textBox("idTextBox").requireText("1");
        window.textBox("idTextBox").requireDisabled();

        window.textBox("nameTextBox").setText("Owais Updated");
        window.textBox("ageTextBox").setText("35");
        window.textBox("diagnosisTextBox").setText("Typhoid");
        window.textBox("doctorNameTextBox").setText("Dr. New");

        window.button(JButtonMatcher.withText("Update Selected")).click();

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(patientRepository.findById("1"))
                .isEqualTo(new Patient("1", "Owais Updated", 35, "Typhoid", "Dr. New"))
        );

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(window.list().contents())
                .containsExactly(new Patient("1", "Owais Updated", 35, "Typhoid", "Dr. New").toString())
        );
    }
}
