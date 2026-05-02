package model;

import org.junit.*;

import static org.junit.Assert.*;

public class InsegnamentoTest {

private Insegnamento insegnamento;
private Docente prof;

@Before
public void setUp() {
    prof=new Docente("Luigi","Bianchi","lb@docenti.università.it","profbianchi","LuiBia");
    insegnamento=new Insegnamento("Analisi",9,3,prof);
}

    @Test
    public void TestInsegnamento() {
    assertEquals("il risultato atteso non coincide, dovrebbe essere Analisi","Analisi",insegnamento.getNomeInsegnamento());
    assertEquals("i cfu non coincidono, dovrebbero essere: 9",9,insegnamento.getCfu());
    assertEquals("l'anno non coincide, dovrebbe essere: 3",3,insegnamento.getAnno());
    assertEquals("il prof non coincide",prof,insegnamento.getDocente());


    }
}
