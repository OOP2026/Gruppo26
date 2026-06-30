package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un docente, ovvero un {@link Utente} abilitato a tenere insegnamenti,
 * a dichiarare vincoli di indisponibilità oraria e a richiedere lo spostamento
 * delle proprie lezioni.
 * <p>
 * Un docente può avere al massimo 3 {@link Vincolo} attivi contemporaneamente.
 * </p>
 *
 * @author Gruppo26
 * @see Responsabile
 * @see Vincolo
 */
public class Docente extends Utente {

    /** Elenco dei vincoli di indisponibilità dichiarati dal docente (massimo 3). */
    private List<Vincolo> vincoli = new ArrayList<>();

    /**
     * Costruisce un nuovo docente con i dati anagrafici e le credenziali indicate.
     *
     * @param nome     il nome del docente
     * @param cognome  il cognome del docente
     * @param email    l'indirizzo email del docente
     * @param login    il login utilizzato per l'autenticazione
     * @param password la password utilizzata per l'autenticazione
     */
    public Docente(String nome, String cognome, String email, String login, String password) {
        super(nome, cognome, email, login, password);
    }

    /**
     * Restituisce la lista dei vincoli di indisponibilità del docente.
     *
     * @return la lista dei vincoli (mai {@code null})
     */
    public List<Vincolo> getVincoli() {
        return vincoli;
    }

    /**
     * Sostituisce la lista dei vincoli del docente.
     *
     * @param vincoli la nuova lista di vincoli; se {@code null} viene impostata
     *                una lista vuota
     */
    public void setVincoli(List<Vincolo> vincoli) {
        this.vincoli = (vincoli != null) ? vincoli : new ArrayList<>();
    }

    /**
     * Aggiunge un nuovo vincolo di indisponibilità al docente, rispettando
     * il limite massimo di 3 vincoli contemporanei.
     *
     * @param vincolo il vincolo da aggiungere
     * @return {@code true} se il vincolo è stato aggiunto con successo,
     *         {@code false} se il limite di 3 vincoli è già stato raggiunto
     */
    public boolean aggiungiVincolo(Vincolo vincolo) {
        if (this.vincoli.size() >= 3) {
            System.out.println("Impossibile aggiungere vincolo: limite massimo di 3 raggiunto per " + cognome);
            return false;
        }
        this.vincoli.add(vincolo);
        return true;
    }

    /**
     * Inoltra al {@link Responsabile} indicato una richiesta di spostamento
     * per la lezione specificata, creando un nuovo oggetto {@link RichiestaSpostamento}
     * con il nuovo giorno e la nuova fascia oraria proposti.
     *
     * @param lezione         la lezione di cui si richiede lo spostamento
     * @param nuovaOraInizio  la nuova ora di inizio proposta
     * @param nuovaOraFine    la nuova ora di fine proposta
     * @param nuovoGiorno     il nuovo giorno proposto
     * @param responsabile    il responsabile a cui inoltrare la richiesta
     */
    public void richiediSpostamento(Lezione lezione, LocalTime nuovaOraInizio, LocalTime nuovaOraFine, LocalDate nuovoGiorno, Responsabile responsabile) {
        RichiestaSpostamento nuovaRichiesta = new RichiestaSpostamento(nuovaOraInizio, nuovaOraFine, nuovoGiorno, lezione);
        responsabile.riceviRichiesta(nuovaRichiesta);
        System.out.println("Richiesta di spostamento inviata al responsabile per la lezione del " + nuovoGiorno);
    }
}
