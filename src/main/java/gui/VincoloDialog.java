package gui;

import controller.Controller;
import model.Docente;
import model.Responsabile;
import model.Utente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Finestra di dialogo per la gestione dei vincoli di indisponibilità dei docenti.
 * <p>
 * L'interfaccia si adatta dinamicamente al ruolo dell'utente loggato:
 * <ul>
 *     <li>un {@link Docente} può inserire nuovi vincoli di indisponibilità
 *     (giorno e fascia oraria), ma non può approvarli o rifiutarli;</li>
 *     <li>un {@link Responsabile} può visualizzare i vincoli esistenti e
 *     approvarli o rifiutarli, ma non può inserirne di nuovi.</li>
 * </ul>
 * La tabella dei vincoli viene aggiornata automaticamente dopo ogni
 * operazione di inserimento, approvazione o rifiuto.
 */
public class VincoloDialog extends JDialog {

    /** Pannello principale generato dal form grafico (GUI Designer). */
    private JPanel mainPanel;

    /** Tabella che mostra l'elenco dei vincoli di indisponibilità. */
    private JTable tableVincoli;

    /** Campo di testo per l'inserimento del giorno del vincolo. */
    private JTextField txtGiorno;

    /** Campo di testo per l'inserimento dell'ora di inizio del vincolo. */
    private JTextField txtOraInizio;

    /** Campo di testo per l'inserimento dell'ora di fine del vincolo. */
    private JTextField txtOraFine;

    /** Pulsante per inviare una nuova richiesta di vincolo (visibile solo ai docenti). */
    private JButton btnInvia;

    /** Pulsante per approvare il vincolo selezionato (visibile solo ai responsabili). */
    private JButton btnApprova;

    /** Pulsante per rifiutare il vincolo selezionato (visibile solo ai responsabili). */
    private JButton btnRifiuta;

    /** Controller applicativo usato per recuperare dati e invocare la logica di business. */
    private Controller controller;

    /**
     * Costruisce la finestra di dialogo per la gestione dei vincoli.
     * <p>
     * Carica i dati iniziali della tabella dei vincoli e configura la
     * visibilità/abilitazione dei componenti in base al ruolo dell'utente
     * loggato (ottenuto tramite {@link Controller#getUtenteLoggato()}).
     * Registra inoltre i listener per l'invio di una nuova richiesta di
     * vincolo e per l'approvazione/rifiuto dei vincoli esistenti.
     *
     * @param parent     la finestra principale da cui viene aperto il dialogo
     * @param controller il controller applicativo da utilizzare per la logica di business
     */
    public VincoloDialog(JFrame parent, Controller controller) {
        super(parent, "Gestione Vincoli", true);
        this.controller = controller;

        setContentPane(mainPanel);
        setSize(500, 400);
        setLocationRelativeTo(parent);

        aggiornaTabella();

        Utente utenteLoggato = controller.getUtenteLoggato();

        if (utenteLoggato instanceof Responsabile) {
            // Vista responsabile: può approvare/rifiutare, non inserire nuovi vincoli
            btnInvia.setVisible(false);
            txtGiorno.setEnabled(false);
            txtOraInizio.setEnabled(false);
            txtOraFine.setEnabled(false);
            btnApprova.setVisible(true);
            btnRifiuta.setVisible(true);

        } else if (utenteLoggato instanceof Docente) {
            // Vista docente: può inserire vincoli, non approvarli
            btnApprova.setVisible(false);
            btnRifiuta.setVisible(false);
            btnInvia.setVisible(true);
            txtGiorno.setEnabled(true);
            txtOraInizio.setEnabled(true);
            txtOraFine.setEnabled(true);

            txtOraInizio.setToolTipText("Formato: H:MM o HH:MM (es. 9:00 oppure 14:30)");
            txtOraFine.setToolTipText("Formato: H:MM o HH:MM (es. 11:00 oppure 16:00)");
        }

        btnInvia.addActionListener(e -> {
            String giorno = txtGiorno.getText().trim();
            String inizio = txtOraInizio.getText().trim();
            String fine = txtOraFine.getText().trim();

            if (giorno.isEmpty() || inizio.isEmpty() || fine.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Compila tutti i campi!");
                return;
            }

            boolean successo = controller.inoltraRichiestaVincolo(giorno, inizio, fine);
            if (successo) {
                JOptionPane.showMessageDialog(mainPanel, "Richiesta inoltrata con successo!");
                txtGiorno.setText("");
                txtOraInizio.setText("");
                txtOraFine.setText("");
                aggiornaTabella();
            } else {
                JOptionPane.showMessageDialog(mainPanel,
                        "Errore: controlla l'orario (H:MM o HH:MM) o hai già raggiunto il limite di 3 vincoli.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnApprova.addActionListener(e -> {
            int rigaSelezionata = tableVincoli.getSelectedRow();
            if (rigaSelezionata == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Seleziona un vincolo dalla tabella per approvarlo!");
                return;
            }
            controller.approvaVincoloDaIndice(rigaSelezionata);
            JOptionPane.showMessageDialog(mainPanel, "Vincolo Approvato!");
            aggiornaTabella();
        });

        btnRifiuta.addActionListener(e -> {
            int rigaSelezionata = tableVincoli.getSelectedRow();
            if (rigaSelezionata == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Seleziona un vincolo dalla tabella per rifiutarlo!");
                return;
            }
            controller.rifiutaVincoloDaIndice(rigaSelezionata);
            JOptionPane.showMessageDialog(mainPanel, "Vincolo Rifiutato!");
            aggiornaTabella();
        });
    }

    /**
     * Aggiorna la tabella dei vincoli mostrata nella finestra di dialogo.
     * <p>
     * Recupera dal {@link Controller} i dati e le intestazioni aggiornate
     * dei vincoli e li imposta come nuovo modello della tabella
     * {@code tableVincoli}, rendendo le celle non editabili dall'utente.
     */
    private void aggiornaTabella() {
        String[][] dati = controller.getVincoliTabella();
        String[] colonne = controller.getIntestazioniVincoli();
        tableVincoli.setModel(new DefaultTableModel(dati, colonne) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
}