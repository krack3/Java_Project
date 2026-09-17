import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HostelManager manager = new HostelManager();
        Scanner sc = new Scanner(System.in);
        int choice;

        System.out.println("==========================================");
        System.out.println("   HOSTEL ROOM ALLOTMENT SYSTEM v1.0     ");
        System.out.println("==========================================");

        do {
            System.out.println("\n---------- MAIN MENU ----------");
            System.out.println("1. Allot Room to Student");
            System.out.println("2. Vacate Room");
            System.out.println("3. View All Rooms");
            System.out.println("4. Search Student");
            System.out.println("5. View Available Rooms");
            System.out.println("6. View Occupied Rooms");
            System.out.println("7. Generate Report");
            System.out.println("8. Save Data to File");
            System.out.println("9. Load Data from File");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: manager.allotRoom(sc); break;
                case 2: manager.vacateRoom(sc); break;
                case 3: manager.viewAllRooms(); break;
                case 4: manager.searchStudent(sc); break;
                case 5: manager.viewAvailableRooms(); break;
                case 6: manager.viewOccupiedRooms(); break;
                case 7: manager.generateReport(); break;
                case 8: manager.saveToFile(); break;
                case 9: manager.loadFromFile(); break;
                case 0: System.out.println("\nThank you for using Hostel Room Allotment System. Goodbye!"); break;
                default: System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 0);

        sc.close();
    }
}
