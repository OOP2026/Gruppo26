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

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblInfo = new JLabel("Bacheca informativa per gli Studenti: monitoraggio dei cambiamenti orari");
        lblInfo.setFont(new Font("Arial", Font.BOLD, 13));
        mainPanel.add(lblInfo, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();

        JPanel panelSpostamenti = new JPanel(new BorderLayout());
        String[] colonneSpostamenti = controller.getIntestazioniRichieste();
        String[][] datiSpostamenti = controller.getRichiesteTabellaPubblica();
        tableSpostamenti = new JTable(new DefaultTableModel(datiSpostamenti, colonneSpostamenti) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        panelSpostamenti.add(new JScrollPane(tableSpostamenti), BorderLayout.CENTER);
        tabbedPane.addTab("Spostamenti Richiesti", panelSpostamenti);

        JPanel panelVincoli = new JPanel(new BorderLayout());
        String[] colonneVincoli = controller.getIntestazioniVincoli();
        String[][] datiVincoli = controller.getVincoliTabellaPubblica();
        tableVincoli = new JTable(new DefaultTableModel(datiVincoli, colonneVincoli) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        panelVincoli.add(new JScrollPane(tableVincoli), BorderLayout.CENTER);
        tabbedPane.addTab("Vincoli e IndisponibilitÃ  Docenti", panelVincoli);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        btnChiudi = new JButton("Chiudi");
        btnChiudi.addActionListener(e -> dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnChiudi);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }
}