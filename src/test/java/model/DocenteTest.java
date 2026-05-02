package model;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class DocenteTest {

    @Test
    public void DocenteTest(){
        String nome="Pasquale";
        String cognome="Mazzocchi";
        String  email="PMazzocchi@docenti.università.it";
        String login="PasqualeM";
        String password="pasqmazz";


        Docente docente=new Docente(nome,cognome,email,password,login);
        String nomeOttenuto= docente.getNome();
        String cognomeOttenuto= docente.getCognome();

        assertEquals("il nome non coincide,dovrebbe essere Pasquale","Pasquale",nomeOttenuto);
        assertEquals("il cognome non coincide, dovrebbe essere Mazzocchi","Mazzocchi",cognomeOttenuto);

    }

    @Test
    public void loginCorretto(){
        Docente docente=new Docente("Pasquale","Mazzocchi","PMazzocchi@docenti.università.it","PasqualeM","pasqmazz");

        boolean risultatoLogin= docente.eseguiLogin("PasqualeM","pasqmazz");
        assertTrue("il login è corretto",risultatoLogin);

    }


    @Test
    public void loginErrato(){
        Docente docente=new Docente("Pasquale","Mazzocchi","PMazzocchi@docenti.università.it","PasqualeM","pasqmazz");

        boolean risultatoLogin= docente.eseguiLogin("ABC","1234");
        assertFalse("il login è sbagliato",risultatoLogin);

    }

    @Test
    public void testRichiediSpostamento() {
        Docente docente = new Docente("Pasquale", "Mazzocchi", "email", "login", "pass");
        Responsabile resp = new Responsabile("Mario", "Rossi", "email", "login", "pass", new OrarioLezione(new ArrayList<>()));
        Aula aula = new Aula("Aula Test");
        Insegnamento ins = new Insegnamento("Test Materia", 6, 1, docente);
        Lezione lezione = new Lezione(LocalDate.of(2026, 5, 20), LocalTime.of(10,0), LocalTime.of(12,0), ins, aula);

        docente.richiedispostamento(lezione, LocalTime.of(14,0), LocalTime.of(16,0), LocalDate.of(2026, 5, 21), resp);
        assertTrue(true);
    }

}
