package gui;

import controller.Controller;
import model.Lezione;
import model.Utente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class HomeFrame extends JFrame {
    private JPanel mainPanel;
    private JLabel lblBenvenuto;
    private JButton btnLogout;
    private JButton btnGestioneAule;
    private JTable tableOrario;
    private JButton btnRichiediSpostamento;

    private Controller controller;


    public HomeFrame(Controller controller) {
        this.controller = controller;

        setContentPane(mainPanel);
        setTitle("Dashboard Università");
        setSize(700, 500); // L'ho ingrandito un po' per far entrare bene la tabella
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Utente utente = controller.getUtenteLoggato();
        lblBenvenuto.setText("Benvenuto " + utente.getNome() + " " + utente.getCognome());

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginFrame login = new LoginFrame(controller);
                login.setVisible(true);
            }
        });

        btnRichiediSpostamento.setVisible(controller.puoRichiedereSpostamento());
        btnRichiediSpostamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = tableOrario.getSelectedRow();
                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(mainPanel,
                            "Attenzione: devi prima selezionare una lezione dalla tabella!",
                            "Errore di selezione",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Lezione lezioneDaSpostare = controller.getLezioneDaIndice(rigaSelezionata);

                SpostamentoDialog dialog = new SpostamentoDialog(HomeFrame.this, controller, lezioneDaSpostare);
                dialog.setVisible(true); // Questa riga blocca la Home finché la dialog non viene chiusa
            }
        });

        btnGestioneAule.setVisible(controller.puoGestireAule());
        String[][] datiTabella = controller.getOrarioTabella();
        String[] colonneTabella = controller.getIntestazioniTabella();

        tableOrario.setModel(new DefaultTableModel(datiTabella, colonneTabella) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }



}
