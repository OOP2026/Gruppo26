package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class ConnessioneDatabase {
    private static final String URL = "jdbc:postgresql://localhost:5432/Gruppo26";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Simo26";

    private static Connection connection = null;

    private ConnessioneDatabase() {}

    public static Connection getConnection() {
        if (connection == null) {
            try {

                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connessione al database stabilita con successo.");
            } catch (ClassNotFoundException | SQLException e) {
                System.err.println("Errore di connessione al database: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }
}