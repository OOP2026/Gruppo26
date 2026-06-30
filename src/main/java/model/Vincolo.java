package model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.text.Normalizer;

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


    public boolean coincideConGiorno(LocalDate data) {
        if (data == null || giorno == null) return false;
        return normalizza(giorno).equals(normalizza(nomeGiornoSettimana(data.getDayOfWeek())));
    }

    private static String normalizza(String s) {
        String senzaAccenti = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return senzaAccenti.trim().toUpperCase();
    }

    private static String nomeGiornoSettimana(DayOfWeek dow) {
        switch (dow) {
            case MONDAY:    return "LUNEDI";
            case TUESDAY:   return "MARTEDI";
            case WEDNESDAY: return "MERCOLEDI";
            case THURSDAY:  return "GIOVEDI";
            case FRIDAY:    return "VENERDI";
            case SATURDAY:  return "SABATO";
            default:        return "DOMENICA";
        }
    }
}