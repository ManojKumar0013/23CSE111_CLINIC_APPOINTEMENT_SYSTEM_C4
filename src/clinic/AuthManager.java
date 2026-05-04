package clinic;

import java.io.*;
import java.util.*;

/*
 * ============================================
 * AUTH MANAGER (ROLE BASED SYSTEM)
 * ============================================
 * Handles:
 * - Login
 * - Registration
 * - Role validation
 * - Access control
 * ============================================
 */

public class AuthManager {

    // ================= FILE PATH =================
    private static final String FILE_PATH = "users.txt";

    // ================= DATA STRUCTURES =================
    private Map<String, String> userPasswords;
    private Map<String, String> userRoles;

    private String currentUser;
    private String currentRole;

    // ================= CONSTRUCTOR =================
    public AuthManager() {
        userPasswords = new HashMap<>();
        userRoles = new HashMap<>();
        loadUsers();
    }

    // ================= LOAD USERS =================
    private void loadUsers() {

        File file = new File(FILE_PATH);

        // If file not exists → create default system
        if (!file.exists()) {

            System.out.println("Creating default users...");

            userPasswords.put("admin", "admin123");
            userRoles.put("admin", "ADMIN");

            userPasswords.put("reception", "rec123");
            userRoles.put("reception", "RECEPTIONIST");

            userPasswords.put("doctor1", "doc123");
            userRoles.put("doctor1", "DOCTOR");

            userPasswords.put("patient1", "pat123");
            userRoles.put("patient1", "PATIENT");

            saveUsers();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length == 3) {
                    userPasswords.put(parts[0], parts[1]);
                    userRoles.put(parts[0], parts[2]);
                }
            }

            System.out.println("Users loaded successfully.");

        } catch (Exception e) {
            System.out.println("Error loading users.");
        }
    }

    // ================= SAVE USERS =================
    private void saveUsers() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for (String user : userPasswords.keySet()) {

                bw.write(user + "," +
                        userPasswords.get(user) + "," +
                        userRoles.get(user));

                bw.newLine();
            }

        } catch (Exception e) {
            System.out.println("Error saving users.");
        }
    }

    // ================= LOGIN =================
    public boolean login(String username, String password) {

        if (!userPasswords.containsKey(username)) {
            System.out.println("User not found.");
            return false;
        }

        if (!userPasswords.get(username).equals(password)) {
            System.out.println("Incorrect password.");
            return false;
        }

        currentUser = username;
        currentRole = userRoles.get(username);

        System.out.println("Login successful!");
        System.out.println("Logged in as: " + currentRole);

        return true;
    }

    // ================= REGISTER USER =================
    public void registerUser(String username, String password, String role) {

        if (userPasswords.containsKey(username)) {
            System.out.println("User already exists.");
            return;
        }

        role = role.toUpperCase();

        if (!isValidRole(role)) {
            System.out.println("Invalid role.");
            return;
        }

        userPasswords.put(username, password);
        userRoles.put(username, role);

        saveUsers();

        System.out.println("User registered successfully.");
    }

    // ================= ROLE VALIDATION =================
    private boolean isValidRole(String role) {

        return role.equals("ADMIN") ||
               role.equals("DOCTOR") ||
               role.equals("PATIENT") ||
               role.equals("RECEPTIONIST");
    }

    // ================= PERMISSION CHECK =================
    public boolean hasPermission(String action) {

        if (currentRole == null) return false;

        switch (currentRole) {

            case "ADMIN":
                return true;

            case "PATIENT":
                return action.equals("BOOK") ||
                       action.equals("CANCEL") ||
                       action.equals("VIEW");

            case "DOCTOR":
                return action.equals("ACCEPT") ||
                       action.equals("UPDATE") ||
                       action.equals("VIEW");

            case "RECEPTIONIST":
                return action.equals("REPORT") ||
                       action.equals("VIEW");

            default:
                return false;
        }
    }

    // ================= GET CURRENT USER =================
    public String getCurrentUser() {
        return currentUser;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    // ================= LOGOUT =================
    public void logout() {
        currentUser = null;
        currentRole = null;
        System.out.println("Logged out.");
    }

    // ================= DEBUG USERS =================
    public void printAllUsers() {

        System.out.println("\n--- USERS LIST ---");

        for (String user : userPasswords.keySet()) {
            System.out.println(user + " (" + userRoles.get(user) + ")");
        }
    }

    // ================= REMOVE USER =================
    public void removeUser(String username) {

        if (!userPasswords.containsKey(username)) {
            System.out.println("User not found.");
            return;
        }

        userPasswords.remove(username);
        userRoles.remove(username);

        saveUsers();

        System.out.println("User removed.");
    }

    // ================= CHANGE PASSWORD =================
    public void changePassword(String username, String newPassword) {

        if (!userPasswords.containsKey(username)) {
            System.out.println("User not found.");
            return;
        }

        userPasswords.put(username, newPassword);
        saveUsers();

        System.out.println("Password updated.");
    }

    // ================= CHECK ROLE =================
    public boolean isAdmin() {
        return "ADMIN".equals(currentRole);
    }

    public boolean isDoctor() {
        return "DOCTOR".equals(currentRole);
    }

    public boolean isPatient() {
        return "PATIENT".equals(currentRole);
    }

    public boolean isReceptionist() {
        return "RECEPTIONIST".equals(currentRole);
    }

    // ================= ROLE BASED MESSAGE =================
    public void showRoleMenu() {

        System.out.println("\n===== ROLE MENU =====");

        switch (currentRole) {

            case "ADMIN":
                System.out.println("Admin can:");
                System.out.println("- Add Doctor");
                System.out.println("- Register Patient");
                break;

            case "PATIENT":
                System.out.println("Patient can:");
                System.out.println("- Book Appointment");
                System.out.println("- Cancel Appointment");
                break;

            case "DOCTOR":
                System.out.println("Doctor can:");
                System.out.println("- Accept Appointment");
                System.out.println("- Update Availability");
                break;

            case "RECEPTIONIST":
                System.out.println("Receptionist can:");
                System.out.println("- Generate Report");
                break;
        }
    }
}