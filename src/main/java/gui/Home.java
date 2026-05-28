package gui;
import controller.Controller;

public class Home {
	public static void main(String[] args) {
		// 1. Creo l'unico controller del sistema (che carica i dati fittizi)
		Controller controller = new Controller();

		// 2. Avvio la prima schermata passandole il controller
		LoginFrame login = new LoginFrame(controller);
		login.setVisible(true);
	}
}