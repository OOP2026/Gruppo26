package gui;
import controller.Controller;

public class Home {
	public static void main(String[] args) {
		Controller controller = new Controller();

		LoginFrame login = new LoginFrame(controller);
		login.setVisible(true);
	}
}