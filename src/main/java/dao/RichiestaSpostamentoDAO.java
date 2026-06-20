package dao;

import model.RichiestaSpostamento;

public interface RichiestaSpostamentoDAO {
    boolean inserisciRichiesta(RichiestaSpostamento richiesta);
    boolean aggiornaStatoRichiesta(RichiestaSpostamento richiesta);
}
