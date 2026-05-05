package clinic;

import java.io.*;
import java.util.*;

public class FileManager {

    private static final String DATA_DIR = "data/";

    private static final String USERS = DATA_DIR + "users.txt";
    private static final String PATIENTS = DATA_DIR + "patients.txt";
    private static final String DOCTORS = DATA_DIR + "doctors.txt";
    private static final String APPOINTMENTS = DATA_DIR + "appointments.txt";

    static {
        new File(DATA_DIR).mkdirs();
    }

    // ================= WRITE =================
    public static void write(String file, String data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(data);
            bw.newLine();
        } catch (Exception e) {
            System.out.println("File write error: " + file);
        }
    }

    // ================= READ =================
    public static List<String> read(String file) {

        List<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty())
                    list.add(line);
            }

        } catch (Exception e) {}

        return list;
    }

    // =====================================================
    // ================= USERS ==============================
    // =====================================================

    public static void saveUser(String u, String p, String r, String id) {

        u = u.toLowerCase(); // 🔥 normalize

        if (loadUsers().containsKey(u)) return;

        write(USERS, u + "," + p + "," + r + "," + id);
    }

    public static Map<String, String[]> loadUsers() {

        Map<String, String[]> map = new HashMap<>();

        for (String s : read(USERS)) {

            String[] d = s.split(",");

            if (d.length >= 4)
                map.put(d[0].toLowerCase(), d);
        }

        return map;
    }

    // =====================================================
    // ================= PATIENT ============================
    // =====================================================

    public static void savePatient(String id, String name,
                                   String phone, int age, String disease) {

        if (loadPatients().containsKey(id)) return;

        write(PATIENTS, id + "," + name + "," + phone + "," + age + "," + disease);
    }

    public static Map<String, String[]> loadPatients() {

        Map<String, String[]> map = new HashMap<>();

        for (String s : read(PATIENTS)) {

            String[] d = s.split(",");

            if (d.length >= 5)
                map.put(d[0], d);
        }

        return map;
    }

    // =====================================================
    // ================= DOCTOR =============================
    // =====================================================

    public static void saveDoctor(String id, String name, String spec) {

        if (loadDoctors().containsKey(id)) return;

        write(DOCTORS, id + "," + name + "," + spec);
    }

    public static Map<String, String[]> loadDoctors() {

        Map<String, String[]> map = new HashMap<>();

        for (String s : read(DOCTORS)) {

            String[] d = s.split(",");

            if (d.length >= 3)
                map.put(d[0], d);
        }

        return map;
    }

    // =====================================================
    // ================= APPOINTMENT ========================
    // =====================================================

    public static void saveAppointment(String id, String pid, String did,
                                       String date, String time, String status) {

        List<String> existing = read(APPOINTMENTS);

        for (String s : existing) {

            String[] d = s.split(",");

            if (d.length >= 6 &&
                d[2].equals(did) &&
                d[3].equals(date) &&
                d[4].equals(time)) {

                return; // duplicate slot
            }
        }

        write(APPOINTMENTS,
                id + "," + pid + "," + did + "," + date + "," + time + "," + status);
    }

    public static Map<String, String[]> loadAppointments() {

        Map<String, String[]> map = new HashMap<>();

        for (String s : read(APPOINTMENTS)) {

            String[] d = s.split(",");

            if (d.length >= 6)
                map.put(d[0], d);
        }

        return map;
    }

    // =====================================================
    // 🔥 UPDATE APPOINTMENT STATUS (NEW FEATURE)
    // =====================================================

    public static void updateAppointmentStatus(String appointmentID, String newStatus) {

        List<String> lines = read(APPOINTMENTS);
        List<String> updated = new ArrayList<>();

        for (String s : lines) {

            String[] d = s.split(",");

            if (d.length >= 6 && d[0].equals(appointmentID)) {
                d[5] = newStatus;
                updated.add(String.join(",", d));
            } else {
                updated.add(s);
            }
        }

        rewriteFile(APPOINTMENTS, updated);
    }

    // =====================================================
    // 🔥 REWRITE FILE (UTILITY)
    // =====================================================

    private static void rewriteFile(String file, List<String> data) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            for (String line : data) {
                bw.write(line);
                bw.newLine();
            }

        } catch (Exception e) {
            System.out.println("File rewrite error: " + file);
        }
    }

    // =====================================================
    // ================= ID GENERATION ======================
    // =====================================================

    public static String generatePatientId() {
        return generateId(read(PATIENTS), "P");
    }

    public static String generateDoctorId() {
        return generateId(read(DOCTORS), "D");
    }

    public static String generateAppointmentId() {
        return generateId(read(APPOINTMENTS), "APT");
    }

    private static String generateId(List<String> data, String prefix) {

        int max = 0;

        for (String s : data) {

            String[] d = s.split(",");

            if (d.length >= 1 && d[0].startsWith(prefix)) {

                try {
                    int num = Integer.parseInt(d[0].substring(prefix.length()));
                    if (num > max) max = num;
                } catch (Exception e) {}
            }
        }

        return prefix + String.format("%03d", max + 1);
    }

    // =====================================================
    // ================= SMART DOCTOR =======================
    // =====================================================

    public static String getDoctorByDisease(String disease) {

        disease = disease.toLowerCase();

        for (String[] d : loadDoctors().values()) {

            String spec = d[2].toLowerCase();

            if (disease.contains("heart") && spec.contains("cardio")) return d[0];
            if (disease.contains("skin") && spec.contains("derma")) return d[0];
            if (disease.contains("bone") && spec.contains("ortho")) return d[0];
            if (disease.contains("fever") && spec.contains("general")) return d[0];
            if (disease.contains("brain") && spec.contains("neuro")) return d[0];
            if ((disease.contains("ear") || disease.contains("nose") || disease.contains("throat")) && spec.contains("ent")) return d[0];
            if ((disease.contains("child") || disease.contains("baby")) && spec.contains("pediatric")) return d[0];
        }

        return loadDoctors().isEmpty()
                ? null
                : loadDoctors().values().iterator().next()[0];
    }
}