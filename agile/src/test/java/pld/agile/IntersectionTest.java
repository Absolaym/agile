package pld.agile;

import junit.framework.*;
import model.Geolocation;
import model.Intersection;

public class IntersectionTest extends TestCase {

    // The three important things : 
    // - a test returns void, 
    // - it goes without params 
    // - it begins with "test" lowercase
    public void testSectionConstruction() throws Exception {

        double latitude1 = 45;
        double longitude1 = 80;
        Geolocation geolocation1 = new Geolocation(latitude1, longitude1);
        String id1 = "10";
        Intersection intersection = new Intersection(geolocation1, id1);

        System.out.println(intersection.toString());

        assertEquals("Intersection{geolocation=[45.0:80.0], id=10}", intersection.toString());
    }

}
