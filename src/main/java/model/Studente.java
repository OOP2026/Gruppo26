package model;

/**
 * Rappresenta uno studente, ovvero un {@link Utente} che può consultare l'orario
 * delle lezioni e gli avvisi pubblici relativi a spostamenti e vincoli dei docenti,
 * senza poter modificare alcun dato.
 *
 * @author Gruppo26
 */
public class Studente extends Utente {

    /** Numero di matricola dello studente. */
    private String matricola;

    /** Anno di corso a cui lo studente è iscritto. */
    private int annoCorso;

    /**
     * Costruisce un nuovo studente con i dati anagrafici, le credenziali e le
     * informazioni di carriera indicate.
     *
     * @param nome      il nome dello studente
     * @param cognome   il cognome dello studente
     * @param email     l'indirizzo email dello studente
     * @param login     il login utilizzato per l'autenticazione
     * @param password  la password utilizzata per l'autenticazione
     * @param matricola il numero di matricola
     * @param annoCorso l'anno di corso
     */
    public Studente(String nome, String cognome, String email, String login, String password, String matricola, int annoCorso) {
        super(nome, cognome, email, login, password);
        this.matricola = matricola;
        this.annoCorso = annoCorso;
    }

    /**
     * Restituisce il numero di matricola dello studente.
     *
     * @return la matricola
     */
    public String getMatricola() {
        return matricola;
    }

    /**
     * Restituisce l'anno di corso dello studente.
     *
     * @return l'anno di corso
     */
    public int getAnnoCorso() {
        return annoCorso;
    }
}
