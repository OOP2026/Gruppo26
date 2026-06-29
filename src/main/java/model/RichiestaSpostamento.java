package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class RichiestaSpostamento {

    public enum StatoRichiesta {
        IN_ATTESA, APPROVATA, RIFIUTATA
    }

    private int id;
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
        this.id = -1;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Lezione getLezioneRichiesta() { return lezione; }
    public LocalTime getOrarioInizioRichiesta() { return orarioInizioRichiesta; }
    public LocalTime getOrarioFineRichiesta() { return orarioFineRichiesta; }
    public LocalDate getDataRichiesta() { return dataRichiesta; }
    public StatoRichiesta getStatoRichiesta() { return statoRichiesta; }

    public void setStatoRichiesta(StatoRichiesta statoRichiesta) {
        this.statoRichiesta = statoRichiesta;
    }

    public void setStato(StatoRichiesta stato) {
        this.statoRichiesta = stato;
    }
}

