package Hotel_Reservation;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;


public class hotel_reservation_system {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_shanice";
    private static final String username = "root";
    private static final String password = "priyanshu@";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            while (true) {
                System.out.println();
                System.out.println("************************ ~HOTEL_SHANICE~ ********************************");
                System.out.println();
                System.out.println("                 || HOTEL MANAGEMENT SYSTEM || ");
                System.out.println();
                Scanner scanner = new Scanner(System.in);
                System.out.println("1. Reserve A Room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.print("Choose an option : ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(connection, scanner);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection, scanner);
                        break;
                    case 4:
                        updateReservation(connection, scanner);
                        break;
                    case 5:
                        deleteReservation(connection, scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid Choice! Try Again.");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    private static void reserveRoom(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Guest Name : ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.print("Enter Room Number : ");
            int roomNumber = scanner.nextInt();
            System.out.print("Enter Contact Number : ");
            String contactNumber = scanner.next();

            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) " + "VALUES ('" + guestName + "'," + roomNumber + ",'" + contactNumber + "')";

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation Successful...");
                } else {
                    System.out.println("Reservation failed!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReservations(Connection connection) throws SQLException {
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Current Reservations : ");
            System.out.println("+----------------+----------------+----------------+--------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest          | Room number    | Contact Number     | Reservation Date        |");
            System.out.println("+----------------+----------------+----------------+--------------------+-------------------------+");

            while (resultSet.next()) {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                // format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-13d | %-20s| %-19s  |\n",
                        reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }

            System.out.println("+----------------+----------------+----------------+--------------------+-------------------------+");
        }
    }


    private static void getRoomNumber(Connection connection, Scanner scanner){
        try {
            System.out.println("Enter reservation ID : ");
            int reservationId = scanner.nextInt();
            System.out.println("Enter guest name : ");
            String guestName = scanner.next();

            String sql = "SELECT room_number FROM reservations " +
                    "WHERE reservation_id = " + reservationId +
                    " AND guest_name = '" + guestName + "'";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if(resultSet.next()){
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number for Reservation ID " + reservationId +
                            " and Guest " + guestName + " is in : " + roomNumber);
                } else {
                    System.out.println("Reservation not found for the given ID and guest name !");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static void updateReservation(Connection connection , Scanner scanner) {
        try{
            System.out.println("Enter reservation ID you want to update : ");
            int reservationId = scanner.nextInt();
            scanner.nextLine(); // consume the newLine character

            if(!reservationExists(connection, reservationId)){
                System.out.println("Reservation not found for the given ID !");
                return;
            }

            System.out.println("Enter new guest name : ");
            String newGuestName = scanner.nextLine();
            System.out.println("Enter new room number : ");
            int newRoomNumber = scanner.nextInt();
            System.out.println("Enter new contact number : ");
            String newContactNumber = scanner.next();

            String sql = "UPDATE reservations SET guest_name = '"+ newGuestName + "'," + "room_number = " + newRoomNumber + ", " + "contact_number = '" + newContactNumber + "' " + "WHERE reservation_id = " + reservationId;

            try(Statement statement = connection.createStatement()){
                int affectedRows = statement.executeUpdate(sql);

                if(affectedRows > 0){
                    System.out.println("Reservation updated successfully...");
                } else{
                    System.out.println("Reservation update failed !");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static void deleteReservation(Connection connection, Scanner scanner){
        try {
            System.out.println("Enter Reservation ID to be deleted : ");
            int reservationId = scanner.nextInt();

            if(!reservationExists(connection, reservationId)){
                System.out.println("Reservation not found for given ID ! ");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

            try(Statement statement = connection.createStatement()){
                int affectedRows = statement.executeUpdate(sql);

                if(affectedRows > 0){
                    System.out.println("Reservation deleted successfully.");
                } else{
                    System.out.println("Reservation deletion failed ! ");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static boolean reservationExists(Connection connection, int reservationId) {
        try {
            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){

                return resultSet.next(); // if there's a result , the reservation exits.....
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // handle database errors as needed
        }
    }


    public static void exit() throws InterruptedException{
        System.out.println();
        System.out.print("Exiting System");
        int i = 5;
        while(i != 0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println();
        System.out.println("********** THANK YOU FOR USING HOTEL RESERVATION SYSTEM **********");
    }
}
