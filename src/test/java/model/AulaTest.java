package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class AulaTest {

    @Test
    public void AulaTest(){
        String nomeAula = "Aula1";

        Aula aula = new Aula(nomeAula);
        String aulaOttenuta=aula.getAula();

        assertEquals("l'aula dovrebbe essere: Aula1",aulaOttenuta);

    }
}
