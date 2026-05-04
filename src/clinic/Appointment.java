package clinic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/*
 * ============================================================
 * APPOINTMENT ENTITY (CORE DOMAIN OBJECT)
 * ============================================================
 * Responsibilities:
 * - Represent a booking between Patient and Doctor
 * - Maintain lifecycle: PENDING → CONFIRMED → CANCELLED
 * - Validate date/time formats
 * - Provide audit trail (history logs)
 * - Provide formatted output for console/UI
 * - Support persistence (ID setter)
 * ============================================================
 */

public class Appointment {

    // ================= ENUM: STATUS =================
    public enum Status {
        PENDING,
        CONFIRMED,
        CANCELLED
    }

    // ================= FIELDS =================
    private String appointmentId;

    private Patient patient;
    private Doctor doctor;

    private String date; // YYYY-MM-DD
    private String time; // HH:MM

    private Status status;

    private LocalDate createdDate;
    private LocalTime createdTime;

    // Audit trail logs
    private List<String> historyLogs;

    // ================= CONSTRUCTOR =================
    public Appointment(Patient patient, Doctor doctor, String date, String time) {

        this.appointmentId = generateAppointmentId();
        this.patient = patient;
        this.doctor = doctor;

        this.date = date;
        this.time = time;

        this.status = Status.PENDING;

        this.createdDate = LocalDate.now();
        this.createdTime = LocalTime.now();

        this.historyLogs = new ArrayList<>();

        log("Appointment created.");
        validate();
    }

    // ================= ID GENERATION =================
    private String generateAppointmentId() {
        return "APT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // ================= VALIDATION =================
    private void validate() {

        if (patient == null)
            throw new IllegalArgumentException("Patient cannot be null");

        if (doctor == null)
            throw new IllegalArgumentException("Doctor cannot be null");

        if (!isValidDate(date))
            throw new IllegalArgumentException("Invalid date format");

        if (!isValidTime(time))
            throw new IllegalArgumentException("Invalid time format");
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidTime(String time) {
        try {
            LocalTime.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ================= STATUS MANAGEMENT =================
    public void confirm() {

        if (status == Status.CANCELLED) {
            System.out.println("Cannot confirm a cancelled appointment.");
            return;
        }

        this.status = Status.CONFIRMED;
        log("Appointment confirmed.");
    }

    public void cancel() {

        if (status == Status.CANCELLED) {
            System.out.println("Already cancelled.");
            return;
        }

        this.status = Status.CANCELLED;
        log("Appointment cancelled.");
    }

    public void reschedule(String newDate, String newTime) {

        if (!isValidDate(newDate) || !isValidTime(newTime)) {
            System.out.println("Invalid new date/time.");
            return;
        }

        log("Rescheduled from " + date + " " + time +
                " to " + newDate + " " + newTime);

        this.date = newDate;
        this.time = newTime;
    }

    // ================= HISTORY =================
    private void log(String message) {

        String entry = "[" + LocalDate.now() + " " + LocalTime.now() + "] " + message;
        historyLogs.add(entry);
    }

    public void printHistory() {

        System.out.println("\n--- HISTORY (" + appointmentId + ") ---");

        for (String log : historyLogs) {
            System.out.println(log);
        }
    }

    // ================= DISPLAY =================
    public void printSummary() {

        System.out.println("\n----- APPOINTMENT SUMMARY -----");
        System.out.println("ID      : " + appointmentId);
        System.out.println("Patient : " + patient.getName());
        System.out.println("Doctor  : Dr. " + doctor.getName());
        System.out.println("Date    : " + date);
        System.out.println("Time    : " + time);
        System.out.println("Status  : " + status);
        System.out.println("--------------------------------");
    }

    @Override
    public String toString() {

        return "\n================ APPOINTMENT =================\n" +
                "Appointment ID : " + appointmentId + "\n" +
                "Patient ID     : " + patient.getPatientId() + "\n" +
                "Patient Name   : " + patient.getName() + "\n" +
                "Doctor ID      : " + doctor.getDoctorId() + "\n" +
                "Doctor Name    : Dr. " + doctor.getName() + "\n" +
                "Specialization : " + doctor.getSpecialization() + "\n" +
                "Date           : " + date + "\n" +
                "Time           : " + time + "\n" +
                "Status         : " + status + "\n" +
                "Created On     : " + createdDate + " " + createdTime + "\n" +
                "================================================";
    }

    // ================= GETTERS =================
    public String getAppointmentId() {
        return appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status.toString();
    }

    // ================= SETTERS (FOR FILE LOAD) =================
    public void setAppointmentId(String id) {
        this.appointmentId = id;
    }

    public void setStatus(String status) {
        try {
            this.status = Status.valueOf(status);
        } catch (Exception e) {
            this.status = Status.PENDING;
        }
    }

    // ================= EXTRA UTILITY =================
    public boolean isConfirmed() {
        return status == Status.CONFIRMED;
    }

    public boolean isCancelled() {
        return status == Status.CANCELLED;
    }

    public boolean belongsToPatient(String patientId) {
        return patient.getPatientId().equals(patientId);
    }

    public boolean belongsToDoctor(String doctorId) {
        return doctor.getDoctorId().equals(doctorId);
    }

    public boolean isOnDate(String date) {
        return this.date.equals(date);
    }

    // ================= DEBUG =================
    public void debug() {

        System.out.println("\n[DEBUG]");
        System.out.println("ID=" + appointmentId);
        System.out.println("Patient=" + patient.getName());
        System.out.println("Doctor=" + doctor.getName());
        System.out.println("Status=" + status);
    }
}