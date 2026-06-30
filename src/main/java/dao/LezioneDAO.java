package dao;

import model.Lezione;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Definisce le operazioni di accesso ai dati (CRUD) per l'entità {@link Lezione}.
 *
 * @author Gruppo26
 */
public interface LezioneDAO {

    /**
     * Restituisce tutte le lezioni presenti nella sorgente dati.
     *
     * @return la lista di tutte le lezioni
     */
    List<Lezione> trovaTutte();

    /**
     * Restituisce tutte le lezioni che si svolgono nel giorno indicato.
     *
     * @param giorno il giorno da cercare
     * @return la lista delle lezioni del giorno indicato
     */
    List<Lezione> trovaPerGiorno(LocalDate giorno);

    /**
     * Restituisce tutte le lezioni che si svolgono nell'aula indicata.
     *
     * @param nomeAula il nome dell'aula
     * @return la lista delle lezioni che si svolgono in quell'aula
     */
    List<Lezione> trovaPerAula(String nomeAula);

    /**
     * Restituisce tutte le lezioni tenute dal docente identificato dal login indicato.
     *
     * @param loginDocente il login del docente
     * @return la lista delle lezioni tenute dal docente
     */
    List<Lezione> trovaPerDocente(String loginDocente);

    /**
     * Cerca una lezione a partire dal suo identificativo.
     *
     * @param id l'identificativo della lezione
     * @return un {@link Optional} contenente la lezione trovata, oppure vuoto
     *         se nessuna lezione corrisponde all'id indicato
     */
    Optional<Lezione> trovaPerID(int id);

    /**
     * Inserisce una nuova lezione nella sorgente dati.
     *
     * @param lezione la lezione da inserire
     * @return {@code true} se l'inserimento è andato a buon fine, {@code false} altrimenti
     */
    boolean inserisci(Lezione lezione);

    /**
     * Aggiorna i dati della lezione identificata dall'id indicato.
     *
     * @param id      l'identificativo della lezione da aggiornare
     * @param lezione la lezione con i dati aggiornati
     * @return {@code true} se l'aggiornamento è andato a buon fine, {@code false} altrimenti
     */
    boolean aggiorna(int id, Lezione lezione);

    /**
     * Elimina la lezione identificata dall'id indicato.
     *
     * @param id l'identificativo della lezione da eliminare
     * @return {@code true} se l'eliminazione è andata a buon fine, {@code false} altrimenti
     */
    boolean elimina(int id);
}
