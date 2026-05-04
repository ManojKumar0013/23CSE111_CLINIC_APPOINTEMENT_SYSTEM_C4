package clinic;

import java.util.*;

/*
 * ============================================================
 * RECEPTIONIST CLASS (SYSTEM OPERATOR ROLE)
 * ============================================================
 * Responsibilities:
 * - View doctors and schedules
 * - Check availability
 * - Generate daily reports
 * - Assist in basic system operations
 * - Acts as an interface between user and Clinic system
 * ============================================================
 */

public class Receptionist {

    // ================= BASIC DETAILS =================
    private String name;
    private String employeeId;

    private Clinic clinic;

    // ================= LOGS =================
    private List<String> activityLogs;

    // ================= CONSTRUCTOR =================
    public Receptionist(String name, String employeeId, Clinic clinic) {

        this.name = name;
        this.employeeId = employeeId;
        this.clinic = clinic;

        this.activityLogs = new ArrayList<>();
    }

    // =====================================================
    // ================= VIEW DOCTORS =======================
    // =====================================================

    public void viewDoctors() {

        log("Viewed doctors list");

        System.out.println("\n===== DOCTORS LIST =====");
        clinic.listDoctors();
    }

    // =====================================================
    // ================= CHECK AVAILABILITY =================
    // =====================================================

    public void checkDoctorAvailability(String doctorId, String date) {

        log("Checked availability for doctor: " + doctorId + " on " + date);

        System.out.println("\n===== CHECK AVAILABILITY =====");

        clinic.showDoctorSlots(doctorId, date);
    }

    // =====================================================
    // ================= GENERATE REPORT ====================
    // =====================================================

    public void generateDailyReport(String date) {

        log("Generated daily report for: " + date);

        System.out.println("\n===== DAILY REPORT =====");

        clinic.generateDailyReport(date);
    }

    // =====================================================
    // ================= VIEW SYSTEM STATUS =================
    // =====================================================

    public void viewSystemSummary() {

        log("Viewed system summary");

        System.out.println("\n===== SYSTEM SUMMARY =====");

        clinic.printSystemSummary();
    }

    // =====================================================
    // ================= VIEW ALL APPOINTMENTS ==============
    // =====================================================

    public void viewAllAppointments() {

        log("Viewed all appointments");

        System.out.println("\n===== ALL APPOINTMENTS =====");

        clinic.printAllAppointments();
    }

    // =====================================================
    // ================= SEARCH FEATURES ====================
    // =====================================================

    public void searchAppointmentsByDate(String date) {

        log("Searched appointments for date: " + date);

        System.out.println("\n===== SEARCH RESULTS =====");

        clinic.generateDailyReport(date);
    }

    public void searchDoctorSchedule(String doctorId) {

        log("Viewed weekly schedule for doctor: " + doctorId);

        Doctor doc = clinic.getDoctor(doctorId);

        if (doc == null) {
            System.out.println("Doctor not found.");
            return;
        }

        doc.printWeeklySchedule();
    }

    // =====================================================
    // ================= ADVANCED FEATURES ==================
    // =====================================================

    public void printTodayReport() {

        String today = java.time.LocalDate.now().toString();

        log("Generated today's report");

        System.out.println("\n===== TODAY'S REPORT =====");

        clinic.generateDailyReport(today);
    }

    public void assistPatientBooking(String patientId, String doctorId, String date, String time) {

        log("Assisted booking for patient: " + patientId);

        clinic.bookAppointment(patientId, doctorId, date, time);
    }

    public void assistPatientCancellation(String appointmentId) {

        log("Assisted cancellation for appointment: " + appointmentId);

        clinic.cancelAppointment(appointmentId);
    }

    // =====================================================
    // ================= LOGGING ============================
    // =====================================================

    private void log(String message) {

        String entry = "[" + new Date() + "] " + message;
        activityLogs.add(entry);
    }

    public void printLogs() {

        System.out.println("\n===== RECEPTIONIST LOGS =====");

        if (activityLogs.isEmpty()) {
            System.out.println("No logs.");
            return;
        }

        for (String log : activityLogs) {
            System.out.println(log);
        }
    }

    public void clearLogs() {
        activityLogs.clear();
        System.out.println("Logs cleared.");
    }

    // =====================================================
    // ================= VALIDATION =========================
    // =====================================================

    public boolean isValidReceptionist() {

        if (name == null || employeeId == null)
            return false;

        return true;
    }

    // =====================================================
    // ================= DEBUG ==============================
    // =====================================================

    public void debug() {

        System.out.println("\n[RECEPTIONIST DEBUG]");
        System.out.println("Name: " + name);
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Logs: " + activityLogs.size());
    }

    // =====================================================
    // ================= GETTERS ============================
    // =====================================================

    public String getName() {
        return name;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    // =====================================================
    // ================= TO STRING ==========================
    // =====================================================

    @Override
    public String toString() {

        return "\n========== RECEPTIONIST ==========\n" +
                "Name : " + name + "\n" +
                "Employee ID : " + employeeId + "\n" +
                "Total Logs : " + activityLogs.size() + "\n" +
                "==================================";
    }
}