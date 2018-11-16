package pld.agile;

import junit.framework.*;
import model.Delivery;
import model.DeliveryRequest;
import model.CityMap;
import model.Geolocation;
import model.Intersection;
import model.Section;
import model.Vector2D;
import utils.XmlParser;

public class Vector2DTest extends TestCase {

    // The three important things : 
    // - a test returns void, 
    // - it goes without params 
    // - it begins with "test" lowercase
    public void testVector2DConstruction() throws Exception {

        double x = 4;
        double y = 5;

        Vector2D vect = new Vector2D(x, y);

        assertEquals("[4.0:5.0]", vect.toString());
    }

    public void testVectorNormalization() throws Exception {

        Vector2D vect = new Vector2D(4.5, 43.09);

        vect.normalize();

        assertEquals("[0.0:1.0]", vect.toString());
    }

}
