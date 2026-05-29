package gui;

import controller.Controller;
import model.Lezione;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SpostamentoDialog extends JDialog {
    private JPanel mainPanel;
    private JTextField txtData;
    private JTextField txtOraInizio;
    private JTextField txtOraFine;
    private JButton btnInvia;
    private JButton btnAnnulla;
    private JLabel lblInfoLezione;

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

        btnAnnulla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        btnInvia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }
}