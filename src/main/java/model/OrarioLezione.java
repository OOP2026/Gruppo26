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

    public void mostraOrario(OrarioLezione orario){
        System.out.println("--- ORARIO DELLE LEZIONI ---");
        for (Lezione l : orario.getLezioni()) {
            System.out.println("Data: " + l.getGiorno() +
                    " | Ore: " + l.getOraInizio() + " - " + l.getOraFine() +
                    " | Materia: " + l.getInsegnamento().getNomeInsegnamento() +
                    " | Aula: " + l.getAula().getNomeAula() +
                    " | Prof: " + l.getInsegnamento().getDocente().getCognome());
        }
        System.out.println("----------------------------");
    }
}
