package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Rappresenta il responsabile della pianificazione didattica, ovvero un {@link Docente}
 * con privilegi amministrativi: gestisce l'orario delle lezioni, le aule disponibili,
 * gli insegnamenti, le richieste di spostamento inoltrate dai docenti e verifica
 * eventuali conflitti prima di creare o modificare una lezione.
 * <p>
 * Essendo una sottoclasse di {@code Docente}, un responsabile eredita anche la
 * possibilità di dichiarare propri vincoli di indisponibilità e di richiedere
 * spostamenti delle proprie lezioni.
 * </p>
 *
 * @author Gruppo26
 */
public class Responsabile extends Docente {

    /** Orario complessivo delle lezioni gestito dal responsabile. */
    private OrarioLezione orarioLezioni;

    /** Richieste di spostamento ricevute dai docenti, in attesa o già gestite. */
    private List<RichiestaSpostamento> richieste;

    /** Elenco delle aule disponibili per la pianificazione delle lezioni. */
    private List<Aula> auleDisponibili;

    /** Elenco degli insegnamenti gestiti dal responsabile. */
    private List<Insegnamento> insegnamenti;

    /**
     * Costruisce un nuovo responsabile con i dati anagrafici, le credenziali
     * e l'orario delle lezioni indicati.
     *
     * @param nome          il nome del responsabile
     * @param cognome       il cognome del responsabile
     * @param email         l'indirizzo email del responsabile
     * @param login         il login utilizzato per l'autenticazione
     * @param password      la password utilizzata per l'autenticazione
     * @param orarioLezioni l'orario delle lezioni da gestire (può essere {@code null}
     *                      e impostato successivamente tramite {@link #setOrario(OrarioLezione)})
     */
    public Responsabile(String nome, String cognome, String email, String login, String password, OrarioLezione orarioLezioni) {
        super(nome, cognome, email, login, password);
        this.orarioLezioni = orarioLezioni;
        this.richieste = new ArrayList<>();
        this.auleDisponibili = new ArrayList<>();
        this.insegnamenti = new ArrayList<>();
    }

    /**
     * Imposta (o sostituisce) l'orario delle lezioni gestito dal responsabile.
     *
     * @param orario il nuovo orario delle lezioni
     */
    public void setOrario(OrarioLezione orario) {
        this.orarioLezioni = orario;
    }

    /**
     * Aggiunge un'aula all'elenco delle aule disponibili.
     *
     * @param aula l'aula da aggiungere
     */
    public void aggiungiAula(Aula aula) {
        this.auleDisponibili.add(aula);
        System.out.println("Aggiunta aula: " + aula.getNomeAula());
    }

    /**
     * Aggiunge un insegnamento all'elenco degli insegnamenti gestiti.
     *
     * @param NuovoInsegnamento l'insegnamento da aggiungere
     */
    public void aggiungiInsegnamento(Insegnamento NuovoInsegnamento) {
        this.insegnamenti.add(NuovoInsegnamento);
        System.out.println("Aggiunto insegnamento: " + NuovoInsegnamento.getNomeInsegnamento());
    }

    /**
     * Riceve e accoda una richiesta di spostamento inoltrata da un docente.
     *
     * @param richiesta la richiesta di spostamento ricevuta
     */
    public void riceviRichiesta(RichiestaSpostamento richiesta) {
        this.richieste.add(richiesta);
    }

    /**
     * Verifica se l'inserimento (o lo spostamento) di una lezione nei parametri
     * indicati genera conflitti con l'orario esistente o con i vincoli approvati
     * del docente.
     * <p>
     * Vengono controllati, in ordine:
     * </p>
     * <ol>
     *     <li>sovrapposizione con un'altra lezione nella stessa aula e nello stesso giorno;</li>
     *     <li>sovrapposizione con un'altra lezione dello stesso docente, nello stesso
     *     giorno e con la stessa ora di inizio;</li>
     *     <li>sovrapposizione con un vincolo di indisponibilità del docente, già approvato,
     *     che coincide con il giorno indicato.</li>
     * </ol>
     *
     * @param giorno         il giorno in cui si vuole collocare la lezione
     * @param inizioLezione  l'ora di inizio della lezione
     * @param fineLezione    l'ora di fine della lezione
     * @param aula           l'aula in cui si vuole tenere la lezione
     * @param docente        il docente che terrà la lezione
     * @return {@code true} se è stato rilevato un conflitto, {@code false} se
     *         lo slot richiesto è libero
     */
    public boolean verificaConflitti(LocalDate giorno, LocalTime inizioLezione, LocalTime fineLezione, Aula aula, Docente docente) {
        for (Lezione l : orarioLezioni.getLezioni()) {
            boolean sovrapposizione = inizioLezione.isBefore(l.getOraFine()) && fineLezione.isAfter(l.getOraInizio());

            if (sovrapposizione) {
                if ((l.getAula().getNomeAula().equals(aula.getNomeAula())) && (l.getGiorno().equals(giorno))) {
                    System.out.println("Conflitto: è già presente una lezione in quell'aula a quell'ora");
                    return true;
                }
                if ((l.getInsegnamento().getDocente().equals(docente)) && (l.getGiorno().equals(giorno)) && (l.getOraInizio().equals(inizioLezione))) {
                    System.out.println("Conflitto: il docente ha già una lezione nell'orario selezionato");
                    return true;
                }
            }
        }

        for (Vincolo v : docente.getVincoli()) {
            if (v.isApprovato() && v.coincideConGiorno(giorno)) {
                boolean sovrapposizioneVincolo = inizioLezione.isBefore(v.getOraFine()) && fineLezione.isAfter(v.getOraInizio());
                if (sovrapposizioneVincolo) {
                    System.out.println("Conflitto: il docente non è disponibile in quella fascia oraria (vincolo approvato)");
                    return true;
                }
            }
        }

        System.out.println("Non ci sono conflitti");
        return false;
    }

    /**
     * Crea una nuova lezione e la aggiunge all'orario, a condizione che non
     * generi conflitti (si veda {@link #verificaConflitti}).
     *
     * @param giorno         il giorno della nuova lezione
     * @param inizioLezione  l'ora di inizio della nuova lezione
     * @param fineLezione    l'ora di fine della nuova lezione
     * @param aula           l'aula in cui si terrà la lezione
     * @param insegnamento   l'insegnamento a cui appartiene la lezione
     * @return {@code true} se la lezione è stata creata con successo,
     *         {@code false} se è stato rilevato un conflitto
     */
    public boolean creaLezione(LocalDate giorno, LocalTime inizioLezione, LocalTime fineLezione, Aula aula, Insegnamento insegnamento) {
        boolean conflitti = verificaConflitti(giorno, inizioLezione, fineLezione, aula, insegnamento.getDocente());
        if (conflitti) {
            System.out.println("Non è possibile creare lezione");
            return false;
        } else {
            Lezione nuovaLezione = new Lezione(giorno, inizioLezione, fineLezione, insegnamento, aula);
            orarioLezioni.aggiungiLezione(nuovaLezione);
            return true;
        }
    }

    /**
     * Gestisce (approva o rifiuta) una richiesta di spostamento, verificando
     * preventivamente l'eventuale presenza di conflitti nella nuova collocazione
     * proposta. In caso di approvazione, la lezione originale viene aggiornata
     * con il nuovo giorno e la nuova fascia oraria.
     *
     * @param richiesta la richiesta di spostamento da gestire
     * @return {@code true} se la richiesta è stata approvata e la lezione spostata,
     *         {@code false} se è stata rifiutata a causa di un conflitto
     */
    public boolean gestisciRichiesta(RichiestaSpostamento richiesta) {
        boolean conflitti = verificaConflitti(
                richiesta.getDataRichiesta(),
                richiesta.getOrarioInizioRichiesta(),
                richiesta.getOrarioFineRichiesta(),
                richiesta.getLezioneRichiesta().getAula(),
                richiesta.getLezioneRichiesta().getInsegnamento().getDocente());
        if (!conflitti) {
            richiesta.setStato(RichiestaSpostamento.StatoRichiesta.APPROVATA);
            System.out.println("Lezione spostata");
            Lezione lezioneDaModificare = richiesta.getLezioneRichiesta();
            lezioneDaModificare.setGiorno(richiesta.getDataRichiesta());
            lezioneDaModificare.setOraInizio(richiesta.getOrarioInizioRichiesta());
            lezioneDaModificare.setOraFine(richiesta.getOrarioFineRichiesta());
            return true;
        } else {
            richiesta.setStato(RichiestaSpostamento.StatoRichiesta.RIFIUTATA);
            System.out.println("Non è stato possibile spostare la lezione");
            return false;
        }
    }

    /**
     * Restituisce l'elenco delle richieste di spostamento ricevute dal responsabile.
     *
     * @return la lista delle richieste
     */
    public List<RichiestaSpostamento> getRichieste() {
        return this.richieste;
    }
}