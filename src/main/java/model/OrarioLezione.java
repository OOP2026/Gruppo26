package model;

import java.util.ArrayList;
import java.util.List;


public class OrarioLezione {
    private List<Lezione> lezioni=new ArrayList<Lezione>();
    public OrarioLezione(List<Lezione> lezioni) {
        this.lezioni=lezioni;
    }
    public List<Lezione> getLezioni() {return lezioni;}
    public void aggiungiLezione(Lezione l) {
        this.lezioni.add(l);
    }

    @Override
    public String toString() {
        if (lezioni.isEmpty()) {
            return "Nessuna lezione presente in orario.";
        }
        StringBuilder testoOrario = new StringBuilder();
        testoOrario.append("--- ORARIO DELLE LEZIONI ---\n");
        for (Lezione l : lezioni) {
            testoOrario.append(l.toString()).append("\n");
        }
        testoOrario.append("----------------------------");
        return testoOrario.toString();
    }
}
