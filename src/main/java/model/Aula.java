package model;

/**
 * Rappresenta un'aula universitaria in cui può svolgersi una {@link Lezione}.
 * <p>
 * È un semplice value object identificato dal proprio nome.
 * </p>
 *
 * @author Gruppo26
 */
public class Aula {

    /** Nome identificativo dell'aula. */
    private String nomeAula;

    /**
     * Costruisce una nuova aula con il nome indicato.
     *
     * @param nomeAula il nome dell'aula
     */
    public Aula(String nomeAula) {
        this.nomeAula = nomeAula;
    }

    /**
     * Restituisce il nome dell'aula.
     *
     * @return il nome dell'aula
     */
    public String getNomeAula() {
        return nomeAula;
    }

}
