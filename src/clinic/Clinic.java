package clinic;

import java.util.*;

/*
 * =====================================================
 * CLINIC SYSTEM CORE (MAIN CONTROLLER)
 * =====================================================
 * Responsibilities:
 * - Manage Patients, Doctors, Appointments
 * - Enforce Role-Based Access (via AuthManager)
 * - Execute Use-Cases (from UML)
 * - Match Sequence Diagrams
 * =====================================================
 */

public class Clinic {

    private String clinicName;

    private Map<String, Patient> patients;
    private Map<String, Doctor> doctors;
    private Map<String, Appointment> appointments;

    private Schedule schedule;
    private AuthManager auth;

    // ================= CONSTRUCTOR =================
    public Clinic(String clinicName, AuthManager auth) {
        this.clinicName = clinicName;
        this.auth = auth;

        patients = new HashMap<>();
        doctors = new HashMap<>();
        appointments = new HashMap<>();
        schedule = new Schedule();
    }

    // =====================================================
    // ================= ADMIN FUNCTIONS ====================
    // =====================================================

    // Register Patient
    public void registerPatient(Patient patient) {

        if (!auth.isAdmin()) {
            System.out.println("Access Denied: Only ADMIN can register patients.");
            return;
        }

        patients.put(patient.getPatientId(), patient);

        System.out.println("Patient Registered: " + patient.getName());
    }

    // Add Doctor
    public void addDoctor(Doctor doctor) {

        if (!auth.isAdmin()) {
            System.out.println("Access Denied: Only ADMIN can add doctors.");
            return;
        }

        doctors.put(doctor.getDoctorId(), doctor);
        schedule.addDoctor(doctor);

        System.out.println("Doctor Added: Dr. " + doctor.getName());
    }

    // =====================================================
    // ================= PATIENT FUNCTIONS ==================
    // =====================================================

    public Appointment bookAppointment(String patientId, String doctorId, String date, String time) {

        if (!auth.isPatient()) {
            System.out.println("Access Denied: Only PATIENT can book.");
            return null;
        }

        Patient patient = patients.get(patientId);
        Doctor doctor = doctors.get(doctorId);

        if (patient == null || doctor == null) {
            System.out.println("Invalid Patient or Doctor.");
            return null;
        }

        // Step 1: Check Availability (Sequence Diagram)
        if (!schedule.checkAvailability(doctor, date, time)) {
            System.out.println("Slot not available.");
            return null;
        }

        // Step 2: Create Appointment
        Appointment appointment = new Appointment(patient, doctor, date, time);

        // Step 3: Doctor Accepts
        if (!doctor.acceptAppointment(appointment)) {
            System.out.println("Doctor rejected appointment.");
            return null;
        }

        // Step 4: Reserve Slot
        schedule.reserveSlot(doctor, date, time);

        // Step 5: Store
        appointments.put(appointment.getAppointmentId(), appointment);

        System.out.println("Appointment Booked Successfully!");
        return appointment;
    }

    // Cancel Appointment
    public void cancelAppointment(String appointmentId) {

        if (!auth.isPatient()) {
            System.out.println("Access Denied: Only PATIENT can cancel.");
            return;
        }

        Appointment appt = appointments.get(appointmentId);

        if (appt == null) {
            System.out.println("Appointment not found.");
            return;
        }

        // Sequence Diagram Logic
        appt.cancel();

        Doctor doctor = appt.getDoctor();
        doctor.cancelAppointment(appt);

        schedule.releaseSlot(doctor, appt.getDate(), appt.getTime());

        System.out.println("Appointment Cancelled Successfully.");
    }

    // View Appointments
    public void viewAppointments(String patientId) {

        if (!auth.isPatient()) {
            System.out.println("Access Denied.");
            return;
        }

        for (Appointment a : appointments.values()) {
            if (a.getPatient().getPatientId().equals(patientId)) {
                System.out.println(a);
            }
        }
    }

    // =====================================================
    // ================= DOCTOR FUNCTIONS ===================
    // =====================================================

    // Accept Appointment
    public void acceptAppointment(String appointmentId) {

        if (!auth.isDoctor()) {
            System.out.println("Access Denied: Only DOCTOR.");
            return;
        }

        Appointment appt = appointments.get(appointmentId);

        if (appt == null) {
            System.out.println("Appointment not found.");
            return;
        }

        appt.confirm();
        System.out.println("Appointment Accepted.");
    }

    // Update Availability
    public void updateDoctorAvailability(String doctorId, String date, String time) {

        if (!auth.isDoctor()) {
            System.out.println("Access Denied.");
            return;
        }

        Doctor doctor = doctors.get(doctorId);

        if (doctor == null) {
            System.out.println("Doctor not found.");
            return;
        }

        schedule.addCustomSlot(doctor, date, time);

        System.out.println("Availability Updated.");
    }

    // View Doctor Appointments
    public void viewDoctorAppointments(String doctorId) {

        if (!auth.isDoctor()) {
            System.out.println("Access Denied.");
            return;
        }

        for (Appointment a : appointments.values()) {
            if (a.getDoctor().getDoctorId().equals(doctorId)) {
                System.out.println(a);
            }
        }
    }

    // =====================================================
    // ================= RECEPTIONIST =======================
    // =====================================================

    public void generateDailyReport(String date) {

        if (!auth.isReceptionist()) {
            System.out.println("Access Denied.");
            return;
        }

        System.out.println("\n===== DAILY REPORT =====");

        boolean found = false;

        for (Appointment a : appointments.values()) {

            if (a.getDate().equals(date)) {
                System.out.println(a);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments.");
        }
    }

    // =====================================================
    // ================= GENERAL ============================
    // =====================================================

    public void listDoctors() {

        System.out.println("\n--- Doctors ---");

        for (Doctor d : doctors.values()) {
            System.out.println("ID: " + d.getDoctorId() +
                    " | Name: Dr. " + d.getName() +
                    " | Spec: " + d.getSpecialization());
        }
    }

    public void showDoctorSlots(String doctorId, String date) {

        Doctor doctor = doctors.get(doctorId);

        if (doctor == null) {
            System.out.println("Doctor not found.");
            return;
        }

        schedule.printAvailableSlots(doctor, date);
    }

    // =====================================================
    // ================= DEBUG / UTILITY ====================
    // =====================================================

    public void printAllAppointments() {

        System.out.println("\n=== ALL APPOINTMENTS ===");

        for (Appointment a : appointments.values()) {
            System.out.println(a);
        }
    }

    public void printSystemSummary() {

        System.out.println("\n===== SYSTEM SUMMARY =====");

        System.out.println("Clinic: " + clinicName);
        System.out.println("Total Patients: " + patients.size());
        System.out.println("Total Doctors: " + doctors.size());
        System.out.println("Total Appointments: " + appointments.size());
    }

    // =====================================================
    // ================= GETTERS ============================
    // =====================================================

    public Patient getPatient(String id) {
        return patients.get(id);
    }

    public Doctor getDoctor(String id) {
        return doctors.get(id);
    }

    public Map<String, Appointment> getAppointments() {
        return appointments;
    }
}