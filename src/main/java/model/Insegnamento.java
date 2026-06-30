package model;

/**
 * Rappresenta un insegnamento (corso) erogato da un {@link Docente}, con i
 * relativi crediti formativi (CFU) e l'anno di corso a cui appartiene.
 *
 * @author Gruppo26
 */
public class Insegnamento {

    /** Nome dell'insegnamento. */
    private String nome;

    /** Numero di crediti formativi universitari (CFU) associati. */
    private int cfu;

    /** Anno di corso a cui appartiene l'insegnamento. */
    private int anno;

    /** Docente titolare dell'insegnamento. */
    private Docente docente;

    /**
     * Costruisce un nuovo insegnamento con i dati indicati.
     *
     * @param nome    il nome dell'insegnamento
     * @param cfu     il numero di crediti formativi
     * @param anno    l'anno di corso
     * @param docente il docente titolare
     */
    public Insegnamento(String nome, int cfu, int anno, Docente docente) {
        this.nome = nome;
        this.cfu = cfu;
        this.anno = anno;
        this.docente = docente;
    }

    /**
     * Restituisce il nome dell'insegnamento.
     *
     * @return il nome dell'insegnamento
     */
    public String getNomeInsegnamento() {
        return nome;
    }

    /**
     * Restituisce il docente titolare dell'insegnamento.
     *
     * @return il docente titolare
     */
    public Docente getDocente() {
        return docente;
    }

    /**
     * Restituisce il numero di crediti formativi (CFU) dell'insegnamento.
     *
     * @return i CFU
     */
    public int getCfu() {
        return cfu;
    }

    /**
     * Restituisce l'anno di corso a cui appartiene l'insegnamento.
     *
     * @return l'anno di corso
     */
    public int getAnno() {
        return anno;
    }
}
