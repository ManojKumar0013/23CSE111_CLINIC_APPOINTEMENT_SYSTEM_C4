package clinic;

/*
 * ============================================================
 * RECEPTIONIST CLASS (FINAL STABLE VERSION)
 * ============================================================
 * Features:
 * - Booking support
 * - Cancellation support
 * - Daily report generation
 * - Clean output
 * ============================================================
 */

public class Receptionist {

    private String receptionistID;
    private String name;
    private String contact;

    private Clinic clinic;

    // ================= CONSTRUCTOR =================
    public Receptionist(String id, String name, String contact, Clinic clinic) {

        this.receptionistID = id;
        this.name = name;
        this.contact = contact;
        this.clinic = clinic;
    }

    // =====================================================
    // ================= BOOK APPOINTMENT ===================
    // =====================================================

    public void bookAppointment(String patientID,
                                String doctorID,
                                String date,
                                String time) {

        System.out.println("\n=================================");
        System.out.println(" RECEPTIONIST BOOKING PROCESS");
        System.out.println("=================================");

        Appointment appt = clinic.manageBooking(
                patientID, doctorID, date, time
        );

        if (appt != null) {
            System.out.println("✅ Booking successful.");
        } else {
            System.out.println("❌ Booking failed (slot taken or invalid).");
        }
    }

    // =====================================================
    // ================= CANCEL APPOINTMENT =================
    // =====================================================

    public void cancelAppointment(String appointmentID) {

        System.out.println("\n=================================");
        System.out.println(" RECEPTIONIST CANCELLATION");
        System.out.println("=================================");

        boolean success = clinic.cancelAppointment(appointmentID);

        if (success) {
            System.out.println("✅ Appointment cancelled.");
        } else {
            System.out.println("❌ Appointment not found.");
        }
    }

    // =====================================================
    // ================= DAILY REPORT =======================
    // =====================================================

    public void generateDailySchedule(String date) {

        System.out.println("\n=================================");
        System.out.println("     DAILY APPOINTMENT REPORT");
        System.out.println("=================================");

        clinic.generateReports(date);
    }

    // =====================================================
    // ================= DISPLAY ============================
    // =====================================================

    @Override
    public String toString() {

        return "\n====================================\n" +
                "Receptionist ID : " + receptionistID + "\n" +
                "Name            : " + name + "\n" +
                "Contact         : " + contact + "\n" +
                "====================================";
    }
}