package gui;

import controller.Controller;

/**
 * Classe di avvio (entry point) dell'applicazione.
 * <p>
 * Si occupa esclusivamente di istanziare il {@link Controller} principale
 * e di mostrare la prima finestra dell'interfaccia grafica, ovvero la
 * schermata di login ({@link LoginFrame}).
 */
public class Home {

    /**
     * Metodo main: punto di ingresso dell'applicazione.
     * <p>
     * Crea un nuovo {@link Controller} e rende visibile la finestra
     * di login affinché l'utente possa autenticarsi.
     *
     * @param args argomenti da riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        LoginFrame login = new LoginFrame(controller);
        login.setVisible(true);
    }
}
