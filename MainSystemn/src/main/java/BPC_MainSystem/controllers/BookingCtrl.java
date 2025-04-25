package BPC_MainSystem.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import BPC_MainSystem.models.*;
import BPC_MainSystem.filehandlers.*;



public class BookingCtrl {

    private static MainSystemCtrl mainsystemctrl;
    static Scanner scanner = new Scanner(System.in);
    public static boolean backBooking;
    public static boolean backCancelMenu;
    public static boolean backAttendMenu;

    public BookingCtrl(MainSystemCtrl mainSystemCtrl) {
        this.mainsystemctrl = mainsystemctrl;
        this.scanner = new Scanner(System.in);
        this.backBooking = false;
        this.backCancelMenu = false;
        this.backAttendMenu = false;
    }

    public static void startBooking() {
        backBooking = false;
        while (!backBooking) {
            System.out.println("\nBook an Appointment:");
            System.out.println("1. Do You Want To Search by Expertise Area");
            System.out.println("2. Do You Want To Search by Physiotherapist Name");
            System.out.println("3. Return to Main Menu");
            System.out.print("Enter your choice: ");
            try{
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        forExpertiseArea();
                        if(backCancelMenu){
                            toExit();
                        }
                        break;
                    case 2:
                        forPhysioId();
                        if(backCancelMenu){
                            toExit();
                        }
                        break;
                    case 3:
                        backBooking = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
            catch (Exception e) {
                System.out.println("Please enter a valid input. " + e.getMessage());
            }
        }
    }

    private static void toExit() {
        System.out.print("\nDo you want to exit the program? (Y/N): ");
        String response = scanner.next();
        if (response.equalsIgnoreCase("y")) {
            mainsystemctrl.exit = true;
            backBooking = true;
            backCancelMenu = true;
            System.out.println("Exiting the program. Goodbye!");
        }
    }

    private static void forPhysioId() {
        try{
            List<Timetable> availableSlots;
            List<Physiotherapists> physios = PhysiotherapistsFile.readPhysio();

            System.out.print("Enter Physio Name: ");
            String physioName = scanner.nextLine().trim();
            List<Integer> matching = new ArrayList<>();
            for (Physiotherapists physio : physios) {
                if (physio.getPhyName().toLowerCase().contains(physioName.toLowerCase())) {
                    matching.add(physio.getId());
                }
            }
            if(matching.size()>0){
                availableSlots = TimetableFile.getSlotsByPhysiotherapists(matching);

                if(availableSlots.size()>0){
                    // Display available appointments
                    System.out.println("\nAvailable Slots for " + physioName);
                    showingSlots(physioName, availableSlots);

                    System.out.print("\nDo you want to book an appointment? (Y/N): ");
                    String responseBooking = scanner.next();
                    if (responseBooking.equalsIgnoreCase("y")) {
                        Modification("Booked");
                    }
                }
                else{
                    System.out.println("\nNo Available Slots for " + physioName);
                }
            }
            else{
                System.out.println("No physiotherapists found with the name " + physioName );
            }

        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void attendBooking() {
        backAttendMenu = false;
        while (!backAttendMenu) {
            System.out.println("\nAttend an Appointment:");
            Modification("Attended");
            System.out.print("\nDo you want to attend another appointment (Y/N): ");
            String responseCancel = scanner.next();
            if (responseCancel.equalsIgnoreCase("n")) {
                backAttendMenu = true;
                backBooking = true;
                break;
            }
        }

    }

    private static void forExpertiseArea() {
        try{
            List<Timetable> availableSlots;

            System.out.print("Enter Expertise Area: ");
            String expertise = scanner.nextLine().trim();
            availableSlots = TimetableFile.getSlotsByExpertiseArea(expertise);

            if(availableSlots.size()>0){
                System.out.println("\nAvailable Slots for " + expertise);
                showingSlots(expertise, availableSlots);

                System.out.print("\nNeed to book an appointment? (Y/N): ");
                String responseBooking = scanner.next();
                if (responseBooking.equalsIgnoreCase("y")) {
                    Modification("Booked");
                }
            }
            else{
                System.out.println("\nNo Slots for " + expertise);
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void Modification(String status) {
        try{
            System.out.print("Enter Booking Id: ");
            String bookingID = scanner.next();

            System.out.print("Enter Patient Id: ");
            String patientId = scanner.next();
            ArrayList<Patient> patients = PatientFile.readPatientstxt();
            Patient patientToBook = null;
            for (Patient patient : patients) {
                if (patient.getId() == Integer.parseInt(patientId)) {
                    patientToBook = patient;
                    break;
                }
            }

            if (patientToBook != null) {
                if(status == "Booked" && TimetableFile.BookSlot(patientId, bookingID) ){
                    //only used for booking an appointment only. This will validates the duplicate booking
                    if(TimetableFile.updateBookingTime(patientId, bookingID, status)){
                        List<Timetable> slots = TimetableFile.readTimetable();
                        for (Timetable slot : slots) {
                            if (slot.getId() == Integer.parseInt(bookingID)) {
                                List<Timetable> slotList = Collections.singletonList(slot);
                                System.out.println("\nBooking Details");
                                showingSlots("Booking Details", slotList);
                            }
                        }
                    }
                }
                else if(status == "Cancelled" && TimetableFile.CancelSlot(patientId, bookingID) ){
                    if(TimetableFile.updateBookingTime("0", bookingID, status)){
                        List<Timetable> slots = TimetableFile.readTimetable();
                        for (Timetable slot : slots) {
                            if (slot.getId() == Integer.parseInt(bookingID)) {
                                List<Timetable> slotList = Collections.singletonList(slot);
                                System.out.println("\nBooking Details");
                                showingSlots("Booking Details", slotList);
                            }
                        }
                        System.out.print("\nDo you want to book an appointment again? (Y/N): ");
                        String responseReBooking = scanner.next();
                        if (responseReBooking.equalsIgnoreCase("y")) {
                            backBooking = false;
                            startBooking();
                        }
                    }
                }
                else if(status == "Attended" && TimetableFile.AttendSlot(patientId, bookingID)){
                    if(TimetableFile.updateBookingTime(patientId, bookingID, status)){
                        List<Timetable> slots = TimetableFile.readTimetable();
                        for (Timetable slot : slots) {
                            if (slot.getId() == Integer.parseInt(bookingID)) {
                                List<Timetable> slotList = Collections.singletonList(slot);
                                System.out.println("\nBooking Details");
                                showingSlots("Booking Details", slotList);
                            }
                        }
                    }
                }
            }
            else{
                System.out.println("No patient found with the ID " + patientId);
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void showingSlots(String expertise, List<Timetable> Slots) {
        try{
            List<Physiotherapists> physios = PhysiotherapistsFile.readPhysio();

            System.out.printf("%-5s%-25s%-30s%-30s%-15s%-15s%-15s%n", "ID", "Physio ID", "Expertise", "Treatment", "Date", "Time", "Status");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
            for (int i = 0; i < Slots.size(); i++) {
                Timetable slot = Slots.get(i);
                String physioName = "Unknown";
                for (Physiotherapists physio : physios) {
                    if (physio.getId() == slot.getPhysioId()) {
                        physioName = physio.getPhyName();
                        break;
                    }
                }

                System.out.printf("%-5d%-25s%-30s%-30s%-15s%-15s%-15s%n",
                        slot.getId(), physioName, slot.getExpertiseArea(), slot.getTreatment(),
                        slot.getDate(),slot.getTime(), slot.getStatus());
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void cancelBooking() {
        backCancelMenu = false;
        while (!backCancelMenu) {
            System.out.println("\nCancel an Appointment:");

            Modification("Cancelled");
            System.out.print("\nDo you want to cancel another appointment (Y/N): ");
            String responseAttend = scanner.next();
            if (responseAttend.equalsIgnoreCase("n")) {
                backCancelMenu = true;
                backBooking = true;
                break;
            }
        }
    }


}
