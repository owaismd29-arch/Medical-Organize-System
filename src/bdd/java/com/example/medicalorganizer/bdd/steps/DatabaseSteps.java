package com.example.medicalorganizer.bdd.steps;

import java.util.List;

import org.bson.Document;

import com.example.medicalorganizer.bdd.MedicalSwingAppBDD;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;

public class DatabaseSteps {

    static final String DB_NAME = "test-db";
    static final String COLLECTION_NAME = "test-collection";

    static final String PATIENT_FIXTURE_1_ID = "1";
    static final String PATIENT_FIXTURE_1_NAME = "Owais";
    static final int PATIENT_FIXTURE_1_AGE = 30;
    static final String PATIENT_FIXTURE_1_DIAGNOSIS = "Flu";
    static final String PATIENT_FIXTURE_1_DOCTOR = "Dr. Ahmed";

    static final String PATIENT_FIXTURE_2_ID = "2";
    static final String PATIENT_FIXTURE_2_NAME = "Sara Malik";
    static final int PATIENT_FIXTURE_2_AGE = 25;
    static final String PATIENT_FIXTURE_2_DIAGNOSIS = "Cold";
    static final String PATIENT_FIXTURE_2_DOCTOR = "Dr. Sara";

    private MongoClient mongoClient;

    @Before
    public void setUp() {
        mongoClient = new MongoClient("localhost", MedicalSwingAppBDD.mongoPort);
        mongoClient.getDatabase(DB_NAME).drop();
    }

    @After
    public void tearDown() {
        mongoClient.close();
    }

    @Given("The database contains the patients with the following values")
    public void the_database_contains_the_patients_with_the_following_values(List<List<String>> values) {
        values.stream().skip(1).forEach(
            v -> addTestPatientToDatabase(v.get(0), v.get(1), Integer.parseInt(v.get(2)), v.get(3), v.get(4))
        );
    }

    @Given("The database contains a few patients")
    public void the_database_contains_a_few_patients() {
        addTestPatientToDatabase(PATIENT_FIXTURE_1_ID, PATIENT_FIXTURE_1_NAME, PATIENT_FIXTURE_1_AGE,
                PATIENT_FIXTURE_1_DIAGNOSIS, PATIENT_FIXTURE_1_DOCTOR);
        addTestPatientToDatabase(PATIENT_FIXTURE_2_ID, PATIENT_FIXTURE_2_NAME, PATIENT_FIXTURE_2_AGE,
                PATIENT_FIXTURE_2_DIAGNOSIS, PATIENT_FIXTURE_2_DOCTOR);
    }

    @Given("The patient is in the meantime removed from the database")
    public void the_patient_is_in_the_meantime_removed_from_the_database() {
        mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME)
                .deleteOne(Filters.eq("id", PATIENT_FIXTURE_1_ID));
    }

    private void addTestPatientToDatabase(String id, String name, int age, String diagnosis, String doctorName) {
        mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME).insertOne(new Document()
                .append("id", id).append("name", name).append("age", age)
                .append("diagnosis", diagnosis).append("doctorName", doctorName));
    }
}
