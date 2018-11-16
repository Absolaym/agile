package pld.agile;

import junit.framework.*;
import model.Geolocation;
import model.Intersection;
import model.Section;

public class SectionTest extends TestCase {

    // The three important things : 
    // - a test returns void, 
    // - it goes without params 
    // - it begins with "test" lowercase
    public void testSectionConstruction() throws Exception {

        String aStreetName = "rue machin";
        double aLength = 1000;

        double aLatitude1 = 45;
        double aLongitude1 = 80;
        Geolocation aGeolocation1 = new Geolocation(aLatitude1, aLongitude1);
        String anId1 = "10";
        Intersection aStartIntersection = new Intersection(aGeolocation1, anId1);

        double aLatitude2 = 60;
        double aLongitude2 = 70;
        Geolocation aGeolocation2 = new Geolocation(aLatitude2, aLongitude2);
        String anId2 = "20";
        Intersection anEndIntersection = new Intersection(aGeolocation2, anId2);

        Section section = new Section(aStreetName, aLength, aStartIntersection, anEndIntersection);

        assertEquals("Section{streetName=rue machin, length=1000.0, startIntersection=Intersection{geolocation=[45.0:80.0], id=10}, endIntersection=Intersection{geolocation=[60.0:70.0], id=20}}", section.toString());
    }

}
