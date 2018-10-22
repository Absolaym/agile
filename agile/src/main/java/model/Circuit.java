package model;

import java.util.LinkedList;

public class Circuit {
	
	public static final int SPEED =  15;

    private String warehouseAddress = "";
    private Time departureTime = new Time();
    private String courierId = "";
    
    private LinkedList<Trip> 	trips;
    private LinkedList<Delivery> deliveries;
    
    public Circuit() {
    		this.trips = new LinkedList<Trip>();
    		this.deliveries = new LinkedList<Delivery>();
    }
    
	public LinkedList<Trip> getTrips() {
		return trips;
	}

	public LinkedList<Delivery> getDeliveries() {
		return deliveries;
	}

	public String getWarehouseAddress() {
		return warehouseAddress;
	}

	public void setWarehouseAddress(String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}

	public Time getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Time departureTime) {
		this.departureTime = departureTime;
	}

	public String getCourierId() {
		return courierId;
	}

	public void setCourierId(String courierId) {
		this.courierId = courierId;
	}

}