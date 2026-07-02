package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe di utilità responsabile della creazione di connessioni JDBC verso
 * il database PostgreSQL del sistema.
 * <p>
 * Non è istanziabile: espone unicamente il metodo statico {@link #getConnection()}.
 * </p>
 * <p>
 * <b>Nota:</b> URL, utente e password sono attualmente impostati come costanti
 * nel codice sorgente. In un contesto di produzione è preferibile esternalizzare
 * queste credenziali (es. variabili d'ambiente o file di configurazione escluso
 * dal controllo di versione) per evitare di esporle in chiaro nel repository.
 * </p>
 *
 * @author Gruppo26
 */
public class ConnessioneDatabase {

    /** URL JDBC del database PostgreSQL. */
    private static final String URL      = "jdbc:postgresql://localhost:5432/Gruppo26";

    /** Utente utilizzato per la connessione al database. */
    private static final String USER     = "postgres";

    /** Password utilizzata per la connessione al database. */
    private static final String PASSWORD = "Simo26";

    /** Costruttore privato: la classe espone solo membri statici. */
    private ConnessioneDatabase() {}

    /**
     * Apre e restituisce una nuova connessione JDBC verso il database PostgreSQL,
     * caricando preventivamente il driver {@code org.postgresql.Driver}.
     *
     * @return una nuova connessione aperta verso il database
     * @throws SQLException se il driver PostgreSQL non viene trovato sul
     *                       classpath, oppure se la connessione al database fallisce
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL non trovato: " + e.getMessage(), e);
        }
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        return conn;
    }
}
