package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class AulaTest {

    @Test
    public void AulaTest(){

        Aula aula = new Aula("Aula1");
        assertEquals("Aspettative:Aula1","Aula1",aula.getNomeAula());

    }
}
