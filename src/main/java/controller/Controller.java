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


		Studente studente = new Studente("Pasquale", "Mazzocchi", "pm@università.it", "pasmaz", "pasmaz123", "1234567", 1);
		this.utenti.add(studente);
		Docente docente = new Docente("Enzo", "Rossi", "enzo.rossi@unipa.it", "docente", "pass456");
		this.utenti.add(docente);

		Responsabile responsabile = new Responsabile("Anna", "Bianchi", "anna.bianchi@unipa.it", "admin", "admin123", this.orario);
		this.utenti.add(responsabile);
		responsabile.aggiungiAula(aula1);
		responsabile.aggiungiAula(aula2);
		this.utenti.add(responsabile);

		Docente profBarra = new Docente("Silvio", "Barra", "silvio.barra@unipa.it", "sbarra", "barra123");
		Insegnamento basiDiDati = new Insegnamento("Basi di Dati", 6, 2, profBarra);
		this.utenti.add(profBarra);

		Docente profTramontana = new Docente("Porfirio", "Tramontana", "porfirio.tramontana@unipa.it", "ptramontana", "poo123");
		Insegnamento poo = new Insegnamento("Programmazione ad Oggetti", 9, 2, profTramontana);
		this.utenti.add(profTramontana);

		Docente profCutolo = new Docente("Giovanni", "Cutolo", "generoso.cutolo@unipa.it", "gcutolo", "algebra123");
		Insegnamento algebra = new Insegnamento("Algebra", 6, 1, profCutolo);
		this.utenti.add(profCutolo);

		java.time.LocalDate oggi = java.time.LocalDate.now();

		Lezione lez1 = new Lezione(
				oggi,
				java.time.LocalTime.of(9, 0),
				java.time.LocalTime.of(11, 0),
				basiDiDati,
				aula1
		);
		Lezione lez2 = new Lezione(
				oggi,
				java.time.LocalTime.of(11, 0),
				java.time.LocalTime.of(13, 0),
				poo,
				aula1
		);
		Lezione lez3 = new Lezione(
				oggi.plusDays(1),
				java.time.LocalTime.of(9, 0),
				java.time.LocalTime.of(11, 0),
				algebra,
				aula2
		);
		this.orario.aggiungiLezione(lez1);
		this.orario.aggiungiLezione(lez2);
		this.orario.aggiungiLezione(lez3);


	}

	public boolean verificaLogin(String username, String password) {
		for (Utente utente : this.utenti) {
			if (utente.eseguiLogin(username, password)) {
				this.utentelogg = utente;
				System.out.println("Login effettuato: Benvenuto " + utente.getNome());
				return true;
			}
		}


		System.out.println("Errore: credenziali non valide.");
		return false;
	}
	public boolean puoGestireAule() {
		if (this.utentelogg instanceof Responsabile) {
			return true;
		}
		return false;
	}
	public boolean puoRichiedereSpostamento(){
		if (this.utentelogg instanceof Responsabile) {
			return false;
		}
		if(this.utentelogg instanceof Docente) {
			return true;
		}
		return false;
	}
	public Utente getUtenteLoggato() {
		return this.utentelogg;
	}

	public String[][] getOrarioTabella() {
		List<Lezione> listaLezioni = this.orario.getLezioni();

		int numeroRighe = listaLezioni.size();
		int numeroColonne = 5;

		String[][] matrice = new String[numeroRighe][numeroColonne];

		for (int i = 0; i < numeroRighe; i++) {
			Lezione lezioneCorrente = listaLezioni.get(i);

			matrice[i][0] = lezioneCorrente.getGiorno().toString();
			matrice[i][1] = lezioneCorrente.getOraInizio() + " - " + lezioneCorrente.getOraFine();
			matrice[i][2] = lezioneCorrente.getInsegnamento().getNomeInsegnamento();
			matrice[i][3] = lezioneCorrente.getAula().getNomeAula();
			matrice[i][4] = lezioneCorrente.getInsegnamento().getDocente().getCognome();
		}

		return matrice;
	}
	public Lezione getLezioneDaIndice(int indiceRiga) {
		return this.orario.getLezioni().get(indiceRiga);
	}
	public String[] getIntestazioniTabella() {
		return new String[]{"Data", "Orario", "Materia", "Aula", "Docente"};
	}


	public boolean inoltraRichiestaSpostamento(Lezione lezione, String data, String oraInizio, String oraFine) {
		try{
			java.time.LocalDate nuovaData =java.time.LocalDate.parse(data);
			java.time.LocalTime nuovaOraInizio = java.time.LocalTime.parse(oraInizio);
			java.time.LocalTime nuovaOraFine = java.time.LocalTime.parse(oraFine);

			Docente docenteRichiedente = (Docente) this.utentelogg;

			Responsabile responsabileDestinatario =null;
			for(Utente u: this.utenti) {
				if(u instanceof Responsabile) {
					responsabileDestinatario = (Responsabile) u;
					break;
				}
			}
			if(responsabileDestinatario != null) {
				docenteRichiedente.richiedispostamento(lezione , nuovaOraInizio, nuovaOraFine, nuovaData, responsabileDestinatario);
				System.out.println("Controller: Richiesta inoltrata con successo.");
				return true;
			} else{
				System.out.println("Controller: nessun responsabile trovato");
				return false;
			}

        } catch (java.time.format.DateTimeParseException e){
			System.out.println("Controller: formato data o ora non valido");
			return false;
		}
	}

	// Il metodo chiamato dalla HomeFrame quando l'Admin clicca "Gestione Aule"
	public void apriGestioneAuleAdmin() {
		System.out.println("Controller: Apertura pannello admin in corso...");

		// Creiamo la finestrella e le passiamo "this" (ovvero questo stesso Controller)
		gui.GestioneDialog dialog = new gui.GestioneDialog(this);
		dialog.setVisible(true);
	}
}
