package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;


public class Controller {
	private List<Utente> utenti;
	private List<Lezione> lezioni;
	private List<Aula> aule;
	private OrarioLezione orario;
	private Utente utentelogg;


	public Controller() {
		this.utenti = new ArrayList<>();
		this.aule = new ArrayList<>();
		this.lezioni = new ArrayList<>();
		this.orario = new OrarioLezione(lezioni);
		this.utentelogg = null;
		this.inizializzadati();
	}

	private void inizializzadati() {
		Aula aula1 = new Aula("Aula1");
		Aula aula2 = new Aula("Aula2");
		this.aule.add(aula1);
		this.aule.add(aula2);

		Studente studente=new Studente("Pasquale","Mazzocchi","pm@università.it","pasmaz","pasmaz123","1234567",1);
		this.utenti.add(studente);
		Docente docente=new Docente("Enzo", "Rossi", "enzo.rossi@unipa.it", "docente", "pass456");
		this.utenti.add(docente);

		Responsabile responsabile=new Responsabile("Anna", "Bianchi", "anna.bianchi@unipa.it", "admin", "admin123", this.orario);
		this.utenti.add(responsabile);
		responsabile.aggiungiAula(aula1);
		responsabile.aggiungiAula(aula2);
		this.utenti.add(responsabile);
	}

	public boolean verificaLogin(String username, String password) {
		// 1. Il buttafuori scorre TUTTA la lista
		for (Utente utente : this.utenti) {
			if (utente.eseguiLogin(username, password)) {
				this.utentelogg = utente;
				System.out.println("Login effettuato: Benvenuto " + utente.getNome());
				return true; // Se lo trova, lo fa entrare e chiude tutto
			}
			// NESSUN ELSE QUI! Se non è lui, semplicemente passa al prossimo giro del for.
		}

		// 2. Se arriviamo qui, significa che il for ha finito tutti i giri a vuoto.
		System.out.println("Errore: credenziali non valide.");
		return false; // Rispondiamo alla GUI che il login è fallito
	}
		}
