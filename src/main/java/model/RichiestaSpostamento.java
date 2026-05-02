package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class RichiestaSpostamento {

    public enum StatoRichiesta {
        IN_ATTESA, APPROVATA, RIFIUTATA
    }

    private LocalTime orarioInizioRichiesta;
    private LocalTime orarioFineRichiesta;
    private LocalDate dataRichiesta;
    private StatoRichiesta statoRichiesta;
    private Lezione lezione;

    public RichiestaSpostamento(LocalTime orarioInizioRichiesta, LocalTime orarioFineRichiesta, LocalDate dataRichiesta, Lezione lezione) {
        this.orarioInizioRichiesta = orarioInizioRichiesta;
        this.orarioFineRichiesta = orarioFineRichiesta;
        this.dataRichiesta = dataRichiesta;
        this.lezione = lezione;
        this.statoRichiesta = StatoRichiesta.IN_ATTESA;
    }
    public Lezione getLezioneRichiesta(Lezione lezione){ return lezione; }
    public LocalTime getOrarioInizioRichiesta(LocalTime orarioInizioRichiesta){ return orarioInizioRichiesta;}
    public LocalTime getOrarioFineRichiesta(LocalTime orarioFineRichiesta){ return this.orarioFineRichiesta;}
    public LocalDate getDataRichiesta(){ return dataRichiesta;}

    public StatoRichiesta getStatoRichiesta(){ return statoRichiesta; }
    public void setStatoRichiesta(StatoRichiesta statoRichiesta) {
        this.statoRichiesta = statoRichiesta;
    }

}

