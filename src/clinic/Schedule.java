package clinic;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/*
 * ============================================================
 * SCHEDULE CLASS (SYSTEM BACKBONE)
 * ============================================================
 * Responsibilities:
 * - Manage all booking slots
 * - Prevent double booking
 * - Validate availability
 * - Handle reservation and release
 * - Work with Doctor schedules
 * ============================================================
 */

public class Schedule {

    // ================= DATA STRUCTURE =================
    // doctorId → booked slots (date + time)
    private Map<String, Set<String>> bookedSlots;

    // Optional custom slots added dynamically
    private Map<String, Set<String>> customSlots;

    // ================= CONSTRUCTOR =================
    public Schedule() {
        bookedSlots = new HashMap<>();
        customSlots = new HashMap<>();
    }

    // =====================================================
    // ================= INITIALIZE =========================
    // =====================================================

    public void addDoctor(Doctor doctor) {
        bookedSlots.putIfAbsent(doctor.getDoctorId(), new HashSet<>());
        customSlots.putIfAbsent(doctor.getDoctorId(), new HashSet<>());
    }

    // =====================================================
    // ================= CHECK AVAILABILITY =================
    // =====================================================

    public boolean checkAvailability(Doctor doctor, String date, String time) {

        String slot = buildSlot(date, time);

        // 1. Check doctor's working schedule
        if (!doctor.isAvailable(date, time)) {
            return false;
        }

        // 2. Check if already booked
        Set<String> slots = bookedSlots.get(doctor.getDoctorId());

        if (slots != null && slots.contains(slot)) {
            return false;
        }

        return true;
    }

    // =====================================================
    // ================= RESERVE SLOT =======================
    // =====================================================

    public boolean reserveSlot(Doctor doctor, String date, String time) {

        String slot = buildSlot(date, time);

        bookedSlots.putIfAbsent(doctor.getDoctorId(), new HashSet<>());
        Set<String> slots = bookedSlots.get(doctor.getDoctorId());

        if (slots.contains(slot)) {
            return false;
        }

        slots.add(slot);
        return true;
    }

    // =====================================================
    // ================= RELEASE SLOT =======================
    // =====================================================

    public void releaseSlot(Doctor doctor, String date, String time) {

        String slot = buildSlot(date, time);

        Set<String> slots = bookedSlots.get(doctor.getDoctorId());

        if (slots != null) {
            slots.remove(slot);
        }
    }

    // =====================================================
    // ================= CUSTOM SLOT ========================
    // =====================================================

    public void addCustomSlot(Doctor doctor, String date, String time) {

        String slot = buildSlot(date, time);

        customSlots.putIfAbsent(doctor.getDoctorId(), new HashSet<>());
        customSlots.get(doctor.getDoctorId()).add(slot);

        System.out.println("Custom slot added: " + slot);
    }

    public boolean isCustomSlotAvailable(Doctor doctor, String date, String time) {

        String slot = buildSlot(date, time);

        Set<String> slots = customSlots.get(doctor.getDoctorId());

        return slots != null && slots.contains(slot);
    }

    // =====================================================
    // ================= PRINT AVAILABLE ====================
    // =====================================================

    public void printAvailableSlots(Doctor doctor, String date) {

        System.out.println("\n===== AVAILABLE SLOTS =====");
        System.out.println("Doctor: " + doctor.getName());
        System.out.println("Date: " + date);

        boolean found = false;

        // Generate slots (hourly basis)
        for (int hour = 0; hour < 24; hour++) {

            String time = String.format("%02d:00", hour);

            if (doctor.isAvailable(date, time) &&
                checkAvailability(doctor, date, time)) {

                System.out.println(time);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No available slots.");
        }
    }

    // =====================================================
    // ================= UTILITIES ==========================
    // =====================================================

    private String buildSlot(String date, String time) {
        return date + " " + time;
    }

    public int totalBookings(String doctorId) {

        Set<String> slots = bookedSlots.get(doctorId);

        return (slots == null) ? 0 : slots.size();
    }

    public void printDoctorBookings(String doctorId) {

        System.out.println("\n--- Booked Slots ---");

        Set<String> slots = bookedSlots.get(doctorId);

        if (slots == null || slots.isEmpty()) {
            System.out.println("No bookings.");
            return;
        }

        for (String s : slots) {
            System.out.println(s);
        }
    }

    public void clearDoctorSchedule(String doctorId) {

        bookedSlots.remove(doctorId);
        customSlots.remove(doctorId);

        System.out.println("Doctor schedule cleared.");
    }

    // =====================================================
    // ================= VALIDATION =========================
    // =====================================================

    public boolean isValidDateTime(String date, String time) {

        try {
            LocalDate.parse(date);
            LocalTime.parse(time);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // =====================================================
    // ================= DEBUG ==============================
    // =====================================================

    public void debug() {

        System.out.println("\n[SCHEDULE DEBUG]");
        System.out.println("Doctors tracked: " + bookedSlots.size());
    }

    // =====================================================
    // ================= SUMMARY ============================
    // =====================================================

    public void printSummary() {

        System.out.println("\n===== SCHEDULE SUMMARY =====");

        for (String doctorId : bookedSlots.keySet()) {
            System.out.println("Doctor: " + doctorId +
                    " | Bookings: " + totalBookings(doctorId));
        }
    }
}