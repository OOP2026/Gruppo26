package implementazioneDao;

import dao.LezioneDAO;
import database_connection.ConnessioneDatabase;
import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementazione di {@link LezioneDAO} basata su database PostgreSQL.
 * <p>
 * Ogni riga letta dalla tabella {@code lezioni} viene unita (JOIN) con le
 * tabelle {@code insegnamenti} e {@code utenti} per ricostruire interamente
 * il grafo di oggetti {@link Lezione} &rarr; {@link Insegnamento} &rarr; {@link Docente}.
 * </p>
 *
 * @author Gruppo26
 */
public class LezionePostgresDAO implements LezioneDAO {

    /** Query base con i JOIN necessari a ricostruire una {@link Lezione} completa. */
    private static final String SELECT_BASE =
            "SELECT l.id_lezione, l.giorno, l.ora_inizio, l.ora_fine, l.nome_aula, " +
                    "       i.id_insegnamento, i.nome_insegnamento, i.cfu, i.anno, " +
                    "       u.login, u.nome, u.cognome, u.email " +
                    "FROM lezioni l " +
                    "JOIN insegnamenti i ON l.id_insegnamento = i.id_insegnamento " +
                    "JOIN utenti u       ON i.login_docente   = u.login ";

    /** Query per il recupero di tutte le lezioni. */
    private static final String SQL_TROVA_TUTTE = SELECT_BASE;

    /** Query per il recupero delle lezioni di un determinato giorno. */
    private static final String SQL_PER_GIORNO  = SELECT_BASE + "WHERE l.giorno = ?";

    /** Query per il recupero delle lezioni di una determinata aula. */
    private static final String SQL_PER_AULA    = SELECT_BASE + "WHERE l.nome_aula = ?";

    /** Query per il recupero delle lezioni di un determinato docente. */
    private static final String SQL_PER_DOCENTE = SELECT_BASE + "WHERE u.login = ?";

    /** Query per il recupero di una lezione a partire dal suo id. */
    private static final String SQL_PER_ID      = SELECT_BASE + "WHERE l.id_lezione = ?";

    /** Query per l'inserimento di una nuova lezione. */
    private static final String SQL_INSERISCI =
            "INSERT INTO lezioni (giorno, ora_inizio, ora_fine, id_insegnamento, nome_aula) " +
                    "VALUES (?, ?, ?, ?, ?) RETURNING id_lezione";

    /** Query per l'aggiornamento di giorno, orario e aula di una lezione. */
    private static final String SQL_AGGIORNA =
            "UPDATE lezioni SET giorno = ?, ora_inizio = ?, ora_fine = ?, nome_aula = ? " +
                    "WHERE id_lezione = ?";

    /** Query per l'eliminazione di una lezione. */
    private static final String SQL_ELIMINA =
            "DELETE FROM lezioni WHERE id_lezione = ?";

    /**
     * Mappa la riga corrente del {@link ResultSet} in un oggetto {@link Lezione}
     * completo, ricostruendo anche {@link Aula}, {@link Docente} e {@link Insegnamento} associati.
     * <p>
     * Nota: il docente viene ricostruito con password vuota, poiché la query
     * non la recupera (non necessaria per la visualizzazione dell'orario).
     * </p>
     *
     * @param rs il result set posizionato sulla riga da mappare
     * @return la lezione mappata, con id già impostato
     * @throws SQLException se si verifica un errore nella lettura del result set
     */
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
        lezione.setId(rs.getInt("id_lezione"));
        return lezione;
    }

    /**
     * Esegue una query parametrizzata con un singolo parametro stringa e
     * restituisce la lista delle lezioni risultanti.
     *
     * @param sql       la query SQL da eseguire, con un singolo placeholder {@code ?}
     * @param parametro il valore da associare al placeholder
     * @return la lista delle lezioni trovate, vuota in caso di errore o nessun risultato
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lezione> trovaPerAula(String nomeAula) { return eseguiQuery(SQL_PER_AULA, nomeAula); }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lezione> trovaPerDocente(String loginDocente) { return eseguiQuery(SQL_PER_DOCENTE, loginDocente); }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Recupera l'identificativo numerico dell'insegnamento a partire dal
     * suo nome, necessario per popolare la foreign key {@code id_insegnamento}
     * in fase di inserimento di una lezione.
     *
     * @param conn              la connessione al database da utilizzare
     * @param nomeInsegnamento  il nome dell'insegnamento da cercare
     * @return l'identificativo dell'insegnamento
     * @throws SQLException se l'insegnamento non viene trovato o si verifica un errore SQL
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * L'identificativo generato dal database viene riportato sull'oggetto
     * {@code lezione} passato come parametro tramite {@link Lezione#setId(int)}.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Se l'oggetto {@code lezione} possiede già un id valido (maggiore di 0),
     * questo ha priorità sul parametro {@code id} per identificare la riga da aggiornare.
     * </p>
     */
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
            System.err.println("LezionePostgresDAO.elimina(): " + e.getMessage());
            return false;
        }
    }
}