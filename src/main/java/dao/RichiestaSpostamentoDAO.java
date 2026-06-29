package dao;

import model.RichiestaSpostamento;
import model.RichiestaSpostamento.StatoRichiesta;
import java.util.List;
import java.util.Optional;


public interface RichiestaSpostamentoDAO {

    List<RichiestaSpostamento> trovaTutte();

    List<RichiestaSpostamento> trovaPerDocente(String loginDocente);

    List<RichiestaSpostamento> trovaPerStato(StatoRichiesta stato);

    Optional<RichiestaSpostamento> trovaPerID(int id);

    boolean inserisci(RichiestaSpostamento richiesta);

    boolean aggiornaStato(int id, StatoRichiesta stato);

    boolean elimina(int id);
}