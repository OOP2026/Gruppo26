package controller;

import dao.*;
import model.*;
import implementazioneDao.RichiestaSpostamentoPostgresDAO;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Controller dell'applicazione secondo il pattern MVC: rappresenta l'unico
 * punto di contatto tra la GUI ({@code package gui}) e la logica di dominio
 * ({@code package model}), delegando la persistenza ai DAO ({@code package dao}
 * e relative implementazioni in {@code package implementazioneDao}).
 * <p>
 * Mantiene lo stato della sessione corrente (utente autenticato) e una copia
 * in memoria dell'orario delle lezioni, ricaricata dal database quando necessario.
 * </p>
 *
 * @author Gruppo26
 */
public class Controller {

	/** DAO per l'accesso ai dati degli utenti. */
	private final UtenteDAO               utenteDAO;

	/** DAO per l'accesso ai dati delle lezioni. */
	private final LezioneDAO              lezioneDAO;

	/** DAO per l'accesso ai dati dei vincoli. */
	private final VincoloDAO              vincoloDAO;

	/** DAO per l'accesso ai dati delle richieste di spostamento. */
	private final RichiestaSpostamentoDAO richiestaDAO;

	/** Orario delle lezioni mantenuto in memoria, sincronizzato con il database. */
	private OrarioLezione orario;

	/** Utente attualmente autenticato, oppure {@code null} se nessuno ha eseguito il login. */
	private Utente utentelogg;

	/**
	 * Costruisce il controller, istanziando le implementazioni concrete dei DAO
	 * (basate su PostgreSQL) e caricando l'orario iniziale dal database.
	 */
	public Controller() {
		this.utenteDAO    = new implementazioneDao.UtentePostgresDAO();
		this.lezioneDAO   = new implementazioneDao.LezionePostgresDAO();
		this.vincoloDAO   = new implementazioneDao.VincoloPostgresDAO();
		this.richiestaDAO = new RichiestaSpostamentoPostgresDAO();
		this.utentelogg   = null;
		this.orario       = new OrarioLezione(new java.util.ArrayList<>());
		ricaricaOrarioDalDB();
	}

	/**
	 * Ricarica l'elenco delle lezioni dal database, sostituendo il contenuto
	 * dell'orario mantenuto in memoria.
	 */
	private void ricaricaOrarioDalDB() {
		List<Lezione> lezioniDB = lezioneDAO.trovaTutte();
		this.orario.getLezioni().clear();
		this.orario.getLezioni().addAll(lezioniDB);
	}

	/**
	 * Cerca tra tutti gli utenti il primo {@link Responsabile} presente,
	 * collegandogli l'orario attualmente gestito dal controller.
	 *
	 * @return il responsabile trovato, oppure {@code null} se non ne esiste alcuno
	 */
	private Responsabile getResponsabile() {
		for (Utente u : utenteDAO.trovaTutti()) {
			if (u instanceof Responsabile) {
				((Responsabile) u).setOrario(this.orario);
				return (Responsabile) u;
			}
		}
		return null;
	}

	/**
	 * Verifica le credenziali fornite e, in caso di successo, imposta l'utente
	 * come utente attualmente autenticato.
	 *
	 * @param username il login inserito
	 * @param password la password inserita
	 * @return {@code true} se l'accesso è stato consentito, {@code false} se
	 *         le credenziali non sono valide
	 */
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

	/**
	 * Esegue il logout, azzerando l'utente attualmente autenticato.
	 */
	public void logout() {
		System.out.println("Logout effettuato per: " +
				(utentelogg != null ? utentelogg.getNome() : "nessuno"));
		this.utentelogg = null;
	}

	/**
	 * Indica se l'utente attualmente autenticato può gestire aule, insegnamenti
	 * e richieste, ovvero se è un {@link Responsabile}.
	 *
	 * @return {@code true} se l'utente autenticato è un responsabile
	 */
	public boolean puoGestireAule()          { return this.utentelogg instanceof Responsabile; }

	/**
	 * Indica se l'utente attualmente autenticato può richiedere lo spostamento
	 * di una lezione, ovvero se è un {@link Docente} ma non un {@link Responsabile}.
	 *
	 * @return {@code true} se l'utente autenticato è un docente non responsabile
	 */
	public boolean puoRichiedereSpostamento() {
		return (this.utentelogg instanceof Docente) && !(this.utentelogg instanceof Responsabile);
	}

	/**
	 * Restituisce l'utente attualmente autenticato.
	 *
	 * @return l'utente autenticato, oppure {@code null} se nessuno ha eseguito il login
	 */
	public Utente getUtenteLoggato() { return this.utentelogg; }

	/**
	 * Ricarica l'orario dal database e lo converte in una matrice di stringhe
	 * pronta per essere mostrata in una {@code JTable} (colonne: data, orario,
	 * materia, aula, docente).
	 *
	 * @return la matrice dei dati dell'orario
	 */
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

	/**
	 * Restituisce la lezione corrispondente all'indice di riga indicato
	 * nell'orario mantenuto in memoria.
	 *
	 * @param indice l'indice di riga (corrispondente alla riga selezionata in tabella)
	 * @return la lezione corrispondente
	 */
	public Lezione getLezioneDaIndice(int indice) { return this.orario.getLezioni().get(indice); }

	/**
	 * Restituisce le intestazioni di colonna per la tabella dell'orario.
	 *
	 * @return l'array delle intestazioni
	 */
	public String[] getIntestazioniTabella()      { return new String[]{"Data","Orario","Materia","Aula","Docente"}; }

	/**
	 * Inoltra una richiesta di spostamento per la lezione indicata, a nome
	 * dell'utente attualmente autenticato (che deve essere un docente non
	 * responsabile), e la persiste sul database.
	 *
	 * @param lezione   la lezione di cui si richiede lo spostamento
	 * @param data      la nuova data proposta, in formato ISO ({@code AAAA-MM-GG})
	 * @param oraInizio la nuova ora di inizio proposta, in formato {@code H:MM} o {@code HH:MM}
	 * @param oraFine   la nuova ora di fine proposta, in formato {@code H:MM} o {@code HH:MM}
	 * @return {@code true} se la richiesta è stata creata e salvata con successo;
	 *         {@code false} se l'utente non è autorizzato, se non esiste un
	 *         responsabile, se il formato di data/ora non è valido, o se il
	 *         salvataggio sul database fallisce
	 */
	public boolean inoltraRichiestaSpostamento(Lezione lezione, String data, String oraInizio, String oraFine) {
		if (!(this.utentelogg instanceof Docente) || this.utentelogg instanceof Responsabile) {
			return false;
		}
		try {
			java.time.LocalDate nuovaData      = java.time.LocalDate.parse(data);
			java.time.LocalTime nuovaOraInizio = java.time.LocalTime.parse(oraInizio);
			java.time.LocalTime nuovaOraFine   = java.time.LocalTime.parse(oraFine);

			Responsabile resp = getResponsabile();
			if (resp == null) {  return false; }

			Docente doc = (Docente) this.utentelogg;
			doc.richiediSpostamento(lezione, nuovaOraInizio, nuovaOraFine, nuovaData, resp);


			List<RichiestaSpostamento> richieste = resp.getRichieste();
			RichiestaSpostamento ultima = richieste.get(richieste.size() - 1);
			boolean salvata = richiestaDAO.inserisci(ultima);
			if (!salvata) ;
			return salvata;

		} catch (java.time.format.DateTimeParseException e) {
			System.out.println("Controller: formato data o ora non valido.");
			return false;
		}
	}

	/**
	 * Restituisce tutte le richieste di spostamento (vista per il pannello
	 * di gestione del responsabile), convertite in matrice di stringhe.
	 *
	 * @return la matrice dei dati delle richieste
	 */
	public String[][] getRichiesteTabella() {
		return costruisciMatriceRichieste(richiestaDAO.trovaTutte());
	}

	/**
	 * Restituisce tutte le richieste di spostamento (vista pubblica, identica
	 * a {@link #getRichiesteTabella()}, mostrata nella bacheca consultabile
	 * dagli studenti), convertite in matrice di stringhe.
	 *
	 * @return la matrice dei dati delle richieste
	 */
	public String[][] getRichiesteTabellaPubblica() {
		return costruisciMatriceRichieste(richiestaDAO.trovaTutte());
	}

	/**
	 * Converte una lista di richieste di spostamento in una matrice di stringhe
	 * pronta per essere mostrata in una {@code JTable} (colonne: materia, docente,
	 * nuova data, nuovo orario, aula, stato).
	 *
	 * @param list la lista di richieste da convertire
	 * @return la matrice dei dati
	 */
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

	/**
	 * Restituisce le intestazioni di colonna per la tabella delle richieste di spostamento.
	 *
	 * @return l'array delle intestazioni
	 */
	public String[] getIntestazioniRichieste() {
		return new String[]{"Materia","Docente","Nuova Data","Nuovo Orario","Aula","Stato"};
	}

	/**
	 * Approva o rifiuta la richiesta di spostamento corrispondente all'indice
	 * di riga indicato. In caso di approvazione viene ricaricata la lista
	 * aggiornata dei vincoli approvati del docente interessato, viene eseguita
	 * la verifica dei conflitti tramite {@link Responsabile#gestisciRichiesta(RichiestaSpostamento)}
	 * e, se l'esito è positivo, la lezione viene aggiornata anche sul database
	 * e l'orario in memoria viene ricaricato.
	 *
	 * @param indice  l'indice di riga della richiesta nella tabella delle richieste
	 * @param approva {@code true} per tentare l'approvazione, {@code false} per rifiutare direttamente
	 * @return {@code true} se la richiesta è stata gestita con successo
	 *         (approvata senza conflitti, oppure rifiutata su richiesta esplicita);
	 *         {@code false} se l'utente autenticato non è un responsabile,
	 *         se l'indice non è valido, oppure se l'approvazione fallisce per un conflitto
	 */
	public boolean gestisciRichiestaDaIndice(int indice, boolean approva) {
		if (!(this.utentelogg instanceof Responsabile)) return false;

		Responsabile resp = (Responsabile) this.utentelogg;

		List<RichiestaSpostamento> richieste = richiestaDAO.trovaTutte();

		if (indice < 0 || indice >= richieste.size()) {
			return false;
		}

		RichiestaSpostamento richiesta = richieste.get(indice);
		int idRealeDB = richiesta.getId();

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

	/**
	 * Inserisce un nuovo vincolo di indisponibilità per l'utente attualmente
	 * autenticato (che deve essere un docente non responsabile), rispettando
	 * il limite massimo di 3 vincoli per docente.
	 *
	 * @param giorno    il giorno della settimana del vincolo
	 * @param oraInizio l'ora di inizio della fascia, in formato {@code H:mm} o {@code HH:mm}
	 * @param oraFine   l'ora di fine della fascia, in formato {@code H:mm} o {@code HH:mm}
	 * @return {@code true} se il vincolo è stato inserito con successo;
	 *         {@code false} se l'utente non è autorizzato, se è stato già
	 *         raggiunto il limite di 3 vincoli, se il formato dell'orario
	 *         non è valido, o se il salvataggio sul database fallisce
	 */
	public boolean inoltraRichiestaVincolo(String giorno, String oraInizio, String oraFine) {
		if (!(this.utentelogg instanceof Docente) || this.utentelogg instanceof Responsabile) {
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

	/**
	 * Restituisce tutti i vincoli (vista per il pannello di gestione del
	 * responsabile), convertiti in matrice di stringhe.
	 *
	 * @return la matrice dei dati dei vincoli
	 */
	public String[][] getVincoliTabella()        { return costruisciMatriceVincoli(vincoloDAO.trovaTutti()); }

	/**
	 * Restituisce tutti i vincoli (vista pubblica, identica a
	 * {@link #getVincoliTabella()}, mostrata nella bacheca consultabile
	 * dagli studenti), convertiti in matrice di stringhe.
	 *
	 * @return la matrice dei dati dei vincoli
	 */
	public String[][] getVincoliTabellaPubblica() { return costruisciMatriceVincoli(vincoloDAO.trovaTutti()); }

	/**
	 * Converte una lista di vincoli in una matrice di stringhe pronta per
	 * essere mostrata in una {@code JTable} (colonne: docente, giorno,
	 * ora inizio, ora fine, stato).
	 *
	 * @param vincoli la lista di vincoli da convertire
	 * @return la matrice dei dati
	 */
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

	/**
	 * Restituisce le intestazioni di colonna per la tabella dei vincoli.
	 *
	 * @return l'array delle intestazioni
	 */
	public String[] getIntestazioniVincoli() {
		return new String[]{"Docente","Giorno/Data","Ora Inizio","Ora Fine","Stato"};
	}

	/**
	 * Approva il vincolo corrispondente all'indice di riga indicato nella
	 * tabella dei vincoli, aggiornandone lo stato sul database.
	 *
	 * @param indice l'indice di riga del vincolo da approvare
	 */
	public void approvaVincoloDaIndice(int indice) {
		List<Vincolo> vincoli = vincoloDAO.trovaTutti();
		if (indice >= 0 && indice < vincoli.size()) {
			int idReale = vincoli.get(indice).getId();
			vincoloDAO.aggiornaApprovazione(idReale, true);
			System.out.println("Controller: Vincolo approvato sul DB (id=" + idReale + ").");
		}
	}

	/**
	 * Rifiuta (elimina definitivamente) il vincolo corrispondente all'indice
	 * di riga indicato nella tabella dei vincoli.
	 *
	 * @param indice l'indice di riga del vincolo da rifiutare
	 */
	public void rifiutaVincoloDaIndice(int indice) {
		List<Vincolo> vincoli = vincoloDAO.trovaTutti();
		if (indice >= 0 && indice < vincoli.size()) {
			int idReale = vincoli.get(indice).getId();
			vincoloDAO.elimina(idReale);
			System.out.println("Controller: Vincolo eliminato dal DB (id=" + idReale + ").");
		}
	}

	/**
	 * Restituisce, in forma di array di stringhe leggibili, l'elenco dei
	 * vincoli approvati, da mostrare agli studenti come avvisi di indisponibilità
	 * dei docenti.
	 *
	 * @return l'array degli avvisi testuali relativi ai vincoli approvati
	 */
	public String[] getVincoliApprovatiPerStudente() {
		return vincoloDAO.trovaApprovati().stream()
				.map(v -> "Docente: " + v.getDocente().getCognome() +
						" - Giorno: " + v.getGiorno() +
						" (" + v.getOraInizio() + "-" + v.getOraFine() + ")")
				.toArray(String[]::new);
	}
}