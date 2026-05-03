package clinic;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Schedule {

    private String doctorID;

    private Map<DayOfWeek, List<TimeRange>> weeklySchedule;
    private Set<String> bookedSlots;

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("HH:mm");

    // ================= INNER CLASS =================
    static class TimeRange {
        LocalTime start;
        LocalTime end;

        TimeRange(String s, String e) {
            this.start = LocalTime.parse(s);
            this.end = LocalTime.parse(e);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TimeRange)) return false;
            TimeRange tr = (TimeRange) o;
            return start.equals(tr.start) && end.equals(tr.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
    }

    // ================= CONSTRUCTOR =================
    public Schedule(String doctorID) {

        this.doctorID = doctorID;
        this.weeklySchedule = new HashMap<>();
        this.bookedSlots = new HashSet<>();

        initializeDefaultSchedule();
        loadBookedSlotsFromFile(); 
    }

    // =====================================================
    // ================= LOAD BOOKED SLOTS ==================
    // =====================================================

    private void loadBookedSlotsFromFile() {

        Map<String, String[]> data = FileManager.loadAppointments();

        for (String[] a : data.values()) {

            String did = a[2];
            String date = a[3];
            String time = a[4];

            if (did.equals(doctorID)) {
                bookedSlots.add(date + " " + time);
            }
        }
    }

    // =====================================================
    // ================= DEFAULT SCHEDULE ===================
    // =====================================================

    private void initializeDefaultSchedule() {

        for (DayOfWeek day : DayOfWeek.values()) {

            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {

                addWorkingHours(day, "10:00", "14:00");
                addWorkingHours(day, "17:00", "19:00");
            }
        }
    }

    // =====================================================
    // ================= ADD WORKING HOURS ==================
    // =====================================================

    public void addWorkingHours(DayOfWeek day, String start, String end) {

        weeklySchedule.putIfAbsent(day, new ArrayList<>());

        TimeRange newRange = new TimeRange(start, end);

        if (!weeklySchedule.get(day).contains(newRange)) {
            weeklySchedule.get(day).add(newRange);
        }
    }

    // =====================================================
    // ================= GET AVAILABLE SLOTS ================
    // =====================================================

    public List<String> getAvailableSlots(String dateStr) {

        Set<String> uniqueSlots = new LinkedHashSet<>();

        LocalDate date = LocalDate.parse(dateStr);
        DayOfWeek day = date.getDayOfWeek();

        if (!weeklySchedule.containsKey(day)) {
            return new ArrayList<>();
        }

        for (TimeRange tr : weeklySchedule.get(day)) {

            LocalTime time = tr.start;

            while (time.isBefore(tr.end)) {

                String formattedTime = time.format(TIME_FMT);
                String slot = date + " " + formattedTime;

                if (!bookedSlots.contains(slot)) {
                    uniqueSlots.add(slot);
                }

                time = time.plusMinutes(30);
            }
        }

        return new ArrayList<>(uniqueSlots);
    }

    // =====================================================
    // ================= BOOK SLOT ==========================
    // =====================================================

    public boolean bookSlot(String date, String time) {

        String slot = date + " " + time;

        if (bookedSlots.contains(slot)) {
            return false;
        }

        bookedSlots.add(slot);
        return true;
    }

    // =====================================================
    // ================= RELEASE SLOT =======================
    // =====================================================

    public void releaseSlot(String date, String time) {
        bookedSlots.remove(date + " " + time);
    }

    // =====================================================
    // ================= SHOW AVAILABLE =====================
    // =====================================================

    public void showAvailableSlots(String date) {

        List<String> slots = getAvailableSlots(date);

        System.out.println("\n===== AVAILABLE SLOTS =====");

        if (slots.isEmpty()) {
            System.out.println("No slots available.");
            return;
        }

        for (String s : slots) {
            System.out.println("👉 " + s);
        }
    }
}