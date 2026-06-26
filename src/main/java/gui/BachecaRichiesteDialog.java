package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BachecaRichiesteDialog extends JDialog {

    // FIX CRITICO: rimossi i campi d'istanza ridondanti che venivano oscurati
    // dalle variabili locali nel costruttore (variable shadowing).
    // I componenti sono gestiti direttamente come variabili locali nel costruttore,
    // dato che non servono riferimenti a livello di classe.

    public BachecaRichiesteDialog(JFrame parent, Controller controller) {
        super(parent, "Bacheca Pubblica - Spostamenti e Vincoli", true);
        setSize(750, 450);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // FIX: usato this.lblInfo invece di ridichiarare la variabile localmente
        JLabel lblInfo = new JLabel("Bacheca informativa per gli Studenti: monitoraggio dei cambiamenti orari");
        lblInfo.setFont(new Font("Arial", Font.BOLD, 13));
        mainPanel.add(lblInfo, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab spostamenti
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

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // MIGLIORAMENTO: lambda invece di classe anonima
        JButton btnChiudi = new JButton("Chiudi");
        btnChiudi.addActionListener(e -> dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnChiudi);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }
}
