import controller.*;
import gui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();

        LoginFrame loginFrame=new LoginFrame(controller);
        loginFrame.setVisible(true);
    }
}