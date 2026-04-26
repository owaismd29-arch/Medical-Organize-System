package com.example.medicalorganizer.controller;

import com.example.medicalorganizer.model.Patient;
import com.example.medicalorganizer.repository.PatientRepository;
import com.example.medicalorganizer.view.MedicalView;

// Controller responsible for handling all Patient CRUD operations
public class PatientController {

    private PatientRepository patientRepository;
    private MedicalView medicalView;

    public PatientController(PatientRepository patientRepository, MedicalView medicalView) {
        this.patientRepository = patientRepository;
        this.medicalView = medicalView;
    }

    public synchronized void addPatient(Patient patient) {
        Patient existingPatient = patientRepository.findById(patient.getId());
        if (existingPatient != null) {
            medicalView.showErrorMessage("Already existing patient with id " + patient.getId(), existingPatient);
            return;
        }

        patientRepository.save(patient);
        medicalView.addPatient(patient);
    }

    public void getAllPatients() {
        medicalView.displayPatients(patientRepository.findAll());
    }

    public synchronized void updatePatient(Patient patient) {
        Patient existingPatient = patientRepository.findById(patient.getId());
        if (existingPatient == null) {
            medicalView.showErrorMessage("No existing patient with id " + patient.getId(), existingPatient);
            return;
        }

        patientRepository.update(patient);
        medicalView.updatePatient(patient);
    }

    public synchronized void deletePatient(Patient patient) {
        if (patientRepository.findById(patient.getId()) == null) {
            medicalView.showErrorMessage("No existing patient with id " + patient.getId(), patient);
            return;
        }

        patientRepository.delete(patient.getId());
        medicalView.deletePatient(patient);
    }
}
