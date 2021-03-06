package pld.agile;

import controller.CommandAddDelivery;
import controller.CommandMoveDeliveryAfter;
import controller.Controller;
import junit.framework.*;
import static junit.framework.Assert.assertEquals;
import model.Circuit;
import model.Delivery;
import model.DeliveryRequest;
import model.CityMap;
import model.Model;
import utils.XmlParser;

public class CommandMoveDeliveryAfterTest extends TestCase {

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

        if (cityMap != null) {
            model.setCityMap(cityMap);
            model.setDeliveryRequest(null);
            model.resetShortestPaths();
            model.setCircuits(null);
        }

        DeliveryRequest delReq = null;
        delReq = xmlParser.parseDeliveryRequest("src/main/assets/deliveries/dl-petit-6.xml");

        if (delReq != null) {
            delReq.computeDeliveryRequestGeolocation(model.getCityMap());
            model.setDeliveryRequest(delReq);
            model.computeShortestPaths();
            model.setCircuits(null);
        }

        model.setNumberOfCouriers(1);
        model.computeCircuits();

        Circuit circuit = model.getCircuits().getFirst();
        Circuit circuitBeforeMoveAfter = new Circuit(circuit);
        
        Delivery delivery = circuit.getDeliveries().getFirst();

        CommandMoveDeliveryAfter cmda = new CommandMoveDeliveryAfter(delivery, circuit);
        cmda.execute();

        assertEquals(circuitBeforeMoveAfter.getDeliveries().getFirst().toString(), model.getCircuits().getFirst().getDeliveries().get(1).toString());;
    }

}
