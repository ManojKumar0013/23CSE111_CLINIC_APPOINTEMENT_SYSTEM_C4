package clinic;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/*
 * ============================================================
 * DOCTOR CLASS (CORE DOMAIN – RESOURCE + AUTHORITY)
 * ============================================================
 * Responsibilities:
 * - Maintain weekly working schedule
 * - Validate availability using calendar
 * - Accept / reject appointments
 * - Maintain doctor-side appointment list
 * - Provide schedule visualization
 * - Support dynamic updates (availability changes)
 * ============================================================
 */

public class Doctor {

    // ================= BASIC INFO =================
    private String doctorId;
    private String name;
    private String specialization;

    // Weekly schedule: DayOfWeek → List of time ranges
    private Map<DayOfWeek, List<TimeSlot>> weeklySchedule;

    // Doctor's appointments
    private List<Appointment> appointments;

    // ================= INNER CLASS =================
    private static class TimeSlot {
        private LocalTime start;
        private LocalTime end;

        public TimeSlot(LocalTime start, LocalTime end) {
            this.start = start;
            this.end = end;
        }

        public boolean contains(LocalTime time) {
            return !time.isBefore(start) && !time.isAfter(end);
        }

        @Override
        public String toString() {
            return start + " - " + end;
        }
    }

    // ================= CONSTRUCTOR =================
    public Doctor(String doctorId, String name, String specialization) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;

        this.weeklySchedule = new HashMap<>();
        this.appointments = new ArrayList<>();
    }

    // =====================================================
    // ================= AVAILABILITY =======================
    // =====================================================

    public void setAvailability(DayOfWeek day, String startTime, String endTime) {

        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);

            if (end.isBefore(start)) {
                System.out.println("Invalid time range.");
                return;
            }

            weeklySchedule.putIfAbsent(day, new ArrayList<>());
            weeklySchedule.get(day).add(new TimeSlot(start, end));

        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format (HH:MM).");
        }
    }

    public boolean isAvailable(String date, String time) {

        try {
            LocalDate localDate = LocalDate.parse(date);
            DayOfWeek day = localDate.getDayOfWeek();
            LocalTime t = LocalTime.parse(time);

            if (!weeklySchedule.containsKey(day)) return false;

            for (TimeSlot slot : weeklySchedule.get(day)) {
                if (slot.contains(t)) return true;
            }

        } catch (Exception e) {
            System.out.println("Invalid date/time format.");
        }

        return false;
    }

    // =====================================================
    // ================= APPOINTMENT CONTROL =================
    // =====================================================

    public boolean acceptAppointment(Appointment appointment) {

        if (!isAvailable(appointment.getDate(), appointment.getTime())) {
            System.out.println("Doctor not available at that time.");
            return false;
        }

        appointments.add(appointment);
        appointment.confirm();

        System.out.println("Dr. " + name + " accepted appointment.");
        return true;
    }

    public void rejectAppointment(Appointment appointment) {

        appointment.cancel();
        System.out.println("Dr. " + name + " rejected appointment.");
    }

    public void cancelAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    // =====================================================
    // ================= VIEW APPOINTMENTS ==================
    // =====================================================

    public void viewAppointments() {

        System.out.println("\n===== DOCTOR APPOINTMENTS =====");

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
    // ================= SCHEDULE DISPLAY ===================
    // =====================================================

    public void printWeeklySchedule() {

        System.out.println("\n===== WEEKLY SCHEDULE =====");
        System.out.println("Doctor: Dr. " + name);

        if (weeklySchedule.isEmpty()) {
            System.out.println("No schedule set.");
            return;
        }

        List<DayOfWeek> days = new ArrayList<>(weeklySchedule.keySet());
        Collections.sort(days);

        for (DayOfWeek day : days) {

            System.out.print(day + " : ");

            List<TimeSlot> slots = weeklySchedule.get(day);

            for (int i = 0; i < slots.size(); i++) {
                System.out.print(slots.get(i));

                if (i != slots.size() - 1)
                    System.out.print(" | ");
            }

            System.out.println();
        }
    }

    public void clearSchedule() {
        weeklySchedule.clear();
        System.out.println("Schedule cleared.");
    }

    // =====================================================
    // ================= ADVANCED FEATURES ==================
    // =====================================================

    public void removeAvailability(DayOfWeek day) {

        if (weeklySchedule.containsKey(day)) {
            weeklySchedule.remove(day);
            System.out.println("Removed schedule for " + day);
        }
    }

    public void printTodaySchedule() {

        DayOfWeek today = LocalDate.now().getDayOfWeek();

        System.out.println("\nToday's Schedule (" + today + ")");

        if (!weeklySchedule.containsKey(today)) {
            System.out.println("No schedule today.");
            return;
        }

        for (TimeSlot slot : weeklySchedule.get(today)) {
            System.out.println(slot);
        }
    }

    public int totalAppointments() {
        return appointments.size();
    }

    public boolean hasAppointments() {
        return !appointments.isEmpty();
    }

    // =====================================================
    // ================= VALIDATION =========================
    // =====================================================

    public boolean isValidDoctor() {
        return doctorId != null && name != null && specialization != null;
    }

    // =====================================================
    // ================= GETTERS ============================
    // =====================================================

    public String getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    // =====================================================
    // ================= DEBUG ==============================
    // =====================================================

    public void debug() {

        System.out.println("\n[DOCTOR DEBUG]");
        System.out.println("ID: " + doctorId);
        System.out.println("Name: " + name);
        System.out.println("Specialization: " + specialization);
        System.out.println("Appointments: " + appointments.size());
    }

    // =====================================================
    // ================= TO STRING ==========================
    // =====================================================

    @Override
    public String toString() {

        return "\n========== DOCTOR ==========\n" +
                "ID : " + doctorId + "\n" +
                "Name : Dr. " + name + "\n" +
                "Specialization : " + specialization + "\n" +
                "Total Appointments : " + appointments.size() + "\n" +
                "============================";
    }
}