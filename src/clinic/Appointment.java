package clinic;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Appointment {

    private String appointmentID;
    private String patientID;
    private String doctorID;

    private String date;
    private String time;

    private String status;
    private LocalDateTime createdTime;

    // =====================================================
    // ================= NEW BOOKING ========================
    // =====================================================
    public Appointment(String patientID,
                       String doctorID,
                       String date,
                       String time) {

        if (!isValidDateTime(date, time)) {
            throw new IllegalArgumentException("Invalid date or time format.");
        }

        this.appointmentID = FileManager.generateAppointmentId();
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.time = time;

        this.status = "PENDING";
        this.createdTime = LocalDateTime.now();
    }

    // =====================================================
    // ================= LOAD FROM FILE =====================
    // =====================================================
    public Appointment(String id,
                       String patientID,
                       String doctorID,
                       String date,
                       String time,
                       String status) {

        this.appointmentID = id;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.time = time;
        this.status = status;
        this.createdTime = LocalDateTime.now();
    }

    // =====================================================
    // ================= SAVE ===============================
    // =====================================================
    public void saveToFile() {

        FileManager.saveAppointment(
                appointmentID,
                patientID,
                doctorID,
                date,
                time,
                status
        );
    }

    // =====================================================
    // ================= STATUS CONTROL =====================
    // =====================================================
    public void confirmAppointment() {

        if (!status.equals("PENDING")) return;

        this.status = "CONFIRMED";
    }

    public void cancelAppointment() {

        if (status.equals("CANCELLED")) return;

        this.status = "CANCELLED";
    }

    public void completeAppointment() {

        if (!status.equals("CONFIRMED")) return;

        this.status = "COMPLETED";
    }

    public void rescheduleAppointment(String newDate, String newTime) {

        if (!isValidDateTime(newDate, newTime)) {
            System.out.println("Invalid new date/time.");
            return;
        }

        this.date = newDate;
        this.time = newTime;
        this.status = "RESCHEDULED";
    }

    // =====================================================
    // ================= VALIDATION =========================
    // =====================================================
    private boolean isValidDateTime(String date, String time) {

        try {
            LocalDateTime.parse(date + "T" + time);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // =====================================================
    // ================= GETTERS ============================
    // =====================================================
    public String getAppointmentID() { return appointmentID; }
    public String getPatientID() { return patientID; }
    public String getDoctorID() { return doctorID; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getStatus() { return status; }

    // =====================================================
    // ================= DISPLAY ============================
    // =====================================================
    @Override
    public String toString() {

        return "\n------------------------------------\n" +
                "ID     : " + appointmentID + "\n" +
                "Patient: " + patientID + "\n" +
                "Doctor : " + doctorID + "\n" +
                "Date   : " + date + "\n" +
                "Time   : " + time + "\n" +
                "Status : " + status + "\n" +
                "------------------------------------";
    }
}