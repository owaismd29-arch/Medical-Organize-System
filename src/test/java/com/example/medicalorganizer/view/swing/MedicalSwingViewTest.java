package com.example.medicalorganizer.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.awaitility.Awaitility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.medicalorganizer.controller.PatientController;
import com.example.medicalorganizer.model.Patient;


@RunWith(GUITestRunner.class)
public class MedicalSwingViewTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private MedicalSwingView medicalSwingView;

    @Mock
    private PatientController patientController;

    private AutoCloseable closeable;

    @Override
    protected void onSetUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);
        GuiActionRunner.execute(() -> {
            medicalSwingView = new MedicalSwingView();
            medicalSwingView.setPatientController(patientController);
            return medicalSwingView;
        });
        window = new FrameFixture(robot(), medicalSwingView);
        window.show();
    }

    @Override
    protected void onTearDown() throws Exception {
        closeable.close();
    }

    @Test @GUITest
    public void testInitialState() {
        window.button("btnAdd").requireDisabled();
        window.button("btnCancel").requireDisabled();
        window.button("btnDeleteSelected").requireDisabled();
        window.button("btnUpdateSelected").requireDisabled();

        window.textBox("idTextBox").requireEmpty();
        window.textBox("nameTextBox").requireEmpty();
        window.textBox("ageTextBox").requireEmpty();
        window.textBox("diagnosisTextBox").requireEmpty();
        window.textBox("doctorNameTextBox").requireEmpty();
    }

    @Test @GUITest
    public void testEnableAddButtonOnValidInput() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("nameTextBox").enterText("Owais");
        window.textBox("ageTextBox").enterText("30");
        window.textBox("diagnosisTextBox").enterText("Flu");
        window.textBox("doctorNameTextBox").enterText("Dr. Ahmed");

        window.button("btnAdd").requireEnabled();
    }

    @Test @GUITest
    public void testWhenAnyInputIsEmptyAddButtonShouldBeDisabled() {
        JTextComponentFixture idTextBox = window.textBox("idTextBox");
        JTextComponentFixture nameTextBox = window.textBox("nameTextBox");
        JTextComponentFixture ageTextBox = window.textBox("ageTextBox");
        JTextComponentFixture diagnosisTextBox = window.textBox("diagnosisTextBox");
        JTextComponentFixture doctorNameTextBox = window.textBox("doctorNameTextBox");

        // when id is empty
        idTextBox.enterText(" ");
        nameTextBox.enterText("Owais");
        ageTextBox.enterText("30");
        diagnosisTextBox.enterText("Flu");
        doctorNameTextBox.enterText("Dr. Ahmed");
        window.button(JButtonMatcher.withText("Add Patient")).requireDisabled();

        idTextBox.setText(""); nameTextBox.setText(""); ageTextBox.setText("");
        diagnosisTextBox.setText(""); doctorNameTextBox.setText("");

        // when Name is empty
        idTextBox.enterText("1");
        nameTextBox.enterText(" ");
        ageTextBox.enterText("30");
        diagnosisTextBox.enterText("Flu");
        doctorNameTextBox.enterText("Dr. Ahmed");
        window.button(JButtonMatcher.withText("Add Patient")).requireDisabled();

        idTextBox.setText(""); nameTextBox.setText(""); ageTextBox.setText("");
        diagnosisTextBox.setText(""); doctorNameTextBox.setText("");

        // when Age is empty
        idTextBox.enterText("1");
        nameTextBox.enterText("Owais");
        ageTextBox.enterText(" ");
        diagnosisTextBox.enterText("Flu");
        doctorNameTextBox.enterText("Dr. Ahmed");
        window.button(JButtonMatcher.withText("Add Patient")).requireDisabled();

        idTextBox.setText(""); nameTextBox.setText(""); ageTextBox.setText("");
        diagnosisTextBox.setText(""); doctorNameTextBox.setText("");

        // when Diagnosis is empty
        idTextBox.enterText("1");
        nameTextBox.enterText("Owais");
        ageTextBox.enterText("30");
        diagnosisTextBox.enterText(" ");
        doctorNameTextBox.enterText("Dr. Ahmed");
        window.button(JButtonMatcher.withText("Add Patient")).requireDisabled();

        idTextBox.setText(""); nameTextBox.setText(""); ageTextBox.setText("");
        diagnosisTextBox.setText(""); doctorNameTextBox.setText("");

        // when DoctorName is empty
        idTextBox.enterText("1");
        nameTextBox.enterText("Owais");
        ageTextBox.enterText("30");
        diagnosisTextBox.enterText("Flu");
        doctorNameTextBox.enterText(" ");
        window.button(JButtonMatcher.withText("Add Patient")).requireDisabled();
    }

    @Test
    public void testDeleteButtonShouldBeEnabledOnlyWhenAPatientIsSelected() {
        GuiActionRunner.execute(
            () -> medicalSwingView.getListPatientModel()
                    .addElement(new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed"))
        );

        window.list("patientList").selectItem(0);
        JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Selected"));
        deleteButton.requireEnabled();
        window.list("patientList").clearSelection();
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(deleteButton::requireDisabled);
    }

    @Test
    public void testsDisplayPatientsShouldAddPatientDescriptionsToTheList() {
        Patient patient1 = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        Patient patient2 = new Patient("2", "Sara Malik", 25, "Cold", "Dr. Sara");
        GuiActionRunner.execute(
            () -> medicalSwingView.displayPatients(Arrays.asList(patient1, patient2))
        );

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            String[] listContents = window.list().contents();
            assertThat(listContents).containsExactly(patient1.toString(), patient2.toString());
        });
    }

    @Test
    public void testShowErrorMessageShouldShowTheMessageInTheErrorLabel() {
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        medicalSwingView.showErrorMessage("error message", patient);

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            window.label("errorMessageLabel")
                .requireText("error message: " + patient);
        });
    }

    @Test
    public void testPatientAddedShouldAddThePatientToTheListAndResetTheErrorLabel() {
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        medicalSwingView.addPatient(patient);
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            String[] listContents = window.list().contents();
            assertThat(listContents).containsExactly(patient.toString());
            window.label("errorMessageLabel").requireText(" ");
        });
    }

    @Test
    public void testPatientUpdatedShouldUpdateThePatientInTheListAndResetTheErrorLabel() {
        Patient updatedPatient = new Patient("1", "Owais Updated", 35, "Typhoid", "Dr. New");

        medicalSwingView.addPatient(new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed"));
        medicalSwingView.updatePatient(updatedPatient);

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            String[] listContents = window.list().contents();
            assertThat(listContents).containsExactly(updatedPatient.toString());
            window.label("errorMessageLabel").requireText(" ");
        });
    }

    @Test
    public void testPatientRemovedShouldRemoveThePatientFromTheListAndResetTheErrorLabel() {
        Patient patient1 = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        Patient patient2 = new Patient("2", "Sara Malik", 25, "Cold", "Dr. Sara");
        GuiActionRunner.execute(
            () -> {
                DefaultListModel<Patient> listPatientModel = medicalSwingView.getListPatientModel();
                listPatientModel.addElement(patient1);
                listPatientModel.addElement(patient2);
            }
        );

        GuiActionRunner.execute(
            () -> medicalSwingView.deletePatient(new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed"))
        );

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            String[] listContents = window.list().contents();
            assertThat(listContents).containsExactly(patient2.toString());
            window.label("errorMessageLabel").requireText(" ");
        });
    }

    @Test
    public void testAddButtonShouldDelegateToPatientControllerNewPatient() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("nameTextBox").enterText("Owais");
        window.textBox("ageTextBox").enterText("30");
        window.textBox("diagnosisTextBox").enterText("Flu");
        window.textBox("doctorNameTextBox").enterText("Dr. Ahmed");
        window.button(JButtonMatcher.withText("Add Patient")).click();
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(patientController).addPatient(new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed"));
        });
    }

    @Test
    public void testAddButtonShouldBeDisabledWhenAllFieldsAreFilledButPatientIsSelected() {
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        GuiActionRunner.execute(() -> medicalSwingView.getListPatientModel().addElement(patient));

        window.list("patientList").selectItem(0);

        window.textBox("idTextBox").requireText("1");
        window.textBox("nameTextBox").enterText("Owais");
        window.textBox("ageTextBox").enterText("30");
        window.textBox("diagnosisTextBox").enterText("Flu");
        window.textBox("doctorNameTextBox").enterText("Dr. Ahmed");

        window.button(JButtonMatcher.withText("Add Patient")).requireDisabled();
    }

    @Test
    public void testDeleteButtonShouldDelegateToPatientControllerDeletePatient() {
        Patient patient1 = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        Patient patient2 = new Patient("2", "Sara Malik", 25, "Cold", "Dr. Sara");
        GuiActionRunner.execute(
            () -> {
                DefaultListModel<Patient> listPatientsModel = medicalSwingView.getListPatientModel();
                listPatientsModel.addElement(patient1);
                listPatientsModel.addElement(patient2);
            }
        );
        window.list("patientList").selectItem(1);
        window.button(JButtonMatcher.withText("Delete Selected")).click();
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(patientController).deletePatient(patient2);
        });
    }

    @Test
    public void testUpdateButtonShouldDelegateToPatientControllerUpdateSelectedPatient() {
        Patient originalPatient1 = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        Patient originalPatient2 = new Patient("2", "Sara Malik", 25, "Cold", "Dr. Sara");

        GuiActionRunner.execute(() -> {
            DefaultListModel<Patient> listPatientModel = medicalSwingView.getListPatientModel();
            listPatientModel.addElement(originalPatient1);
            listPatientModel.addElement(originalPatient2);
        });

        window.list("patientList").selectItem(1);

        window.textBox("idTextBox").requireText("2");
        window.textBox("idTextBox").requireDisabled();

        window.textBox("nameTextBox").setText("Sara Updated");
        window.textBox("ageTextBox").setText("26");
        window.textBox("diagnosisTextBox").setText("Fever");
        window.textBox("doctorNameTextBox").setText("Dr. New");

        window.button(JButtonMatcher.withText("Update Selected")).click();

        Patient updatedPatient = new Patient("2", "Sara Updated", 26, "Fever", "Dr. New");
        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(patientController).updatePatient(updatedPatient);
        });
    }

    @Test
    public void testCancelButtonShouldClearSelectedPatientAndResetFields() {
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        GuiActionRunner.execute(() -> medicalSwingView.getListPatientModel().addElement(patient));

        window.list("patientList").selectItem(0);

        window.textBox("idTextBox").requireText("1");
        window.textBox("nameTextBox").requireText("Owais");
        window.textBox("ageTextBox").requireText("30");
        window.textBox("diagnosisTextBox").requireText("Flu");
        window.textBox("doctorNameTextBox").requireText("Dr. Ahmed");

        window.button(JButtonMatcher.withText("Update Selected")).requireEnabled();
        window.button(JButtonMatcher.withText("Delete Selected")).requireEnabled();

        window.button(JButtonMatcher.withText("Cancel")).click();

        window.textBox("idTextBox").requireText("");
        window.textBox("nameTextBox").requireText("");
        window.textBox("ageTextBox").requireText("");
        window.textBox("diagnosisTextBox").requireText("");
        window.textBox("doctorNameTextBox").requireText("");

        window.button(JButtonMatcher.withText("Update Selected")).requireDisabled();
        window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
        window.button(JButtonMatcher.withText("Add Patient")).requireDisabled();

        assertThat(window.list("patientList").selection()).isEmpty();
    }

    @Test
    public void testAddButtonShouldBeDisabledWhenPatientIsSelected() {
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        GuiActionRunner.execute(() -> medicalSwingView.getListPatientModel().addElement(patient));

        window.list("patientList").selectItem(0);

        window.button(JButtonMatcher.withText("Add Patient")).requireDisabled();
    }

    @Test
    public void testAddButtonShouldBeEnabledWhenFieldsAreFilledAndNoPatientIsSelected() {
        window.list("patientList").clearSelection();

        window.textBox("idTextBox").enterText("1");
        window.textBox("nameTextBox").enterText("Owais");
        window.textBox("ageTextBox").enterText("30");
        window.textBox("diagnosisTextBox").enterText("Flu");
        window.textBox("doctorNameTextBox").enterText("Dr. Ahmed");

        window.button(JButtonMatcher.withText("Add Patient")).requireEnabled();
    }

    @Test
    public void testPatientUpdateWhenPatientNotFound() {
        Patient nonExistentPatient = new Patient("2", "Sara Malik", 25, "Cold", "Dr. Sara");

        medicalSwingView.addPatient(new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed"));
        medicalSwingView.updatePatient(nonExistentPatient);

        Awaitility.await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            String[] listContents = window.list().contents();
            assertThat(listContents)
                .containsExactly(new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed").toString());
        });

        window.label("errorMessageLabel").requireText(" ");
    }
}
