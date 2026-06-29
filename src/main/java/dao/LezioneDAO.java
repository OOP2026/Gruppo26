package dao;

import model.Lezione;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface LezioneDAO {

    List<Lezione> trovaTutte();

    List<Lezione> trovaPerGiorno(LocalDate giorno);

    List<Lezione> trovaPerAula(String nomeAula);

    List<Lezione> trovaPerDocente(String loginDocente);

    Optional<Lezione> trovaPerID(int id);

    boolean inserisci(Lezione lezione);

    boolean aggiorna(int id, Lezione lezione);

    boolean elimina(int id);
}
