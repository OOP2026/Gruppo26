package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtenteTest {
    @Test
    public void UtenteTest(){
        String nome="Pasquale";
        String cognome="Mazzocchi";
        String email="pasqmazz@università.it";
        String login="PMazz";
        String password="pasqmazz";

    Utente utente = new Utente(nome,cognome,email,login,password);

    String nomeOttenuto= utente.getNome();
    String cognomeOttenuto= utente.getCognome();

    assertEquals("il nome non coincide, dovrebbe essere Pasquale","Pasquale",nomeOttenuto);
    assertEquals("il cognome non coincide, dovrebbe essere Mazzocchi", "Mazzocchi",cognomeOttenuto);

    }

    @Test
    public void LoginCorretto(){
        Utente utente=new Utente("Pasquale","Mazzocchi","pasqmazz@università.it","PMazz","pasqmazz");
        boolean risultatoLogin= utente.eseguiLogin("PMazz","pasqmazz");
        assertTrue("Il login è corretto",risultatoLogin);
    }

    @Test
    public void loginErrato(){
        Utente utente=new Utente("Pasquale","Mazzocchi","pasqmazz@università.it","PMazz","pasqmazz");
        boolean risultatoLogin= utente.eseguiLogin("ABC","1234");
        assertFalse("il login è errato",risultatoLogin);
    }
}
