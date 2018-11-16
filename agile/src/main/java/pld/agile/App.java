package pld.agile;

import controller.Controller;
import view.Window;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        Controller c = new Controller();
        Window window = new Window(c);
    }
}
