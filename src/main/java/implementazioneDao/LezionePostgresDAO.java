package dao.postgres;

import dao.LezioneDAO;
import database_connection.ConnessioneDatabase;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LezionePostgresDAO implements LezioneDAO {

    private static final String SELECT_BASE =
            "SELECT l.id_lezione, l.giorno, l.ora_inizio, l.ora_fine, l.nome_aula, " +
                    "       i.id_insegnamento, i.nome_insegnamento, i.cfu, i.anno, " +
                    "       u.login, u.nome, u.cognome, u.email " +
                    "FROM lezioni l " +
                    "JOIN insegnamenti i ON l.id_insegnamento = i.id_insegnamento " +
                    "JOIN utenti u       ON i.login_docente   = u.login ";

    private static final String SQL_TROVA_TUTTE = SELECT_BASE;
    private static final String SQL_PER_GIORNO  = SELECT_BASE + "WHERE l.giorno = ?";
    private static final String SQL_PER_AULA    = SELECT_BASE + "WHERE l.nome_aula = ?";
    private static final String SQL_PER_DOCENTE = SELECT_BASE + "WHERE u.login = ?";
    private static final String SQL_PER_ID      = SELECT_BASE + "WHERE l.id_lezione = ?";

    private static final String SQL_INSERISCI =
            "INSERT INTO lezioni (giorno, ora_inizio, ora_fine, id_insegnamento, nome_aula) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING id_lezione";

    private static final String SQL_AGGIORNA =
            "UPDATE lezioni SET giorno = ?, ora_inizio = ?, ora_fine = ?, nome_aula = ? " +
                    "WHERE id_lezione = ?";

    private static final String SQL_ELIMINA =
            "DELETE FROM lezioni WHERE id_lezione = ?";

    private Lezione mappaRiga(ResultSet rs) throws SQLException {
        LocalDate giorno    = rs.getDate("giorno").toLocalDate();
        LocalTime oraInizio = rs.getTime("ora_inizio").toLocalTime();
        LocalTime oraFine   = rs.getTime("ora_fine").toLocalTime();

        Aula aula = new Aula(rs.getString("nome_aula"));

        Docente docente = new Docente(
                rs.getString("nome"), rs.getString("cognome"),
                rs.getString("email"), rs.getString("login"), "");

        Insegnamento insegnamento = new Insegnamento(
                rs.getString("nome_insegnamento"), rs.getInt("cfu"),
                rs.getInt("anno"), docente);

        Lezione lezione = new Lezione(giorno, oraInizio, oraFine, insegnamento, aula);
        // Salva l'ID reale del DB
        lezione.setId(rs.getInt("id_lezione"));
        return lezione;
    }

    private List<Lezione> eseguiQuery(String sql, String parametro) {
        List<Lezione> lista = new ArrayList<>();
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mappaRiga(rs));
            }
        } catch (SQLException e) {
            System.err.println("LezionePostgresDAO.eseguiQuery(): " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Lezione> trovaTutte() {
        List<Lezione> lista = new ArrayList<>();
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_TROVA_TUTTE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mappaRiga(rs));
        } catch (SQLException e) {
            System.err.println("LezionePostgresDAO.trovaTutte(): " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Lezione> trovaPerGiorno(LocalDate giorno) {
        List<Lezione> lista = new ArrayList<>();
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_PER_GIORNO)) {
            ps.setDate(1, Date.valueOf(giorno));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mappaRiga(rs));
            }
        } catch (SQLException e) {
            System.err.println("LezionePostgresDAO.trovaPerGiorno(): " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Lezione> trovaPerAula(String nomeAula) { return eseguiQuery(SQL_PER_AULA, nomeAula); }

    @Override
    public List<Lezione> trovaPerDocente(String loginDocente) { return eseguiQuery(SQL_PER_DOCENTE, loginDocente); }

    @Override
    public Optional<Lezione> trovaPerID(int id) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_PER_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mappaRiga(rs));
            }
        } catch (SQLException e) {
            System.err.println("LezionePostgresDAO.trovaPerID(): " + e.getMessage());
        }
        return Optional.empty();
    }

    private int getIdInsegnamento(Connection conn, String nomeInsegnamento) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT id_insegnamento FROM insegnamenti WHERE nome_insegnamento = ?")) {
            ps.setString(1, nomeInsegnamento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_insegnamento");
            }
        }
        throw new SQLException("Insegnamento non trovato: " + nomeInsegnamento);
    }

    @Override
    public boolean inserisci(Lezione lezione) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERISCI)) {
            int idInsegnamento = getIdInsegnamento(conn, lezione.getInsegnamento().getNomeInsegnamento());
            ps.setDate  (1, Date.valueOf(lezione.getGiorno()));
            ps.setTime  (2, Time.valueOf(lezione.getOraInizio()));
            ps.setTime  (3, Time.valueOf(lezione.getOraFine()));
            ps.setInt   (4, idInsegnamento);
            ps.setString(5, lezione.getAula().getNomeAula());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lezione.setId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("LezionePostgresDAO.inserisci(): " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean aggiorna(int id, Lezione lezione) {
        // Usa sempre l'ID passato come parametro (o quello nell'oggetto se disponibile)
        int idDaUsare = (lezione.getId() > 0) ? lezione.getId() : id;
        System.out.println("LezionePostgresDAO.aggiorna(): id=" + idDaUsare +
                " giorno=" + lezione.getGiorno() +
                " oraInizio=" + lezione.getOraInizio() +
                " oraFine=" + lezione.getOraFine());
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_AGGIORNA)) {
            ps.setDate  (1, Date.valueOf(lezione.getGiorno()));
            ps.setTime  (2, Time.valueOf(lezione.getOraInizio()));
            ps.setTime  (3, Time.valueOf(lezione.getOraFine()));
            ps.setString(4, lezione.getAula().getNomeAula());
            ps.setInt   (5, idDaUsare);
            int righe = ps.executeUpdate();
            System.out.println("LezionePostgresDAO.aggiorna(): righe aggiornate=" + righe);
            return righe > 0;
        } catch (SQLException e) {
            System.err.println("LezionePostgresDAO.aggiorna(): " + e.getMessage());
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
            System.err.println("LezionePostgresDAO.elimina(): " + e.getMessage());
            return false;
        }
    }
}
