package BPC_MainSystem.filehandlers;

import BPC_MainSystem.models.Patient;

import java.io.*;
import java.util.ArrayList;

public class PatientFile {

    private static final String Patient_FILE_PATH = "src/main/java/BPC_MainSystem/files/patients.txt";

    public static boolean saveAllPatients(ArrayList<Patient> patients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Patient_FILE_PATH))) {
            for (Patient patient : patients) {
                String patientData = patient.getId() + "," + patient.getpName() + "," + patient.getAddress() + "," + patient.getPhone();
                writer.write(patientData);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving" + e.getMessage());
            return false;
        }
    }

    public static boolean savePatient(Patient patient) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Patient_FILE_PATH, true))) { // Append mode
            File file = new File(Patient_FILE_PATH);
            boolean isEmpty = !file.exists() || file.length() == 0;
            String patientData = patient.getId() + "," + patient.getpName() + "," + patient.getAddress() + "," + patient.getPhone();
            if (!isEmpty) {
                writer.newLine();
            }// Add a newline at the end
            writer.write(patientData);
            return true; // Successfully saved
        } catch (IOException e) {
            System.out.println("Error saving" + e.getMessage());
            return false; // Failed to save
        }
    }

    public static ArrayList<Patient> readPatientstxt() {
        ArrayList<Patient> patients = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Patient_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", 3);
                if (data.length == 3) {
                    int id = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    String address = data[2].substring(0, data[2].lastIndexOf(',')).trim();
                    String phone = data[2].substring(data[2].lastIndexOf(',') + 1).trim();
                    patients.add(new Patient(id, name, address, phone));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading" + e.getMessage());
        }
        return patients;
    }
}
