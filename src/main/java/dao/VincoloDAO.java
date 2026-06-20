package dao;

import model.Vincolo;
import java.util.List;

public interface VincoloDAO {

    boolean inserisciVincolo(Vincolo vincolo);

    List<Vincolo> getAllVincoli();

    boolean aggiornaVincolo(Vincolo vincolo);

    boolean eliminaVincolo(Vincolo vincolo);

}
