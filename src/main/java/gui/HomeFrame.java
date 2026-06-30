package gui;

import controller.Controller;
import model.Lezione;
import model.Utente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Finestra principale (dashboard) dell'applicazione, mostrata dopo il login.
 * <p>
 * Visualizza l'orario delle lezioni dell'utente autenticato e fornisce
 * accesso, in base al ruolo dell'utente ({@code Studente}, {@code Docente},
 * {@code Responsabile}), alle varie funzionalità dell'applicazione:
 * <ul>
 *     <li>richiesta di spostamento di una lezione;</li>
 *     <li>inserimento/gestione dei vincoli di indisponibilità dei docenti;</li>
 *     <li>gestione amministrativa delle richieste (solo per i responsabili);</li>
 *     <li>visualizzazione della bacheca pubblica di spostamenti e vincoli;</li>
 *     <li>visualizzazione degli avvisi di indisponibilità docenti (solo per studenti).</li>
 * </ul>
 */
public class HomeFrame extends JFrame {

    /** Pannello principale generato dal form grafico (GUI Designer). */
    private JPanel mainPanel;

    /** Etichetta di benvenuto con nome, cognome e ruolo dell'utente loggato. */
    private JLabel lblBenvenuto;

    /** Pulsante per effettuare il logout e tornare alla schermata di login. */
    private JButton btnLogout;

    /** Pulsante per accedere al pannello di gestione delle richieste (solo responsabili). */
    private JButton btnGestioneAule;

    /** Tabella che mostra l'orario delle lezioni dell'utente. */
    private JTable tableOrario;

    /** Pulsante per richiedere lo spostamento della lezione selezionata. */
    private JButton btnRichiediSpostamento;

    /** Pulsante per aprire la finestra di gestione dei vincoli (docenti/responsabili). */
    private JButton btnVincolo;

    /** Pulsante per visualizzare gli avvisi di indisponibilità docenti (solo studenti). */
    private JButton btnAvvisi;

    /** Controller applicativo usato per recuperare dati e invocare la logica di business. */
    private Controller controller;

    /**
     * Costruisce la dashboard principale per l'utente attualmente autenticato.
     * <p>
     * Configura il titolo, le dimensioni della finestra e il messaggio di
     * benvenuto, costruisce il menu "Visualizza" per l'apertura della bacheca
     * pubblica, e mostra/nasconde i pulsanti funzionali in base al ruolo
     * dell'utente loggato (Studente, Docente, Responsabile). Registra inoltre
     * tutti i listener necessari alla navigazione verso le altre finestre
     * dell'applicazione e aggiorna la tabella dell'orario all'avvio.
     *
     * @param controller il controller applicativo da utilizzare per la logica di business
     */
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
        btnGestioneAule.addActionListener(e -> {
            GestioneDialog dialog = new GestioneDialog(HomeFrame.this, controller);
            dialog.setVisible(true);
            aggiornaTabellaOrario();
        });

        aggiornaTabellaOrario();
    }

    /**
     * Aggiorna la tabella dell'orario delle lezioni mostrata nella dashboard.
     * <p>
     * Recupera dal {@link Controller} i dati e le intestazioni aggiornate
     * dell'orario e li imposta come nuovo modello della tabella
     * {@code tableOrario}, rendendo le celle non editabili dall'utente.
     */
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