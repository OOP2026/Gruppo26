package model;

import java.time.LocalTime;

public class Vincolo{
    private int id = -1;
    private Docente docente;
    private String giorno;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private boolean approvato;

    public Vincolo(Docente docente, String giorno, LocalTime oraInizio, LocalTime oraFine) {
        this.docente = docente;
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.approvato = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Docente getDocente() { return docente; }
    public String getGiorno() { return giorno; }
    public LocalTime getOraInizio() { return oraInizio; }
    public LocalTime getOraFine() { return oraFine; }
    public boolean isApprovato() { return approvato; }
    public void setApprovato(boolean approvato) { this.approvato = approvato; }
}