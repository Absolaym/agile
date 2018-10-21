package model;

public class Geolocation {

	private double latitude;
	private double longitude;
	
	public Geolocation(double aLatitude, double aLongitude) {
		setLatitude(aLatitude);
		setLongitude(aLongitude);
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
}
