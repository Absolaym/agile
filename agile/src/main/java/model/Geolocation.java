package model;

public class Geolocation {

    public final double EARTH_RADIUS = 6371.0;

    private double latitude;
    private double longitude;

    public Geolocation(double aLatitude, double aLongitude) {
        setLatitude(aLatitude);
        setLongitude(aLongitude);
    }
    
    public Geolocation(Geolocation cpy) {
    		 this(cpy.latitude,cpy.longitude);
    }
    
    public Geolocation copy() {
    		return new Geolocation(this);
    }

    public static Geolocation center(Geolocation lhs, Geolocation rhs) {
    		Geolocation center = new Geolocation(
    				(lhs.getLatitude() +rhs.getLatitude()) / 2,
    				(lhs.getLongitude() +rhs.getLongitude()) / 2);
    		return center;
    }
    
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double distance(Geolocation rhs) {
        double lat2 = DegToRad(rhs.latitude);
        double lat1 = DegToRad(this.latitude);
        double lon2 = DegToRad(rhs.longitude);
        double lon1 = DegToRad(this.longitude);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1; 
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(lat1) * Math.cos(lat2) * 
            Math.sin(dLon/2) * Math.sin(dLon/2); 
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
        double d = EARTH_RADIUS * c;
        return d;
    }

    public static double DegToRad(double deg) {
        return deg / 180 * Math.PI;
    }

    public static double RadToDeg(double rad) {
        return rad / Math.PI * 180;
    }

    public String toString() {
        return "[" + this.latitude + ":" + this.longitude + "]";
    }
}
