package pld.agile;

import java.util.HashMap;

import junit.framework.TestCase;
import model.CityMap;
import model.DeliveryRequest;
import model.Trip;
import model.computers.ShortestPathComputer;
import utils.XmlParser;

public class ShortestPathComputerTest extends TestCase {
	
	public void testShortestPath(){
		XmlParser xmlParser = new XmlParser();
    CityMap map = xmlParser.parseMap("src/main/assets/maps/petitPlan.xml");

    DeliveryRequest delReq = xmlParser.parseDeliveryRequest("src/main/assets/deliveries/dl-petit-3.xml");
    delReq.computeDeliveryRequestGeolocation(map);
    
    ShortestPathComputer spc = new ShortestPathComputer();
    spc.init(map, delReq);
    spc.computeAllShortestPaths();
    HashMap<String, HashMap<String, Trip>> shortestPaths = spc.result();
    
    //trip length between two deliveries
    assertNotNull(shortestPaths);
    assertNotNull(shortestPaths.get("26317242"));
    Trip shortestPath = shortestPaths.get("26317242").get("2650077854");
    assertNotNull(shortestPath);
    assertEquals(1227, shortestPath.getLength());
    
    //trip length between the delivery and itself
    shortestPath = shortestPaths.get("26317242").get("26317242");
    assertNull(shortestPath);
    
	}

}
