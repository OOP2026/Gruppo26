package gui;

import controller.Controller;
import model.Utente;

import javax.swing.*;

public class HomeFrame extends JFrame {
    private JPanel mainPanel;
    private JLabel lblBenvenuto;
    private JButton btnLogout;
    private JButton btnGestioneAule;
    private JTable tableOrario;

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

        btnGestioneAule.setVisible(controller.puoGestireAule());
        String[][] datiTabella = controller.getOrarioTabella();
        String[] colonneTabella = controller.getIntestazioniTabella();

        tableOrario.setModel(new javax.swing.table.DefaultTableModel(datiTabella, colonneTabella) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }


}
