package model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class RichiestaSpostamentoTest {

    private RichiestaSpostamento richiesta;
    private Lezione lezioneDaSpostare;

    @Before
    public void setUp() {
        Docente docente=new Docente("Luigi","Bianchi","lb@docenti.università.it","profbianchi","LuiBia");
        Insegnamento insegnamento=new Insegnamento("Analisi",9,3,docente);
        Aula aula=new Aula("Aula1");
        lezioneDaSpostare=new Lezione(LocalDate.of(2026,9,15), LocalTime.of(9, 0), LocalTime.of(11, 0), insegnamento, aula);

        LocalDate dataNuovaLezione = LocalDate.of(2026, 9, 16);
        richiesta=new RichiestaSpostamento(LocalTime.of(9,0),LocalTime.of(11,0),dataNuovaLezione,lezioneDaSpostare);

    }

    @Test
    public void testaStatoIniziale(){

    assertEquals("lo stato non coincide, dovrebbe essere in attesa",RichiestaSpostamento.StatoRichiesta.IN_ATTESA,richiesta.getStatoRichiesta());

    }

    @Test
    public void testaSpostamentoRichiesta(){
    richiesta.setStato(RichiestaSpostamento.StatoRichiesta.APPROVATA);
    assertEquals("la richiesta non coincide, dovrebbe essere approvata", RichiestaSpostamento.StatoRichiesta.APPROVATA, richiesta.getStatoRichiesta());

    }
}
