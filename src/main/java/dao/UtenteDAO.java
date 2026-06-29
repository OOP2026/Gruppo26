package dao;

import model.Utente;
import java.util.List;
import java.util.Optional;


public interface UtenteDAO {

    List<Utente> trovaTutti();

    Optional<Utente> trovaPerLogin(String login);

    Optional<Utente> verificaCredenziali(String login, String password);

    boolean inserisci(Utente utente);

    boolean aggiorna(Utente utente);

    boolean elimina(String login);
}
