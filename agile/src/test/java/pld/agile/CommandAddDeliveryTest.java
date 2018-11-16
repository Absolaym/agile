package pld.agile;

import controller.CommandAddDelivery;
import controller.CommandsList;
import controller.Controller;
import junit.framework.*;
import model.Circuit;
import model.Delivery;
import model.DeliveryRequest;
import model.CityMap;
import model.Model;
import utils.XmlParser;

public class CommandAddDeliveryTest extends TestCase {

	// The three important things : 
	// - a test returns void, 
	// - it goes without params 
	// - it begins with "test" lowercase

	public void testExecute() throws Exception {
            
            Controller c = new Controller();
            CityMap cityMap = null;
            Model model = c.getModel();
            XmlParser xmlParser = new XmlParser();            
            cityMap = xmlParser.parseMap("src/main/assets/maps/petitPlan.xml");
            
            if (cityMap != null){
              model.setCityMap(cityMap);
              model.setDeliveryRequest(null);
              model.resetShortestPaths();
              model.setCircuits(null);
            }
            
            DeliveryRequest delReq = null;
            delReq = xmlParser.parseDeliveryRequest("src/main/assets/deliveries/dl-petit-6.xml");
            
            if(delReq!=null){
                delReq.computeDeliveryRequestGeolocation(model.getCityMap());
                model.setDeliveryRequest(delReq);
                model.computeShortestPaths();
                model.setCircuits(null);
            }
            
            model.computeCircuits();
//            Controller.setCommandsList(new CommandsList());
            
            Circuit circuit = model.getCircuits().getFirst();
            Delivery delivery = circuit.getDeliveries().getLast();           
            
//            Controller.commandsList.addCommand(new CommandAddDelivery(d, c));
            CommandAddDelivery cad = new CommandAddDelivery(delivery, circuit);
            cad.execute();
            
            assertEquals( "", circuit.toString() );
	}

}
