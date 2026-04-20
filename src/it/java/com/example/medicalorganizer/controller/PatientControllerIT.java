package com.example.medicalorganizer.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.medicalorganizer.repository.mongo.PatientMongoRepository;
import com.example.medicalorganizer.model.Patient;
import com.example.medicalorganizer.repository.PatientRepository;
import com.example.medicalorganizer.view.MedicalView;
import com.mongodb.MongoClient;

// docker run -p 27017:27017 --rm mongo:4.4.3

public class PatientControllerIT {

    @Mock
    private MedicalView medicalView;

    private PatientRepository patientRepository;

    private PatientController patientController;

    private AutoCloseable closeable;

    public static final String PATIENT_COLLECTION_NAME = "patient";
    public static final String MEDICAL_DB_NAME = "medical";

    private static int mongoPort =
            Integer.parseInt(System.getProperty("mongo.port", "27017"));

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        patientRepository = new PatientMongoRepository(
                new MongoClient("localhost", mongoPort), MEDICAL_DB_NAME, PATIENT_COLLECTION_NAME);
        for (Patient patient : patientRepository.findAll()) {
            patientRepository.delete(patient.getId());
        }
        patientController = new PatientController(patientRepository, medicalView);
    }

    @After
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void testAllPatients() {
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        patientRepository.save(patient);
        patientController.getAllPatients();
        verify(medicalView)
            .displayPatients(asList(patient));
    }

    @Test
    public void testNewPatient() {
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        patientController.addPatient(patient);
        verify(medicalView).addPatient(patient);
    }

    @Test
    public void testDeletePatient() {
        Patient patientToDelete = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        patientRepository.save(patientToDelete);
        patientController.deletePatient(patientToDelete);
        verify(medicalView).deletePatient(patientToDelete);
    }

    @Test
    public void testUpdatePatient() {
        Patient originalPatient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        patientRepository.save(originalPatient);

        Patient updatedPatient = new Patient("1", "Owais", 31, "Fever", "Dr. Ahmed");
        patientController.updatePatient(updatedPatient);

        verify(medicalView).updatePatient(updatedPatient);
    }
}
