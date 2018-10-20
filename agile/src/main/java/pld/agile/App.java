package pld.agile;
import controller.Controller;
import model.Map;
import pld.agile.view.*;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Map m = new Map();
        Controller c = new Controller();
        Window window = new Window(m,c);
    }
}
