package model;

import org.junit.*;

import java.time.*;

import static org.junit.Assert.*;

public class LezioneTest {

    private Lezione lezione;
    private Insegnamento insegnamentoProva;
    private Aula aulaProva;


    @Before
    public void setUp() {
        Docente prof = new Docente("Luigi", "Bianchi", "lb@docenti.università.it", "profbianchi", "LuiBia");
        insegnamentoProva = new Insegnamento("Analisi", 9, 3, prof);
        aulaProva = new Aula("Aula1");
        LocalDate dataLezione = LocalDate.of(2026, 5, 10);
        lezione = new Lezione(dataLezione, LocalTime.of(9, 0), LocalTime.of(11, 0), insegnamentoProva, aulaProva);
    }

    @Test
    public void TestLezione() {
        assertEquals("la data non coincide, dovrebbe essere: 2026/5/10", LocalDate.of(2026, 5, 10), lezione.getGiorno());
        assertEquals("l'ora di inizio non coincide, dovrebbe essere 9", LocalTime.of(9, 0), lezione.getOraInizio());
        assertEquals("l'ora di fine non coincide, dovrebbe essere 9", LocalTime.of(11, 0), lezione.getOraFine());
        assertEquals("l'insegnamento non coincide, dovrebbe essere: analisi", insegnamentoProva, lezione.getInsegnamento());
        assertEquals("l'aula assegnata non coincide, dovrebbe essere Aula1", aulaProva, lezione.getAula());
    }


    @Test
    public void TestModifica() {
        LocalDate nuovaData = LocalDate.of(2026, 6, 10);
        lezione.setGiorno(nuovaData);
        lezione.setOraInizio(LocalTime.of(10,0));
        lezione.setOraFine(LocalTime.of(12,0));

        assertEquals("il nuovo giorno non coincide, dovrebbe essere 2026/6/10",nuovaData,lezione.getGiorno());
        assertEquals("la nuova ora inizio non coincide, dovrebbe essere le 10",LocalTime.of(10,0), lezione.getOraInizio());
        assertEquals("la nuova ora fine non coincide, dovrebbe essere le 10",LocalTime.of(12,0), lezione.getOraFine());
    }
}
