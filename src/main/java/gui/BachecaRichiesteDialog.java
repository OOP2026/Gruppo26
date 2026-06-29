package gui;

import controller.Controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BachecaRichiesteDialog extends JDialog {

    private JPanel contentPane;
    private JLabel lblInfo;
    private JTabbedPane tabbedPane;
    private JTable tableSpostamenti;
    private JTable tableVincoli;
    private JButton btnChiudi;

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

        // MIGLIORAMENTO: lambda invece di classe anonima
        JButton btnChiudi = new JButton("Chiudi");
        btnChiudi.addActionListener(e -> dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnChiudi);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(contentPane);
    }
}
