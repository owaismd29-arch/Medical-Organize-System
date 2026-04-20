package com.example.medicalorganizer.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.example.medicalorganizer.model.Patient;
import com.example.medicalorganizer.repository.PatientRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class PatientMongoRepository implements PatientRepository {

    private MongoCollection<Document> patientCollection;
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String DIAGNOSIS = "diagnosis";
    private static final String DOCTOR_NAME = "doctorName";

    public PatientMongoRepository(MongoClient client, String databaseName, String collectionName) {
        patientCollection = client
            .getDatabase(databaseName)
            .getCollection(collectionName);
    }

    @Override
    public void save(Patient patient) {
        patientCollection.insertOne(
            new Document()
                .append(ID, patient.getId())
                .append(NAME, patient.getName())
                .append(AGE, patient.getAge())
                .append(DIAGNOSIS, patient.getDiagnosis())
                .append(DOCTOR_NAME, patient.getDoctorName()));
    }

    @Override
    public List<Patient> findAll() {
        return StreamSupport
            .stream(patientCollection.find().spliterator(), false)
            .map(this::fromDocumentToPatient)
            .collect(Collectors.toList());
    }

    private Patient fromDocumentToPatient(Document d) {
        return new Patient(
            "" + d.get(ID),
            "" + d.get(NAME),
            ((Number) d.get(AGE)).intValue(),
            "" + d.get(DIAGNOSIS),
            "" + d.get(DOCTOR_NAME)
        );
    }

    @Override
    public Patient findById(String id) {
        Document d = patientCollection.find(Filters.eq("id", id)).first();
        if (d != null)
            return fromDocumentToPatient(d);
        return null;
    }

    @Override
    public void update(Patient patient) {
        Document updatedDocument = new Document()
            .append(NAME, patient.getName())
            .append(AGE, patient.getAge())
            .append(DIAGNOSIS, patient.getDiagnosis())
            .append(DOCTOR_NAME, patient.getDoctorName());

        patientCollection.updateOne(
            Filters.eq("id", patient.getId()),
            new Document("$set", updatedDocument)
        );
    }

    @Override
    public void delete(String id) {
        patientCollection.deleteOne(Filters.eq("id", id));
    }
}
