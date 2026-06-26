package gui;

import controller.Controller;
import model.Lezione;
import model.Utente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HomeFrame extends JFrame {

    private JPanel mainPanel;
    private JLabel lblBenvenuto;
    private JButton btnLogout;
    private JButton btnGestioneAule;
    private JTable tableOrario;
    private JButton btnRichiediSpostamento;
    private JButton btnVincolo;
    private JButton btnAvvisi;

    private Controller controller;

    public HomeFrame(Controller controller) {
        this.controller = controller;

        setContentPane(mainPanel);
        setTitle("Dashboard Università");
        setSize(750, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Utente utente = controller.getUtenteLoggato();
        lblBenvenuto.setText("Benvenuto " + utente.getNome() + " " + utente.getCognome()
                + " (" + utente.getClass().getSimpleName() + ")");

        // Menu bacheca
        JMenuBar menuBar = new JMenuBar();
        JMenu menuVisualizza = new JMenu("Visualizza");
        JMenuItem itemBacheca = new JMenuItem("Apri Bacheca Spostamenti e Vincoli");
        menuVisualizza.add(itemBacheca);
        menuBar.add(menuVisualizza);
        setJMenuBar(menuBar);

        // MIGLIORAMENTO: lambda invece di classe anonima
        itemBacheca.addActionListener(e -> {
            BachecaRichiesteDialog bacheca = new BachecaRichiesteDialog(HomeFrame.this, controller);
            bacheca.setVisible(true);
        });

        // Pulsante avvisi (solo per studenti)
        btnAvvisi.setVisible(utente instanceof model.Studente);
        btnAvvisi.addActionListener(e -> {
            String[] avvisi = controller.getVincoliApprovatiPerStudente();
            if (avvisi.length == 0) {
                JOptionPane.showMessageDialog(this, "Nessun avviso di indisponibilità docenti.");
            } else {
                JTextArea areaTesto = new JTextArea(10, 30);
                for (String s : avvisi) {
                    areaTesto.append(s + "\n");
                }
                areaTesto.setEditable(false);
                JOptionPane.showMessageDialog(this, new JScrollPane(areaTesto),
                        "Avvisi Indisponibilità Docenti", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Pulsante vincolo (solo per docenti e responsabili)
        boolean visibilePerDocenti = (utente instanceof model.Docente) || (utente instanceof model.Responsabile);
        btnVincolo.setVisible(visibilePerDocenti);
        btnVincolo.addActionListener(e -> {
            VincoloDialog dialog = new VincoloDialog(HomeFrame.this, controller);
            dialog.setVisible(true);
            aggiornaTabellaOrario();
        });

        // FIX WARN: aggiunto controller.logout() prima di tornare al LoginFrame,
        // così utentelogg viene azzerato e non rimane il riferimento all'utente precedente.
        btnLogout.addActionListener(e -> {
            controller.logout();
            dispose();
            LoginFrame login = new LoginFrame(controller);
            login.setVisible(true);
        });

        // Pulsante richiedi spostamento
        btnRichiediSpostamento.setVisible(controller.puoRichiedereSpostamento());
        btnRichiediSpostamento.addActionListener(e -> {
            int rigaSelezionata = tableOrario.getSelectedRow();
            if (rigaSelezionata == -1) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Attenzione: devi prima selezionare una lezione!",
                        "Errore",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Lezione lezioneDaSpostare = controller.getLezioneDaIndice(rigaSelezionata);

            if (utente instanceof model.Docente && !(utente instanceof model.Responsabile)) {
                String loginDocenteLezione = lezioneDaSpostare.getInsegnamento().getDocente().getLogin();
                if (!utente.getLogin().equals(loginDocenteLezione)) {
                    JOptionPane.showMessageDialog(mainPanel,
                            "Errore: Non puoi spostare le lezioni di un altro docente!",
                            "Accesso Negato",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            SpostamentoDialog dialog = new SpostamentoDialog(HomeFrame.this, controller, lezioneDaSpostare);
            dialog.setVisible(true);
            aggiornaTabellaOrario();
        });

        // Pulsante gestione aule (solo per responsabili)
        btnGestioneAule.setVisible(controller.puoGestireAule());
        // FIX WARN: GestioneDialog ora riceve HomeFrame.this come parent,
        // invece di essere istanziato orfano dal Controller.
        btnGestioneAule.addActionListener(e -> {
            GestioneDialog dialog = new GestioneDialog(HomeFrame.this, controller);
            dialog.setVisible(true);
            aggiornaTabellaOrario();
        });

        aggiornaTabellaOrario();
    }

    public void aggiornaTabellaOrario() {
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
