package model;

import java.util.List;
import java.util.ArrayList;

public class Responsabile extends Docente{
   private OrarioLezione orarioLezione;
   private List<RichiestaSpostamento> richieste;
   private List<Aula> auleDisponibili;
   private List<Insegnamento> insegnamenti;

   public Responsabile(String nome, String cognome, String email, String login, String password) {
    super(nome,cognome,email,login,password);
   }

    public void aggiungiAula(Aula aula){
       this.auleDisponibili.add(aula);
       System.out.println("Aggiunta aula: "+aula.getNomeAula());
    }
    public void aggiungiInsegnamento(Insegnamento NuovoInsegnamento){
       this.insegnamenti.add(NuovoInsegnamento);
       System.out.println("Aggiunto insegnamento: "+NuovoInsegnamento.getNomeInsegnamento(NuovoInsegnamento));
    }
    public void creaLezione

}
