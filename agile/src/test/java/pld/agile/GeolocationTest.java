package pld.agile;

import junit.framework.TestCase;
import model.Geolocation;

public class GeolocationTest extends TestCase {
	
	public void testDistanceBetweenSameGeolocation() throws Exception {
		Geolocation a = new Geolocation(45.75871, 4.8704023);
			assertEquals( a.distance(a), 0.0 );
	}
	
	public void testDistanceIsCommutative() throws Exception {
		Geolocation a = new Geolocation(45.75871, 4.8704023);
		Geolocation b = new Geolocation(45.75850, 4.8704078);
		double distanceAB = a.distance(b);
		double distanceBA = b.distance(a);
		assertEquals( distanceAB, distanceBA );
	}
	
	public void testDistanceBetweenGeolocations() throws Exception {
		Geolocation a = new Geolocation(45.7835119, 4.8707119);
		Geolocation b = new Geolocation(45.7842624, 4.8741744);
		double distanceAB = a.distance(b) * 1000;
		long approximateDistance = Math.round(distanceAB);
		assertEquals( approximateDistance, 281);
	}

}
