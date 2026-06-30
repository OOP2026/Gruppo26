package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class Responsabile extends Docente {
    private OrarioLezione orarioLezioni;
    private List<RichiestaSpostamento> richieste;
    private List<Aula> auleDisponibili;
    private List<Insegnamento> insegnamenti;

    public Responsabile(String nome, String cognome, String email, String login, String password, OrarioLezione orarioLezioni) {
        super(nome, cognome, email, login, password);
        this.orarioLezioni = orarioLezioni;
        this.richieste = new ArrayList<>();
        this.auleDisponibili = new ArrayList<>();
        this.insegnamenti = new ArrayList<>();
    }

    public void setOrario(OrarioLezione orario) {
        this.orarioLezioni = orario;
    }

    public void aggiungiAula(Aula aula) {
        this.auleDisponibili.add(aula);
        System.out.println("Aggiunta aula: " + aula.getNomeAula());
    }

    public void aggiungiInsegnamento(Insegnamento NuovoInsegnamento) {
        this.insegnamenti.add(NuovoInsegnamento);
        System.out.println("Aggiunto insegnamento: " + NuovoInsegnamento.getNomeInsegnamento());
    }

    public void riceviRichiesta(RichiestaSpostamento richiesta) {
        this.richieste.add(richiesta);
    }

    public boolean verificaConflitti(LocalDate giorno, LocalTime inizioLezione, LocalTime fineLezione, Aula aula, Docente docente) {
        for (Lezione l : orarioLezioni.getLezioni()) {
            boolean sovrapposizione = inizioLezione.isBefore(l.getOraFine()) && fineLezione.isAfter(l.getOraInizio());

            if (sovrapposizione) {
                if ((l.getAula().getNomeAula().equals(aula.getNomeAula())) && (l.getGiorno().equals(giorno))) {
                    System.out.println("Conflitto: è già presente una lezione in quell'aula a quell'ora");
                    return true;
                }
                if ((l.getInsegnamento().getDocente().equals(docente)) && (l.getGiorno().equals(giorno)) && (l.getOraInizio().equals(inizioLezione))) {
                    System.out.println("Conflitto: il docente ha già una lezione nell'orario selezionato");
                    return true;
                }
            }
        }

        for (Vincolo v : docente.getVincoli()) {
            if (v.isApprovato() && v.coincideConGiorno(giorno)) {
                boolean sovrapposizioneVincolo = inizioLezione.isBefore(v.getOraFine()) && fineLezione.isAfter(v.getOraInizio());
                if (sovrapposizioneVincolo) {
                    System.out.println("Conflitto: il docente non è disponibile in quella fascia oraria (vincolo approvato)");
                    return true;
                }
            }
        }

        System.out.println("Non ci sono conflitti");
        return false;
    }

    public boolean creaLezione(LocalDate giorno, LocalTime inizioLezione, LocalTime fineLezione, Aula aula, Insegnamento insegnamento) {
        boolean conflitti = verificaConflitti(giorno, inizioLezione, fineLezione, aula, insegnamento.getDocente());
        if (conflitti) {
            System.out.println("Non è possibile creare lezione");
            return false;
        } else {
            Lezione nuovaLezione = new Lezione(giorno, inizioLezione, fineLezione, insegnamento, aula);
            orarioLezioni.aggiungiLezione(nuovaLezione);
            return true;
        }
    }

    public boolean gestisciRichiesta(RichiestaSpostamento richiesta) {
        boolean conflitti = verificaConflitti(
                richiesta.getDataRichiesta(),
                richiesta.getOrarioInizioRichiesta(),
                richiesta.getOrarioFineRichiesta(),
                richiesta.getLezioneRichiesta().getAula(),
                richiesta.getLezioneRichiesta().getInsegnamento().getDocente());
        if (!conflitti) {
            richiesta.setStato(RichiestaSpostamento.StatoRichiesta.APPROVATA);
            System.out.println("Lezione spostata");
            Lezione lezioneDaModificare = richiesta.getLezioneRichiesta();
            lezioneDaModificare.setGiorno(richiesta.getDataRichiesta());
            lezioneDaModificare.setOraInizio(richiesta.getOrarioInizioRichiesta());
            lezioneDaModificare.setOraFine(richiesta.getOrarioFineRichiesta());
            return true;
        } else {
            richiesta.setStato(RichiestaSpostamento.StatoRichiesta.RIFIUTATA);
            System.out.println("Non è stato possibile spostare la lezione");
            return false;
        }
    }

    public List<RichiestaSpostamento> getRichieste() {
        return this.richieste;
    }
}