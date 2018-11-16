package pld.agile;

import junit.framework.*;
import model.Delivery;
import model.DeliveryRequest;
import model.CityMap;
import utils.XmlParser;

public class ParserTest extends TestCase {

    // The three important things : 
    // - a test returns void, 
    // - it goes without params 
    // - it begins with "test" lowercase
    public void testParseDeliveryRequestMeta() throws Exception {
        XmlParser xmlParser = new XmlParser();

        DeliveryRequest delReq = xmlParser.parseDeliveryRequest("src/main/assets/deliveries/dl-petit-6.xml");
        assertEquals("Warehouse: 25611425 | Departure time: 08:00:00	6 deliveries", delReq.toString());
    }

    public void testParseDeliveryRequestDeliveries() throws Exception {
        XmlParser xmlParser = new XmlParser();

        DeliveryRequest delReq = xmlParser.parseDeliveryRequest("src/main/assets/deliveries/dl-petit-6.xml");
        String[] strs = {
            "Delivery - Location: 26079802 	| Duration: 00:03:00",
            "Delivery - Location: 26079653 	| Duration: 00:06:00",
            "Delivery - Location: 479185301 	| Duration: 00:01:00",
            "Delivery - Location: 3267479470 	| Duration: 00:02:00",
            "Delivery - Location: 26317293 	| Duration: 00:05:00",
            "Delivery - Location: 55474973 	| Duration: 00:03:00"
        };
        int i = 0;
        for (Delivery del : delReq.getDeliveries()) {
            assertEquals(strs[i++], del.toString());
        }
    }

    public void testParseMap() throws Exception {
        XmlParser xmlParser = new XmlParser();

        CityMap map = xmlParser.parseMap("src/test/assets/maps/petitPlan.xml");
        assertEquals("Plan : 308 intersections, 616 sections", map.getPlanInfos());
        assertEquals(map.getIntersections().containsKey("26079656"), true);
        assertEquals(map.getIntersections().containsKey("41"), false);

        map = xmlParser.parseMap("src/test/assets/maps/moyenPlan.xml");
        assertEquals("Plan : 1448 intersections, 3097 sections", map.getPlanInfos());
        assertEquals(map.getIntersections().containsKey("1440845047"), true);
        assertEquals(map.getIntersections().containsKey("41"), false);
    }

    public void testNoLatitude() throws Exception {
        XmlParser xmlParser = new XmlParser();
        CityMap map = null;
        try {
            map = xmlParser.parseMap("src/test/assets/maps/noLatitude.xml");
        } catch (Exception e) {
        }
        assertEquals(map, null);
    }

    public void testPositiveLength() throws Exception {
        XmlParser xmlParser = new XmlParser();
        CityMap map = xmlParser.parseMap("src/test/assets/maps/negativeLength.xml");
        assertEquals(map, null);
    }

}
