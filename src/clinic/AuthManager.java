package clinic;

import java.util.*;

public class AuthManager {

    private Map<String, String[]> users;

    private String currentUser;
    private String currentRole;
    private String currentUserId;

    // ================= CONSTRUCTOR =================
    public AuthManager() {
        loadUsers();
        createDefaultAdmin();
    }

    // ================= LOAD USERS =================
    private void loadUsers() {
        users = new HashMap<>();

        Map<String, String[]> raw = FileManager.loadUsers();

        // 🔥 normalize usernames to lowercase
        for (String key : raw.keySet()) {
            users.put(key.toLowerCase(), raw.get(key));
        }
    }

    // ================= DEFAULT ADMIN =================
    private void createDefaultAdmin() {

        if (!users.containsKey("admin")) {
            FileManager.saveUser("admin", "admin123", "ADMIN", "ADMIN");
            loadUsers();
        }
    }

    // ================= SIGNUP =================
    public boolean signup(String username, String password, String role,
                          String name, String phone, int age, String extra) {

        username = username.toLowerCase(); // 🔥 prevent duplicates
        role = role.toUpperCase();

        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
            return false;
        }

        String userId = "";

        // ================= PATIENT =================
        if (role.equals("PATIENT")) {

            userId = FileManager.generatePatientId();
            FileManager.savePatient(userId, name, phone, age, extra);

            System.out.println("Patient Registered!");
            System.out.println("Patient ID: " + userId);
        }

        // ================= DOCTOR =================
        else if (role.equals("DOCTOR")) {

            userId = FileManager.generateDoctorId();
            FileManager.saveDoctor(userId, name, extra);

            System.out.println("Doctor Registered!");
            System.out.println("Doctor ID: " + userId);
        }

        // ================= RECEPTIONIST =================
        else if (role.equals("RECEPTIONIST")) {

            userId = generateReceptionistId();

            FileManager.write("data/receptionists.txt",
                    userId + "," + name + "," + phone);

            System.out.println("Receptionist Registered!");
            System.out.println("Receptionist ID: " + userId);
        }

        // ================= ADMIN =================
        else if (role.equals("ADMIN")) {
            userId = "ADMIN";
        }

        else {
            System.out.println("Invalid role.");
            return false;
        }

        // 🔥 Save user
        FileManager.saveUser(username, password, role, userId);

        loadUsers();
        return true;
    }

    // ================= LOGIN =================
    public boolean login(String username, String password) {

        username = username.toLowerCase(); // 🔥 case-insensitive login

        if (!users.containsKey(username)) {
            System.out.println("User not found.");
            return false;
        }

        String[] data = users.get(username);

        if (!data[1].equals(password)) {
            System.out.println("Incorrect password.");
            return false;
        }

        currentUser = username;
        currentRole = data[2];
        currentUserId = data[3];

        System.out.println("\nLogin Successful as " + currentRole);
        System.out.println("Linked ID: " + currentUserId);

        return true;
    }

    // ================= RECEPTIONIST ID =================
    private String generateReceptionistId() {

        List<String> list = FileManager.read("data/receptionists.txt");

        int max = 0;

        for (String s : list) {

            String[] d = s.split(",");

            if (d.length > 0 && d[0].startsWith("R")) {
                try {
                    int num = Integer.parseInt(d[0].substring(1));
                    if (num > max) max = num;
                } catch (Exception e) {}
            }
        }

        return "R" + String.format("%03d", max + 1);
    }

    // ================= GETTERS =================
    public String getCurrentUser() {
        return currentUser;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    // ================= ROLE CHECK =================
    public boolean isAdmin() { return "ADMIN".equals(currentRole); }
    public boolean isPatient() { return "PATIENT".equals(currentRole); }
    public boolean isDoctor() { return "DOCTOR".equals(currentRole); }
    public boolean isReceptionist() { return "RECEPTIONIST".equals(currentRole); }

    // ================= LOGOUT =================
    public void logout() {
        currentUser = null;
        currentRole = null;
        currentUserId = null;
        System.out.println("Logged out.");
    }
}