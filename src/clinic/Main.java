package clinic;

import java.time.DayOfWeek;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // SINGLE INSTANCE (FIXED)
        AuthManager auth = new AuthManager();
        Clinic clinic = new Clinic("Amrita Clinic", auth);

        // ================= SAMPLE DOCTORS =================
        System.out.println("Initializing system...");

        if (auth.login("admin", "admin123")) { // temporary admin login to preload
            Doctor d1 = new Doctor("D101", "Arun", "Cardiology");
            Doctor d2 = new Doctor("D102", "Meena", "Dermatology");

            clinic.addDoctor(d1);
            clinic.addDoctor(d2);

            for (DayOfWeek day : DayOfWeek.values()) {
                d1.setAvailability(day, "10:00", "14:00");
                d1.setAvailability(day, "17:00", "19:00");
            }

            d2.setAvailability(DayOfWeek.MONDAY, "09:00", "13:00");

            auth.logout();
        }

        // ================= AUTH MENU =================
        System.out.println("\n===== CLINIC SYSTEM =====");
        System.out.println("1. Login");
        System.out.println("2. Signup");

        int option = sc.nextInt();
        sc.nextLine();

        // ================= SIGNUP =================
        if (option == 2) {

            System.out.println("\n===== SIGNUP =====");

            System.out.print("Username: ");
            String newUser = sc.nextLine();

            System.out.print("Password: ");
            String newPass = sc.nextLine();

            System.out.println("Select Role:");
            System.out.println("1. Patient");
            System.out.println("2. Doctor");
            System.out.println("3. Receptionist");

            int roleChoice = sc.nextInt();
            sc.nextLine();

            String role = switch (roleChoice) {
                case 1 -> "PATIENT";
                case 2 -> "DOCTOR";
                case 3 -> "RECEPTIONIST";
                default -> null;
            };

            if (role == null) {
                System.out.println("Invalid role.");
                return;
            }

            auth.registerUser(newUser, newPass, role);
        }

        // ================= LOGIN =================
        System.out.println("\n===== LOGIN =====");

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        if (!auth.login(username, password)) {
            System.out.println("Exiting...");
            return;
        }

        // ================= ROLE DASHBOARD =================
        while (true) {

            System.out.println("\n===== DASHBOARD (" + auth.getCurrentRole() + ") =====");

            // ================= ADMIN =================
            if (auth.isAdmin()) {

                System.out.println("1. Add Doctor");
                System.out.println("2. Register Patient");
                System.out.println("3. View Doctors");
                System.out.println("4. System Summary");
                System.out.println("5. Logout");

                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1 -> {
                        System.out.print("Doctor ID: ");
                        String id = sc.nextLine();

                        System.out.print("Name: ");
                        String name = sc.nextLine();

                        System.out.print("Specialization: ");
                        String spec = sc.nextLine();

                        clinic.addDoctor(new Doctor(id, name, spec));
                    }

                    case 2 -> {
                        System.out.print("Patient ID: ");
                        String pid = sc.nextLine();

                        System.out.print("Name: ");
                        String name = sc.nextLine();

                        System.out.print("Age: ");
                        int age = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Phone: ");
                        String phone = sc.nextLine();

                        clinic.registerPatient(new Patient(pid, name, age, phone));
                    }

                    case 3 -> clinic.listDoctors();
                    case 4 -> clinic.printSystemSummary();
                    case 5 -> { auth.logout(); return; }
                }
            }

            // ================= PATIENT =================
            else if (auth.isPatient()) {

                System.out.println("1. Book Appointment");
                System.out.println("2. Cancel Appointment");
                System.out.println("3. View Appointments");
                System.out.println("4. Logout");

                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1 -> {
                        System.out.print("Patient ID: ");
                        String pid = sc.nextLine();

                        System.out.print("Doctor ID: ");
                        String did = sc.nextLine();

                        System.out.print("Date: ");
                        String date = sc.nextLine();

                        System.out.print("Time: ");
                        String time = sc.nextLine();

                        clinic.bookAppointment(pid, did, date, time);
                    }

                    case 2 -> {
                        System.out.print("Appointment ID: ");
                        String id = sc.nextLine();
                        clinic.cancelAppointment(id);
                    }

                    case 3 -> {
                        System.out.print("Patient ID: ");
                        clinic.viewAppointments(sc.nextLine());
                    }

                    case 4 -> { auth.logout(); return; }
                }
            }

            // ================= DOCTOR =================
            else if (auth.isDoctor()) {

                System.out.println("1. View Appointments");
                System.out.println("2. Accept Appointment");
                System.out.println("3. Logout");

                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1 -> {
                        System.out.print("Doctor ID: ");
                        clinic.viewDoctorAppointments(sc.nextLine());
                    }

                    case 2 -> {
                        System.out.print("Appointment ID: ");
                        clinic.acceptAppointment(sc.nextLine());
                    }

                    case 3 -> { auth.logout(); return; }
                }
            }

            // ================= RECEPTIONIST =================
            else {

                System.out.println("1. View Doctors");
                System.out.println("2. Check Availability");
                System.out.println("3. Generate Report");
                System.out.println("4. Logout");

                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1 -> clinic.listDoctors();

                    case 2 -> {
                        System.out.print("Doctor ID: ");
                        String id = sc.nextLine();

                        System.out.print("Date: ");
                        String date = sc.nextLine();

                        clinic.showDoctorSlots(id, date);
                    }

                    case 3 -> {
                        System.out.print("Date: ");
                        clinic.generateDailyReport(sc.nextLine());
                    }

                    case 4 -> { auth.logout(); return; }
                }
            }
        }
    }
}