package implementazioneDao;

import dao.UtenteDAO;
import database_connection.ConnessioneDatabase;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UtentePostgresDAO implements UtenteDAO {

    private static final String SQL_TROVA_TUTTI =
            "SELECT login, password, nome, cognome, email, ruolo, matricola, anno_corso FROM utenti";

    private static final String SQL_TROVA_PER_LOGIN =
            SQL_TROVA_TUTTI + " WHERE login = ?";

    private static final String SQL_VERIFICA_CREDENZIALI =
            SQL_TROVA_TUTTI + " WHERE login = ? AND password = ?";

    private static final String SQL_INSERISCI =
            "INSERT INTO utenti (login, password, nome, cognome, email, ruolo, matricola, anno_corso) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_AGGIORNA =
            "UPDATE utenti SET password = ?, nome = ?, cognome = ?, email = ? WHERE login = ?";

    private static final String SQL_ELIMINA =
            "DELETE FROM utenti WHERE login = ?";

    private Utente mappaRiga(ResultSet rs) throws SQLException {
        String login    = rs.getString("login");
        String password = rs.getString("password");
        String nome     = rs.getString("nome");
        String cognome  = rs.getString("cognome");
        String email    = rs.getString("email");
        String ruolo    = rs.getString("ruolo");

        switch (ruolo.toUpperCase()) {
            case "STUDENTE": {
                String matricola  = rs.getString("matricola");
                int    annoCorso  = rs.getInt("anno_corso");
                return new Studente(nome, cognome, email, login, password, matricola, annoCorso);
            }
            case "DOCENTE":
                return new Docente(nome, cognome, email, login, password);

            case "RESPONSABILE":

                return new Responsabile(nome, cognome, email, login, password, null);

            default:
                System.err.println("UtentePostgresDAO: ruolo sconosciuto '" + ruolo + "' per login=" + login);
                return null;
        }
    }

    @Override
    public List<Utente> trovaTutti() {
        List<Utente> lista = new ArrayList<>();
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_TROVA_TUTTI);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Utente u = mappaRiga(rs);
                if (u != null) lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("UtentePostgresDAO.trovaTutti(): " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Optional<Utente> trovaPerLogin(String login) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_TROVA_PER_LOGIN)) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(mappaRiga(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("UtentePostgresDAO.trovaPerLogin(): " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Utente> verificaCredenziali(String login, String password) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_VERIFICA_CREDENZIALI)) {

            ps.setString(1, login);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.ofNullable(mappaRiga(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("UtentePostgresDAO.verificaCredenziali(): " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean inserisci(Utente utente) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERISCI)) {

            ps.setString(1, utente.getLogin());
            ps.setString(2, utente.getPassword() != null ? utente.getPassword() : "");
            ps.setString(3, utente.getNome());
            ps.setString(4, utente.getCognome());
            ps.setString(5, utente.getEmail() != null ? utente.getEmail() : "");

            String ruolo;
            String matricola = null;
            int    annoCorso = 0;

            if (utente instanceof Responsabile) {
                ruolo = "RESPONSABILE";
            } else if (utente instanceof Docente) {
                ruolo = "DOCENTE";
            } else if (utente instanceof Studente) {
                ruolo     = "STUDENTE";
                matricola = ((Studente) utente).getMatricola();
                annoCorso = ((Studente) utente).getAnnoCorso();
            } else {
                ruolo = "UTENTE";
            }

            ps.setString(6, ruolo);
            ps.setString(7, matricola);
            if (annoCorso > 0) ps.setInt(8, annoCorso); else ps.setNull(8, Types.INTEGER);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("UtentePostgresDAO.inserisci(): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean aggiorna(Utente utente) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_AGGIORNA)) {

            ps.setString(1, utente.getPassword() != null ? utente.getPassword() : "");
            ps.setString(2, utente.getNome());
            ps.setString(3, utente.getCognome());
            ps.setString(4, utente.getEmail() != null ? utente.getEmail() : "");
            ps.setString(5, utente.getLogin());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("UtentePostgresDAO.aggiorna(): " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean elimina(String login) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_ELIMINA)) {

            ps.setString(1, login);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("UtentePostgresDAO.elimina(): " + e.getMessage());
            return false;
        }
    }
}
