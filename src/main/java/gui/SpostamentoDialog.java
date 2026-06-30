package gui;

import controller.Controller;
import model.Lezione;
import javax.swing.*;

/**
 * Finestra di dialogo per la richiesta di spostamento di una lezione.
 * <p>
 * Consente all'utente (studente o docente) di indicare una nuova data e
 * una nuova fascia oraria per la lezione precedentemente selezionata, e di
 * inoltrare la richiesta di spostamento al {@link Controller}. Il
 * controller si occupa della validazione dei dati e dell'eventuale
 * registrazione della richiesta.
 */
public class SpostamentoDialog extends JDialog {

    /** Pannello principale generato dal form grafico (GUI Designer). */
    private JPanel mainPanel;

    /** Campo di testo per l'inserimento della nuova data (formato AAAA-MM-GG). */
    private JTextField txtData;

    /** Campo di testo per l'inserimento della nuova ora di inizio (formato H:MM o HH:MM). */
    private JTextField txtOraInizio;

    /** Campo di testo per l'inserimento della nuova ora di fine (formato H:MM o HH:MM). */
    private JTextField txtOraFine;

    /** Pulsante per inviare la richiesta di spostamento. */
    private JButton btnInvia;

    /** Pulsante per annullare e chiudere la finestra senza inviare alcuna richiesta. */
    private JButton btnAnnulla;

    /** Etichetta informativa che mostra il nome dell'insegnamento della lezione da spostare. */
    private JLabel lblInfoLezione;

    /** Etichetta descrittiva del campo "ora di fine". */
    private JLabel lblOraFine;

    /** Etichetta descrittiva del campo "ora di inizio". */
    private JLabel lblOraInizio;

    /** Etichetta descrittiva del campo "data". */
    private JLabel lblData;

    /** Controller applicativo usato per inoltrare la richiesta di spostamento. */
    private Controller controller;

    /** Lezione selezionata dall'utente, oggetto della richiesta di spostamento. */
    private Lezione lezioneSelezionata;

    /**
     * Costruisce la finestra di dialogo per la richiesta di spostamento di una lezione.
     * <p>
     * Imposta i tooltip dei campi di input, mostra le informazioni sulla
     * lezione selezionata e registra i listener dei pulsanti "Invia"
     * (che valida i campi e inoltra la richiesta tramite
     * {@link Controller#inoltraRichiestaSpostamento(Lezione, String, String, String)})
     * e "Annulla" (che chiude semplicemente la finestra).
     *
     * @param parent     la finestra principale da cui viene aperto il dialogo
     * @param controller il controller applicativo da utilizzare per la logica di business
     * @param lezione    la lezione di cui si vuole richiedere lo spostamento
     */
    public SpostamentoDialog(JFrame parent, Controller controller, Lezione lezione) {
        super(parent, "Nuova Richiesta Spostamento", true);
        this.controller = controller;
        this.lezioneSelezionata = lezione;

        setContentPane(mainPanel);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        lblInfoLezione.setText("Stai spostando: " + lezione.getInsegnamento().getNomeInsegnamento());

        txtData.setToolTipText("Formato: AAAA-MM-GG (es. 2025-06-15)");
        txtOraInizio.setToolTipText("Formato: H:MM o HH:MM (es. 9:00 oppure 14:30)");
        txtOraFine.setToolTipText("Formato: H:MM o HH:MM (es. 11:00 oppure 16:00)");

        btnAnnulla.addActionListener(e -> dispose());

        btnInvia.addActionListener(e -> {
            String nuovaData = txtData.getText().trim();
            String nuovaOraInizio = txtOraInizio.getText().trim();
            String nuovaOraFine = txtOraFine.getText().trim();

            if (nuovaData.isEmpty() || nuovaOraInizio.isEmpty() || nuovaOraFine.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Compila tutti i campi prima di inviare la richiesta.",
                        "Campi mancanti",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean successo = controller.inoltraRichiestaSpostamento(
                    lezioneSelezionata, nuovaData, nuovaOraInizio, nuovaOraFine);

            if (successo) {
                JOptionPane.showMessageDialog(this, "Richiesta di spostamento inoltrata con successo!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Errore: controlla il formato di data (AAAA-MM-GG) e ora (H:MM o HH:MM).",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
