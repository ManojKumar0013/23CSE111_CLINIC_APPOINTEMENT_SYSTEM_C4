package clinic;

import java.util.*;

/*
 * ============================================================
 * PATIENT CLASS (END USER / CLIENT SIDE)
 * ============================================================
 * Responsibilities:
 * - Book appointments
 * - Cancel appointments
 * - View appointments
 * - Maintain personal appointment history
 * - Validate input
 * - Support rescheduling
 * ============================================================
 */

public class Patient {

    // ================= BASIC DETAILS =================
    private String patientId;
    private String name;
    private int age;
    private String phoneNumber;

    // ================= APPOINTMENTS =================
    private List<Appointment> appointments;

    // ================= CONSTRUCTOR =================
    public Patient(String patientId, String name, int age, String phoneNumber) {

        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;

        this.appointments = new ArrayList<>();
    }

    // =====================================================
    // ================= BOOK APPOINTMENT ===================
    // =====================================================

    public void bookAppointment(Clinic clinic, String doctorId, String date, String time) {

        if (!isValidDate(date) || !isValidTime(time)) {
            System.out.println("Invalid date/time format.");
            return;
        }

        Appointment appointment = clinic.bookAppointment(patientId, doctorId, date, time);

        if (appointment != null) {
            appointments.add(appointment);
            System.out.println("Appointment booked successfully!");
        } else {
            System.out.println("Booking failed.");
        }
    }

    // =====================================================
    // ================= CANCEL APPOINTMENT =================
    // =====================================================

    public void cancelAppointment(Clinic clinic, String appointmentId) {

        Appointment target = findAppointment(appointmentId);

        if (target == null) {
            System.out.println("Appointment not found.");
            return;
        }

        clinic.cancelAppointment(appointmentId);
        appointments.remove(target);

        System.out.println("Appointment cancelled successfully.");
    }

    // =====================================================
    // ================= VIEW APPOINTMENTS ==================
    // =====================================================

    public void viewAppointments() {

        System.out.println("\n===== MY APPOINTMENTS =====");

        if (appointments.isEmpty()) {
            System.out.println("No appointments.");
            return;
        }

        for (Appointment a : appointments) {
            System.out.println(a);
        }
    }

    public void viewAppointmentsByDate(String date) {

        System.out.println("\nAppointments on " + date);

        boolean found = false;

        for (Appointment a : appointments) {
            if (a.getDate().equals(date)) {
                System.out.println(a);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments found.");
        }
    }

    // =====================================================
    // ================= RESCHEDULE =========================
    // =====================================================

    public void rescheduleAppointment(Clinic clinic, String appointmentId, String newDate, String newTime) {

        Appointment target = findAppointment(appointmentId);

        if (target == null) {
            System.out.println("Appointment not found.");
            return;
        }

        if (!isValidDate(newDate) || !isValidTime(newTime)) {
            System.out.println("Invalid new date/time.");
            return;
        }

        // Cancel old
        clinic.cancelAppointment(appointmentId);

        // Book new
        Appointment newAppt = clinic.bookAppointment(patientId, target.getDoctor().getDoctorId(), newDate, newTime);

        if (newAppt != null) {
            appointments.remove(target);
            appointments.add(newAppt);

            System.out.println("Appointment rescheduled successfully.");
        } else {
            System.out.println("Reschedule failed.");
        }
    }

    // =====================================================
    // ================= SEARCH =============================
    // =====================================================

    private Appointment findAppointment(String appointmentId) {

        for (Appointment a : appointments) {
            if (a.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                return a;
            }
        }

        return null;
    }

    public boolean hasAppointments() {
        return !appointments.isEmpty();
    }

    public int totalAppointments() {
        return appointments.size();
    }

    // =====================================================
    // ================= VALIDATION =========================
    // =====================================================

    private boolean isValidDate(String date) {
        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isValidTime(String time) {
        return time != null && time.matches("\\d{2}:\\d{2}");
    }

    public boolean isValidPatient() {

        if (patientId == null || name == null) return false;

        if (age <= 0) return false;

        if (phoneNumber == null || phoneNumber.length() < 10) return false;

        return true;
    }

    // =====================================================
    // ================= PROFILE ============================
    // =====================================================

    public void printProfile() {

        System.out.println("\n===== PATIENT PROFILE =====");
        System.out.println("ID    : " + patientId);
        System.out.println("Name  : " + name);
        System.out.println("Age   : " + age);
        System.out.println("Phone : " + phoneNumber);
        System.out.println("Total Appointments: " + appointments.size());
    }

    // =====================================================
    // ================= DEBUG ==============================
    // =====================================================

    public void debug() {

        System.out.println("\n[PATIENT DEBUG]");
        System.out.println("ID: " + patientId);
        System.out.println("Name: " + name);
        System.out.println("Appointments: " + appointments.size());
    }

    // =====================================================
    // ================= GETTERS ============================
    // =====================================================

    public String getPatientId() {
        return patientId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    // =====================================================
    // ================= TO STRING ==========================
    // =====================================================

    @Override
    public String toString() {

        return "\n========== PATIENT ==========\n" +
                "ID : " + patientId + "\n" +
                "Name : " + name + "\n" +
                "Age : " + age + "\n" +
                "Phone : " + phoneNumber + "\n" +
                "Appointments : " + appointments.size() + "\n" +
                "============================";
    }
}