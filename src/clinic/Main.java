package clinic;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        AuthManager auth = new AuthManager();
        Clinic clinic = new Clinic("Amrita Clinic", auth);

        System.out.println("=================================");
        System.out.println("   CLINIC MANAGEMENT SYSTEM");
        System.out.println("=================================");

        // ================= LOGIN / SIGNUP =================
        System.out.println("1. Login");
        System.out.println("2. Signup");

        int option = sc.nextInt();
        sc.nextLine();

        if (option == 2) {

            System.out.println("\n===== SIGNUP =====");

            System.out.print("Username: ");
            String user = sc.nextLine();

            System.out.print("Password: ");
            String pass = sc.nextLine();

            System.out.println("Role: PATIENT / DOCTOR / RECEPTIONIST");
            String role = sc.nextLine();

            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Phone: ");
            String phone = sc.nextLine();

            System.out.print("Age: ");
            int age = sc.nextInt();
            sc.nextLine();

            String extra = "";

            if (role.equalsIgnoreCase("PATIENT")) {
                System.out.print("Disease: ");
                extra = sc.nextLine();
            } else if (role.equalsIgnoreCase("DOCTOR")) {
                System.out.print("Specialization: ");
                extra = sc.nextLine();
            }

            auth.signup(user, pass, role, name, phone, age, extra);
        }

        // ================= LOGIN =================
        System.out.println("\n===== LOGIN =====");

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        if (!auth.login(username, password)) {
            System.out.println("Login failed.");
            return;
        }

        // ================= DASHBOARD =================
        while (true) {

            System.out.println("\n=================================");
            System.out.println("Logged in as: " + auth.getCurrentRole());
            System.out.println("=================================");

            // ================= ADMIN =================
            if (auth.isAdmin()) {

                System.out.println("1. Add Doctor");
                System.out.println("2. View Doctors");
                System.out.println("3. Exit");

                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {

                    case 1:
                        System.out.print("Doctor Name: ");
                        String dname = sc.nextLine();

                        System.out.print("Specialization: ");
                        String spec = sc.nextLine();

                        String did = FileManager.generateDoctorId();

                        Doctor doc = new Doctor(did, dname, spec);
                        clinic.addDoctor(doc);
                        break;

                    case 2:
                        clinic.listDoctors();
                        break;

                    case 3:
                        return;
                }
            }

            // ================= PATIENT =================
            else if (auth.isPatient()) {

                String pid = auth.getCurrentUserId();

                Patient patient = Patient.loadFromFile(pid);
                clinic.addPatientToSystem(patient);

                if (patient == null) {
                    System.out.println("Patient record not found.");
                    return;
                }

                System.out.println("\n1. Book Appointment");
                System.out.println("2. View My Appointments");
                System.out.println("3. Cancel Appointment");
                System.out.println("4. Exit");

                int ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {

                    case 1:
                        System.out.print("Enter Date (YYYY-MM-DD): ");
                        String date = sc.nextLine();

                        String doctorID =
                                FileManager.getDoctorByDisease(patient.getDisease());

                        if (doctorID == null) {
                            System.out.println("No doctor available.");
                            break;
                        }

                        System.out.println("Assigned Doctor ID: " + doctorID);

                        clinic.showDoctorAvailability(doctorID, date);

                        System.out.print("Choose Time (HH:MM): ");
                        String time = sc.nextLine();

                        patient.bookAppointment(clinic, date, time);
                        break;

                    case 2:
                        patient.viewAppointments(clinic);
                        break;

                    case 3:
                        System.out.print("Enter Appointment ID to cancel: ");
                        String aid = sc.nextLine();

                        patient.cancelAppointment(clinic, aid);
                        break;

                    case 4:
                        return;
                }
            }

            // ================= DOCTOR =================
            else if (auth.isDoctor()) {

                String did = auth.getCurrentUserId();

                System.out.println("\n1. View My Appointments");
                System.out.println("2. Exit");

                int ch = sc.nextInt();
                sc.nextLine();

                if (ch == 1) {
                    clinic.viewDoctorAppointments(did);
                } else return;
            }

            // ================= RECEPTIONIST =================
            else if (auth.isReceptionist()) {

                Receptionist r = new Receptionist(
                        "R1", "FrontDesk", "9999999999", clinic
                );

                System.out.println("1. Generate Daily Report");
                System.out.println("2. Exit");

                int ch = sc.nextInt();
                sc.nextLine();

                if (ch == 1) {

                    System.out.print("Enter Date: ");
                    String date = sc.nextLine();

                    r.generateDailySchedule(date);
                }

                else return;
            }
        }
    }
}