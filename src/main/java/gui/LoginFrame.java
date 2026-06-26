package gui;

import controller.Controller;
import javax.swing.*;

public class LoginFrame extends JFrame {

    private JPanel MainPanel;
    private JTextField textUsername;
    private JPasswordField txtPassword;
    private JButton loginButton;
    private JCheckBox chkMostraPassword;

    private Controller controller;

    public LoginFrame(Controller controller) {
        this.controller = controller;

        setContentPane(MainPanel);
        setTitle("Login Università");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // MIGLIORAMENTO: lambda invece di classe anonima ActionListener
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
                // MIGLIORAMENTO: pulizia del campo password dopo un tentativo fallito
                txtPassword.setText("");
            }
        });

        // MIGLIORAMENTO: lambda invece di classe anonima ActionListener
        chkMostraPassword.addActionListener(e -> {
            if (chkMostraPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar('•');
            }
        });
    }
}
