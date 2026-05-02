package model;

import java.util.ArrayList;
import java.util.List;


public class OrarioLezione {
    private List<Lezione> lezioni=new ArrayList<Lezione>();
    public OrarioLezione(List<Lezione> lezioni) {
        this.lezioni=lezioni;
    }
    public List<Lezione> getLezioni() {return lezioni;}
}
