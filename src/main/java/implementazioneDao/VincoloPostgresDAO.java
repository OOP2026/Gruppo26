package implementazioneDao;

import dao.VincoloDAO;
import database_connection.ConnessioneDatabase;
import model.*;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class VincoloPostgresDAO implements VincoloDAO {


    private static final String SELECT_BASE =
            "SELECT v.id_vincolo, v.giorno, v.ora_inizio, v.ora_fine, v.approvato, " +
                    "       u.login, u.nome, u.cognome, u.email " +
                    "FROM vincoli v " +
                    "JOIN utenti u ON v.login_docente = u.login ";

    private static final String SQL_TROVA_TUTTI   = SELECT_BASE;
    private static final String SQL_PER_DOCENTE   = SELECT_BASE + "WHERE v.login_docente = ?";
    private static final String SQL_APPROVATI     = SELECT_BASE + "WHERE v.approvato = TRUE";
    private static final String SQL_PER_ID        = SELECT_BASE + "WHERE v.id_vincolo = ?";

    private static final String SQL_CONTA_PER_DOCENTE =
            "SELECT COUNT(*) FROM vincoli WHERE login_docente = ?";

    private static final String SQL_INSERISCI =
            "INSERT INTO vincoli (login_docente, giorno, ora_inizio, ora_fine, approvato) " +
                    "VALUES (?, ?, ?, ?, FALSE) RETURNING id_vincolo";

    private static final String SQL_AGGIORNA_APPROVAZIONE =
            "UPDATE vincoli SET approvato = ? WHERE id_vincolo = ?";

    private static final String SQL_ELIMINA =
            "DELETE FROM vincoli WHERE id_vincolo = ?";


    private Vincolo mappaRiga(ResultSet rs) throws SQLException {
        Docente docente = new Docente(
                rs.getString("nome"),
                rs.getString("cognome"),
                rs.getString("email"),
                rs.getString("login"),
                ""
        );

        String    giorno     = rs.getString("giorno");
        LocalTime oraInizio  = rs.getTime("ora_inizio").toLocalTime();
        LocalTime oraFine    = rs.getTime("ora_fine").toLocalTime();
        boolean   approvato  = rs.getBoolean("approvato");

        Vincolo v = new Vincolo(docente, giorno, oraInizio, oraFine);
        v.setApprovato(approvato);
        v.setId(rs.getInt("id_vincolo")); // FIX: salva l'id reale del DB, prima andava perso
        return v;
    }

    @Override
    public List<Vincolo> trovaTutti() {
        List<Vincolo> lista = new ArrayList<>();
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_TROVA_TUTTI);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mappaRiga(rs));

        } catch (SQLException e) {
            System.err.println("VincoloPostgresDAO.trovaTutti(): " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Vincolo> trovaPerDocente(String loginDocente) {
        List<Vincolo> lista = new ArrayList<>();
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_PER_DOCENTE)) {

            ps.setString(1, loginDocente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mappaRiga(rs));
            }
        } catch (SQLException e) {
            System.err.println("VincoloPostgresDAO.trovaPerDocente(): " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Vincolo> trovaApprovati() {
        List<Vincolo> lista = new ArrayList<>();
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_APPROVATI);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(mappaRiga(rs));

        } catch (SQLException e) {
            System.err.println("VincoloPostgresDAO.trovaApprovati(): " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Optional<Vincolo> trovaPerID(int id) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_PER_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mappaRiga(rs));
            }
        } catch (SQLException e) {
            System.err.println("VincoloPostgresDAO.trovaPerID(): " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public int contaPerDocente(String loginDocente) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_CONTA_PER_DOCENTE)) {

            ps.setString(1, loginDocente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("VincoloPostgresDAO.contaPerDocente(): " + e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean inserisci(Vincolo vincolo) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERISCI)) {

            ps.setString(1, vincolo.getDocente().getLogin());
            ps.setString(2, vincolo.getGiorno());
            ps.setTime  (3, Time.valueOf(vincolo.getOraInizio()));
            ps.setTime  (4, Time.valueOf(vincolo.getOraFine()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vincolo.setId(rs.getInt("id_vincolo")); // FIX: salva l'id generato nell'oggetto
                    return true;
                }
            }
            return false;

        } catch (SQLException e) {
            System.err.println("VincoloPostgresDAO.inserisci(): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean aggiornaApprovazione(int id, boolean approvato) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_AGGIORNA_APPROVAZIONE)) {

            ps.setBoolean(1, approvato);
            ps.setInt    (2, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("VincoloPostgresDAO.aggiornaApprovazione(): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean elimina(int id) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_ELIMINA)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("VincoloPostgresDAO.elimina(): " + e.getMessage());
            return false;
        }
    }
}
