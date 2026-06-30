package gui;

import controller.Controller;
import javax.swing.*;

/**
 * Finestra di login dell'applicazione.
 * <p>
 * Permette all'utente di inserire le proprie credenziali (username e
 * password) e di effettuare l'accesso al sistema. In caso di successo
 * viene aperta la {@link HomeFrame}; in caso di errore viene mostrato
 * un messaggio di credenziali non valide.
 * <p>
 * È inoltre presente una checkbox che consente di mostrare/nascondere
 * la password digitata nel campo dedicato.
 */
public class LoginFrame extends JFrame {

    /** Pannello principale generato dal form grafico (GUI Designer). */
    private JPanel MainPanel;

    /** Campo di testo per l'inserimento dello username. */
    private JTextField textUsername;

    /** Campo per l'inserimento della password (con caratteri mascherati). */
    private JPasswordField txtPassword;

    /** Pulsante per avviare la procedura di login. */
    private JButton loginButton;

    /** Checkbox per mostrare in chiaro la password inserita. */
    private JCheckBox chkMostraPassword;

    /** Controller applicativo usato per la logica di autenticazione. */
    private Controller controller;

    /**
     * Costruisce la finestra di login.
     * <p>
     * Inizializza i componenti grafici e registra i listener per:
     * <ul>
     *     <li>il pulsante di login, che verifica le credenziali tramite
     *     {@link Controller#verificaLogin(String, String)} e, in caso di
     *     successo, apre la {@link HomeFrame};</li>
     *     <li>la checkbox "Mostra password", che alterna la visualizzazione
     *     in chiaro o mascherata del campo password.</li>
     * </ul>
     *
     * @param controller il controller applicativo da utilizzare per la logica di business
     */
    public LoginFrame(Controller controller) {
        this.controller = controller;

        setContentPane(MainPanel);
        setTitle("Login Università");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loginButton.addActionListener(e -> {
            String username = textUsername.getText();
            String password = new String(txtPassword.getPassword());
            boolean accessoConsentito = controller.verificaLogin(username, password);

            if (accessoConsentito) {
                JOptionPane.showMessageDialog(MainPanel, "Accesso eseguito con successo!");
                dispose();
                HomeFrame home = new HomeFrame(controller);
                home.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(MainPanel,
                        "Credenziali non valide!",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
            }
        });

        chkMostraPassword.addActionListener(e -> {
            if (chkMostraPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });
    }
}