package model;

import java.util.LinkedList;
import utils.Time;

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

    public void setTrips(LinkedList<Trip> list){
        trips = list;
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

    @Override
    public String toString() {
        return "Circuit{" + "warehouseAddress=" + warehouseAddress + ", departureTime=" + departureTime + ", courierId=" + courierId + ", trips=" + trips + ", deliveries=" + deliveries + '}';
    }
        
        

}
