package clinic;

import java.util.*;

public class Clinic {

    private String clinicName;

    private Map<String, Doctor> doctors;
    private Map<String, Patient> patients;
    private Map<String, Appointment> appointments;

    private AuthManager auth;

    public Clinic(String clinicName, AuthManager auth) {

        this.clinicName = clinicName;
        this.auth = auth;

        doctors = new HashMap<>();
        patients = new HashMap<>();
        appointments = new HashMap<>();

        loadDoctors();
        loadPatients();
        loadAppointments();
    }

    // ================= LOAD =================

    private void loadDoctors() {

        for (String[] d : FileManager.loadDoctors().values()) {

            Doctor doc = new Doctor(d[0], d[1], d[2]);
            doctors.put(doc.getDoctorID(), doc);
        }
    }

    private void loadPatients() {

        for (String[] p : FileManager.loadPatients().values()) {

            Patient patient = new Patient(
                    p[0], p[1], p[2],
                    Integer.parseInt(p[3]), p[4]
            );

            patients.put(patient.getPatientID(), patient);
        }
    }

    private void loadAppointments() {

        for (Map.Entry<String, String[]> entry :
                FileManager.loadAppointments().entrySet()) {

            String[] a = entry.getValue();

            Appointment appt = new Appointment(
                    a[0], // ID
                    a[1], // patientID
                    a[2], // doctorID
                    a[3], // date
                    a[4], // time
                    a[5]  // status
            );

            appointments.put(a[0], appt);
        }
    }

    // ================= BOOK =================

    public Appointment manageBooking(String patientID,
                                     String doctorID,
                                     String date,
                                     String time) {

        for (Appointment a : appointments.values()) {

            if (a.getDoctorID().equals(doctorID) &&
                a.getDate().equals(date) &&
                a.getTime().equals(time)) {

                System.out.println("❌ Slot already booked.");
                return null;
            }
        }

        Patient patient = patients.get(patientID);
        Doctor doctor = doctors.get(doctorID);

        if (patient == null || doctor == null) {
            System.out.println("Invalid Patient or Doctor.");
            return null;
        }

        Appointment appt =
                new Appointment(patientID, doctorID, date, time);

        if (!doctor.acceptAppointment(appt)) {
            System.out.println("Slot not available.");
            return null;
        }

        appt.confirmAppointment();

        // ✅ SAFE SAVE (FileManager already blocks duplicates)
        appt.saveToFile();

        appointments.put(appt.getAppointmentID(), appt);

        System.out.println("✅ Appointment booked successfully!");

        return appt;
    }

    // ================= VIEW =================

    public void viewPatientAppointments(String patientID) {

        boolean found = false;

        for (Appointment a : appointments.values()) {

            if (a.getPatientID().equals(patientID)) {
                System.out.println(a);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments found.");
        }
    }

    public void viewDoctorAppointments(String doctorID) {

        boolean found = false;

        for (Appointment a : appointments.values()) {

            if (a.getDoctorID().equals(doctorID)) {
                System.out.println(a);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments found.");
        }
    }

    // ================= REPORT =================

    public void generateReports(String date) {

        System.out.println("\n===== DAILY REPORT =====");

        List<Appointment> list = new ArrayList<>();

        for (Appointment a : appointments.values()) {

            if (a.getDate().equals(date)) {
                list.add(a);
            }
        }

        if (list.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        
        list.sort(Comparator.comparing(Appointment::getTime));

        for (Appointment a : list) {
            System.out.println(a);
        }
    }

    // ================= HELPERS =================

    public void addPatientToSystem(Patient patient) {
        patients.put(patient.getPatientID(), patient);
    }

    public void listDoctors() {

        System.out.println("\n===== DOCTORS =====");

        for (Doctor d : doctors.values()) {
            System.out.println(d);
        }
    }

    public void addDoctor(Doctor doctor) {

        doctors.put(doctor.getDoctorID(), doctor);


        FileManager.saveDoctor(
                doctor.getDoctorID(),
                doctor.getName(),          
                doctor.getSpecialization() 
        );

        System.out.println("Doctor added successfully.");
    }

    public void showDoctorAvailability(String doctorID, String date) {

        Doctor doctor = doctors.get(doctorID);

        if (doctor == null) {
            System.out.println("Doctor not found.");
            return;
        }

        doctor.showAvailability(date);
    }
    public boolean cancelAppointment(String appointmentID) {

        Appointment appt = appointments.get(appointmentID);

        if (appt == null) return false;

        Doctor doctor = doctors.get(appt.getDoctorID());

        if (doctor != null) {
            doctor.cancelAppointment(appt);
        }

        appointments.remove(appointmentID);

        return true;
    }
}