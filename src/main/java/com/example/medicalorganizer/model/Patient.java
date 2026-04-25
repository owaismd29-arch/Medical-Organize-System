package com.example.medicalorganizer.model;

import java.util.Objects;
//Patient entity representing a medical patient record
public class Patient {
    private String id;
    private String name;
    private int age;
    private String diagnosis;
    private String doctorName;

    public Patient() {

    }

    public Patient(String id, String name, int age, String diagnosis, String doctorName) {
        this.setId(id);
        this.setName(name);
        this.setAge(age);
        this.setDiagnosis(diagnosis);
        this.setDoctorName(doctorName);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return age == patient.age &&
               Objects.equals(id, patient.id) &&
               Objects.equals(name, patient.name) &&
               Objects.equals(diagnosis, patient.diagnosis) &&
               Objects.equals(doctorName, patient.doctorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, diagnosis, doctorName);
    }

    @Override
    public String toString() {
        return id + " - " + name + " - " + age + " - " + diagnosis + " - " + doctorName;
    }
}
