package BPC_MainSystem.controllers;

public class MainSystemCtrl {

    private PatientCtrl patientCtrl;
    private PhysioCtrl physioCtrl;
    private BookingCtrl bookingCtrl;
    public boolean exit;

    public void startMainSystem() {
        while (!exit) {

            System.out.println("Main Menu:");
            System.out.println("1. Manage Patients");
            System.out.println("2. Manage Physios");
            System.out.println("3. Book a Treatment Appointment");
            System.out.println("4. Change a Booking");
            System.out.println("5. Attend a Booking");
            System.out.println("6. Generate Reports");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            try{
                int choice = new java.util.Scanner(System.in).nextInt();

                switch (choice) {
                    case 1:
                        PatientCtrl.startPatient();
                        break;
                    case 2:
                        PhysioCtrl.startPhysio();
                        break;
                    case 3:
                        BookingCtrl.startBooking();
                        break;
                    case 4:
                        BookingCtrl.cancelBooking();
                        break;
                    case 5:
                        BookingCtrl.attendBooking();
                        break;
                    case 6:
                        BookingCtrl.startBooking();
                        break;
                    case 7:
                        System.out.println("Exiting the program");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please enter a valid input.");
            }

        }
    }

    public MainSystemCtrl() {
        this.patientCtrl = new PatientCtrl(this);
        this.physioCtrl = new PhysioCtrl(this);
        this.bookingCtrl = new BookingCtrl(this);
        this.exit = false;
    }

    public void TopMessage() {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("#         Welcome to Boost Physio Clinic!      #");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
    }
}
