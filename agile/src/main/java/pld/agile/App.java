package pld.agile;
import controller.Controller;
import model.DeliveryRequest;
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
        Map m = new Map(null, null);
        Controller c = new Controller();
        DeliveryRequest dr = new DeliveryRequest();
        Window window = new Window(m,c,dr);
    }
}
