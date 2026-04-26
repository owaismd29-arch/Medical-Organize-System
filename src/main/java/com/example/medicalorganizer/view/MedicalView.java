// Swing-based GUI view for Medical Organize System
package com.example.medicalorganizer.view;

import java.util.List;

import com.example.medicalorganizer.model.Patient;

public interface MedicalView {
    void displayPatients(List<Patient> patients);
    void addPatient(Patient patient);
    void deletePatient(Patient patient);
    void updatePatient(Patient patient);
    void showErrorMessage(String message, Patient patient);
}
