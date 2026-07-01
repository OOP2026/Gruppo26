package dao;

import model.Vincolo;
import java.util.List;
import java.util.Optional;

/**
 * Definisce le operazioni di accesso ai dati (CRUD) per l'entità {@link Vincolo}.
 *
 * @author Gruppo26
 */
public interface VincoloDAO {

    /**
     * Restituisce tutti i vincoli presenti nella sorgente dati.
     *
     * @return la lista di tutti i vincoli
     */
    List<Vincolo> trovaTutti();

    /**
     * Restituisce tutti i vincoli dichiarati dal docente identificato dal login indicato.
     *
     * @param loginDocente il login del docente
     * @return la lista dei vincoli del docente
     */
    List<Vincolo> trovaPerDocente(String loginDocente);

    /**
     * Restituisce tutti i vincoli già approvati dal responsabile.
     *
     * @return la lista dei vincoli approvati
     */
    List<Vincolo> trovaApprovati();

    /**
     * Cerca un vincolo a partire dal suo identificativo.
     *
     * @param id l'identificativo del vincolo
     * @return un {@link Optional} contenente il vincolo trovato, oppure vuoto
     *         se nessun vincolo corrisponde all'id indicato
     */
    Optional<Vincolo> trovaPerID(int id);

    /**
     * Conta il numero di vincoli dichiarati dal docente identificato dal login indicato.
     *
     * @param loginDocente il login del docente
     * @return il numero di vincoli dichiarati dal docente
     */
    int contaPerDocente(String loginDocente);

    /**
     * Inserisce un nuovo vincolo (non approvato) nella sorgente dati.
     *
     * @param vincolo il vincolo da inserire
     * @return {@code true} se l'inserimento è andato a buon fine, {@code false} altrimenti
     */
    boolean inserisci(Vincolo vincolo);

    /**
     * Aggiorna lo stato di approvazione del vincolo identificato dall'id indicato.
     *
     * @param id        l'identificativo del vincolo
     * @param approvato il nuovo stato di approvazione
     * @return {@code true} se l'aggiornamento è andato a buon fine, {@code false} altrimenti
     */
    boolean aggiornaApprovazione(int id, boolean approvato);

    /**
     * Elimina il vincolo identificato dall'id indicato.
     *
     * @param id l'identificativo del vincolo da eliminare
     * @return {@code true} se l'eliminazione è andata a buon fine, {@code false} altrimenti
     */
    boolean elimina(int id);
}
