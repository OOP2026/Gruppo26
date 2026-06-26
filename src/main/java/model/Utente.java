package model;

public class Utente {
    protected String nome;
    protected String cognome;
    protected String email;
    protected String login;
    protected String password;
    protected String ruolo;

    public Utente(String nome, String cognome, String email, String login, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public void mostraOrario(OrarioLezione orario){
        System.out.println(orario);
    }
    public boolean eseguiLogin(String login, String password){
        if(login.equals(this.login) && password.equals(this.password)){
            return true;
        }else {
            return false;
        }
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getLogin(){ return login; }
}
