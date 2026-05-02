package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Lezione {
    private LocalDate giorno;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private Docente docente;
    private Insegnamento insegnamento;
    private Aula aula;

    public Lezione(LocalDate giorno, LocalTime oraInizio,LocalTime oraFine,Insegnamento insegnamento,Docente docente,Aula aula) {
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.insegnamento=insegnamento;
        this.docente=docente;
        this.aula=aula
    }

    public LocalDate getGiorno() {return giorno;}
    public LocalTime getOraInizio() {return oraInizio;}
    public LocalTime getOraFine() {return oraFine;}
    public Docente getDocente() {return docente;}
    public Insegnamento getInsegnamento() {return insegnamento; }
    public Aula getAula() { return aula; }
}
