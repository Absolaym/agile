/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Observable;
import utils.Time;

/**
 *
 * @author olivia & Johnny
 */
public class Delivery extends Observable {
    /**
     * The id of the intersection corresponding to the address
     */
    private String address = "";
    private Geolocation geolocation;
    private Circuit circuit = null; 
    private boolean isSelected = false;

    /**
     * The time required to deliver the item to the customer (minutes)
     */
    private int duration = 0;
    
    /**
     * The time the courier shall arrive at the delivery address
     */
    private Time arrivalTime = new Time();

    public Delivery() {

    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public Time getArrivalTime() {
        return this.arrivalTime;
    }

    public void setArrivalTime(Time time) {
        this.arrivalTime = time;
    }

    public String toString() {
        String str = "";

        str += "Delivery - Location: " + this.address + " \t| ";
        str += "Duration: " + new Time(this.duration * 60);

        return str;
    }

    public Geolocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }
    
    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
        setChanged();
        notifyObservers(isSelected);
    }
    
    
}
