import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class for managing PostgreSQL database connections
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Database configuration
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/hockey_league";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres"; // Change this to your PostgreSQL password
    private static final String DB_DRIVER = "org.postgresql.Driver";

    private DatabaseConnection() {
        try {
            // Load PostgreSQL driver
            Class.forName(DB_DRIVER);
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL Driver not found!");
            System.err.println("Add postgresql-XX.X.X.jar to your project libraries");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Get singleton instance of DatabaseConnection
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Get active database connection
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Connection lost, attempting to reconnect...");
            throw new RuntimeException(e);
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}

