package com.example.medicalorganizer.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.medicalorganizer.controller.PatientController;
import com.example.medicalorganizer.model.Patient;
import com.example.medicalorganizer.repository.mongo.PatientMongoRepository;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

import org.awaitility.Awaitility;


@RunWith(GUITestRunner.class)
public class PatientSwingViewIT extends AssertJSwingJUnitTestCase {

    private static MongoServer server;
    private static InetSocketAddress serverAddress;

    private MongoClient mongoClient;

    private FrameFixture window;
    private MedicalSwingView medicalSwingView;
    private PatientController patientController;
    private PatientMongoRepository patientRepository;

    public static final String PATIENT_COLLECTION_NAME = "patient";
    public static final String MEDICAL_DB_NAME = "medical";

    @BeforeClass
    public static void setupServer() {
        server = new MongoServer(new MemoryBackend());
        serverAddress = server.bind();
    }

    @AfterClass
    public static void shutdownServer() {
        server.shutdown();
    }

    @Override
    protected void onSetUp() {
        mongoClient = new MongoClient(new ServerAddress(serverAddress));
        patientRepository = new PatientMongoRepository(mongoClient, MEDICAL_DB_NAME, PATIENT_COLLECTION_NAME);
        for (Patient patient : patientRepository.findAll()) {
            patientRepository.delete(patient.getId());
        }
        GuiActionRunner.execute(() -> {
            medicalSwingView = new MedicalSwingView();
            patientController = new PatientController(patientRepository, medicalSwingView);
            medicalSwingView.setPatientController(patientController);
            return medicalSwingView;
        });
        window = new FrameFixture(robot(), medicalSwingView);
        window.show();
    }

    @Override
    protected void onTearDown() {
        mongoClient.close();
    }

    @Test @GUITest
    public void testAllPatients() {
        Patient patient1 = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        Patient patient2 = new Patient("2", "Sara Malik", 25, "Cold", "Dr. Sara");
        patientRepository.save(patient1);
        patientRepository.save(patient2);
        GuiActionRunner.execute(() -> patientController.getAllPatients());
        assertThat(window.list().contents())
            .containsExactly(patient1.toString(), patient2.toString());
    }

    @Test @GUITest
    public void testAddButtonSuccess() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("nameTextBox").enterText("Owais");
        window.textBox("ageTextBox").enterText("30");
        window.textBox("diagnosisTextBox").enterText("Flu");
        window.textBox("doctorNameTextBox").enterText("Dr. Ahmed");
        window.button(JButtonMatcher.withText("Add Patient")).click();

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(window.list().contents())
                .containsExactly(new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed").toString())
        );
    }

    @Test @GUITest
    public void testDeleteButtonSuccess() {
        patientController.addPatient(new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed"));

        window.list().selectItem(0);
        window.button(JButtonMatcher.withText("Delete Selected")).click();

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(window.list().contents()).isEmpty()
        );
    }

    @Test @GUITest
    public void testUpdateButtonSuccess() {
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        patientRepository.save(patient);

        GuiActionRunner.execute(() -> {
            medicalSwingView.getListPatientModel().addElement(patient);
        });

        window.list().selectItem(0);

        window.textBox("idTextBox").requireText("1");
        window.textBox("idTextBox").requireDisabled();

        window.textBox("nameTextBox").setText("Owais Updated");
        window.textBox("ageTextBox").setText("35");
        window.textBox("diagnosisTextBox").setText("Typhoid");
        window.textBox("doctorNameTextBox").setText("Dr. New");

        window.button(JButtonMatcher.withText("Update Selected")).click();

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() ->
            assertThat(window.list().contents())
                .containsExactly(new Patient("1", "Owais Updated", 35, "Typhoid", "Dr. New").toString())
        );
    }
}
