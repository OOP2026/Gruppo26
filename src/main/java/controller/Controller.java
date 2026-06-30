package controller;

import dao.*;
import model.*;
import implementazioneDao.RichiestaSpostamentoPostgresDAO;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class Controller {

	private final UtenteDAO               utenteDAO;
	private final LezioneDAO              lezioneDAO;
	private final VincoloDAO              vincoloDAO;
	private final RichiestaSpostamentoDAO richiestaDAO;

	private OrarioLezione orario;
	private Utente utentelogg;

	public Controller() {
		this.utenteDAO    = new implementazioneDao.UtentePostgresDAO();
		this.lezioneDAO   = new implementazioneDao.LezionePostgresDAO();
		this.vincoloDAO   = new implementazioneDao.VincoloPostgresDAO();
		this.richiestaDAO = new RichiestaSpostamentoPostgresDAO();
		this.utentelogg   = null;
		this.orario       = new OrarioLezione(new java.util.ArrayList<>());
		ricaricaOrarioDalDB();
	}

	private void ricaricaOrarioDalDB() {
		List<Lezione> lezioniDB = lezioneDAO.trovaTutte();
		System.out.println("Controller: lezioni caricate dal DB: " + lezioniDB.size());
		this.orario.getLezioni().clear();
		this.orario.getLezioni().addAll(lezioniDB);
	}

	private Responsabile getResponsabile() {
		for (Utente u : utenteDAO.trovaTutti()) {
			if (u instanceof Responsabile) {
				((Responsabile) u).setOrario(this.orario);
				return (Responsabile) u;
			}
		}
		return null;
	}

	public boolean verificaLogin(String username, String password) {
		Optional<Utente> risultato = utenteDAO.verificaCredenziali(username, password);
		if (risultato.isPresent()) {
			this.utentelogg = risultato.get();
			if (this.utentelogg instanceof Responsabile)
				((Responsabile) this.utentelogg).setOrario(this.orario);
			System.out.println("Login effettuato: Benvenuto " + utentelogg.getNome());
			return true;
		}
		System.out.println("Errore: credenziali non valide.");
		return false;
	}

	public void logout() {
		System.out.println("Logout effettuato per: " +
				(utentelogg != null ? utentelogg.getNome() : "nessuno"));
		this.utentelogg = null;
	}

	public boolean puoGestireAule()          { return this.utentelogg instanceof Responsabile; }
	public boolean puoRichiedereSpostamento() {
		return (this.utentelogg instanceof Docente) && !(this.utentelogg instanceof Responsabile);
	}
	public Utente getUtenteLoggato() { return this.utentelogg; }

	public String[][] getOrarioTabella() {
		ricaricaOrarioDalDB();
		List<Lezione> lista = this.orario.getLezioni();
		String[][] matrice  = new String[lista.size()][5];
		for (int i = 0; i < lista.size(); i++) {
			Lezione l     = lista.get(i);
			matrice[i][0] = l.getGiorno().toString();
			matrice[i][1] = l.getOraInizio() + " - " + l.getOraFine();
			matrice[i][2] = l.getInsegnamento().getNomeInsegnamento();
			matrice[i][3] = l.getAula().getNomeAula();
			matrice[i][4] = l.getInsegnamento().getDocente().getCognome();
		}
		return matrice;
	}

	public Lezione getLezioneDaIndice(int indice) { return this.orario.getLezioni().get(indice); }
	public String[] getIntestazioniTabella()      { return new String[]{"Data","Orario","Materia","Aula","Docente"}; }

	public boolean inoltraRichiestaSpostamento(Lezione lezione, String data, String oraInizio, String oraFine) {
		if (!(this.utentelogg instanceof Docente) || this.utentelogg instanceof Responsabile) {
			System.out.println("Controller: Solo un docente può richiedere uno spostamento.");
			return false;
		}
		try {
			java.time.LocalDate nuovaData      = java.time.LocalDate.parse(data);
			java.time.LocalTime nuovaOraInizio = java.time.LocalTime.parse(oraInizio);
			java.time.LocalTime nuovaOraFine   = java.time.LocalTime.parse(oraFine);

			Responsabile resp = getResponsabile();
			if (resp == null) { System.out.println("Controller: nessun responsabile trovato."); return false; }

			Docente doc = (Docente) this.utentelogg;
			doc.richiediSpostamento(lezione, nuovaOraInizio, nuovaOraFine, nuovaData, resp);


			List<RichiestaSpostamento> richieste = resp.getRichieste();
			RichiestaSpostamento ultima = richieste.get(richieste.size() - 1);
			boolean salvata = richiestaDAO.inserisci(ultima);
			if (!salvata) System.out.println("Controller: attenzione – richiesta non persistita sul DB.");
			return salvata;

		} catch (java.time.format.DateTimeParseException e) {
			System.out.println("Controller: formato data o ora non valido.");
			return false;
		}
	}

	public String[][] getRichiesteTabella() {
		return costruisciMatriceRichieste(richiestaDAO.trovaTutte());
	}

	public String[][] getRichiesteTabellaPubblica() {
		return costruisciMatriceRichieste(richiestaDAO.trovaTutte());
	}

	private String[][] costruisciMatriceRichieste(List<RichiestaSpostamento> list) {
		String[][] matrice = new String[list.size()][6];
		for (int i = 0; i < list.size(); i++) {
			RichiestaSpostamento r = list.get(i);
			matrice[i][0] = r.getLezioneRichiesta().getInsegnamento().getNomeInsegnamento();
			matrice[i][1] = r.getLezioneRichiesta().getInsegnamento().getDocente().getCognome();
			matrice[i][2] = r.getDataRichiesta().toString();
			matrice[i][3] = r.getOrarioInizioRichiesta() + " - " + r.getOrarioFineRichiesta();
			matrice[i][4] = r.getLezioneRichiesta().getAula().getNomeAula();
			matrice[i][5] = r.getStatoRichiesta().toString();
		}
		return matrice;
	}

	public String[] getIntestazioniRichieste() {
		return new String[]{"Materia","Docente","Nuova Data","Nuovo Orario","Aula","Stato"};
	}

	public boolean gestisciRichiestaDaIndice(int indice, boolean approva) {
		if (!(this.utentelogg instanceof Responsabile)) return false;

		Responsabile resp = (Responsabile) this.utentelogg;

		List<RichiestaSpostamento> richieste = richiestaDAO.trovaTutte();

		if (indice < 0 || indice >= richieste.size()) {
			System.out.println("Controller: indice richiesta non valido.");
			return false;
		}

		RichiestaSpostamento richiesta = richieste.get(indice);
		int idRealeDB = richiesta.getId(); // ID reale letto dal DB
		System.out.println("Controller: gestendo richiesta con id_richiesta=" + idRealeDB);

		if (approva) {
			resp.getRichieste().clear();
			resp.getRichieste().addAll(richieste);

			Docente docenteLezione = richiesta.getLezioneRichiesta().getInsegnamento().getDocente();
			List<Vincolo> vincoliApprovati = vincoloDAO.trovaPerDocente(docenteLezione.getLogin())
					.stream()
					.filter(Vincolo::isApprovato)
					.collect(java.util.stream.Collectors.toList());
			docenteLezione.setVincoli(vincoliApprovati);

			boolean esito = resp.gestisciRichiesta(richiesta);

			RichiestaSpostamento.StatoRichiesta nuovoStato = esito
					? RichiestaSpostamento.StatoRichiesta.APPROVATA
					: RichiestaSpostamento.StatoRichiesta.RIFIUTATA;

			richiestaDAO.aggiornaStato(idRealeDB, nuovoStato);

			if (esito) {
				Lezione lezMod = richiesta.getLezioneRichiesta();
				boolean aggiornata = lezioneDAO.aggiorna(lezMod.getId(), lezMod);
				if (aggiornata) {
					System.out.println("Controller: lezione aggiornata sul DB con id=" + lezMod.getId());
				} else {
					System.out.println("Controller: ATTENZIONE - aggiornamento lezione non riuscito per id=" + lezMod.getId());
				}
				ricaricaOrarioDalDB();
			}
			return esito;

		} else {
			richiesta.setStato(RichiestaSpostamento.StatoRichiesta.RIFIUTATA);
			richiestaDAO.aggiornaStato(idRealeDB, RichiestaSpostamento.StatoRichiesta.RIFIUTATA);
			return true;
		}
	}

	public boolean inoltraRichiestaVincolo(String giorno, String oraInizio, String oraFine) {
		if (!(this.utentelogg instanceof Docente) || this.utentelogg instanceof Responsabile) {
			System.out.println("Controller: Solo un docente può inserire vincoli.");
			return false;
		}
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
			java.time.LocalTime inizio = java.time.LocalTime.parse(oraInizio, formatter);
			java.time.LocalTime fine   = java.time.LocalTime.parse(oraFine,   formatter);

			Docente doc = (Docente) this.utentelogg;
			if (vincoloDAO.contaPerDocente(doc.getLogin()) >= 3) {
				System.out.println("Controller: Limite massimo di 3 vincoli raggiunto.");
				return false;
			}

			Vincolo nuovoVincolo = new Vincolo(doc, giorno, inizio, fine);
			boolean salvato = vincoloDAO.inserisci(nuovoVincolo);
			if (salvato) System.out.println("Controller: Vincolo inserito sul DB.");
			return salvato;

		} catch (java.time.format.DateTimeParseException e) {
			System.out.println("Controller: Formato ora non valido (usa H:MM o HH:MM).");
			return false;
		}
	}

	public String[][] getVincoliTabella()        { return costruisciMatriceVincoli(vincoloDAO.trovaTutti()); }
	public String[][] getVincoliTabellaPubblica() { return costruisciMatriceVincoli(vincoloDAO.trovaTutti()); }

	private String[][] costruisciMatriceVincoli(List<Vincolo> vincoli) {
		String[][] matrice = new String[vincoli.size()][5];
		for (int i = 0; i < vincoli.size(); i++) {
			Vincolo v     = vincoli.get(i);
			matrice[i][0] = v.getDocente().getCognome() + " " + v.getDocente().getNome();
			matrice[i][1] = v.getGiorno();
			matrice[i][2] = v.getOraInizio().toString();
			matrice[i][3] = v.getOraFine().toString();
			matrice[i][4] = v.isApprovato() ? "Approvato" : "In attesa";
		}
		return matrice;
	}

	public String[] getIntestazioniVincoli() {
		return new String[]{"Docente","Giorno/Data","Ora Inizio","Ora Fine","Stato"};
	}

	public void approvaVincoloDaIndice(int indice) {
		List<Vincolo> vincoli = vincoloDAO.trovaTutti();
		if (indice >= 0 && indice < vincoli.size()) {
			int idReale = vincoli.get(indice).getId();
			vincoloDAO.aggiornaApprovazione(idReale, true);
			System.out.println("Controller: Vincolo approvato sul DB (id=" + idReale + ").");
		}
	}

	public void rifiutaVincoloDaIndice(int indice) {
		List<Vincolo> vincoli = vincoloDAO.trovaTutti();
		if (indice >= 0 && indice < vincoli.size()) {
			int idReale = vincoli.get(indice).getId();
			vincoloDAO.elimina(idReale);
			System.out.println("Controller: Vincolo eliminato dal DB (id=" + idReale + ").");
		}
	}

	public String[] getVincoliApprovatiPerStudente() {
		return vincoloDAO.trovaApprovati().stream()
				.map(v -> "Docente: " + v.getDocente().getCognome() +
						" - Giorno: " + v.getGiorno() +
						" (" + v.getOraInizio() + "-" + v.getOraFine() + ")")
				.toArray(String[]::new);
	}
}