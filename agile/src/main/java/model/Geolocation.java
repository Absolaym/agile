package model;

/**
 * This class represents a point in the globe with its longitude and latitude
 */
public class Geolocation {

    public static final double EARTH_RADIUS = 6371.0;

    private double latitude;
    private double longitude;

    public Geolocation(double aLatitude, double aLongitude) {
        setLatitude(aLatitude);
        setLongitude(aLongitude);
    }

    public Geolocation(Geolocation cpy) {
        this(cpy.latitude, cpy.longitude);
    }

    public Geolocation copy() {
        return new Geolocation(this);
    }

    /**
     * Computes a Geolocation located in the middle point of two Geolocations
     * @param lhs one Geolocation
     * @param rhs other Geolocation
     * @return center Geolocation
     */
    public static Geolocation center(Geolocation lhs, Geolocation rhs) {
        Geolocation center = new Geolocation(
            (lhs.getLatitude() + rhs.getLatitude()) / 2,
            (lhs.getLongitude() + rhs.getLongitude()) / 2);
        return center;
    }
    
    /**
     * Asserts if two Geolocations have the same coordinates
     * @param rhs the Geolocation to compare to this
     * @return true if they Geolocations have the same coordinates
     */
    public boolean equals(Geolocation rhs) {
        return rhs != null && (this.latitude == rhs.latitude && this.longitude == rhs.longitude);
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
    
    /**
     * Computes the distance between two points given their coordinates
     * @param lat1 latitude of point A
     * @param lon1 longitude of point A
     * @param lat2 latitude of point B
     * @param lon2 longitude of point B
     * @return the distance in km
     */

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        lat2 = DegToRad(lat2);
        lat1 = DegToRad(lat1);
        lon2 = DegToRad(lon2);
        lon1 = DegToRad(lon1);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(lat1) * Math.cos(lat2)
            * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = EARTH_RADIUS * c;
        return d;
    }

    /**
     * Computes the distance between this and another Geolocation
     * @param rhs the other Geolocation
     * @return the distance in km
     */
    public double distance(Geolocation rhs) {
        return distance(this.latitude, this.longitude, rhs.latitude, rhs.longitude);
    }

    /**
     * Turns degrees to radians
     * @param deg the angle in degrees
     * @return the angle in radians
     */
    public static double DegToRad(double deg) {
        return deg / 180 * Math.PI;
    }

    /**
     * Turns radians to degrees
     * @param rad the angle in radians
     * @return the angle in degrees
     */
    public static double RadToDeg(double rad) {
        return rad / Math.PI * 180;
    }

    public String toString() {
        return "[" + this.latitude + ":" + this.longitude + "]";
    }
}
