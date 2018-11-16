package model;

import java.util.LinkedList;
import utils.Time;

/**
 * This class represents a circuit composed by deliveries and trips between them
 * @author johnny
 *
 */
public class Circuit {

		/**
		 * The average speed of a courier on a trip in km/h
		 */
    public static final int SPEED = 15;

    private Time departureTime = new Time();
    private int courierId;

    private LinkedList<Trip> trips;
    private LinkedList<Delivery> deliveries;

    /**
     * Create a circuit
     */
    public Circuit() {
        this.trips = new LinkedList<>();
        this.deliveries = new LinkedList<>();
        courierId = 0;
    }
    
    public Circuit(Circuit c){
        departureTime = new Time(c.getDepartureTime());
        courierId = c.getCourierId();
        
        trips = new LinkedList<>();
        for(Trip t : c.getTrips()){
            trips.add(new Trip(t));        
        }
        
        deliveries = new LinkedList<>();
        for(Delivery d : c.getDeliveries()){
            deliveries.add(new Delivery(d));            
        }
    }
    
    /**
     * A section knows to which circuit it belongs
     * This method makes sure the section belongs to the right circuit
     */
    public void updateSections() {

        for(Trip t : trips){
            java.util.List<Section> sections = t.getSections();
            for (Section s : sections)
                s.setCircuit(this);
        }
    }

    /**
     * Adds a trip to the end of the circuit
     * @param trip
     */
    public void addTrip(Trip trip) {
        trips.add(trip);
        updateSections();
    }
    
    /**
     * Adds a trip to a specific index in circuit
     * @param index
     * @param trip
     */
    public void addTrip(int index, Trip trip) {
        trips.add(index, trip);
        updateSections();
    }

    /**
     * Adds a delivery stop to the circuit
     * @param delivery
     */
    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
    }
    
    /**
     * Adds a delivery to a specific index of the circuit
     * @param index
     * @param delivery
     */
    public void addDelivery(int index, Delivery delivery) {
        deliveries.add(index, delivery);
    }

    /**
     * Returns the trips in the circuit
     * @return a list of the trips in the circuit
     */
    public LinkedList<Trip> getTrips() {
        return trips;
    }

    /**
     * Returns the deliveries in the circuit
     * @return a list of the deliveries in the circuit
     */
    public LinkedList<Delivery> getDeliveries() {
        return deliveries;
    }

    /**
     * Returns the departure time of the circuit
     * @return the departure time from the warehouse
     */
    public Time getDepartureTime() {
        return departureTime;
    }

    /**
     * Sets the departure time of the circuit
     * @param departureTime
     */
    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * Gets the id of the courier. It's just a number
     * @return the id of the courier
     */
    public int getCourierId() {
        return courierId;
    }

    /**
     * Gets the id of the courier. It's just a number
     * @param courierId the number of the courier
     */
    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }
    
    /**
     * Updates the departure and arrival time for each delivery
     */
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
        return "Circuit{" + "departureTime=" + departureTime + ", courierId=" + courierId + ", trips=" + trips + ", deliveries=" + deliveries + '}';
    }

}
