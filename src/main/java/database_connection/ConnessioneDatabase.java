package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    private static final String URL      = "jdbc:postgresql://localhost:5432/Gruppo26";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "Simo26";

    private ConnessioneDatabase() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL non trovato: " + e.getMessage(), e);
        }
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("ConnessioneDatabase: connessione aperta.");
        return conn;
    }
}
