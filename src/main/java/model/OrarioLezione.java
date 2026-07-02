package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta l'orario complessivo delle lezioni, inteso come collezione
 * ordinata di oggetti {@link Lezione}.
 *
 * @author Gruppo26
 */
public class OrarioLezione {

    /** Lista delle lezioni che compongono l'orario. */
    private List<Lezione> lezioni = new ArrayList<Lezione>();

    /**
     * Costruisce un nuovo orario a partire dalla lista di lezioni indicata.
     *
     * @param lezioni la lista iniziale di lezioni
     */
    public OrarioLezione(List<Lezione> lezioni) {
        this.lezioni = lezioni;
    }

    /**
     * Restituisce la lista delle lezioni che compongono l'orario.
     *
     * @return la lista delle lezioni
     */
    public List<Lezione> getLezioni() {return lezioni;}

    /**
     * Aggiunge una lezione all'orario.
     *
     * @param l la lezione da aggiungere
     */
    public void aggiungiLezione(Lezione l) {
        this.lezioni.add(l);
    }
}
