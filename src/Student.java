/**
 * Represents a student with personal and academic details.
 * Used for room allotment in the Hostel Management System.
 */
public class Student {
    private String studentId;
    private String name;
    private String course;
    private String year;
    private String contactNumber;
    private String email;
    private String allotmentDate;

    public Student(String studentId, String name, String course, String year,
                   String contactNumber, String email, String allotmentDate) {
        this.studentId = studentId;
        this.name = name;
        this.course = course;
        this.year = year;
        this.contactNumber = contactNumber;
        this.email = email;
        this.allotmentDate = allotmentDate;
    }

    // Getters
    public String getStudentId()     { return studentId; }
    public String getName()          { return name; }
    public String getCourse()        { return course; }
    public String getYear()          { return year; }
    public String getContactNumber() { return contactNumber; }
    public String getEmail()         { return email; }
    public String getAllotmentDate() { return allotmentDate; }

    /**
     * Serialize student data as a comma-separated string for file storage.
     */
    public String toFileString() {
        return studentId + "," + name + "," + course + "," + year + ","
                + contactNumber + "," + email + "," + allotmentDate;
    }

    /**
     * Deserialize a Student object from a comma-separated string.
     */
    public static Student fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 7) return null;
        return new Student(parts[0], parts[1], parts[2], parts[3],
                           parts[4], parts[5], parts[6]);
    }

    @Override
    public String toString() {
        return String.format(
            "  Student ID   : %s\n  Name         : %s\n  Course       : %s\n" +
            "  Year         : %s\n  Contact      : %s\n  Email        : %s\n  Allotted On  : %s",
            studentId, name, course, year, contactNumber, email, allotmentDate
        );
    }
}
