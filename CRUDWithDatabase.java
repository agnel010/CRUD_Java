import java.sql.*;
import java.util.Scanner;

public class CRUDWithDatabase {
    
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/crud_example?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";  
    static final String PASSWORD = "root";  
    
    private static Connection conn;

    public static void main(String[] args) {
        try {
            // Explicitly load driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Connected to the database.");
            
            Scanner scanner = new Scanner(System.in);
            boolean running = true;
            
            while (running) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Create a new person");
                System.out.println("2. Read all persons");
                System.out.println("3. Update a person");
                System.out.println("4. Delete a person");
                System.out.println("5. Exit");
                
                int choice = scanner.nextInt();
                scanner.nextLine();  
                
                switch (choice) {
                    case 1:
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter age: ");
                        int age = scanner.nextInt();
                        createPerson(name, age);
                        break;
                    case 2:
                        readAllPersons();
                        break;
                    case 3:
                        System.out.print("Enter person ID to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter new name: ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter new age: ");
                        int newAge = scanner.nextInt();
                        updatePerson(updateId, newName, newAge);
                        break;
                    case 4:
                        System.out.print("Enter person ID to delete: ");
                        int deleteId = scanner.nextInt();
                        deletePerson(deleteId);
                        break;
                    case 5:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
            
            conn.close();
            scanner.close();
            System.out.println("Connection closed.");
            
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found! Make sure the JAR is in your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPerson(String name, int age) {
        String sql = "INSERT INTO Person (name, age) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Person added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void readAllPersons() {
        String sql = "SELECT * FROM Person";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nList of Persons:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePerson(int id, String newName, int newAge) {
        String sql = "UPDATE Person SET name = ?, age = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, newAge);
            stmt.setInt(3, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Person updated successfully.");
            } else {
                System.out.println("Person not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePerson(int id) {
        String sql = "DELETE FROM Person WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Person deleted successfully.");
            } else {
                System.out.println("Person not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
