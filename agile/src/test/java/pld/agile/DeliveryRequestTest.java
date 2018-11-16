package pld.agile;

import junit.framework.*;
import model.Delivery;
import model.DeliveryRequest;
import model.CityMap;
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
            "",
            "",
            "",
            "",
            "",
            ""
        };
        int i = 0;
        for (Delivery del : delReq.getDeliveries()) {
            assertEquals(strs[i++], del.getGeolocation().toString());
        }

    }

}
