import controller.*;
import gui.LoginFrame;

/**
 * Classe principale dell'applicazione.
 * <p>
 * Contiene il metodo {@code main}, punto di ingresso del programma,
 * che si occupa di istanziare il {@link Controller} e di avviare
 * l'interfaccia grafica mostrando la finestra di login ({@link LoginFrame}).
 * </p>
 *
 * @author
 * @version 1.0
 */
public class Main {

    /**
     * Metodo principale (entry point) dell'applicazione.
     * <p>
     * Crea un'istanza del {@link Controller}, la passa alla
     * {@link LoginFrame} e rende visibile la finestra di login,
     * avviando così l'interazione con l'utente.
     * </p>
     *
     * @param args argomenti passati da linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
        // Crea il controller che gestisce la logica applicativa
        Controller controller = new Controller();

        // Crea la finestra di login passando il controller
        LoginFrame loginFrame = new LoginFrame(controller);

        // Rende visibile la finestra di login
        loginFrame.setVisible(true);
    }
}