package model;


public class Insegnamento {
    private String nome;
    private int cfu;
    private int anno;
    private Docente docente;

    public Insegnamento(String nome, int cfu, int anno, Docente docente) {
        this.nome = nome;
        this.cfu = cfu;
        this.anno = anno;
        this.docente = docente;
    }
    public String getNomeInsegnamento() { return nome; }
    public Docente getDocente() { return docente;}
    public int getCfu() { return cfu; }
    public int getAnno() { return anno; }
}
