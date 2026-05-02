package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Lezione {
    private LocalDate giorno;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private Insegnamento insegnamento;
    private Aula aula;

    public Lezione(LocalDate giorno, LocalTime oraInizio,LocalTime oraFine,Insegnamento insegnamento,Aula aula) {
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.insegnamento=insegnamento;
        this.aula=aula
    }

    public LocalDate getGiorno() {return giorno;}
    public LocalTime getOraInizio() {return oraInizio;}
    public LocalTime getOraFine() {return oraFine;}
    public Insegnamento getInsegnamento() {return insegnamento; }
    public Aula getAula() { return aula; }

    public void setGiorno(LocalDate giorno) {this.giorno = giorno;}
    public void setOraFine(LocalTime oraFine) {this.oraFine = oraFine;}
    public void setOraInizio(LocalTime oraInizio) {this.oraInizio = oraInizio;}
}
