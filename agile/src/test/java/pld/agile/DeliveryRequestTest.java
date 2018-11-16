package pld.agile;

import java.util.LinkedList;
import junit.framework.*;
import model.Delivery;
import model.DeliveryRequest;
import model.CityMap;
import model.Geolocation;
import model.Intersection;
import utils.Time;
import utils.XmlParser;

public class DeliveryRequestTest extends TestCase {

    // The three important things : 
    // - a test returns void, 
    // - it goes without params 
    // - it begins with "test" lowercase
    
    public void testComputeDeliveryRequestGeolocation() throws Exception {
        XmlParser xmlParser = new XmlParser();
        CityMap map = xmlParser.parseMap("src/main/assets/maps/petitPlan.xml");

        DeliveryRequest delReq = xmlParser.parseDeliveryRequest("src/main/assets/deliveries/dl-petit-6.xml");
        delReq.computeDeliveryRequestGeolocation(map);

        String[] strs = {
            "[45.754997:4.8576107]",
            "[45.758255:4.865917]",
            "[45.750896:4.859119]",
            "[45.75974:4.872321]",
            "[45.752815:4.8769016]",
            "[45.76165:4.875242]"
        };
        
        int i = 0;
        for (Delivery del : delReq.getDeliveries()) {
            assertEquals(strs[i++], del.getGeolocation().toString());
        }

    }

}
