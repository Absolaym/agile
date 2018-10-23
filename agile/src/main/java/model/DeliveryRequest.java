/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.LinkedList;
import utils.Time;

/**
 *
 * @author olivi & Johnny
 */
public class DeliveryRequest {
    
	// While there is no bottleneck here, I do prefer to use LinkedLists over ArrayList
	// since we might have a bunch of addition/deletion to make. Though it's not that relevant

    private LinkedList<Delivery> deliveries;
    private String warehouseAddress = "";
    private Time departureTime = new Time();

    public DeliveryRequest() {
        this.deliveries = new LinkedList<Delivery>();
    }
    
    /**
     * 
     * @param delivery The delivery you want to add to the list of deliveries
     * @return this
     */
    public DeliveryRequest addDelivery(Delivery delivery) {
        this.deliveries.add( delivery );
        return this;
    }
    
    /**
     * 
     * @param delivery The delivery you want to remove from the list
     * @return this
     */
    public DeliveryRequest removeDelivery(Delivery delivery) {
        this.deliveries.remove(delivery);
        return this;
    }
    
    public LinkedList<Delivery> getDeliveries() {
        return deliveries;
    }
    
    public void setDeliveries(LinkedList<Delivery> d){
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

    public String toString() {
        String str = "";
        str += "Warehouse: " + this.warehouseAddress + " | ";
        str += "Departure time: " + this.getDepartureTime();

        str += "\t" + this.getDeliveries().size() + " deliveries";

        return str;
    }
    
}

