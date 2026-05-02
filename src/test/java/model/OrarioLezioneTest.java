package model;

import org.junit.*;
import static org.junit.Assert.*;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class OrarioLezioneTest {

    private OrarioLezione orarioTest;
    private Lezione lezioneTest1;
    private Lezione lezioneTest2;

    @Before
    public void setUp() {
        orarioTest=new OrarioLezione(new ArrayList<>());

        lezioneTest1=new Lezione(LocalDate.of(2026,5,10),LocalTime.of(9,0),LocalTime.of(11,0),null,null);
        lezioneTest2=new Lezione(LocalDate.of(2026,5,11),LocalTime.of(10,0),LocalTime.of(12,0),null,null);



    }

    @Test
    public void testAggiungiLezione(){
        orarioTest.aggiungiLezione(lezioneTest1);
        orarioTest.aggiungiLezione(lezioneTest2);

        assertEquals("le lezioni non coincidono, dovrebbero essere 2",2,orarioTest.getLezioni().size());
        assertEquals("la prima lezione non coincide, è quella del 10/05/2026",lezioneTest1.getGiorno(),orarioTest.getLezioni().get(0).getGiorno());
    }
}
