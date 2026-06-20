package dao;

import model.Utente;

public interface UtenteDAO {

    Utente eseguiLogin(String username, String password);
}