// Unit tests for PatientController CRUD operations
package com.example.medicalorganizer.controller;

import com.example.medicalorganizer.model.Patient;
import com.example.medicalorganizer.repository.PatientRepository;
import com.example.medicalorganizer.view.MedicalView;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.mockito.Mockito.*;

import static java.util.Arrays.asList;

import org.junit.After;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class PatientControllerTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private MedicalView medicalView;

    @InjectMocks
    private PatientController patientController;

    private AutoCloseable closeable;

    @Before
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void testAllPatients() {
        List<Patient> patients = asList(new Patient());
        when(patientRepository.findAll())
            .thenReturn(patients);
        patientController.getAllPatients();
        verify(medicalView)
            .displayPatients(patients);
    }

    @Test
    public void testGetAllPatientsWithEmptyList() {
        when(patientRepository.findAll()).thenReturn(null);
        patientController.getAllPatients();
        verify(medicalView).displayPatients(null);
    }

    @Test
    public void testNewPatientWhenPatientDoesNotAlreadyExist() {
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        when(patientRepository.findById("1"))
            .thenReturn(null);
        patientController.addPatient(patient);
        InOrder inOrder = inOrder(patientRepository, medicalView);
        inOrder.verify(patientRepository).save(patient);
        inOrder.verify(medicalView).addPatient(patient);
    }

    @Test
    public void testNewPatientWhenPatientAlreadyExists() {
        Patient patientToAdd = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        Patient existingPatient = new Patient("1", "Owais", 30, "Cold", "Dr. Ahmed");
        when(patientRepository.findById("1"))
            .thenReturn(existingPatient);
        patientController.addPatient(patientToAdd);
        verify(medicalView)
            .showErrorMessage("Already existing patient with id 1", existingPatient);
        verifyNoMoreInteractions(ignoreStubs(patientRepository));
    }

    @Test
    public void testUpdatePatientWhenPatientExists() {
        Patient patientToUpdate = new Patient("1", "Owais", 31, "Fever", "Dr. Ahmed");
        when(patientRepository.findById("1"))
            .thenReturn(patientToUpdate);
        patientController.updatePatient(patientToUpdate);
        InOrder inOrder = inOrder(patientRepository, medicalView);
        inOrder.verify(patientRepository).update(patientToUpdate);
        inOrder.verify(medicalView).updatePatient(patientToUpdate);
    }

    @Test
    public void testUpdatePatientWhenPatientDoesNotExist() {
        Patient patientToUpdate = new Patient("1", "Owais", 31, "Fever", "Dr. Ahmed");
        when(patientRepository.findById("1"))
            .thenReturn(null);
        patientController.updatePatient(patientToUpdate);
        verify(medicalView)
            .showErrorMessage("No existing patient with id 1", null);
        verifyNoMoreInteractions(ignoreStubs(patientRepository));
    }

    @Test
    public void testDeletePatientWhenPatientExists() {
        Patient patientToDelete = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        when(patientRepository.findById("1"))
            .thenReturn(patientToDelete);
        patientController.deletePatient(patientToDelete);
        InOrder inOrder = inOrder(patientRepository, medicalView);
        inOrder.verify(patientRepository).delete("1");
        inOrder.verify(medicalView).deletePatient(patientToDelete);
    }

    @Test
    public void testDeletePatientWhenPatientDoesNotExist() {
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        when(patientRepository.findById("1"))
            .thenReturn(null);
        patientController.deletePatient(patient);
        verify(medicalView)
            .showErrorMessage("No existing patient with id 1", patient);
        verifyNoMoreInteractions(ignoreStubs(patientRepository));
    }
}
