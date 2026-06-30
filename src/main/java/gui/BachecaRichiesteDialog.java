package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Finestra di dialogo che rappresenta la bacheca pubblica informativa,
 * consultabile in particolare dagli studenti, per monitorare i cambiamenti
 * relativi all'orario delle lezioni.
 * <p>
 * La finestra organizza le informazioni in due schede ({@link JTabbedPane}):
 * <ul>
 *     <li>"Spostamenti Richiesti": elenco delle richieste di spostamento
 *     delle lezioni;</li>
 *     <li>"Vincoli e Indisponibilità Docenti": elenco dei vincoli di
 *     indisponibilità inseriti dai docenti.</li>
 * </ul>
 * Entrambe le tabelle sono di sola lettura.
 */
public class BachecaRichiesteDialog extends JDialog {

    /** Pannello principale del contenuto della finestra di dialogo. */
    private JPanel contentPane;

    /** Etichetta informativa mostrata nella parte superiore della finestra. */
    private JLabel lblInfo;

    /** Componente a schede che organizza le tabelle di spostamenti e vincoli. */
    private JTabbedPane tabbedPane;

    /** Tabella di sola lettura con l'elenco degli spostamenti richiesti. */
    private JTable tableSpostamenti;

    /** Tabella di sola lettura con l'elenco dei vincoli e delle indisponibilità dei docenti. */
    private JTable tableVincoli;

    /** Pulsante per chiudere la finestra di dialogo. */
    private JButton btnChiudi;

    /**
     * Costruisce la finestra di dialogo della bacheca pubblica.
     * <p>
     * Costruisce dinamicamente l'interfaccia (non basata su un layout
     * predefinito del GUI Designer) creando il pannello principale, le due
     * tabelle di sola lettura per spostamenti e vincoli (popolate tramite
     * {@link Controller#getRichiesteTabellaPubblica()} e
     * {@link Controller#getVincoliTabellaPubblica()}) e il pulsante di
     * chiusura.
     *
     * @param parent     la finestra principale da cui viene aperto il dialogo
     * @param controller il controller applicativo da cui recuperare i dati da visualizzare
     */
    public BachecaRichiesteDialog(JFrame parent, Controller controller) {
        super(parent, "Bacheca Pubblica - Spostamenti e Vincoli", true);
        setSize(750, 450);
        setLocationRelativeTo(parent);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblInfo = new JLabel("Bacheca informativa per gli Studenti: monitoraggio dei cambiamenti orari");
        lblInfo.setFont(new Font("Arial", Font.BOLD, 13));
        contentPane.add(lblInfo, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panelSpostamenti = new JPanel(new BorderLayout());
        String[] colonneSpostamenti = controller.getIntestazioniRichieste();
        String[][] datiSpostamenti = controller.getRichiesteTabellaPubblica();
        JTable tableSpostamenti = new JTable(new DefaultTableModel(datiSpostamenti, colonneSpostamenti) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        panelSpostamenti.add(new JScrollPane(tableSpostamenti), BorderLayout.CENTER);
        tabbedPane.addTab("Spostamenti Richiesti", panelSpostamenti);

        // Tab vincoli
        JPanel panelVincoli = new JPanel(new BorderLayout());
        String[] colonneVincoli = controller.getIntestazioniVincoli();
        String[][] datiVincoli = controller.getVincoliTabellaPubblica();
        JTable tableVincoli = new JTable(new DefaultTableModel(datiVincoli, colonneVincoli) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        panelVincoli.add(new JScrollPane(tableVincoli), BorderLayout.CENTER);
        tabbedPane.addTab("Vincoli e Indisponibilità Docenti", panelVincoli);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        JButton btnChiudi = new JButton("Chiudi");
        btnChiudi.addActionListener(e -> dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnChiudi);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(contentPane);
    }
}