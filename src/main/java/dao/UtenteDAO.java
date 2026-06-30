package dao;

import model.Utente;
import java.util.List;
import java.util.Optional;

/**
 * Definisce le operazioni di accesso ai dati (CRUD) per l'entità {@link Utente}.
 * <p>
 * Le implementazioni concrete (es. {@code UtentePostgresDAO}) si occupano della
 * mappatura tra le righe della tabella {@code utenti} e le sottoclassi concrete
 * di {@code Utente} ({@code Studente}, {@code Docente}, {@code Responsabile}),
 * in base al campo {@code ruolo}.
 * </p>
 *
 * @author Gruppo26
 */
public interface UtenteDAO {

    /**
     * Restituisce tutti gli utenti presenti nella sorgente dati.
     *
     * @return la lista di tutti gli utenti
     */
    List<Utente> trovaTutti();

    /**
     * Cerca un utente a partire dal suo login.
     *
     * @param login il login da cercare
     * @return un {@link Optional} contenente l'utente trovato, oppure vuoto
     *         se nessun utente corrisponde al login indicato
     */
    Optional<Utente> trovaPerLogin(String login);

    /**
     * Verifica le credenziali fornite e restituisce l'utente corrispondente.
     *
     * @param login    il login da verificare
     * @param password la password da verificare
     * @return un {@link Optional} contenente l'utente se le credenziali sono
     *         valide, oppure vuoto in caso contrario
     */
    Optional<Utente> verificaCredenziali(String login, String password);

    /**
     * Inserisce un nuovo utente nella sorgente dati.
     *
     * @param utente l'utente da inserire
     * @return {@code true} se l'inserimento è andato a buon fine, {@code false} altrimenti
     */
    boolean inserisci(Utente utente);

    /**
     * Aggiorna i dati di un utente già esistente, identificato dal proprio login.
     *
     * @param utente l'utente con i dati aggiornati
     * @return {@code true} se l'aggiornamento è andato a buon fine, {@code false} altrimenti
     */
    boolean aggiorna(Utente utente);

    /**
     * Elimina l'utente identificato dal login indicato.
     *
     * @param login il login dell'utente da eliminare
     * @return {@code true} se l'eliminazione è andata a buon fine, {@code false} altrimenti
     */
    boolean elimina(String login);
}
