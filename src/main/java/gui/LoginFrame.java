package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginFrame extends JFrame {
    private JPanel MainPanel;
    private JTextField textUsername;
    private JPasswordField txtPassword;
    private JButton LOGINButton;
    private JCheckBox chkMostraPassword;


    private Controller controller;

    public LoginFrame(Controller controller) {
        this.controller = controller;

        setContentPane(MainPanel);
        setTitle("Login Università");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textUsername.getText();
                String password = new String(txtPassword.getPassword());
                boolean accessoconsentito = controller.verificaLogin(username, password);

                if (accessoconsentito) {
                    JOptionPane.showMessageDialog(MainPanel, "Accesso eseguito con successo!!");
                    dispose(); // Chiudi il login
                    HomeFrame home = new HomeFrame(controller);
                    home.setVisible(true); // Apri la home
                } else {
                    JOptionPane.showMessageDialog(MainPanel, "Credenziali non valide!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        chkMostraPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(chkMostraPassword.isSelected()) {
                    txtPassword.setEchoChar((char)0);
                } else {
                    txtPassword.setEchoChar('•');
                }
            }
        });
    }
}