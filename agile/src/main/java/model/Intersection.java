package model;

/**
 * Class represents the intersection between two roads on a map
 */
public class Intersection {

    private Geolocation geolocation;
    private String id;

    public Intersection (Geolocation aGeolocation, String anId) {
        setGeolocation(aGeolocation);
        setId(anId);
    }


    public Geolocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Intersection{" + "geolocation=" + geolocation + ", id=" + id + '}';
    }
	
}
