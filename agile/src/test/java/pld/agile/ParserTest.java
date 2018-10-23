package pld.agile;

import junit.framework.*;
import model.Delivery;
import model.DeliveryRequest;
import model.Plan;
import utils.XmlParser;

public class ParserTest extends TestCase {
	
	// The three important things : 
	// - a test returns void, 
	// - it goes without params 
	// - it begins with "test" lowercase
	
	public void testParseDeliveryRequestMeta() throws Exception {
            XmlParser xmlParser = new XmlParser();

            DeliveryRequest delReq = xmlParser.parseDeliveryRequest("src/main/assets/deliveries/dl-petit-6.xml");
            assertEquals( "Warehouse: 25611425 | Departure time: 8:0:0	6 deliveries", delReq.toString() );
       
	}
	
	public void testParseDeliveryRequestDeliveries() throws Exception {
            XmlParser xmlParser = new XmlParser();

            DeliveryRequest delReq = xmlParser.parseDeliveryRequest("src/main/assets/deliveries/dl-petit-6.xml");
            String[] strs = {
                    "Delivery - Location: 26079802 	| Duration: 3:0:0",
                    "Delivery - Location: 26079653 	| Duration: 6:0:0",
                    "Delivery - Location: 479185301 	| Duration: 1:0:0",
                    "Delivery - Location: 3267479470 	| Duration: 2:0:0",
                    "Delivery - Location: 26317293 	| Duration: 5:0:0",
                    "Delivery - Location: 55474973 	| Duration: 3:0:0" 
            };
            int i = 0;
            for(Delivery del : delReq.getDeliveries()) {
                            assertEquals( strs[i++], del.toString() );
                }

        }
        
        public void testParseMap() throws Exception {
            XmlParser xmlParser = new XmlParser();

            Plan map = xmlParser.parseMap("src/main/assets/maps/petitPlan.xml");
            assertEquals( "", map.toString() );
       
	}
	
}
