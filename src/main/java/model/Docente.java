package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Docente extends Utente {
    private List<Insegnamento> insegnamenti =new ArrayList<Insegnamento>();
    private List<Lezione> lezioni= new ArrayList<>();
    public Docente(String nome, String cognome, String email, String login, String password) {
        super(nome,cognome,email,login,password);
    }
    public void richiedispostamento(Lezione lezione, LocalTime nuovaOraInizio, LocalTime nuovaOraFine, LocalDate nuovoGiorno, Responsabile responsabile) {
        RichiestaSpostamento nuovaRichiesta = new RichiestaSpostamento(nuovaOraInizio, nuovaOraFine, nuovoGiorno, lezione);
        responsabile.riceviRichiesta(nuovaRichiesta);
        System.out.println("Richiesta di spostamento inviata al responsabile per la lezione del " + nuovoGiorno);
    }
}
