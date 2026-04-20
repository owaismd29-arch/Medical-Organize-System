package com.example.medicalorganizer.controller.racecondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.awaitility.Awaitility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.medicalorganizer.controller.PatientController;
import com.example.medicalorganizer.repository.PatientRepository;
import com.example.medicalorganizer.model.Patient;
import com.example.medicalorganizer.view.MedicalView;

public class PatientControllerRaceConditionTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private MedicalView medicalView;

    @InjectMocks
    private PatientController patientController;

    private AutoCloseable closeable;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void testNewPatientConcurrent() {
        List<Patient> patients = new ArrayList<>();
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        when(patientRepository.findById(anyString()))
            .thenAnswer(invocation -> patients.stream()
                .findFirst().orElse(null));
        doAnswer(invocation -> {
            patients.add(patient);
            return null;
        }).when(patientRepository).save(any(Patient.class));
        List<Thread> threads = IntStream.range(0, 10)
            .mapToObj(i -> new Thread(() -> patientController.addPatient(patient)))
            .peek(t -> t.start())
            .collect(Collectors.toList());
        Awaitility.await().atMost(10, TimeUnit.SECONDS)
            .until(() -> threads.stream().noneMatch(t -> t.isAlive()));
        assertThat(patients)
            .containsExactly(patient);
    }

    @Test
    public void testDeletePatientConcurrent() {
        List<Patient> patients = new ArrayList<>();
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        patients.add(patient);
        when(patientRepository.findById(anyString()))
            .thenAnswer(invocation -> patients.stream()
                .findFirst().orElse(null));
        doAnswer(invocation -> {
            patients.remove(patient);
            return null;
        }).when(patientRepository).delete(anyString());
        List<Thread> threads = IntStream.range(0, 10)
            .mapToObj(i -> new Thread(() -> patientController.deletePatient(patient)))
            .peek(t -> t.start())
            .collect(Collectors.toList());
        Awaitility.await().atMost(10, TimeUnit.SECONDS)
            .until(() -> threads.stream().noneMatch(t -> t.isAlive()));
        assertThat(patients).isEmpty();
    }

    @Test
    public void testUpdatePatientConcurrent() {
        List<Patient> patients = new ArrayList<>();
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        patients.add(patient);
        Patient updatedPatient = new Patient("1", "Owais Updated", 35, "Typhoid", "Dr. New");
        when(patientRepository.findById(anyString()))
            .thenAnswer(invocation -> patients.stream()
                .findFirst().orElse(null));
        doAnswer(invocation -> {
            patients.clear();
            patients.add(updatedPatient);
            return null;
        }).when(patientRepository).update(any(Patient.class));
        List<Thread> threads = IntStream.range(0, 10)
            .mapToObj(i -> new Thread(() -> patientController.updatePatient(updatedPatient)))
            .peek(t -> t.start())
            .collect(Collectors.toList());
        Awaitility.await().atMost(10, TimeUnit.SECONDS)
            .until(() -> threads.stream().noneMatch(t -> t.isAlive()));
        assertThat(patients)
            .containsExactly(updatedPatient);
    }
}
