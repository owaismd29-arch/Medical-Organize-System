package com.example.medicalorganizer.view.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.example.medicalorganizer.model.Patient;
import com.example.medicalorganizer.view.MedicalView;
import com.example.medicalorganizer.controller.PatientController;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MedicalSwingView extends JFrame implements MedicalView {

    private static final long serialVersionUID = 1L;
    private JTextField txtName;
    private JTextField txtID;
    private JTextField txtAge;
    private JTextField txtDiagnosis;
    private JTextField txtDoctorName;
    private JButton btnDeleteSelected;
    private JButton btnUpdateSelected;
    private JButton btnAdd;
    private JButton btnCancel;

    private JLabel lblErrorMessage;

    private JList<Patient> listPatients;
    private DefaultListModel<Patient> listPatientModel;

    private transient PatientController patientController;

    public DefaultListModel<Patient> getListPatientModel() {
        return listPatientModel;
    }

    public void setPatientController(PatientController patientController) {
        this.patientController = patientController;
    }

    public MedicalSwingView() {
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Medical Organize System");
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 45, 0, 770, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 173, 0, 23, 0 };
        gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
        getContentPane().setLayout(gridBagLayout);

        JLabel lblNewLabel_1 = new JLabel("id");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 1;
        gbc_lblNewLabel_1.gridy = 0;
        getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);

        txtID = new JTextField();
        txtID.setName("idTextBox");
        GridBagConstraints gbc_textField_1 = new GridBagConstraints();
        gbc_textField_1.insets = new Insets(0, 0, 5, 5);
        gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_1.gridx = 2;
        gbc_textField_1.gridy = 0;
        getContentPane().add(txtID, gbc_textField_1);
        txtID.setColumns(10);

        JLabel lblNewLabel = new JLabel("Name");
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel.gridx = 1;
        gbc_lblNewLabel.gridy = 1;
        getContentPane().add(lblNewLabel, gbc_lblNewLabel);

        txtName = new JTextField();
        txtName.setName("nameTextBox");
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 5, 5);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 2;
        gbc_textField.gridy = 1;
        getContentPane().add(txtName, gbc_textField);
        txtName.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("Age");
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_2.gridx = 1;
        gbc_lblNewLabel_2.gridy = 2;
        getContentPane().add(lblNewLabel_2, gbc_lblNewLabel_2);

        txtAge = new JTextField();
        txtAge.setName("ageTextBox");
        GridBagConstraints gbc_textField_2 = new GridBagConstraints();
        gbc_textField_2.insets = new Insets(0, 0, 5, 5);
        gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_2.gridx = 2;
        gbc_textField_2.gridy = 2;
        getContentPane().add(txtAge, gbc_textField_2);
        txtAge.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("Diagnosis");
        GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
        gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_3.gridx = 1;
        gbc_lblNewLabel_3.gridy = 3;
        getContentPane().add(lblNewLabel_3, gbc_lblNewLabel_3);

        txtDiagnosis = new JTextField();
        txtDiagnosis.setName("diagnosisTextBox");
        GridBagConstraints gbc_textField_3 = new GridBagConstraints();
        gbc_textField_3.insets = new Insets(0, 0, 5, 5);
        gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_3.gridx = 2;
        gbc_textField_3.gridy = 3;
        getContentPane().add(txtDiagnosis, gbc_textField_3);
        txtDiagnosis.setColumns(10);

        JLabel lblNewLabel_4 = new JLabel("Doctor Name");
        GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
        gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_4.gridx = 1;
        gbc_lblNewLabel_4.gridy = 4;
        getContentPane().add(lblNewLabel_4, gbc_lblNewLabel_4);

        txtDoctorName = new JTextField();
        txtDoctorName.setName("doctorNameTextBox");
        GridBagConstraints gbc_textField_4 = new GridBagConstraints();
        gbc_textField_4.insets = new Insets(0, 0, 5, 5);
        gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField_4.gridx = 2;
        gbc_textField_4.gridy = 4;
        getContentPane().add(txtDoctorName, gbc_textField_4);
        txtDoctorName.setColumns(10);

        btnAdd = new JButton("Add Patient");
        btnAdd.setEnabled(false);
        btnAdd.setName("btnAdd");
        btnAdd.addActionListener(e -> new Thread(() -> patientController
            .addPatient(new Patient(txtID.getText(), txtName.getText(),
                Integer.parseInt(txtAge.getText()),
                txtDiagnosis.getText(), txtDoctorName.getText())))
            .start());

        GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
        gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
        gbc_btnNewButton_2.gridx = 2;
        gbc_btnNewButton_2.gridy = 5;
        getContentPane().add(btnAdd, gbc_btnNewButton_2);

        btnCancel = new JButton("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setName("btnCancel");
        btnCancel.addActionListener(e -> {
            txtID.setText("");
            txtName.setText("");
            txtAge.setText("");
            txtDiagnosis.setText("");
            txtDoctorName.setText("");

            btnUpdateSelected.setEnabled(false);
            btnDeleteSelected.setEnabled(false);

            listPatients.clearSelection();

            txtID.setEnabled(true);
        });
        GridBagConstraints gbc_btnCancel = new GridBagConstraints();
        gbc_btnCancel.insets = new Insets(0, 0, 5, 5);
        gbc_btnCancel.gridx = 1;
        gbc_btnCancel.gridy = 5;
        getContentPane().add(btnCancel, gbc_btnCancel);

        btnDeleteSelected = new JButton("Delete Selected");
        btnDeleteSelected.setEnabled(false);
        btnDeleteSelected.setName("btnDeleteSelected");
        btnDeleteSelected.addActionListener(
            e -> new Thread(() -> patientController.deletePatient(listPatients.getSelectedValue())).start());
        GridBagConstraints gbc_btnDeleteSelected = new GridBagConstraints();
        gbc_btnDeleteSelected.insets = new Insets(0, 0, 5, 5);
        gbc_btnDeleteSelected.gridx = 2;
        gbc_btnDeleteSelected.gridy = 7;
        getContentPane().add(btnDeleteSelected, gbc_btnDeleteSelected);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridwidth = 3;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 6;
        getContentPane().add(scrollPane, gbc_scrollPane);

        listPatientModel = new DefaultListModel<>();
        listPatients = new JList<>(listPatientModel);
        listPatients.addListSelectionListener(e -> {
            boolean isPatientSelected = listPatients.getSelectedIndex() != -1;

            btnDeleteSelected.setEnabled(isPatientSelected);
            btnCancel.setEnabled(isPatientSelected);
            btnUpdateSelected.setEnabled(isPatientSelected);

            if (isPatientSelected) {
                Patient selectedPatient = listPatients.getSelectedValue();

                txtID.setText(selectedPatient.getId());
                txtName.setText(selectedPatient.getName());
                txtAge.setText(String.valueOf(selectedPatient.getAge()));
                txtDiagnosis.setText(selectedPatient.getDiagnosis());
                txtDoctorName.setText(selectedPatient.getDoctorName());

                txtID.setEnabled(false);
                btnAdd.setEnabled(false);

            } else {
                txtID.setText("");
                txtName.setText("");
                txtAge.setText("");
                txtDiagnosis.setText("");
                txtDoctorName.setText("");

                txtID.setEnabled(true);
            }
        });
        listPatients.setCellRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                Patient patient = (Patient) value;
                return super.getListCellRendererComponent(list, getDisplayString(patient), index, isSelected,
                        cellHasFocus);
            }
        });
        scrollPane.setViewportView(listPatients);
        listPatients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPatients.setName("patientList");

        btnUpdateSelected = new JButton("Update Selected");
        btnUpdateSelected.setName("btnUpdateSelected");
        btnUpdateSelected.setEnabled(false);
        btnUpdateSelected.addActionListener(e -> new Thread(() -> {
            String id = txtID.getText();
            String name = txtName.getText();
            int age = Integer.parseInt(txtAge.getText());
            String diagnosis = txtDiagnosis.getText();
            String doctorName = txtDoctorName.getText();

            Patient updatedPatient = new Patient(id, name, age, diagnosis, doctorName);
            patientController.updatePatient(updatedPatient);
        }).start());
        GridBagConstraints gbc_btnUpdateSelected;
        gbc_btnUpdateSelected = new GridBagConstraints();
        gbc_btnUpdateSelected.insets = new Insets(0, 0, 5, 5);
        gbc_btnUpdateSelected.gridx = 1;
        gbc_btnUpdateSelected.gridy = 7;
        getContentPane().add(btnUpdateSelected, gbc_btnUpdateSelected);

        lblErrorMessage = new JLabel("");
        lblErrorMessage.setName("errorMessageLabel");
        GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
        gbc_lblErrorMessage.gridwidth = 3;
        gbc_lblErrorMessage.insets = new Insets(0, 0, 0, 5);
        gbc_lblErrorMessage.gridx = 0;
        gbc_lblErrorMessage.gridy = 8;
        getContentPane().add(lblErrorMessage, gbc_lblErrorMessage);

        KeyAdapter btnAddEnabler = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                btnAdd.setEnabled(listPatients.getSelectedIndex() == -1
                        && !txtID.getText().trim().isEmpty()
                        && !txtName.getText().trim().isEmpty()
                        && !txtAge.getText().trim().isEmpty()
                        && !txtDiagnosis.getText().trim().isEmpty()
                        && !txtDoctorName.getText().trim().isEmpty());
            }
        };

        txtID.addKeyListener(btnAddEnabler);
        txtName.addKeyListener(btnAddEnabler);
        txtAge.addKeyListener(btnAddEnabler);
        txtDiagnosis.addKeyListener(btnAddEnabler);
        txtDoctorName.addKeyListener(btnAddEnabler);
    }

    @Override
    public void displayPatients(List<Patient> patients) {
        patients.stream().forEach(listPatientModel::addElement);
    }

    @Override
    public void addPatient(Patient patient) {
        SwingUtilities.invokeLater(() -> {
            listPatientModel.addElement(patient);
            resetErrorLabel();
        });
    }

    @Override
    public void deletePatient(Patient patient) {
        SwingUtilities.invokeLater(() -> {
            listPatientModel.removeElement(patient);
            resetErrorLabel();
        });
    }

    @Override
    public void updatePatient(Patient patient) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < listPatientModel.size(); i++) {
                if (listPatientModel.get(i).getId().equals(patient.getId())) {
                    listPatientModel.set(i, patient);
                    break;
                }
            }
            resetErrorLabel();
        });
    }

    @Override
    public void showErrorMessage(String message, Patient patient) {
        SwingUtilities.invokeLater(() -> lblErrorMessage.setText(message + ": " + getDisplayString(patient)));
    }

    private void resetErrorLabel() {
        lblErrorMessage.setText(" ");
    }

    private String getDisplayString(Patient patient) {
        return patient.getId() + " - " + patient.getName() + " - " + patient.getAge() + " - "
                + patient.getDiagnosis() + " - " + patient.getDoctorName();
    }
}
