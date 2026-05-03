package clinic;

import java.util.*;

public class Patient {

    private String patientID;
    private String name;
    private String phone;
    private int age;
    private String disease;

    private List<String> appointmentIds;

    // ================= CONSTRUCTOR =================
    public Patient(String patientID,
                   String name,
                   String phone,
                   int age,
                   String disease) {

        this.patientID = patientID;
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.disease = disease;

        this.appointmentIds = new ArrayList<>();
    }

    // =====================================================
    // ================= LOAD FROM FILE =====================
    // =====================================================

    public static Patient loadFromFile(String patientID) {

        Map<String, String[]> data = FileManager.loadPatients();

        if (!data.containsKey(patientID)) {
            return null;
        }

        String[] p = data.get(patientID);

        return new Patient(
                p[0],
                p[1],
                p[2],
                Integer.parseInt(p[3]),
                p[4]
        );
    }

    // =====================================================
    // ================= BOOK APPOINTMENT ===================
    // =====================================================

    public void bookAppointment(Clinic clinic,
                                String date,
                                String time) {

        System.out.println("\n===== BOOK APPOINTMENT =====");

        String doctorID = FileManager.getDoctorByDisease(disease);

        if (doctorID == null) {
            System.out.println("No doctor available for this disease.");
            return;
        }

        System.out.println("Assigned Doctor ID: " + doctorID);

        Appointment appt = clinic.manageBooking(
                patientID,
                doctorID,
                date,
                time
        );

        if (appt != null) {
            appointmentIds.add(appt.getAppointmentID());
            System.out.println("✅ Appointment booked successfully!");
        }
    }

    // =====================================================
    // ================= CANCEL =============================
    // =====================================================

    public void cancelAppointment(Clinic clinic,
                                 String appointmentID) {

        System.out.println("\n===== CANCEL APPOINTMENT =====");

        if (appointmentID == null || appointmentID.isEmpty()) {
            System.out.println("❌ Invalid appointment ID.");
            return;
        }

        boolean success = clinic.cancelAppointment(appointmentID);

        if (success) {

            appointmentIds.remove(appointmentID);

            //  UPDATE FILE STATUS
            FileManager.updateAppointmentStatus(
                    appointmentID,
                    "CANCELLED"
            );

            System.out.println("✅ Appointment cancelled successfully!");
        } else {
            System.out.println("❌ Appointment not found.");
        }
    }

    // =====================================================
    // ================= VIEW ===============================
    // =====================================================

    public void viewAppointments(Clinic clinic) {

        System.out.println("\n===== MY APPOINTMENTS =====");

        clinic.viewPatientAppointments(patientID);
    }

    // =====================================================
    // ================= GETTERS ============================
    // =====================================================

    public String getPatientID() {
        return patientID;
    }

    public String getDisease() {
        return disease;
    }

    // =====================================================
    // ================= DISPLAY ============================
    // =====================================================

    @Override
    public String toString() {

        return "\n====================================\n" +
                "Patient ID : " + patientID + "\n" +
                "Name       : " + name + "\n" +
                "Phone      : " + phone + "\n" +
                "Age        : " + age + "\n" +
                "Disease    : " + disease + "\n" +
                "====================================";
    }
}