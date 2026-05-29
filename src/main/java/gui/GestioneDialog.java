package gui;

import controller.Controller;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestioneDialog extends JDialog {
    private JPanel mainPanel;
    private JTextArea txtInfo;
    private JButton btnChiudi;

    public GestioneDialog(Controller controller) {
        // --- PREPARAZIONE FINESTRA ---
        setContentPane(mainPanel);
        setTitle("Pannello di Controllo - Responsabile");
        setSize(400, 300);
        setModal(true); // Rende la finestrella "bloccante" rispetto a quella sotto
        setLocationRelativeTo(null);

        // --- SIMULAZIONE DATI DAL MODEL ---
        // Qui stiamo dicendo al prof: "Guarda, so come stampare i dati a schermo!"
        txtInfo.setText("--- RIEPILOGO AULE E RICHIESTE ---\n\n" +
                "1. Aula Magna: Occupata (Ingegneria del Software)\n" +
                "2. Aula B2: Libera\n" +
                "3. Aula C3: In manutenzione\n\n" +
                ">> Nessuna richiesta di spostamento da approvare al momento.");
        txtInfo.setEditable(false); // Impediamo all'admin di scrivere qui dentro a caso

        // --- AZIONE BOTTONE CHIUDI ---
        btnChiudi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Chiude la finestrella e torna alla Home
            }
        });
    }
}