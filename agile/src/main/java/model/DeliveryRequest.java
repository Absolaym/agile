/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.LinkedList;

import error.ErrorLogger;
import error.ProjectError;
import utils.Time;

/**
 * A DeliveryRequest is an object containing a list of deliveries to perform, 
 * a warehouse and a departure time
 */
public class DeliveryRequest {

    // While there is no bottleneck here, I do prefer to use LinkedLists over ArrayList
    // since we might have a bunch of addition/deletion to make. Though it's not that relevant
    private LinkedList<Delivery> deliveries;
    private String warehouseAddress = "";
    private Intersection warehouseIntersection;
    private Time departureTime = new Time();

    public DeliveryRequest() {
        this.deliveries = new LinkedList<>();
    }

    /**
     * Constructor for test purposes
     * @param deliveries
     * @param warehouseIntersection
     * @param time
     */
    public DeliveryRequest(LinkedList<Delivery> deliveries, Intersection warehouseIntersection, Time time) {
        this.deliveries = deliveries;
        this.warehouseIntersection = warehouseIntersection;
        this.departureTime = time;
    }

    /**
     *
     * @param delivery The delivery you want to add to the list of deliveries
     * @return this
     */
    public DeliveryRequest addDelivery(Delivery delivery) {
        this.deliveries.add(delivery);
        return this;
    }

    /**
     * Removes a delivery from the request
     * @param delivery The delivery you want to remove from the list
     * @return this
     */
    public boolean removeDelivery(Delivery delivery) {
        return this.deliveries.remove(delivery);
    }

    public LinkedList<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(LinkedList<Delivery> d) {
        this.deliveries = d;
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

    @Override
    public String toString() {
        String str = "";
        str += "Warehouse: " + this.warehouseAddress + " | ";
        str += "Departure time: " + this.getDepartureTime();

        str += "\t" + this.getDeliveries().size() + " deliveries";

        return str;
    }
    
    /**
     * Method matches the addresses of the deliveries in the request to intersections of the cityMap
     * in which it's supposed to be located. This sets the delivery's geolocation to be able to display it
     * @param cityMap the cityMap in which the delivery request is located
     */
    public void computeDeliveryRequestGeolocation(CityMap cityMap){
        
        for (Delivery delivery : this.deliveries) {
            Geolocation geolocation = cityMap.getIntersectionGeolocation(delivery.getAddress());
            if (geolocation == null) {
                ErrorLogger.getInstance().log(ProjectError.DEL_ADDRESS_NOT_IN_MAP);
                this.deliveries.remove(delivery);
                continue;
            }
            delivery.setGeolocation(geolocation);
        }

        this.warehouseIntersection = cityMap.getIntersectionById(this.warehouseAddress);

        if (this.warehouseIntersection == null) {
        		ErrorLogger.getInstance().log(ProjectError.WAR_ADDRESS_NOT_IN_MAP);
        }
    }

    public Intersection getWarehouseIntersection() {
        return warehouseIntersection;
    }

    public void setWarehouseIntersection(Intersection warehouseIntersection) {
        this.warehouseIntersection = warehouseIntersection;
    }

}
