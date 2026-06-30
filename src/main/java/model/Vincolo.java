package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.text.Normalizer;

/**
 * Rappresenta un vincolo di indisponibilità oraria dichiarato da un {@link Docente},
 * relativo a un giorno della settimana e a una fascia oraria. Un vincolo deve
 * essere approvato dal {@link Responsabile} prima di essere considerato vincolante
 * nella verifica dei conflitti.
 *
 * @author Gruppo26
 */
public class Vincolo {

    /** Identificativo univoco del vincolo (corrisponde alla chiave primaria sul DB). */
    private int id = -1;

    /** Docente che ha dichiarato il vincolo. */
    private Docente docente;

    /**
     * Giorno della settimana a cui si riferisce il vincolo, espresso come
     * stringa in italiano (es. {@code "LUNEDI"}, {@code "Martedì"}...).
     * Il confronto con una data avviene in modo case-insensitive e senza
     * tenere conto degli accenti (si veda {@link #coincideConGiorno(LocalDate)}).
     */
    private String giorno;

    /** Ora di inizio della fascia di indisponibilità. */
    private LocalTime oraInizio;

    /** Ora di fine della fascia di indisponibilità. */
    private LocalTime oraFine;

    /** Indica se il vincolo è stato approvato dal responsabile. */
    private boolean approvato;

    /**
     * Costruisce un nuovo vincolo, inizialmente non approvato.
     *
     * @param docente   il docente che dichiara il vincolo
     * @param giorno    il giorno della settimana a cui si riferisce il vincolo
     * @param oraInizio l'ora di inizio della fascia di indisponibilità
     * @param oraFine   l'ora di fine della fascia di indisponibilità
     */
    public Vincolo(Docente docente, String giorno, LocalTime oraInizio, LocalTime oraFine) {
        this.docente = docente;
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.approvato = false;
    }

    /**
     * Restituisce l'identificativo del vincolo.
     *
     * @return l'id del vincolo
     */
    public int getId() { return id; }

    /**
     * Imposta l'identificativo del vincolo.
     *
     * @param id il nuovo identificativo
     */
    public void setId(int id) { this.id = id; }

    /**
     * Restituisce il docente che ha dichiarato il vincolo.
     *
     * @return il docente
     */
    public Docente getDocente() { return docente; }

    /**
     * Restituisce il giorno della settimana a cui si riferisce il vincolo.
     *
     * @return il giorno come stringa
     */
    public String getGiorno() { return giorno; }

    /**
     * Restituisce l'ora di inizio della fascia di indisponibilità.
     *
     * @return l'ora di inizio
     */
    public LocalTime getOraInizio() { return oraInizio; }

    /**
     * Restituisce l'ora di fine della fascia di indisponibilità.
     *
     * @return l'ora di fine
     */
    public LocalTime getOraFine() { return oraFine; }

    /**
     * Indica se il vincolo è stato approvato dal responsabile.
     *
     * @return {@code true} se approvato, {@code false} altrimenti
     */
    public boolean isApprovato() { return approvato; }

    /**
     * Imposta lo stato di approvazione del vincolo.
     *
     * @param approvato {@code true} per approvare il vincolo, {@code false} altrimenti
     */
    public void setApprovato(boolean approvato) { this.approvato = approvato; }


    /**
     * Verifica se il giorno della settimana del vincolo coincide con il
     * giorno della settimana della data fornita, ignorando maiuscole/minuscole
     * e accenti.
     *
     * @param data la data da confrontare
     * @return {@code true} se il giorno del vincolo coincide con il giorno
     *         della settimana di {@code data}, {@code false} se la data o
     *         il giorno del vincolo sono {@code null} oppure se non coincidono
     */
    public boolean coincideConGiorno(LocalDate data) {
        if (data == null || giorno == null) return false;
        return normalizza(giorno).equals(normalizza(nomeGiornoSettimana(data.getDayOfWeek())));
    }

    /**
     * Normalizza una stringa rimuovendo gli accenti e convertendola in
     * maiuscolo, per consentire confronti robusti tra nomi di giorni.
     *
     * @param s la stringa da normalizzare
     * @return la stringa normalizzata
     */
    private static String normalizza(String s) {
        String senzaAccenti = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return senzaAccenti.trim().toUpperCase();
    }

    /**
     * Converte un {@link DayOfWeek} nel corrispondente nome del giorno
     * della settimana in italiano, in maiuscolo e senza accenti
     * (es. {@code MONDAY} &rarr; {@code "LUNEDI"}).
     *
     * @param dow il giorno della settimana da convertire
     * @return il nome del giorno in italiano
     */
    private static String nomeGiornoSettimana(DayOfWeek dow) {
        switch (dow) {
            case MONDAY:    return "LUNEDI";
            case TUESDAY:   return "MARTEDI";
            case WEDNESDAY: return "MERCOLEDI";
            case THURSDAY:  return "GIOVEDI";
            case FRIDAY:    return "VENERDI";
            case SATURDAY:  return "SABATO";
            default:        return "DOMENICA";
        }
    }
}