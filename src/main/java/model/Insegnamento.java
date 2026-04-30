package model;

import java.time.LocalDate;

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
}
