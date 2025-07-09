import java.io.*;
import java.util.*;

class Room {
    int roomNumber;
    String type;
    boolean isBooked;

    public Room(int roomNumber, String type) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.isBooked = false;
    }

    public String toString() {
        return roomNumber + " - " + type + " - " + (isBooked ? "Booked" : "Available");
    }
}

class Booking {
    String customerName;
    Room room;

    public Booking(String customerName, Room room) {
        this.customerName = customerName;
        this.room = room;
    }

    public String toString() {
        return "Customer: " + customerName + ", Room: " + room.roomNumber + " (" + room.type + ")";
    }
}

public class HotelReservationSystem {
    static List<Room> rooms = new ArrayList<>();
    static List<Booking> bookings = new ArrayList<>();
    static final String FILE_NAME = "bookings.txt";

    public static void main(String[] args) {
        initializeRooms();
        loadBookings();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Hotel Reservation System ===");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    showAvailableRooms();
                    break;
                case 2:
                    bookRoom(scanner);
                    break;
                case 3:
                    cancelBooking(scanner);
                    break;
                case 4:
                    showBookings();
                    break;
                case 5:
                    saveBookings();
                    System.out.println("Thank you! Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void initializeRooms() {
        for (int i = 101; i <= 105; i++)
            rooms.add(new Room(i, "Standard"));
        for (int i = 201; i <= 203; i++)
            rooms.add(new Room(i, "Deluxe"));
        for (int i = 301; i <= 302; i++)
            rooms.add(new Room(i, "Suite"));
    }

    private static void showAvailableRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room room : rooms) {
            if (!room.isBooked)
                System.out.println(room);
        }
    }

    private static void bookRoom(Scanner scanner) {
        showAvailableRooms();
        System.out.print("Enter room number to book: ");
        int roomNum = scanner.nextInt();
        scanner.nextLine();  // consume newline

        Room selectedRoom = null;
        for (Room room : rooms) {
            if (room.roomNumber == roomNum && !room.isBooked) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Room not available.");
            return;
        }

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        selectedRoom.isBooked = true;
        Booking booking = new Booking(name, selectedRoom);
        bookings.add(booking);
        System.out.println("Room booked successfully!");
    }

    private static void cancelBooking(Scanner scanner) {
        System.out.print("Enter your name to cancel booking: ");
        String name = scanner.nextLine();
        boolean found = false;

        Iterator<Booking> iterator = bookings.iterator();
        while (iterator.hasNext()) {
            Booking booking = iterator.next();
            if (booking.customerName.equalsIgnoreCase(name)) {
                booking.room.isBooked = false;
                iterator.remove();
                System.out.println("Booking cancelled for " + name);
                found = true;
                break;
            }
        }

        if (!found)
            System.out.println("No booking found for that name.");
    }

    private static void showBookings() {
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
        } else {
            System.out.println("\nCurrent Bookings:");
            for (Booking booking : bookings) {
                System.out.println(booking);
            }
        }
    }

    private static void saveBookings() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Booking booking : bookings) {
                writer.println(booking.customerName + "," + booking.room.roomNumber);
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings.");
        }
    }

    private static void loadBookings() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                int roomNum = Integer.parseInt(parts[1]);

                for (Room room : rooms) {
                    if (room.roomNumber == roomNum) {
                        room.isBooked = true;
                        bookings.add(new Booking(name, room));
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading bookings.");
        }
    }
}
