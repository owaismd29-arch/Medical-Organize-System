// Repository interface for Patient CRUD operations
package com.example.medicalorganizer.repository;

import com.example.medicalorganizer.model.Patient;

import java.util.List;

public interface PatientRepository {
    void save(Patient patient);
    List<Patient> findAll();
    Patient findById(String id);
    void update(Patient patient);
    void delete(String id);
}
