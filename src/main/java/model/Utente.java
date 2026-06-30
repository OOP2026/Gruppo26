package model;

/**
 * Rappresenta un utente generico del sistema di gestione orari universitari.
 * <p>
 * È la classe base della gerarchia degli utenti: {@link Docente}, {@link Studente}
 * e {@link Responsabile} (che a sua volta estende {@code Docente}) ereditano tutti
 * da {@code Utente} i dati anagrafici e le credenziali di accesso.
 * </p>
 *
 * @author Gruppo26
 */
public class Utente {

    /** Nome proprio dell'utente. */
    protected String nome;

    /** Cognome dell'utente. */
    protected String cognome;

    /** Indirizzo email dell'utente. */
    protected String email;

    /** Login (username) utilizzato per l'autenticazione. */
    protected String login;

    /** Password utilizzata per l'autenticazione. */
    protected String password;

    /**
     * Ruolo dell'utente (es. {@code "DOCENTE"}, {@code "STUDENTE"}, {@code "RESPONSABILE"}).
     * Campo attualmente non valorizzato dal costruttore ma riservato per usi futuri
     * o per la mappatura diretta dal database.
     */
    protected String ruolo;

    /**
     * Costruisce un nuovo utente con i dati anagrafici e le credenziali indicate.
     *
     * @param nome     il nome dell'utente
     * @param cognome  il cognome dell'utente
     * @param email    l'indirizzo email dell'utente
     * @param login    il login utilizzato per l'autenticazione
     * @param password la password utilizzata per l'autenticazione
     */
    public Utente(String nome, String cognome, String email, String login, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    /**
     * Stampa su console una rappresentazione testuale dell'orario delle lezioni fornito.
     * <p>
     * Metodo di utilità minimale: per una visualizzazione tabellare completa
     * si veda {@link OrarioLezione#mostraOrario(OrarioLezione)}.
     * </p>
     *
     * @param orario l'orario delle lezioni da mostrare
     */
    public void mostraOrario(OrarioLezione orario) {
        System.out.println(orario);
    }

    /**
     * Verifica se le credenziali fornite corrispondono a quelle dell'utente.
     *
     * @param login    il login da verificare
     * @param password la password da verificare
     * @return {@code true} se login e password coincidono con quelli memorizzati,
     *         {@code false} altrimenti
     */
    public boolean eseguiLogin(String login, String password) {
        if (login.equals(this.login) && password.equals(this.password)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return il nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return il cognome
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Restituisce il login dell'utente.
     *
     * @return il login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Restituisce l'indirizzo email dell'utente.
     *
     * @return l'email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return la password
     */
    public String getPassword() {
        return password;
    }

}
