package model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ResponsabileTest {

    private Responsabile responsabile;
    private OrarioLezione orarioLezione;
    private Docente prof1;
    private Docente prof2;
    private Aula aula1;
    private Aula aula2;
    private Insegnamento analisi;
    private Insegnamento informatica;

    @Before
    public void setUp() {
        orarioLezione=new OrarioLezione(new ArrayList<>());
        responsabile=new Responsabile("Emanuele","Mauriello","EmaMau@docente.università.it","EMau","EmMau.",orarioLezione);

        prof1=new Docente("Luigi", "Bianchi", "lb@docenti.università.it", "profbianchi", "LuiBia");
        prof2=new Docente("Luca", "Verdi", "verdi@uni.it", "lverdi", "pass");

        aula1=new Aula("Aula1");
        aula2=new Aula("Aula2");

        analisi=new Insegnamento("Analisi", 9, 1, prof1);
        informatica=new Insegnamento("Informatica", 12, 1, prof2);

        LocalDate dataLezione=LocalDate.of(2026,9,20);
        responsabile.creaLezione(dataLezione, LocalTime.of(9,0),LocalTime.of(11,0),aula1,analisi);

    }


    @Test
    public void testalezione_conflittoAula(){
        LocalDate datauguale=LocalDate.of(2026,9,20);
        boolean risultato= responsabile.creaLezione(datauguale,LocalTime.of(10,0),LocalTime.of(12,0),aula1,informatica);
        assertFalse("le aule vanno in conflitto",risultato);
        assertEquals("l'orario non coincide, non dovrebbe aver aggiunto un'altra lezione",1,orarioLezione.getLezioni().size());

    }

    @Test
    public void testCrealezione_successo(){
        LocalDate datauguale=LocalDate.of(2026,9,20);
        boolean risultato= responsabile.creaLezione(datauguale,LocalTime.of(14,0),LocalTime.of(16,0),aula2,informatica);
        assertTrue("non dovrebbero esserci problemi di conflitto",risultato);
        assertEquals("la lezione dovrebbe essere salvata nell'orario",2,orarioLezione.getLezioni().size());
    }

    @Test
    public void testCreaLezione_ConflittoDocente() {
        LocalDate datauguale = LocalDate.of(2026, 9, 20);

        boolean risultato = responsabile.creaLezione(datauguale, LocalTime.of(9, 0), LocalTime.of(11, 0), aula2, analisi);

        assertFalse("Il sistema ha permesso a un prof di sdoppiarsi in due aule diverse!", risultato);
    }
}
