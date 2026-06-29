package gui;

import controller.Controller;
import model.Docente;
import model.Responsabile;
import model.Utente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VincoloDialog extends JDialog {

    private JPanel mainPanel;
    private JTable tableVincoli;
    private JTextField txtGiorno;
    private JTextField txtOraInizio;
    private JTextField txtOraFine;
    private JButton btnInvia;
    private JButton btnApprova;
    private JButton btnRifiuta;

    private Controller controller;

    public VincoloDialog(JFrame parent, Controller controller) {
        super(parent, "Gestione Vincoli", true);
        this.controller = controller;

        setContentPane(mainPanel);
        setSize(500, 400);
        setLocationRelativeTo(parent);

        aggiornaTabella();

        Utente utenteLoggato = controller.getUtenteLoggato();

        if (utenteLoggato instanceof Responsabile) {
            btnInvia.setVisible(false);
            txtGiorno.setEnabled(false);
            txtOraInizio.setEnabled(false);
            txtOraFine.setEnabled(false);
            btnApprova.setVisible(true);
            btnRifiuta.setVisible(true);

        } else if (utenteLoggato instanceof Docente) {
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
                        "Errore: controlla l'orario (H:MM o HH:MM) o hai giÃ  raggiunto il limite di 3 vincoli.",
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