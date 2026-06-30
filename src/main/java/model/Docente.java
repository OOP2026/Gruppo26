package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Docente extends Utente {
    private List<Vincolo> vincoli = new ArrayList<>();

    public Docente(String nome, String cognome, String email, String login, String password) {
        super(nome, cognome, email, login, password);
    }

    public List<Vincolo> getVincoli() {
        return vincoli;
    }

    public void setVincoli(List<Vincolo> vincoli) {
        this.vincoli = (vincoli != null) ? vincoli : new ArrayList<>();
    }

    public boolean aggiungiVincolo(Vincolo vincolo) {
        if (this.vincoli.size() >= 3) {
            System.out.println("Impossibile aggiungere vincolo: limite massimo di 3 raggiunto per " + cognome);
            return false;
        }
        this.vincoli.add(vincolo);
        return true;
    }

    public void richiediSpostamento(Lezione lezione, LocalTime nuovaOraInizio, LocalTime nuovaOraFine, LocalDate nuovoGiorno, Responsabile responsabile) {
        RichiestaSpostamento nuovaRichiesta = new RichiestaSpostamento(nuovaOraInizio, nuovaOraFine, nuovoGiorno, lezione);
        responsabile.riceviRichiesta(nuovaRichiesta);
        System.out.println("Richiesta di spostamento inviata al responsabile per la lezione del " + nuovoGiorno);
    }
}
