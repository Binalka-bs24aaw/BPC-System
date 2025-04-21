package BPC_MainSystem.controllers;

import BPC_MainSystem.filehandlers.PhysiotherapistsFile;
import BPC_MainSystem.models.Physiotherapists;

import java.util.*;

public class PhysioCtrl {
    static Scanner scanner = new Scanner(System.in);
    public static boolean physioExit;
    private static MainSystemCtrl mainsystemctrl;

    public PhysioCtrl(MainSystemCtrl mainSystemCtrl) {
        this.mainsystemctrl = mainsystemctrl;
        this.scanner = new Scanner(System.in);
        this.physioExit = false;
    }

    public static void startPhysio() {
        physioExit = false;
        while (!physioExit) {
            System.out.println("\nManage Physios:");
            System.out.println("1. Add a Physio");
            System.out.println("2. Remove a Physio");
            System.out.println("3. View All Physios");
            System.out.println("4. Return to Main Menu");
            System.out.print("Enter your choice: ");
            try{
                int choice = new java.util.Scanner(System.in).nextInt();

                switch (choice) {
                    case 1:
                        addPhysio();
                        Exit();
                        break;
                    case 2:
                        deletePhysio();
                        Exit();
                        break;
                    case 3:
                        allPhysio();
                        Exit();
                        break;
                    case 4:
                        physioExit = true;
                        break;
                    default:
                        System.out.println("Invalid. Please try again.");
                }
            }
            catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please enter a valid input.");
            }
        }

    }

    private static void Exit() {
        System.out.print("\nDo you want to exit the program? (Y/N): ");
        String response = scanner.next();
        if (response.equalsIgnoreCase("y")) {
            mainsystemctrl.exit = true;
            physioExit = true;
            System.out.println("Exiting the program");
        }
    }

    private static void allPhysio() {
        try{
            List<Physiotherapists> physios = PhysiotherapistsFile.readPhysio();

            if (physios != null && !physios.isEmpty()) {
                System.out.println("\n Physios Details \n");
                System.out.println("--------------------------------------------------------------------------------------------------------");
                System.out.printf("%-5s%-25s%-25s%-50s%n", "ID", "Full Name", "Expertise Area", "Treatments");
                System.out.println("--------------------------------------------------------------------------------------------------------");
                for (Physiotherapists physio : physios) {
                    boolean firstRow = true;
                    Map<String, List<String>> expertiseAreas = physio.getExpertiseAreas();
                    if (expertiseAreas.isEmpty()) {
                        System.out.printf("%-5s%-25s%-25s%-50s%n",
                                physio.getId(),
                                physio.getPhyName(),
                                "No Expertise Listed",
                                "N/A"
                        );
                        continue;
                    }

                    for (Map.Entry<String, List<String>> entry : physio.getExpertiseAreas().entrySet()) {
                        String expertiseArea = entry.getKey();
                        String treatments = String.join(", ", entry.getValue()); // Convert list to a string
                        if (firstRow) {
                            System.out.printf("%-5s%-25s%-25s%-50s%n",
                                    physio.getId(),
                                    physio.getPhyName(),
                                    expertiseArea,
                                    treatments);
                            firstRow = false;
                        } else {
                            System.out.printf("%-5s%-25s%-25s%-50s%n",
                                    "",
                                    "",
                                    expertiseArea,
                                    treatments);
                        }
                    }
                    System.out.println("\n");
                }
                System.out.println("--------------------------------------------------------------------------------------------------------");
            } else {
                System.out.println("No Physiotherapists found.");
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void deletePhysio() {
        boolean deletePhy = true;
        while (deletePhy) {
            System.out.print("\nEnter the Physio ID to delete: ");
            String physioId = scanner.next();
            List<Physiotherapists> physios = PhysiotherapistsFile.readPhysio();
            Physiotherapists physioDelete = null;

            for (Physiotherapists physio : physios) {
                if (physio.getId() == Integer.parseInt(physioId)) {
                    physioDelete = physio;
                    break;
                }
            }

            if (physioDelete != null) {
                System.out.println("\n Physio Details \n");
                System.out.println("--------------------------------------------------------------------------------------------------------");
                System.out.printf("%-15s%-20s%-40s%-15s%n", "ID", "Full Name", "Address", "Phone");
                System.out.println("--------------------------------------------------------------------------------------------------------");
                System.out.printf("%-15s%-20s%-40s%-15s%n", physioDelete.getId(), physioDelete.getPhyName(), physioDelete.getAddress(), physioDelete.getPhone());
                System.out.println("--------------------------------------------------------------------------------------------------------");
                System.out.print("Are you sure you want to delete this physio? (Y/N): ");
                String confirmation = scanner.next();

                if (confirmation.equalsIgnoreCase("y")) {
                    physios.remove(physioDelete);
                    if (PhysiotherapistsFile.savePhysio(physios)) {
                        System.out.println("Physio deleted successfully!");
                    } else {
                        System.out.println("Error: Could not update the file.");
                    }
                } else {
                    System.out.println("Physio deletion canceled.");
                }
            } else {
                System.out.println("Physio with ID " + physioId + " not found.");
            }
            System.out.print("\nDo you want to delete another physio? (Y/N): ");
            String response = scanner.next();
            deletePhy = response.equalsIgnoreCase("y");
        }
    }


    private static void addPhysio() {
        try{
            boolean addPhy = true;

            while (addPhy) {
                int physioId = generatePhysiotId();
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }

                System.out.print("\nEnter Full Name: ");
                String fullName = scanner.nextLine().trim();
                System.out.print("Enter Address: ");
                String address = scanner.nextLine().trim();
                System.out.print("Enter Phone Number: ");
                String phone = scanner.nextLine().trim();
                Map<String, List<String>> expertiseAreas = new HashMap<>();

                while (true) {
                    System.out.print("Enter Expertise Area (or type 'done' to finish): ");
                    String expertiseArea = scanner.nextLine().trim();

                    if (expertiseArea.equalsIgnoreCase("done")) break;
                    System.out.print("Enter Treatments for " + expertiseArea + " (comma-separated): ");
                    String[] treatmentsArray = scanner.nextLine().trim().split(",");

                    List<String> treatments = new ArrayList<>();
                    for (String treatment : treatmentsArray) {
                        treatments.add(treatment.trim());
                    }
                    expertiseAreas.put(expertiseArea, treatments);
                }
                Physiotherapists newPhysio = new Physiotherapists(physioId, fullName, address, phone, expertiseAreas);
                List<Physiotherapists> physios = PhysiotherapistsFile.readPhysio();
                physios.add(newPhysio);
                PhysiotherapistsFile.savePhysio(physios);
                System.out.println("\nPhysiotherapist added successfully!");
                addPhy = false;
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred" + e.getMessage());
        }
    }

    private static int generatePhysiotId() {
        List<Physiotherapists> physios = PhysiotherapistsFile.readPhysio();
        int maxId = 0;
        for (Physiotherapists physio : physios) {
            try {
                int currentId = physio.getId();
                if (currentId > maxId) {
                    maxId = currentId;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Physio ID format: " + physio.getId());
            }
        }
        return maxId + 1;
    }
}
