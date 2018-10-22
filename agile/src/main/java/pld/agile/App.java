package pld.agile;
import controller.Controller;
import model.Delivery;
import model.DeliveryRequest;
import model.Plan;
import model.XmlParser;
import pld.agile.view.*;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        
        XmlParser xmlParser = new XmlParser();
        
        Plan plan = xmlParser.parseMap("src/main/assets/maps/petitPlan.xml");
        System.out.println( plan );
        DeliveryRequest delReq = xmlParser.parseDeliveryRequest("src/main/assets/deliveries/dl-petit-6.xml");
        System.out.println( delReq );
        
        for(Delivery del : delReq.getDeliveries()) {
        		System.out.println( del );
        }
        
        Controller c = new Controller();
        //Window window = new Window(m,c,dr);
    }
}
