package model;

import org.junit.Test;
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

        assertEquals("il nome dovrebbe essere Pasquale","Pasquale",nomeOttenuto);
        assertEquals("il cognome dovrebbe essere Mazzocchi","Mazzocchi",cognomeOttenuto);

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

}
