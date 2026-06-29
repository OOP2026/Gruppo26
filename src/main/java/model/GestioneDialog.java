package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GestioneDialog extends JDialog {

    private JPanel mainPanel;
    private JTable tableRichieste;
    private JButton btnApprova;
    private JButton btnRifiuta;

    private Controller controller;


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