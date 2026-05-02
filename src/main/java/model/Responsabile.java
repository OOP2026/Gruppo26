package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;

public class Responsabile extends Docente{
   private OrarioLezione orarioLezioni;
   private List<RichiestaSpostamento> richieste;
   private List<Aula> auleDisponibili;
   private List<Insegnamento> insegnamenti;

   public Responsabile(String nome, String cognome, String email, String login, String password,OrarioLezione orarioLezioni) {
    super(nome,cognome,email,login,password);
    this.orarioLezioni=orarioLezioni;

    this.richieste=new ArrayList<>();
    this.auleDisponibili=new ArrayList<>();
    this.insegnamenti=new ArrayList<>();

   }

    public void aggiungiAula(Aula aula){
       this.auleDisponibili.add(aula);
       System.out.println("Aggiunta aula: "+aula.getNomeAula());
    }
    public void aggiungiInsegnamento(Insegnamento NuovoInsegnamento){
       this.insegnamenti.add(NuovoInsegnamento);
       System.out.println("Aggiunto insegnamento: "+NuovoInsegnamento.getNomeInsegnamento());
    }
    public boolean verificaConflitti(LocalDate giorno, LocalTime inizioLezione,LocalTime fineLezione,Aula aula, Docente docente){
       for(Lezione l : orarioLezioni.getLezioni()){
           boolean sovrapposizione =inizioLezione.isBefore(l.getOraFine()) && fineLezione.isAfter(l.getOraInizio());

           if(sovrapposizione){
               if((l.getAula().getNomeAula().equals(aula.getNomeAula()))&&(l.getGiorno().equals(giorno))){
                   System.out.println("Conflitto, é gia presente una lezione in quell'aula a quell'ora");
                   return true;
               }
               if((l.getDocente().equals(docente))&&(l.getGiorno().equals(giorno))&&(l.getOraInizio().equals(inizioLezione))){
                   System.out.println("Conflitto, il docente indicato ha gia una lezione nell'orario selezionato");
                   return true;
               }
           }
       }
       return false;
       System.out.println("Non ci sono conflitti");
    }

    public void creaLezione(Insegnamento NuovoInsegnamento,){}

}
