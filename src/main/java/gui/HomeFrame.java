package gui;

import controller.Controller;
import model.Responsabile;
import model.Utente;

import javax.swing.*;

public class HomeFrame extends JFrame {
    private JPanel mainPanel;
    private JLabel lblBenvenuto;
    private JButton btnLogout;
    private JButton btnGestioneAule;

    private Controller controller;


    public HomeFrame(Controller controller) {
        this.controller = controller;

        setContentPane(mainPanel);
        setTitle("Dashboard Università");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        Utente utente = controller.getUtenteLoggato();
        lblBenvenuto.setText("Benvenuto "+utente.getNome()+" "+utente.getCognome());

        boolean mostraBottone = controller.puoGestireAule();
        btnGestioneAule.setVisible(mostraBottone);
    }


}
