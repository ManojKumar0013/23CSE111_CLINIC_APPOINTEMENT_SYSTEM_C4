package clinic;

import java.util.*;



public class Doctor {

    private String doctorID;
    private String name;
    private String specialization;

    private Schedule schedule;
    private List<String> appointmentIds;

    // ================= CONSTRUCTOR =================
    public Doctor(String id, String name, String spec) {

        this.doctorID = id;
        this.name = name;
        this.specialization = spec;

        this.schedule = new Schedule(id); 
        this.appointmentIds = new ArrayList<>();
    }

    // =====================================================
    // ================= ACCEPT APPOINTMENT =================
    // =====================================================

    public boolean acceptAppointment(Appointment appt) {

        String date = appt.getDate();
        String time = appt.getTime();

        boolean booked = schedule.bookSlot(date, time);

        if (!booked) {
            System.out.println("❌ Doctor not available for this slot.");
            return false;
        }

        appointmentIds.add(appt.getAppointmentID());

        return true;
    }

    // =====================================================
    // ================= CANCEL APPOINTMENT =================
    // =====================================================

    public void cancelAppointment(Appointment appt) {

        schedule.releaseSlot(appt.getDate(), appt.getTime());
        appointmentIds.remove(appt.getAppointmentID());

        appt.cancelAppointment();

        System.out.println("✅ Appointment cancelled.");
    }

    // =====================================================
    // ================= SHOW AVAILABLE =====================
    // =====================================================

    public void showAvailability(String date) {
        schedule.showAvailableSlots(date);
    }

    // =====================================================
    // ================= VIEW APPOINTMENTS ==================
    // =====================================================

    public void viewAppointments(Clinic clinic) {

        System.out.println("\n===== DOCTOR APPOINTMENTS =====");
        clinic.viewDoctorAppointments(doctorID);
    }

    // =====================================================
    // ================= GETTERS ============================
    // =====================================================

    public String getDoctorID() {
        return doctorID;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getName() { 
        return name;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    // =====================================================
    // ================= DISPLAY ============================
    // =====================================================

    @Override
    public String toString() {

        return "\n====================================\n" +
                "Doctor ID      : " + doctorID + "\n" +
                "Name           : Dr. " + name + "\n" +
                "Specialization : " + specialization + "\n" +
                "Appointments   : " + appointmentIds.size() + "\n" +
                "====================================";
    }
}