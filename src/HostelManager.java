import java.util.*;
import java.io.*;
import java.time.LocalDate;

/**
 * HostelManager handles all operations:
 * room allotment, vacating, search, reporting, and file persistence.
 */
public class HostelManager {
    private List<Room> rooms;
    private static final String DATA_FILE = "hostel_data.txt";

    public HostelManager() {
        rooms = new ArrayList<>();
        initializeRooms();
    }

    /**
     * Pre-populate the hostel with a set of rooms across floors.
     */
    private void initializeRooms() {
        // Floor 1 - Single rooms
        rooms.add(new Room("101", Room.RoomType.SINGLE, 1, 3000));
        rooms.add(new Room("102", Room.RoomType.SINGLE, 1, 3000));
        rooms.add(new Room("103", Room.RoomType.SINGLE, 1, 3000));
        rooms.add(new Room("104", Room.RoomType.DOUBLE, 1, 2000));
        rooms.add(new Room("105", Room.RoomType.DOUBLE, 1, 2000));

        // Floor 2 - Double rooms
        rooms.add(new Room("201", Room.RoomType.DOUBLE, 2, 2000));
        rooms.add(new Room("202", Room.RoomType.DOUBLE, 2, 2000));
        rooms.add(new Room("203", Room.RoomType.TRIPLE, 2, 1500));
        rooms.add(new Room("204", Room.RoomType.TRIPLE, 2, 1500));
        rooms.add(new Room("205", Room.RoomType.SINGLE, 2, 3500));

        // Floor 3 - Premium
        rooms.add(new Room("301", Room.RoomType.SINGLE, 3, 4000));
        rooms.add(new Room("302", Room.RoomType.SINGLE, 3, 4000));
        rooms.add(new Room("303", Room.RoomType.DOUBLE, 3, 2500));
        rooms.add(new Room("304", Room.RoomType.DOUBLE, 3, 2500));
        rooms.add(new Room("305", Room.RoomType.TRIPLE, 3, 1800));
    }

    // ─────────────────────────────────────────────
    //  1. ALLOT ROOM
    // ─────────────────────────────────────────────
    public void allotRoom(Scanner sc) {
        System.out.println("\n========== ALLOT ROOM ==========");

        // Check if any room is available
        boolean anyAvailable = rooms.stream().anyMatch(Room::isAvailable);
        if (!anyAvailable) {
            System.out.println("Sorry! No rooms are currently available.");
            return;
        }

        System.out.print("Enter Student ID       : ");
        String sid = sc.nextLine().trim();

        // Check duplicate student ID
        if (isStudentAlreadyAllotted(sid)) {
            System.out.println("Error: Student ID " + sid + " is already allotted a room!");
            return;
        }

        System.out.print("Enter Student Name     : ");
        String name = sc.nextLine().trim();
        System.out.print("Enter Course           : ");
        String course = sc.nextLine().trim();
        System.out.print("Enter Year (1/2/3/4)   : ");
        String year = sc.nextLine().trim();
        System.out.print("Enter Contact Number   : ");
        String contact = sc.nextLine().trim();
        System.out.print("Enter Email            : ");
        String email = sc.nextLine().trim();

        // Show available rooms
        System.out.println("\n--- Available Rooms ---");
        rooms.stream()
             .filter(Room::isAvailable)
             .forEach(r -> System.out.println(r));

        System.out.print("\nEnter Room Number to allot: ");
        String roomNo = sc.nextLine().trim().toUpperCase();

        Room selected = findRoom(roomNo);
        if (selected == null) {
            System.out.println("Error: Room " + roomNo + " does not exist.");
            return;
        }
        if (!selected.isAvailable()) {
            System.out.println("Error: Room " + roomNo + " is not available.");
            return;
        }

        String today = LocalDate.now().toString();
        Student student = new Student(sid, name, course, year, contact, email, today);
        selected.allot(student);

        System.out.println("\n✓ Room " + roomNo + " successfully allotted to " + name + "!");
        System.out.println("  Monthly Fee : Rs. " + selected.getFeePerMonth());
        System.out.println("  Allotment Date : " + today);
    }

    // ─────────────────────────────────────────────
    //  2. VACATE ROOM
    // ─────────────────────────────────────────────
    public void vacateRoom(Scanner sc) {
        System.out.println("\n========== VACATE ROOM ==========");
        System.out.print("Enter Room Number to vacate: ");
        String roomNo = sc.nextLine().trim().toUpperCase();

        Room room = findRoom(roomNo);
        if (room == null) {
            System.out.println("Error: Room " + roomNo + " does not exist.");
            return;
        }
        if (room.isAvailable()) {
            System.out.println("Error: Room " + roomNo + " is already vacant.");
            return;
        }

        System.out.println("\nCurrent occupant:");
        System.out.println(room.getStudent());
        System.out.print("\nAre you sure you want to vacate this room? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            String studentName = room.getStudent().getName();
            room.vacate();
            System.out.println("✓ Room " + roomNo + " vacated. " + studentName + " has been checked out.");
        } else {
            System.out.println("Vacate operation cancelled.");
        }
    }

    // ─────────────────────────────────────────────
    //  3. VIEW ALL ROOMS
    // ─────────────────────────────────────────────
    public void viewAllRooms() {
        System.out.println("\n========== ALL ROOMS ==========");
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
            return;
        }
        int floor = -1;
        for (Room r : rooms) {
            if (r.getFloor() != floor) {
                floor = r.getFloor();
                System.out.println("\n--- Floor " + floor + " ---");
            }
            System.out.println(r);
        }
    }

    // ─────────────────────────────────────────────
    //  4. SEARCH STUDENT
    // ─────────────────────────────────────────────
    public void searchStudent(Scanner sc) {
        System.out.println("\n========== SEARCH STUDENT ==========");
        System.out.println("Search by: 1. Student ID   2. Student Name");
        System.out.print("Choice: ");
        String opt = sc.nextLine().trim();

        boolean found = false;
        if (opt.equals("1")) {
            System.out.print("Enter Student ID: ");
            String sid = sc.nextLine().trim();
            for (Room r : rooms) {
                if (r.getStudent() != null && r.getStudent().getStudentId().equalsIgnoreCase(sid)) {
                    printStudentRoomInfo(r);
                    found = true;
                    break;
                }
            }
        } else if (opt.equals("2")) {
            System.out.print("Enter Student Name (or part of it): ");
            String namePart = sc.nextLine().trim().toLowerCase();
            for (Room r : rooms) {
                if (r.getStudent() != null && r.getStudent().getName().toLowerCase().contains(namePart)) {
                    printStudentRoomInfo(r);
                    found = true;
                }
            }
        } else {
            System.out.println("Invalid option.");
            return;
        }

        if (!found) System.out.println("No student record found.");
    }

    private void printStudentRoomInfo(Room r) {
        System.out.println("\n--- Student Details ---");
        System.out.println(r.getStudent());
        System.out.println("  Room Number  : " + r.getRoomNumber());
        System.out.println("  Room Type    : " + r.getType());
        System.out.println("  Floor        : " + r.getFloor());
        System.out.printf("  Monthly Fee  : Rs. %.0f%n", r.getFeePerMonth());
    }

    // ─────────────────────────────────────────────
    //  5. VIEW AVAILABLE ROOMS
    // ─────────────────────────────────────────────
    public void viewAvailableRooms() {
        System.out.println("\n========== AVAILABLE ROOMS ==========");
        long count = rooms.stream().filter(Room::isAvailable).count();
        if (count == 0) {
            System.out.println("No rooms are currently available.");
            return;
        }
        rooms.stream().filter(Room::isAvailable).forEach(r -> System.out.println(r));
        System.out.println("\nTotal Available: " + count);
    }

    // ─────────────────────────────────────────────
    //  6. VIEW OCCUPIED ROOMS
    // ─────────────────────────────────────────────
    public void viewOccupiedRooms() {
        System.out.println("\n========== OCCUPIED ROOMS ==========");
        long count = rooms.stream().filter(r -> !r.isAvailable()).count();
        if (count == 0) {
            System.out.println("No rooms are currently occupied.");
            return;
        }
        rooms.stream().filter(r -> r.getStatus() == Room.RoomStatus.OCCUPIED)
             .forEach(r -> System.out.println(r));
        System.out.println("\nTotal Occupied: " + count);
    }

    // ─────────────────────────────────────────────
    //  7. GENERATE REPORT
    // ─────────────────────────────────────────────
    public void generateReport() {
        System.out.println("\n========================================");
        System.out.println("         HOSTEL STATUS REPORT           ");
        System.out.println("  Date: " + LocalDate.now());
        System.out.println("========================================");

        long total     = rooms.size();
        long available = rooms.stream().filter(Room::isAvailable).count();
        long occupied  = rooms.stream().filter(r -> r.getStatus() == Room.RoomStatus.OCCUPIED).count();
        long singles   = rooms.stream().filter(r -> r.getType() == Room.RoomType.SINGLE).count();
        long doubles   = rooms.stream().filter(r -> r.getType() == Room.RoomType.DOUBLE).count();
        long triples   = rooms.stream().filter(r -> r.getType() == Room.RoomType.TRIPLE).count();
        double revenue = rooms.stream()
                              .filter(r -> r.getStatus() == Room.RoomStatus.OCCUPIED)
                              .mapToDouble(Room::getFeePerMonth)
                              .sum();

        System.out.printf("  Total Rooms      : %d%n", total);
        System.out.printf("  Available Rooms  : %d%n", available);
        System.out.printf("  Occupied Rooms   : %d%n", occupied);
        System.out.printf("  Occupancy Rate   : %.1f%%%n", (occupied * 100.0 / total));
        System.out.println("----------------------------------------");
        System.out.printf("  Single Rooms     : %d%n", singles);
        System.out.printf("  Double Rooms     : %d%n", doubles);
        System.out.printf("  Triple Rooms     : %d%n", triples);
        System.out.println("----------------------------------------");
        System.out.printf("  Monthly Revenue  : Rs. %.0f%n", revenue);
        System.out.println("========================================");

        // Floor-wise breakdown
        System.out.println("\n  Floor-wise Summary:");
        for (int f = 1; f <= 3; f++) {
            final int floor = f;
            long fTotal     = rooms.stream().filter(r -> r.getFloor() == floor).count();
            long fOccupied  = rooms.stream().filter(r -> r.getFloor() == floor && r.getStatus() == Room.RoomStatus.OCCUPIED).count();
            System.out.printf("  Floor %d : %d/%d occupied%n", floor, fOccupied, fTotal);
        }
        System.out.println("========================================");
    }

    // ─────────────────────────────────────────────
    //  8. SAVE TO FILE
    // ─────────────────────────────────────────────
    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Room r : rooms) {
                pw.println(r.toFileString());
            }
            System.out.println("✓ Data saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  9. LOAD FROM FILE
    // ─────────────────────────────────────────────
    public void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("No saved data file found. Starting with fresh data.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<Room> loaded = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Room r = Room.fromFileString(line);
                    if (r != null) loaded.add(r);
                }
            }
            if (!loaded.isEmpty()) {
                rooms = loaded;
                System.out.println("✓ Data loaded successfully. " + loaded.size() + " rooms loaded.");
            } else {
                System.out.println("Warning: File was empty or corrupt. Using default data.");
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────
    private Room findRoom(String roomNumber) {
        return rooms.stream()
                    .filter(r -> r.getRoomNumber().equalsIgnoreCase(roomNumber))
                    .findFirst()
                    .orElse(null);
    }

    private boolean isStudentAlreadyAllotted(String studentId) {
        return rooms.stream()
                    .anyMatch(r -> r.getStudent() != null &&
                                  r.getStudent().getStudentId().equalsIgnoreCase(studentId));
    }
}
