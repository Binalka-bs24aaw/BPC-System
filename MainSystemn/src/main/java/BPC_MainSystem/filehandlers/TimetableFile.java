package BPC_MainSystem.filehandlers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import BPC_MainSystem.models.Timetable;

public class TimetableFile {
    private static final String Timetable_FILE_PATH = "src/main/java/BPC_MainSystem/files/Timetable.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static List<Timetable> readTimetable() {
        File file = new File(Timetable_FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<Timetable>>() {});
        } catch (IOException e) {
            System.out.println("Error reading: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static boolean CancelSlot(String patientId, String bookingId) {
        List<Timetable> slots = readTimetable();

        Timetable slt = slots.stream()
                .filter(slot -> slot.getId() == Integer.parseInt(bookingId))
                .findFirst()
                .orElse(null);

        if(slt == null){
            System.out.println("No Appointment found with the booking ID " + bookingId);
            return false;
        }

        if(slt.getStatus().equalsIgnoreCase("Attended")){
            System.out.println("This slot is already attended");
            return false;
        }

        if(!slt.getStatus().equalsIgnoreCase("Booked") || slt.getPatientId() != Integer.parseInt(patientId)) {
            System.out.println("This slot is not booked by the patient Id " + patientId);
            return false;
        }

        return true;
    }

    public static boolean BookSlot(String patientId, String bookingId) {
        List<Timetable> slots = readTimetable();

        Timetable slt = slots.stream()
                .filter(slot -> slot.getId() == Integer.parseInt(bookingId))
                .findFirst()
                .orElse(null);

        if (slt == null) {
            System.out.println("No Appointment found with the booking ID " + bookingId);
            return false;
        }

        if (!(slt.getStatus().equalsIgnoreCase("Available") || slt.getStatus().equalsIgnoreCase("Cancelled"))) {
            System.out.println("This slot is already booked.");
            return false;
        }

        boolean dupBooking = slots.stream()
                .anyMatch(slot ->
                        slot.getPatientId() == Integer.parseInt(patientId) &&
                                slot.getDate().equals(slt.getDate()) &&
                                slot.getTime().equals(slt.getTime())
                );

        if (dupBooking) {
            System.out.println("You already have another booking at this time.");
            return false;
        }

        return true;
    }

    public static List<Timetable> getSlotsByPhysiotherapists(List<Integer> matchingPhysioIds) {
        List<Timetable> allSlots = readTimetable();

        return allSlots.stream()
                .filter(slot -> matchingPhysioIds.contains(slot.getPhysioId()))
                .collect(Collectors.toList());
    }

    public static List<Timetable> getSlotsByExpertiseArea(String expertise) {
        List<Timetable> allSlots = readTimetable();

        return allSlots.stream()
                .filter(slot -> slot.getExpertiseArea().toLowerCase().contains(expertise.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static boolean updateBookingTime(String patientId, String bookingId, String status) {
        List<Timetable> slots = readTimetable();
        boolean success = false;
        try{
            for (Timetable slot : slots) {
                if (slot.getId() == Integer.parseInt(bookingId)) {
                    slot.setPatientId(Integer.parseInt(patientId));
                    slot.setStatus(status);
                }
            }

            objectMapper.writeValue(new File(Timetable_FILE_PATH), slots);
            System.out.println("Booking successfully " + status);
            success = true;
            return success;
        }
        catch (IOException e) {
            System.out.println("Error reading timetable: " + e.getMessage());
            return false;
        }
    }

    public static boolean AttendSlot(String patientId, String bookingId) {
        List<Timetable> slots = readTimetable();

        Timetable selectedSlot = slots.stream()
                .filter(slot -> slot.getId() == Integer.parseInt(bookingId))
                .findFirst()
                .orElse(null);

        if(selectedSlot == null){
            System.out.println("No Appointment found with the booking ID " + bookingId);
            return false;
        }

        if(selectedSlot.getStatus().equalsIgnoreCase("Attended")){
            System.out.println("This slot is already attended");
            return false;
        }

        if(!selectedSlot.getStatus().equalsIgnoreCase("Booked") || selectedSlot.getPatientId() != Integer.parseInt(patientId)) {
            System.out.println("This slot is not booked by the patient Id " + patientId);
            return false;
        }

        return true;
    }
}
