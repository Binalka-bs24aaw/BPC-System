package BPC_MainSystem.controllers;

import BPC_MainSystem.models.Patient;
import BPC_MainSystem.filehandlers.PatientFile;
import java.util.ArrayList;
import java.util.Scanner;

public class PatientCtrl {

    static Scanner scanner = new Scanner(System.in);
    public static boolean patientExit;
    private static MainSystemCtrl mainsystemctrl;

    public PatientCtrl(MainSystemCtrl mainSystemCtrl) {
        this.scanner = new Scanner(System.in);
        this.patientExit = false;
    }

    public static void startPatient() {
        patientExit = false;
        while (!patientExit) {
            System.out.println("\nManage Patients:");
            System.out.println("1. Add a Patient");
            System.out.println("2. Remove a Patient");
            System.out.println("3. View All Patients");
            System.out.println("4. Return to Main Menu");
            System.out.print("Enter your choice: ");
            try{
                int choice = new java.util.Scanner(System.in).nextInt();

                switch (choice) {
                    case 1:
                        addPatient();
                        Exit();
                        break;
                    case 2:
                        deletePatient();
                        Exit();
                        break;
                    case 3:
                        AllPatient();
                        Exit();
                        break;
                    case 4:
                        patientExit = true;
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            }
            catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please enter a correct number:");
            }
        }

    }

    private static void AllPatient() {
        ArrayList<Patient> patients = PatientFile.readPatientstxt();
        if (!patients.isEmpty()) {
            System.out.println("\n View All Patients \n");
            System.out.printf("%-15s%-20s%-40s%-15s%n", "Patient ID Number", "Full Name", "Address", "Phone");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            for (Patient patient : patients) {
                System.out.printf("%-15s%-20s%-40s%-15s%n", patient.getId(), patient.getpName(), patient.getAddress(), patient.getPhone());
            }
            System.out.println("--------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println("Not found.");
        }
    }

    private static void deletePatient() {
        boolean deletePatient = true;
        while (deletePatient) {
            System.out.print("\nEnter the Patient ID to delete: ");
            String patientId = scanner.next();
            ArrayList<Patient> patients = PatientFile.readPatientstxt();
            Patient patientToDelete = null;

            for (Patient patient : patients) {
                if (patient.getId() == Integer.parseInt(patientId)) {
                    patientToDelete = patient;
                    break;
                }
            }

            if (patientToDelete != null) {
                System.out.println("\n Patient Details \n");
                System.out.println("--------------------------------------------------------------------------------------------------------");
                System.out.printf("%-15s%-20s%-40s%-15s%n", "Patient ID", "Patient Full Name", "Address", "Phone");
                System.out.println("--------------------------------------------------------------------------------------------------------");
                System.out.printf("%-15s%-20s%-40s%-15s%n", patientToDelete.getId(), patientToDelete.getpName(), patientToDelete.getAddress(), patientToDelete.getPhone());
                System.out.println("--------------------------------------------------------------------------------------------------------");

                System.out.print("Are you sure you want to delete this patient? (Y/N): ");
                String confirmation = scanner.next();

                if (confirmation.equalsIgnoreCase("y")) {
                    patients.remove(patientToDelete);
                    if (PatientFile.saveAllPatients(patients)) {
                        System.out.println("Patient deleted successfully!");
                    } else {
                        System.out.println("Error: not update the file.");
                    }
                } else {
                    System.out.println("Deletion canceled.");
                }
            } else {
                System.out.println("Patient with ID " + patientId + " not found.");
            }
            System.out.print("\nDo you want to delete another patient? (Y/N): ");
            String response = scanner.next();
            deletePatient = response.equalsIgnoreCase("y");
        }
    }

    private static void Exit() {
        System.out.print("\nDo you want to exit the program? (Y/N): ");
        String response = scanner.next();
        if (response.equalsIgnoreCase("y")) {
            mainsystemctrl.exit = true;
            patientExit = true;
            System.out.println("Exiting the program.");
        }

    }

    private static void addPatient() {
        try {
            int newId = generatePatientId();
            System.out.print("\nEnter Full Name: ");
            String fullName = scanner.next();
            System.out.print("Enter Address: ");
            scanner.nextLine();
            String address = scanner.nextLine();
            System.out.print("Enter Phone Number: ");
            String phone = scanner.next();

            Patient newPatient = new Patient(newId, fullName, address, phone);

            if (PatientFile.savePatient(newPatient)) {
                System.out.println("Patient added successfully ");
            } else {
                System.out.println("Error: Could not save the patient to the file.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred" + e.getMessage());
        }
    }

    private static int generatePatientId() {
        ArrayList<Patient> patients = PatientFile.readPatientstxt();
        int maxId = 0;
        for (Patient patient : patients) {
            try {
                int currentId = patient.getId();
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Patient ID format: " + patient.getId());
            }
        }
        return maxId + 1;
    }
}
