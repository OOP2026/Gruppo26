package dao;

import model.Vincolo;
import java.util.List;
import java.util.Optional;

public interface VincoloDAO {

    List<Vincolo> trovaTutti();

    List<Vincolo> trovaPerDocente(String loginDocente);

    List<Vincolo> trovaApprovati();

    Optional<Vincolo> trovaPerID(int id);

    int contaPerDocente(String loginDocente);

    boolean inserisci(Vincolo vincolo);

    boolean aggiornaApprovazione(int id, boolean approvato);

    boolean elimina(int id);
}
