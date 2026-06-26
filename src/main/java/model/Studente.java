package model;



public class Studente extends Utente {
    private String matricola;
    private int annoCorso;

    public Studente(String nome, String cognome, String email, String login, String password, String matricola, int annoCorso) {
        super(nome,cognome,email,login,password);
        this.matricola = matricola;
        this.annoCorso = annoCorso;
    }
    public String getMatricola() {
        return matricola;
    }
    public int getAnnoCorso() {return annoCorso;}
}
