package implementazioneDao;

import dao.RichiestaSpostamentoDAO;
import database_connection.ConnessioneDatabase;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementazione di {@link RichiestaSpostamentoDAO} basata su database PostgreSQL.
 * <p>
 * Ogni riga letta dalla tabella {@code richieste_spostamento} viene unita (JOIN)
 * con le tabelle {@code lezioni}, {@code insegnamenti} e {@code utenti} per
 * ricostruire interamente il grafo di oggetti {@link RichiestaSpostamento} &rarr;
 * {@link Lezione} &rarr; {@link Insegnamento} &rarr; {@link Docente}.
 * </p>
 *
 * @author Gruppo26
 */
public class RichiestaSpostamentoPostgresDAO implements RichiestaSpostamentoDAO {

    /** Query base con i JOIN necessari a ricostruire una {@link RichiestaSpostamento} completa. */
    private static final String SELECT_BASE =
            "SELECT rs.id_richiesta, rs.data_richiesta, rs.ora_inizio, rs.ora_fine, rs.stato, " +
                    "       l.id_lezione, l.giorno, l.ora_inizio AS lez_ora_inizio, l.ora_fine AS lez_ora_fine, " +
                    "       l.nome_aula, " +
                    "       i.nome_insegnamento, i.cfu, i.anno, " +
                    "       u.login, u.nome, u.cognome, u.email " +
                    "FROM richieste_spostamento rs " +
                    "JOIN lezioni      l ON rs.id_lezione    = l.id_lezione " +
                    "JOIN insegnamenti i ON l.id_insegnamento = i.id_insegnamento " +
                    "JOIN utenti       u ON i.login_docente   = u.login ";

    /** Query per il recupero di tutte le richieste di spostamento. */
    private static final String SQL_TROVA_TUTTE = SELECT_BASE;

    /** Query per il recupero delle richieste relative a un determinato docente. */
    private static final String SQL_PER_DOCENTE = SELECT_BASE + "WHERE u.login = ?";

    /** Query per il recupero delle richieste in un determinato stato. */
    private static final String SQL_PER_STATO   = SELECT_BASE + "WHERE rs.stato = ?";

    /** Query per il recupero di una richiesta a partire dal suo id. */
    private static final String SQL_PER_ID      = SELECT_BASE + "WHERE rs.id_richiesta = ?";

    /** Query per l'inserimento di una nuova richiesta, inizialmente nello stato {@code IN_ATTESA}. */
    private static final String SQL_INSERISCI =
            "INSERT INTO richieste_spostamento (id_lezione, data_richiesta, ora_inizio, ora_fine, stato) " +
                    "VALUES (?, ?, ?, ?, 'IN_ATTESA') RETURNING id_richiesta";

    /** Query per l'aggiornamento dello stato di una richiesta. */
    private static final String SQL_AGGIORNA_STATO =
            "UPDATE richieste_spostamento SET stato = ? WHERE id_richiesta = ?";

    /** Query per l'eliminazione di una richiesta. */
    private static final String SQL_ELIMINA =
            "DELETE FROM richieste_spostamento WHERE id_richiesta = ?";

    /**
     * Mappa la riga corrente del {@link ResultSet} in un oggetto
     * {@link RichiestaSpostamento} completo, ricostruendo anche la
     * {@link Lezione} originale (con {@link Aula}, {@link Insegnamento}
     * e {@link Docente} associati) e lo {@link RichiestaSpostamento.StatoRichiesta stato}.
     *
     * @param rs il result set posizionato sulla riga da mappare
     * @return la richiesta mappata, con id e stato già impostati
     * @throws SQLException se si verifica un errore nella lettura del result set
     */
    private RichiestaSpostamento mappaRiga(ResultSet rs) throws SQLException {
        LocalDate dataRichiesta = rs.getDate("data_richiesta").toLocalDate();
        LocalTime oraInizio     = rs.getTime("ora_inizio").toLocalTime();
        LocalTime oraFine       = rs.getTime("ora_fine").toLocalTime();
        String    statoStr      = rs.getString("stato");

        LocalDate giornoLezione = rs.getDate("giorno").toLocalDate();
        LocalTime lezOraInizio  = rs.getTime("lez_ora_inizio").toLocalTime();
        LocalTime lezOraFine    = rs.getTime("lez_ora_fine").toLocalTime();

        Aula aula = new Aula(rs.getString("nome_aula"));

        Docente docente = new Docente(
                rs.getString("nome"), rs.getString("cognome"),
                rs.getString("email"), rs.getString("login"), "");

        Insegnamento insegnamento = new Insegnamento(
                rs.getString("nome_insegnamento"), rs.getInt("cfu"),
                rs.getInt("anno"), docente);

        Lezione lezione = new Lezione(giornoLezione, lezOraInizio, lezOraFine, insegnamento, aula);
        lezione.setId(rs.getInt("id_lezione"));


        RichiestaSpostamento richiesta =
                new RichiestaSpostamento(oraInizio, oraFine, dataRichiesta, lezione);


        richiesta.setId(rs.getInt("id_richiesta"));

        try {
            richiesta.setStato(RichiestaSpostamento.StatoRichiesta.valueOf(statoStr));
        } catch (IllegalArgumentException e) {
            System.err.println("RichiestaSpostamentoPostgresDAO: stato sconosciuto '" + statoStr + "'");
        }

        return richiesta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RichiestaSpostamento> trovaTutte() {
        List<RichiestaSpostamento> lista = new ArrayList<>();
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_TROVA_TUTTE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mappaRiga(rs));
        } catch (SQLException e) {
            System.err.println("RichiestaSpostamentoPostgresDAO.trovaTutte(): " + e.getMessage());
        }
        return lista;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RichiestaSpostamento> trovaPerDocente(String loginDocente) {
        List<RichiestaSpostamento> lista = new ArrayList<>();
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_PER_DOCENTE)) {
            ps.setString(1, loginDocente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mappaRiga(rs));
            }
        } catch (SQLException e) {
            System.err.println("RichiestaSpostamentoPostgresDAO.trovaPerDocente(): " + e.getMessage());
        }
        return lista;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RichiestaSpostamento> trovaPerStato(RichiestaSpostamento.StatoRichiesta stato) {
        List<RichiestaSpostamento> lista = new ArrayList<>();
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_PER_STATO)) {
            ps.setString(1, stato.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mappaRiga(rs));
            }
        } catch (SQLException e) {
            System.err.println("RichiestaSpostamentoPostgresDAO.trovaPerStato(): " + e.getMessage());
        }
        return lista;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<RichiestaSpostamento> trovaPerID(int id) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_PER_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mappaRiga(rs));
            }
        } catch (SQLException e) {
            System.err.println("RichiestaSpostamentoPostgresDAO.trovaPerID(): " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Recupera l'identificativo della lezione associata a una
     * {@link RichiestaSpostamento}, cercandola per nome dell'insegnamento
     * quando l'oggetto {@link Lezione} non possiede già un id valido.
     *
     * @param conn    la connessione al database da utilizzare
     * @param lezione la lezione di cui recuperare l'id
     * @return l'identificativo della lezione trovata
     * @throws SQLException se la lezione non viene trovata o si verifica un errore SQL
     */
    private int getIdLezione(Connection conn, Lezione lezione) throws SQLException {
        String sql = "SELECT l.id_lezione FROM lezioni l " +
                "JOIN insegnamenti i ON l.id_insegnamento = i.id_insegnamento " +
                "WHERE i.nome_insegnamento = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lezione.getInsegnamento().getNomeInsegnamento());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id_lezione");
            }
        }
        throw new SQLException("Lezione non trovata nel DB per: "
                + lezione.getInsegnamento().getNomeInsegnamento());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Se la lezione associata alla richiesta non possiede già un id valido,
     * questo viene recuperato tramite {@link #getIdLezione(Connection, Lezione)}.
     * L'identificativo generato per la richiesta viene riportato sull'oggetto
     * {@code richiesta} tramite {@link RichiestaSpostamento#setId(int)}.
     * </p>
     */
    @Override
    public boolean inserisci(RichiestaSpostamento richiesta) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERISCI)) {

            int idLezione = richiesta.getLezioneRichiesta().getId();
            if (idLezione <= 0) {
                idLezione = getIdLezione(conn, richiesta.getLezioneRichiesta());
            }
            ps.setInt  (1, idLezione);
            ps.setDate (2, Date.valueOf(richiesta.getDataRichiesta()));
            ps.setTime (3, Time.valueOf(richiesta.getOrarioInizioRichiesta()));
            ps.setTime (4, Time.valueOf(richiesta.getOrarioFineRichiesta()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    richiesta.setId(rs.getInt(1));
                    System.out.println("RichiestaSpostamentoPostgresDAO: inserita con id=" + richiesta.getId());
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("RichiestaSpostamentoPostgresDAO.inserisci(): " + e.getMessage());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean aggiornaStato(int id, RichiestaSpostamento.StatoRichiesta stato) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_AGGIORNA_STATO)) {
            ps.setString(1, stato.name());
            ps.setInt   (2, id);
            int righe = ps.executeUpdate();
            System.out.println("RichiestaSpostamentoPostgresDAO: aggiornaStato id=" + id + " stato=" + stato + " righe=" + righe);
            return righe > 0;
        } catch (SQLException e) {
            System.err.println("RichiestaSpostamentoPostgresDAO.aggiornaStato(): " + e.getMessage());
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean elimina(int id) {
        try (Connection conn = ConnessioneDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_ELIMINA)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("RichiestaSpostamentoPostgresDAO.elimina(): " + e.getMessage());
            return false;
        }
    }
}