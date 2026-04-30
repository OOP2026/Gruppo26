package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Lezione {
    private LocalDate giorno;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private Docente docente;
    private Insegnamento insegnamento;

    public Lezione(LocalDate giorno, LocalTime oraInizio,LocalTime oraFine,Insegnamento insegnamento,Docente docente) {
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.insegnamento=insegnamento;
        this.docente=docente;
    }
}
