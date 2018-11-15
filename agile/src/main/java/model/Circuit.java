package model;

import java.util.LinkedList;
import utils.Time;

public class Circuit {

    public static final int SPEED = 15;

    private String warehouseAddress = "";
    private Time departureTime = new Time();
    private int courierId;

    private LinkedList<Trip> trips;
    private LinkedList<Delivery> deliveries;

    public Circuit() {
        this.trips = new LinkedList<Trip>();
        this.deliveries = new LinkedList<Delivery>();
        courierId = 0;
    }
    
    public void updateSections() {
        if (trips == null) return;
        for(Trip t : trips){
            java.util.List<Section> sections = t.getSections();
            for (Section s : sections)
                s.setCircuit(this);
        }
    }

    public void addTripAndDelivery(Trip trip, Delivery delivery) {
        trips.add(trip);
        updateSections();
        deliveries.add(delivery);
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
        updateSections();
    }
    
    public void addTrip(int index, Trip trip) {
        trips.add(index, trip);
        updateSections();
    }

    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
    }
    
    public void addDelivery(int index, Delivery delivery) {
        deliveries.add(index, delivery);
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

    public int getCourierId() {
        return courierId;
    }

    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }
    
    public void updateDeliveryInfos(){
        int arrivalTimeSeconds = this.getDepartureTime().time;
        System.out.println(this.getDepartureTime());
        int i = 0;
        for(Delivery delivery : this.getDeliveries()){
            delivery.setCircuit(this);
            int tripDurationSeconds = (int)(this.getTrips().get(i).getLength() / (Circuit.SPEED / 3.6));
            arrivalTimeSeconds += tripDurationSeconds;
            delivery.setArrivalTime( arrivalTimeSeconds );
            arrivalTimeSeconds += delivery.getDuration().getSeconds();
            i++;
        }
    }

    @Override
    public String toString() {
        return "Circuit{" + "warehouseAddress=" + warehouseAddress + ", departureTime=" + departureTime + ", courierId=" + courierId + ", trips=" + trips + ", deliveries=" + deliveries + '}';
    }

}
