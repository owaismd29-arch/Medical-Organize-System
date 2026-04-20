package com.example.medicalorganizer.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.medicalorganizer.repository.mongo.PatientMongoRepository;
import com.example.medicalorganizer.view.swing.MedicalSwingView;
import com.example.medicalorganizer.controller.PatientController;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


@Command(mixinStandardHelpOptions = true)
public class MedicalSwingApp implements Callable<Void> {

    @Option(names = { "--mongo-host" }, description = "MongoDB host address")
    private String mongoHost = "localhost";

    @Option(names = { "--mongo-port" }, description = "MongoDB host port")
    private int mongoPort = 27017;

    @Option(names = { "--db-name" }, description = "Database name")
    private String databaseName = "medical";

    @Option(names = { "--db-collection" }, description = "Collection name")
    private String collectionName = "patient";

    public static void main(String[] args) {
        new CommandLine(new MedicalSwingApp()).execute(args);
    }

    @Override
    public Void call() throws Exception {
        EventQueue.invokeLater(() -> {
            try {
                PatientMongoRepository patientRepository = new PatientMongoRepository(
                        new MongoClient(new ServerAddress(mongoHost, mongoPort)), databaseName, collectionName);
                MedicalSwingView patientView = new MedicalSwingView();
                PatientController patientController = new PatientController(patientRepository, patientView);
                patientView.setPatientController(patientController);
                patientView.setVisible(true);
                patientController.getAllPatients();
            } catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception", e);
            }
        });
        return null;
    }
}
