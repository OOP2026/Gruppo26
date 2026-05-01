package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class StudenteTest {
    @Test
    public void testStudente()
    {
        String nome= "Pasquale";
        String cognome= "Mazzocchi";
        String email="pm@studente.università.com";
        String login="pasqualem";
        String password="PasMazz";
        String matricola="N46001380";

    Studente studente=new Studente(nome,cognome,email,login,password,matricola);

    String matricolaOttenuta=studente.getMatricola();
    String nomeOttenuta=studente.getNome();

    assertEquals("La matricola dovrebbe essere N46001380","N46001380",matricolaOttenuta);
    assertEquals("Il nome dovrebbe essere Pasquale", "Pasquale", nomeOttenuta);

    }
    @Test
    public void testLoginCorretto(){
        Studente studente=new Studente("Pasquale","Mazzocchi","pm@studente.università.com","pasqualem","PasMazz","N46001380");
            boolean risultatoLogin= studente.eseguiLogin("pasqualem","PasMazz");
            assertTrue("Il login è corretto", risultatoLogin);
    }

    @Test
    public void testLoginErrato(){
        Studente studente=new Studente("Pasquale","Mazzocchi","pm@studente.università.com","pasqualem","PasMazz","N46001380");
        boolean risultatoLogin= studente.eseguiLogin("pasq","12345");
        assertFalse("Il loging è sbagliato", risultatoLogin);
    }

}
