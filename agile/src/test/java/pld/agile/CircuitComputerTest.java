package pld.agile;

import java.util.HashMap;
import java.util.LinkedList;

import junit.framework.TestCase;
import model.Circuit;
import model.CityMap;
import model.DeliveryRequest;
import model.Trip;
import model.computers.CircuitComputer;
import model.computers.ShortestPathComputer;
import utils.XmlParser;

public class CircuitComputerTest extends TestCase {
	
	public void testClusterSizes(){
		XmlParser xmlParser = new XmlParser();
    CityMap map = xmlParser.parseMap("src/main/assets/maps/moyenPlan.xml");

    DeliveryRequest delReq = xmlParser.parseDeliveryRequest("src/main/assets/deliveries/dl-moyen-12.xml");
    delReq.computeDeliveryRequestGeolocation(map);
    
    ShortestPathComputer spc = new ShortestPathComputer();
    spc.init(map, delReq);
    spc.computeAllShortestPaths();
    HashMap<String, HashMap<String, Trip>> shortestPaths = spc.result();
    
    //trip length between two deliveries
    int numberOfCouriers = 2;
    int minDeliveries = 5;
    int maxDeliveries = 7;
    CircuitComputer cc = new CircuitComputer();
    cc.init(delReq, shortestPaths);
    cc.execute(numberOfCouriers);
    LinkedList<Circuit> circuits = cc.result();
    
    assertNotNull(circuits);
    assertEquals(circuits.size(),numberOfCouriers);
    
    for(Circuit circuit : circuits) {
    	 assertTrue(circuit.getDeliveries().size() >= minDeliveries );
    	 assertTrue(circuit.getDeliveries().size() <= maxDeliveries );

    }
    
    
	}

}


