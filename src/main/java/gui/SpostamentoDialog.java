package gui;

import controller.Controller;
import model.Lezione;
import javax.swing.*;

public class SpostamentoDialog extends JDialog {

    private JPanel mainPanel;
    private JTextField txtData;
    private JTextField txtOraInizio;
    private JTextField txtOraFine;
    private JButton btnInvia;
    private JButton btnAnnulla;
    private JLabel lblInfoLezione;
    private JLabel lblOraFine;
    private JLabel lblOraInizio;
    private JLabel lblData;

    private Controller controller;
    private Lezione lezioneSelezionata;

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
