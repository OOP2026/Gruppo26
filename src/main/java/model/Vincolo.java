package model;

public class Vincolo {
    protected String giorno;
    protected String oraInizio;
    protected String oraFine;


    public Vincolo(String giorno, String oraInizio, String oraFine) {
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
    }

    // Metodo per verificare se un determinato orario si sovrappone a questo vincolo
    public boolean controllaSovrapposizione(String giornoLezione, String inizioLezione, String fineLezione) {
        if (this.giorno.equalsIgnoreCase(giornoLezione)) {
            // Un controllo sulle stringhe orarie (es. "09:00", "11:00")
            // Ritorna true se la lezione si sovrappone alla fascia protetta dal vincolo
            return inizioLezione.compareTo(this.oraFine) < 0 && fineLezione.compareTo(this.oraInizio) > 0;
        }
        return false;
    }


    public String getGiorno() {
        return giorno;
    }

    public String getOraInizio() {
        return oraInizio;
    }

    public String getOraFine() {
        return oraFine;
    }


    @Override
    public String toString() {
        return "Vincolo di indisponibilità: " + giorno + " dalle " + oraInizio + " alle " + oraFine;
    }
}