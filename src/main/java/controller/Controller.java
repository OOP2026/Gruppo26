package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class Controller {

    private List<Utente> utenti;
    private List<Lezione> lezioni;
    private List<Aula> aule;
    private List<Vincolo> vincoli;
    private OrarioLezione orario;
    private Utente utentelogg;

    public Controller() {
        this.utenti = new ArrayList<>();
        this.aule = new ArrayList<>();
        this.lezioni = new ArrayList<>();
        this.orario = new OrarioLezione(lezioni);
        this.utentelogg = null;
        this.vincoli = new ArrayList<>();
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

        // FIX CRITICO: rimosso il primo utenti.add(responsabile) che causava un duplicato.
        // Il responsabile viene aggiunto una sola volta, dopo che le aule sono state assegnate.
        Responsabile responsabile = new Responsabile("Anna", "Bianchi", "anna.bianchi@unipa.it", "admin", "admin123", this.orario);
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

    // MIGLIORAMENTO: metodo privato per recuperare il Responsabile,
    // elimina la ricerca lineare duplicata in 4 metodi diversi.
    private Responsabile getResponsabile() {
        for (Utente u : this.utenti) {
            if (u instanceof Responsabile) {
                return (Responsabile) u;
            }
        }
        return null;
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

    // MIGLIORAMENTO: metodo di logout per resettare lo stato dell'utente loggato.
    // Evita che, dopo il logout, l'utente precedente rimanga in memoria.
    public void logout() {
        System.out.println("Logout effettuato per: " + (utentelogg != null ? utentelogg.getNome() : "nessuno"));
        this.utentelogg = null;
    }

    // MIGLIORAMENTO: semplificato con return diretto dell'espressione booleana.
    public boolean puoGestireAule() {
        return this.utentelogg instanceof Responsabile;
    }

    // MIGLIORAMENTO: semplificato con return diretto dell'espressione booleana.
    // Un Responsabile non è un Docente generico: può gestire ma non richiedere spostamenti.
    public boolean puoRichiedereSpostamento() {
        return (this.utentelogg instanceof Docente) && !(this.utentelogg instanceof Responsabile);
    }

    public Utente getUtenteLoggato() {
        return this.utentelogg;
    }

    public String[][] getOrarioTabella() {
        List<Lezione> listaLezioni = this.orario.getLezioni();
        int numeroRighe = listaLezioni.size();
        String[][] matrice = new String[numeroRighe][5];

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
        // FIX CRITICO: controllo esplicito del tipo prima del cast per evitare ClassCastException.
        if (!(this.utentelogg instanceof Docente) || this.utentelogg instanceof Responsabile) {
            System.out.println("Controller: Solo un docente può richiedere uno spostamento.");
            return false;
        }

        try {
            java.time.LocalDate nuovaData = java.time.LocalDate.parse(data);
            java.time.LocalTime nuovaOraInizio = java.time.LocalTime.parse(oraInizio);
            java.time.LocalTime nuovaOraFine = java.time.LocalTime.parse(oraFine);

            Docente docenteRichiedente = (Docente) this.utentelogg;

            // MIGLIORAMENTO: usa il metodo centralizzato getResponsabile().
            Responsabile responsabileDestinatario = getResponsabile();
            if (responsabileDestinatario != null) {
                docenteRichiedente.richiedispostamento(lezione, nuovaOraInizio, nuovaOraFine, nuovaData, responsabileDestinatario);
                System.out.println("Controller: Richiesta inoltrata con successo.");
                return true;
            } else {
                System.out.println("Controller: nessun responsabile trovato.");
                return false;
            }

        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("Controller: formato data o ora non valido.");
            return false;
        }
    }

    public void apriGestioneAuleAdmin() {
        System.out.println("Controller: Apertura pannello admin in corso...");
        gui.GestioneDialog dialog = new gui.GestioneDialog(this);
        dialog.setVisible(true);
    }

    public boolean inoltraRichiestaVincolo(String giorno, String oraInizio, String oraFine) {
        // FIX CRITICO: controllo esplicito del tipo prima del cast per evitare ClassCastException.
        if (!(this.utentelogg instanceof Docente) || this.utentelogg instanceof Responsabile) {
            System.out.println("Controller: Solo un docente può inserire vincoli.");
            return false;
        }

        try {
            // MIGLIORAMENTO: formatter più robusto che accetta sia "9:00" che "09:00".
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
            java.time.LocalTime inizio = java.time.LocalTime.parse(oraInizio, formatter);
            java.time.LocalTime fine = java.time.LocalTime.parse(oraFine, formatter);

            Docente docenteRichiedente = (Docente) this.utentelogg;

            long conteggioVincoli = this.vincoli.stream()
                    .filter(v -> v.getDocente().getLogin().equals(docenteRichiedente.getLogin()))
                    .count();

            if (conteggioVincoli >= 3) {
                System.out.println("Controller: Errore! Hai già raggiunto il limite massimo di 3 vincoli.");
                return false;
            }

            Vincolo nuovoVincolo = new Vincolo(docenteRichiedente, giorno, inizio, fine);
            this.vincoli.add(nuovoVincolo);
            System.out.println("Controller: Richiesta di vincolo inoltrata con successo.");
            return true;

        } catch (java.time.format.DateTimeParseException e) {
            System.out.println("Controller: Formato ora non valido (usa H:MM o HH:MM).");
            return false;
        }
    }

    public String[][] getVincoliTabella() {
        int numeroRighe = this.vincoli.size();
        String[][] matrice = new String[numeroRighe][5];

        for (int i = 0; i < numeroRighe; i++) {
            Vincolo v = this.vincoli.get(i);
            matrice[i][0] = v.getDocente().getCognome() + " " + v.getDocente().getNome();
            matrice[i][1] = v.getGiorno();
            matrice[i][2] = v.getOraInizio().toString();
            matrice[i][3] = v.getOraFine().toString();
            matrice[i][4] = v.isApprovato() ? "Approvato" : "In attesa";
        }
        return matrice;
    }

    public String[] getIntestazioniVincoli() {
        return new String[]{"Docente", "Giorno/Data", "Ora Inizio", "Ora Fine", "Stato"};
    }

    public void approvaVincoloDaIndice(int indiceRiga) {
        if (indiceRiga >= 0 && indiceRiga < this.vincoli.size()) {
            this.vincoli.get(indiceRiga).setApprovato(true);
            System.out.println("Controller: Vincolo approvato con successo.");
        }
    }

    public String[][] getRichiesteTabella() {
        // MIGLIORAMENTO: usa getResponsabile() invece di ripetere la ricerca.
        Responsabile resp = getResponsabile();

        if (resp != null) {
            List<RichiestaSpostamento> list = resp.getRichieste();
            String[][] matrice = new String[list.size()][6];
            for (int i = 0; i < list.size(); i++) {
                RichiestaSpostamento r = list.get(i);
                matrice[i][0] = r.getLezioneRichiesta().getInsegnamento().getNomeInsegnamento();
                matrice[i][1] = r.getLezioneRichiesta().getInsegnamento().getDocente().getCognome();
                matrice[i][2] = r.getDataRichiesta().toString();
                matrice[i][3] = r.getOrarioInizioRichiesta().toString() + " - " + r.getOrarioFineRichiesta().toString();
                matrice[i][4] = r.getLezioneRichiesta().getAula().getNomeAula();
                matrice[i][5] = r.getStatoRichiesta().toString();
            }
            return matrice;
        }
        return new String[0][0];
    }

    public String[] getIntestazioniRichieste() {
        return new String[]{"Materia", "Docente", "Nuova Data", "Nuovo Orario", "Aula", "Stato"};
    }

    public boolean gestisciRichiestaDaIndice(int indice, boolean approva) {
        if (!(this.utentelogg instanceof Responsabile)) {
            return false;
        }

        Responsabile resp = (Responsabile) this.utentelogg;
        List<RichiestaSpostamento> richieste = resp.getRichieste();

        // FIX CRITICO: aggiunto bounds check per evitare IndexOutOfBoundsException.
        if (indice < 0 || indice >= richieste.size()) {
            System.out.println("Controller: indice richiesta non valido.");
            return false;
        }

        RichiestaSpostamento richiesta = richieste.get(indice);

        if (approva) {
            return resp.gestisciRichiesta(richiesta);
        } else {
            richiesta.setStato(RichiestaSpostamento.StatoRichiesta.RIFIUTATA);
            return true;
        }
    }

    public String[][] getRichiesteTabellaPubblica() {
        // MIGLIORAMENTO: usa getResponsabile() invece di ripetere la ricerca.
        Responsabile responsabileDiTurno = getResponsabile();

        if (responsabileDiTurno != null) {
            List<RichiestaSpostamento> list = responsabileDiTurno.getRichieste();
            String[][] matrice = new String[list.size()][6];

            for (int i = 0; i < list.size(); i++) {
                RichiestaSpostamento r = list.get(i);
                matrice[i][0] = r.getLezioneRichiesta().getInsegnamento().getNomeInsegnamento();
                matrice[i][1] = r.getLezioneRichiesta().getInsegnamento().getDocente().getCognome();
                matrice[i][2] = r.getDataRichiesta().toString();
                matrice[i][3] = r.getOrarioInizioRichiesta().toString() + " - " + r.getOrarioFineRichiesta().toString();
                matrice[i][4] = r.getLezioneRichiesta().getAula().getNomeAula();
                matrice[i][5] = r.getStatoRichiesta().toString();
            }
            return matrice;
        }

        return new String[0][0];
    }

    public String[][] getVincoliTabellaPubblica() {
        String[][] matrice = new String[vincoli.size()][5];
        for (int i = 0; i < vincoli.size(); i++) {
            Vincolo v = vincoli.get(i);
            matrice[i][0] = v.getDocente().getCognome();
            matrice[i][1] = v.getGiorno();
            matrice[i][2] = v.getOraInizio().toString();
            matrice[i][3] = v.getOraFine().toString();
            matrice[i][4] = v.isApprovato() ? "Approvato" : "In attesa";
        }
        return matrice;
    }

    public void rifiutaVincoloDaIndice(int indice) {
        if (indice >= 0 && indice < this.vincoli.size()) {
            this.vincoli.remove(indice);
            System.out.println("Controller: Vincolo rifiutato ed eliminato.");
        }
    }

    public String[] getVincoliApprovatiPerStudente() {
        return this.vincoli.stream()
                .filter(Vincolo::isApprovato)
                .map(v -> "Docente: " + v.getDocente().getCognome() +
                        " - Giorno: " + v.getGiorno() +
                        " (" + v.getOraInizio() + "-" + v.getOraFine() + ")")
                .toArray(String[]::new);
    }
}
