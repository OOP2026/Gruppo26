package dao;

import model.RichiestaSpostamento;
import model.RichiestaSpostamento.StatoRichiesta;
import java.util.List;
import java.util.Optional;

/**
 * Definisce le operazioni di accesso ai dati (CRUD) per l'entità
 * {@link RichiestaSpostamento}.
 *
 * @author Gruppo26
 */
public interface RichiestaSpostamentoDAO {

    /**
     * Restituisce tutte le richieste di spostamento presenti nella sorgente dati.
     *
     * @return la lista di tutte le richieste
     */
    List<RichiestaSpostamento> trovaTutte();

    /**
     * Restituisce tutte le richieste di spostamento relative a lezioni del
     * docente identificato dal login indicato.
     *
     * @param loginDocente il login del docente
     * @return la lista delle richieste del docente
     */
    List<RichiestaSpostamento> trovaPerDocente(String loginDocente);

    /**
     * Restituisce tutte le richieste di spostamento che si trovano nello
     * stato indicato.
     *
     * @param stato lo stato da cercare
     * @return la lista delle richieste in quello stato
     */
    List<RichiestaSpostamento> trovaPerStato(StatoRichiesta stato);

    /**
     * Cerca una richiesta di spostamento a partire dal suo identificativo.
     *
     * @param id l'identificativo della richiesta
     * @return un {@link Optional} contenente la richiesta trovata, oppure vuoto
     *         se nessuna richiesta corrisponde all'id indicato
     */
    Optional<RichiestaSpostamento> trovaPerID(int id);

    /**
     * Inserisce una nuova richiesta di spostamento nella sorgente dati.
     *
     * @param richiesta la richiesta da inserire
     * @return {@code true} se l'inserimento è andato a buon fine, {@code false} altrimenti
     */
    boolean inserisci(RichiestaSpostamento richiesta);

    /**
     * Aggiorna lo stato della richiesta identificata dall'id indicato.
     *
     * @param id    l'identificativo della richiesta
     * @param stato il nuovo stato
     * @return {@code true} se l'aggiornamento è andato a buon fine, {@code false} altrimenti
     */
    boolean aggiornaStato(int id, StatoRichiesta stato);

    /**
     * Elimina la richiesta identificata dall'id indicato.
     *
     * @param id l'identificativo della richiesta da eliminare
     * @return {@code true} se l'eliminazione è andata a buon fine, {@code false} altrimenti
     */
    boolean elimina(int id);
}