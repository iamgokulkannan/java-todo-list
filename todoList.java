import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class todoList {
    public static void main(String[] args) {
        // Database credentials
        String jdbcURL = "jdbc:mysql://localhost:3306/project";
        String dbUsername = "root";
        String dbPassword = "Vi3iq4y8";
        // JDBC objects
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);  // To read user input
        try {
            // Step 1: Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Step 2: Connect to the database
            connection = DriverManager.getConnection(jdbcURL, dbUsername, dbPassword);
            System.out.println("Connected to the database!");
            boolean running = true;
            while (running) {
                // Display menu options
                System.out.println("\nTo-Do List Menu:");
                System.out.println("1. Add new to-do");
                System.out.println("2. Delete a to-do");
                System.out.println("3. Mark as finished");
                System.out.println("4. Show list");
                System.out.println("5. Exit");
                // Get user choice
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character left by nextInt()
                switch (choice) {
                    case 1:
                        // Add new to-do
                        System.out.print("Enter the task name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter the time (in minutes): ");
                        int time = scanner.nextInt();
                        scanner.nextLine(); // Clear the newline character
                        System.out.print("Enter the difficulty (Easy/Medium/Hard): ");
                        String difficulty = scanner.nextLine();
                        String insertQuery = "INSERT INTO proj (name, time, difficulty) VALUES (?, ?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                        preparedStatement.setString(1, name);
                        preparedStatement.setInt(2, time);
                        preparedStatement.setString(3, difficulty);
                        int rowsInserted = preparedStatement.executeUpdate();
                        System.out.println(rowsInserted + " row(s) inserted.");
                        break;
                    case 2:
                        // Delete a to-do
                        System.out.print("Enter the ID of the to-do to delete: ");
                        int deleteId = scanner.nextInt();
                        scanner.nextLine(); // Clear the newline character
                        String deleteQuery = "DELETE FROM proj WHERE id = ?";
                        PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
                        deleteStmt.setInt(1, deleteId);
                        int rowsDeleted = deleteStmt.executeUpdate();
                        if (rowsDeleted > 0) {
                            System.out.println("To-do with ID " + deleteId + " has been deleted.");
                        } else {
                            System.out.println("No to-do found with the given ID.");
                        }
                        break;
                    case 3:
                        // Mark as finished
                        System.out.print("Enter the ID of the to-do to mark as finished: ");
                        int finishedId = scanner.nextInt();
                        scanner.nextLine(); // Clear the newline character
                        String updateQuery = "UPDATE proj SET difficulty = 'Finished' WHERE id = ?";
                        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                        updateStmt.setInt(1, finishedId);
                        int rowsUpdated = updateStmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("To-do with ID " + finishedId + " marked as finished.");
                        } else {
                            System.out.println("No to-do found with the given ID.");
                        }
                        break;
                    case 4:
                        // Show list of to-dos
                        String selectQuery = "SELECT * FROM proj";
                        PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
                        ResultSet resultSet = selectStmt.executeQuery();
                        System.out.println("ID | Name       | Time | Difficulty");
                        System.out.println("---------------------------------------");
                        while (resultSet.next()) {
                            int id = resultSet.getInt("id");
                            String taskName = resultSet.getString("name");
                            int taskTime = resultSet.getInt("time");
                            String taskDifficulty = resultSet.getString("difficulty");
                            System.out.println(id + "  | " + taskName + " | " + taskTime + " min | " + taskDifficulty);
                        }
                        break;
                    case 5:
                        // Exit
                        running = false;
                        System.out.println("Exiting program...");
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("Database connection closed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            scanner.close();
        }
    }
}