package com.example.medicalorganizer.repository.mongo;

import static org.assertj.core.api.Assertions.*;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.medicalorganizer.model.Patient;


public class PatientMongoRepositoryTest {

    private static MongoServer server;
    private static InetSocketAddress serverAddress;

    private MongoClient client;
    private PatientMongoRepository patientMongoRepository;
    private MongoCollection<Document> patientCollection;

    public static final String PATIENT_COLLECTION_NAME = "patient";
    public static final String MEDICAL_DB_NAME = "medical";

    @BeforeClass
    public static void setupServer() {
        server = new MongoServer(new MemoryBackend());
        serverAddress = server.bind();
    }

    @AfterClass
    public static void shutdownServer() {
        server.shutdown();
    }

    @Before
    public void setup() {
        client = new MongoClient(new ServerAddress(serverAddress));
        patientMongoRepository = new PatientMongoRepository(client, MEDICAL_DB_NAME, PATIENT_COLLECTION_NAME);
        MongoDatabase database = client.getDatabase(MEDICAL_DB_NAME);
        database.drop();
        patientCollection = database.getCollection(PATIENT_COLLECTION_NAME);
    }

    @After
    public void tearDown() {
        client.close();
    }

    @Test
    public void testFindAllWhenDatabaseIsEmpty() {
        assertThat(patientMongoRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindAllWhenDatabaseIsNotEmpty() {
        addTestPatientToDatabase("1", "Owais", 30, "Flu", "Dr. Ahmed");
        addTestPatientToDatabase("2", "Sara Malik", 25, "Cold", "Dr. Sara");
        assertThat(patientMongoRepository.findAll())
            .containsExactly(
                new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed"),
                new Patient("2", "Sara Malik", 25, "Cold", "Dr. Sara"));
    }

    @Test
    public void testFindByIdNotFound() {
        assertThat(patientMongoRepository.findById("1"))
            .isNull();
    }

    @Test
    public void testFindByIdFound() {
        addTestPatientToDatabase("1", "Owais", 30, "Flu", "Dr. Ahmed");
        addTestPatientToDatabase("2", "Sara Malik", 25, "Cold", "Dr. Sara");
        assertThat(patientMongoRepository.findById("2"))
            .isEqualTo(new Patient("2", "Sara Malik", 25, "Cold", "Dr. Sara"));
    }

    @Test
    public void testSave() {
        Patient patient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        patientMongoRepository.save(patient);
        assertThat(readAllPatientFromDatabase())
            .containsExactly(patient);
    }

    @Test
    public void testDelete() {
        addTestPatientToDatabase("1", "Owais", 30, "Flu", "Dr. Ahmed");
        patientMongoRepository.delete("1");
        assertThat(readAllPatientFromDatabase())
            .isEmpty();
    }

    @Test
    public void testUpdate() {
        Patient originalPatient = new Patient("1", "Owais", 30, "Flu", "Dr. Ahmed");
        patientMongoRepository.save(originalPatient);

        assertThat(readAllPatientFromDatabase()).containsExactly(originalPatient);

        Patient updatedPatient = new Patient("1", "Owais", 31, "Fever", "Dr. Ahmed");
        patientMongoRepository.update(updatedPatient);
        assertThat(readAllPatientFromDatabase()).containsExactly(updatedPatient);
    }

    private void addTestPatientToDatabase(String id, String name, int age, String diagnosis, String doctorName) {
        patientCollection.insertOne(
            new Document()
                .append("id", id)
                .append("name", name)
                .append("age", age)
                .append("diagnosis", diagnosis)
                .append("doctorName", doctorName));
    }

    private List<Patient> readAllPatientFromDatabase() {
        return StreamSupport
            .stream(patientCollection.find().spliterator(), false)
            .map(d -> new Patient(
                "" + d.get("id"),
                "" + d.get("name"),
                ((Number) d.get("age")).intValue(),
                "" + d.get("diagnosis"),
                "" + d.get("doctorName")))
            .collect(Collectors.toList());
    }
}
