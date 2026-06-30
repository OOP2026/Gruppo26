package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Finestra di dialogo riservata agli amministratori (Responsabili) per la
 * gestione delle richieste di spostamento delle lezioni.
 * <p>
 * Mostra l'elenco completo delle richieste pendenti e consente di
 * approvarle o rifiutarle. In caso di approvazione, il
 * {@link Controller} verifica eventuali conflitti (ad esempio aula
 * occupata o docente già impegnato nella nuova fascia oraria): se viene
 * rilevato un conflitto la richiesta viene automaticamente rifiutata e
 * l'amministratore ne è informato tramite un messaggio di errore.
 */
public class GestioneDialog extends JDialog {

    /** Pannello principale generato dal form grafico (GUI Designer). */
    private JPanel mainPanel;

    /** Tabella che mostra l'elenco delle richieste di spostamento da gestire. */
    private JTable tableRichieste;

    /** Pulsante per approvare la richiesta selezionata. */
    private JButton btnApprova;

    /** Pulsante per rifiutare la richiesta selezionata. */
    private JButton btnRifiuta;

    /** Controller applicativo usato per recuperare dati e invocare la logica di business. */
    private Controller controller;

    /**
     * Costruisce il pannello di gestione delle richieste per l'amministratore.
     * <p>
     * Carica i dati iniziali della tabella delle richieste e registra i
     * listener dei pulsanti "Approva" e "Rifiuta", che operano sulla riga
     * attualmente selezionata nella tabella delegando l'elaborazione al
     * {@link Controller} tramite {@link Controller#gestisciRichiestaDaIndice(int, boolean)}.
     * Dopo ogni operazione la tabella viene ricaricata per riflettere lo
     * stato aggiornato delle richieste.
     *
     * @param parent     la finestra principale da cui viene aperto il dialogo
     * @param controller il controller applicativo da utilizzare per la logica di business
     */
    public GestioneDialog(JFrame parent, Controller controller) {
        super(parent, "Pannello Admin - Gestione Richieste", true);
        this.controller = controller;

        setContentPane(mainPanel);
        setSize(700, 400);
        setLocationRelativeTo(parent);

        caricaDatiTabella();

        btnApprova.addActionListener(e -> {
            int riga = tableRichieste.getSelectedRow();
            if (riga == -1) {
                JOptionPane.showMessageDialog(this, "Seleziona una richiesta dalla tabella.");
                return;
            }

            boolean successo = controller.gestisciRichiestaDaIndice(riga, true);
            if (successo) {
                JOptionPane.showMessageDialog(this,
                        "Richiesta Approvata! L'orario è stato aggiornato.",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "ATTENZIONE: Conflitto Rilevato!\nAula occupata o docente impegnato. La richiesta è stata RIFIUTATA.",
                        "Errore Conflitto",
                        JOptionPane.ERROR_MESSAGE);
            }
            caricaDatiTabella();
        });

        btnRifiuta.addActionListener(e -> {
            int riga = tableRichieste.getSelectedRow();
            if (riga == -1) {
                JOptionPane.showMessageDialog(this, "Seleziona una richiesta dalla tabella.");
                return;
            }
            controller.gestisciRichiestaDaIndice(riga, false);
            JOptionPane.showMessageDialog(this, "Richiesta rifiutata.");
            caricaDatiTabella();
        });
    }

    /**
     * Carica (o ricarica) i dati della tabella delle richieste di spostamento.
     * <p>
     * Recupera dal {@link Controller} i dati e le intestazioni aggiornate
     * delle richieste e li imposta come nuovo modello della tabella
     * {@code tableRichieste}, rendendo le celle non editabili dall'utente.
     */
    private void caricaDatiTabella() {
        String[] colonne = controller.getIntestazioniRichieste();
        String[][] dati = controller.getRichiesteTabella();
        tableRichieste.setModel(new DefaultTableModel(dati, colonne) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
}