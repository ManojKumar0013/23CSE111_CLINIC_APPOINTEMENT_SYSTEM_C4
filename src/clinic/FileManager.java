package clinic;

import java.io.*;
import java.util.*;

public class FileManager {

    private static final String FILE_PATH = "appointments.txt";

    // ================= SAVE =================
    public static void saveAppointments(Map<String, Appointment> appointments) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (Appointment a : appointments.values()) {

                writer.write(
                        a.getAppointmentId() + "," +
                        a.getPatient().getPatientId() + "," +
                        a.getDoctor().getDoctorId() + "," +
                        a.getDate() + "," +
                        a.getTime() + "," +
                        a.getStatus()
                );

                writer.newLine();
            }

            System.out.println("Data saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving appointments.");
        }
    }

    // ================= LOAD =================
    public static List<String[]> loadAppointments() {

        List<String[]> data = new ArrayList<>();

        File file = new File(FILE_PATH);

        // If file does not exist
        if (!file.exists()) {
            System.out.println("No previous data found.");
            return data;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length == 6) {
                    data.add(row);
                }
            }

            System.out.println("Data loaded successfully.");

        } catch (IOException e) {
            System.out.println("Error loading appointments.");
        }

        return data;
    }
}