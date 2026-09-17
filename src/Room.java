/**
 * Represents a hostel room with its type, capacity, and occupancy status.
 */
public class Room {
    public enum RoomType { SINGLE, DOUBLE, TRIPLE }
    public enum RoomStatus { AVAILABLE, OCCUPIED, MAINTENANCE }

    private String roomNumber;
    private RoomType type;
    private RoomStatus status;
    private Student student;   // null if not occupied
    private int floor;
    private double feePerMonth;

    public Room(String roomNumber, RoomType type, int floor, double feePerMonth) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.floor = floor;
        this.feePerMonth = feePerMonth;
        this.status = RoomStatus.AVAILABLE;
        this.student = null;
    }

    // Getters
    public String getRoomNumber()  { return roomNumber; }
    public RoomType getType()      { return type; }
    public RoomStatus getStatus()  { return status; }
    public Student getStudent()    { return student; }
    public int getFloor()          { return floor; }
    public double getFeePerMonth() { return feePerMonth; }
    public boolean isAvailable()   { return status == RoomStatus.AVAILABLE; }

    // Allot room to a student
    public boolean allot(Student s) {
        if (status != RoomStatus.AVAILABLE) return false;
        this.student = s;
        this.status = RoomStatus.OCCUPIED;
        return true;
    }

    // Vacate the room
    public boolean vacate() {
        if (status != RoomStatus.OCCUPIED) return false;
        this.student = null;
        this.status = RoomStatus.AVAILABLE;
        return true;
    }

    /**
     * Serialize room to a pipe-delimited string for file storage.
     * Format: roomNumber|type|floor|fee|status|studentData(or NONE)
     */
    public String toFileString() {
        String studentPart = (student != null) ? student.toFileString() : "NONE";
        return roomNumber + "|" + type.name() + "|" + floor + "|"
                + feePerMonth + "|" + status.name() + "|" + studentPart;
    }

    /**
     * Deserialize a Room from a pipe-delimited string.
     */
    public static Room fromFileString(String line) {
        String[] parts = line.split("\\|", 6);
        if (parts.length < 6) return null;
        Room room = new Room(parts[0], RoomType.valueOf(parts[1]),
                             Integer.parseInt(parts[2]), Double.parseDouble(parts[3]));
        room.status = RoomStatus.valueOf(parts[4]);
        if (!parts[5].equals("NONE")) {
            room.student = Student.fromFileString(parts[5]);
        }
        return room;
    }

    @Override
    public String toString() {
        String occupant = (student != null) ? student.getName() + " (" + student.getStudentId() + ")" : "---";
        return String.format(
            "  Room No  : %-6s | Type : %-6s | Floor : %d | Fee : Rs.%-7.0f | Status : %-12s | Occupant : %s",
            roomNumber, type.name(), floor, feePerMonth, status.name(), occupant
        );
    }
}
