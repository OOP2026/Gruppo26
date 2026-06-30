package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Rappresenta una richiesta di spostamento di una {@link Lezione}, inoltrata
 * da un {@link Docente} a un {@link Responsabile} per ottenere un cambio di
 * giorno e/o di fascia oraria.
 *
 * @author Gruppo26
 */
public class RichiestaSpostamento {

    /**
     * Stato del ciclo di vita di una richiesta di spostamento.
     */
    public enum StatoRichiesta {
        /** La richiesta è stata inoltrata ed è in attesa di essere gestita. */
        IN_ATTESA,
        /** La richiesta è stata approvata e la lezione è stata spostata. */
        APPROVATA,
        /** La richiesta è stata rifiutata (es. per conflitto). */
        RIFIUTATA
    }

    /** Identificativo univoco della richiesta (corrisponde alla chiave primaria sul DB). */
    private int id;

    /** Nuova ora di inizio proposta per la lezione. */
    private LocalTime orarioInizioRichiesta;

    /** Nuova ora di fine proposta per la lezione. */
    private LocalTime orarioFineRichiesta;

    /** Nuovo giorno proposto per la lezione. */
    private LocalDate dataRichiesta;

    /** Stato corrente della richiesta. */
    private StatoRichiesta statoRichiesta;

    /** Lezione originale di cui si richiede lo spostamento. */
    private Lezione lezione;

    /**
     * Costruisce una nuova richiesta di spostamento, inizialmente nello stato
     * {@link StatoRichiesta#IN_ATTESA}.
     *
     * @param orarioInizioRichiesta la nuova ora di inizio proposta
     * @param orarioFineRichiesta   la nuova ora di fine proposta
     * @param dataRichiesta         il nuovo giorno proposto
     * @param lezione               la lezione originale da spostare
     */
    public RichiestaSpostamento(LocalTime orarioInizioRichiesta, LocalTime orarioFineRichiesta, LocalDate dataRichiesta, Lezione lezione) {
        this.orarioInizioRichiesta = orarioInizioRichiesta;
        this.orarioFineRichiesta = orarioFineRichiesta;
        this.dataRichiesta = dataRichiesta;
        this.lezione = lezione;
        this.statoRichiesta = StatoRichiesta.IN_ATTESA;
        this.id = -1;
    }

    /**
     * Restituisce l'identificativo della richiesta.
     *
     * @return l'id della richiesta
     */
    public int getId() { return id; }

    /**
     * Imposta l'identificativo della richiesta (tipicamente assegnato dal
     * livello di persistenza dopo l'inserimento).
     *
     * @param id il nuovo identificativo
     */
    public void setId(int id) { this.id = id; }

    /**
     * Restituisce la lezione originale di cui si richiede lo spostamento.
     *
     * @return la lezione originale
     */
    public Lezione getLezioneRichiesta() { return lezione; }

    /**
     * Restituisce la nuova ora di inizio proposta.
     *
     * @return l'ora di inizio richiesta
     */
    public LocalTime getOrarioInizioRichiesta() { return orarioInizioRichiesta; }

    /**
     * Restituisce la nuova ora di fine proposta.
     *
     * @return l'ora di fine richiesta
     */
    public LocalTime getOrarioFineRichiesta() { return orarioFineRichiesta; }

    /**
     * Restituisce il nuovo giorno proposto.
     *
     * @return la data richiesta
     */
    public LocalDate getDataRichiesta() { return dataRichiesta; }

    /**
     * Restituisce lo stato corrente della richiesta.
     *
     * @return lo stato della richiesta
     */
    public StatoRichiesta getStatoRichiesta() { return statoRichiesta; }

    /**
     * Imposta lo stato della richiesta.
     *
     * @param statoRichiesta il nuovo stato
     */
    public void setStatoRichiesta(StatoRichiesta statoRichiesta) {
        this.statoRichiesta = statoRichiesta;
    }

    /**
     * Imposta lo stato della richiesta (alias semanticamente equivalente a
     * {@link #setStatoRichiesta(StatoRichiesta)}, utilizzato dal livello
     * di business logic).
     *
     * @param stato il nuovo stato
     */
    public void setStato(StatoRichiesta stato) {
        this.statoRichiesta = stato;
    }
}

