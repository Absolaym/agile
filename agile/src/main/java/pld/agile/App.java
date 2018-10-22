package pld.agile;
import controller.Controller;
import model.Delivery;
import model.DeliveryRequest;
import model.Plan;
import utils.XmlParser;
import pld.agile.view.*;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        
        Controller c = new Controller();
        c.loadMap("");
        
        Window window = new Window( c );
    }
}
