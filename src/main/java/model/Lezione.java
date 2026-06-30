package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Rappresenta una singola lezione, collocata in un giorno e in una fascia
 * oraria precisi, relativa a un {@link Insegnamento} e svolta in un'{@link Aula}.
 *
 * @author Gruppo26
 */
public class Lezione {

    /** Identificativo univoco della lezione (corrisponde alla chiave primaria sul DB). */
    private int Id;

    /** Giorno in cui si svolge la lezione. */
    private LocalDate giorno;

    /** Ora di inizio della lezione. */
    private LocalTime oraInizio;

    /** Ora di fine della lezione. */
    private LocalTime oraFine;

    /** Insegnamento a cui appartiene la lezione. */
    private Insegnamento insegnamento;

    /** Aula in cui si svolge la lezione. */
    private Aula aula;

    /**
     * Costruisce una nuova lezione con i dati indicati. L'identificativo
     * viene assegnato successivamente (tipicamente dal livello di persistenza)
     * tramite {@link #setId(int)}.
     *
     * @param giorno       il giorno della lezione
     * @param oraInizio    l'ora di inizio
     * @param oraFine      l'ora di fine
     * @param insegnamento l'insegnamento a cui appartiene la lezione
     * @param aula         l'aula in cui si svolge la lezione
     */
    public Lezione(LocalDate giorno, LocalTime oraInizio, LocalTime oraFine, Insegnamento insegnamento, Aula aula) {
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.insegnamento = insegnamento;
        this.aula = aula;
    }


    /**
     * Restituisce l'identificativo della lezione.
     *
     * @return l'id della lezione
     */
    public int getId() { return Id; }

    /**
     * Imposta l'identificativo della lezione.
     *
     * @param Id il nuovo identificativo
     */
    public void setId(int Id){ this.Id = Id; } // FIX: prima veniva sempre forzato a -1, ignorando il parametro

    /**
     * Restituisce il giorno in cui si svolge la lezione.
     *
     * @return il giorno della lezione
     */
    public LocalDate getGiorno() {return giorno;}

    /**
     * Restituisce l'ora di inizio della lezione.
     *
     * @return l'ora di inizio
     */
    public LocalTime getOraInizio() {return oraInizio;}

    /**
     * Restituisce l'ora di fine della lezione.
     *
     * @return l'ora di fine
     */
    public LocalTime getOraFine() {return oraFine;}

    /**
     * Restituisce l'insegnamento a cui appartiene la lezione.
     *
     * @return l'insegnamento
     */
    public Insegnamento getInsegnamento() {return insegnamento; }

    /**
     * Restituisce l'aula in cui si svolge la lezione.
     *
     * @return l'aula
     */
    public Aula getAula() { return aula; }

    /**
     * Imposta il giorno in cui si svolge la lezione (usato in fase di spostamento).
     *
     * @param giorno il nuovo giorno
     */
    public void setGiorno(LocalDate giorno) {this.giorno = giorno;}

    /**
     * Imposta l'ora di fine della lezione (usato in fase di spostamento).
     *
     * @param oraFine la nuova ora di fine
     */
    public void setOraFine(LocalTime oraFine) {this.oraFine = oraFine;}

    /**
     * Imposta l'ora di inizio della lezione (usato in fase di spostamento).
     *
     * @param oraInizio la nuova ora di inizio
     */
    public void setOraInizio(LocalTime oraInizio) {this.oraInizio = oraInizio;}

    /**
     * Restituisce una rappresentazione testuale leggibile della lezione,
     * comprensiva di data, orario, materia, aula e cognome del docente.
     *
     * @return la rappresentazione testuale della lezione
     */
    @Override
    public String toString() {
        return "Data: " + giorno +
                " | Ore: " + oraInizio + " - " + oraFine +
                " | Materia: " + insegnamento.getNomeInsegnamento() +
                " | Aula: " + aula.getNomeAula() +
                " | Prof: " + insegnamento.getDocente().getCognome();
    }
}