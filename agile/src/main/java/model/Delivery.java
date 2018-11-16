/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import utils.Time;

/**
 * This class represents a delivery, it can be observed by the interface
 *
 * @author olivia & Johnny
 */
public class Delivery {

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
    private Time duration = new Time();

    /**
     * The time the courier shall arrive at the delivery address
     */
    private Time arrivalTime = new Time();

    public Delivery() {
    }

    /**
     * @param address
     * @param geolocation
     */
    public Delivery(String address, Geolocation geolocation) {
        this.address = address;
        this.geolocation = geolocation;
    }

    public Delivery(Delivery d) {
        address = d.getAddress();
        geolocation = d.getGeolocation();
        circuit = d.getCircuit();
        isSelected = d.getIsSelected();
        duration = new Time(d.getDuration());
        arrivalTime = new Time(d.getArrivalTime());
    }

    /**
     * Get the id of the address
     *
     * @return
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Set the id of the address
     *
     * @warning the geolocation is not changed so please set the address wisely
     * to avoid corrupted data
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Get the time object representing the duration of the delivery
     *
     * @return
     */
    public Time getDuration() {
        return this.duration;
    }

    /**
     * Sets the duration of the delivery
     *
     * @param duration in seconds
     */
    public void setDuration(int duration) {
        this.duration.time = duration;
    }

    /**
     * Gets the arrival time as a time object
     *
     * @return
     */
    public Time getArrivalTime() {
        return this.arrivalTime;
    }

    /**
     * Sets the arrival time of the delivery
     *
     * @param time in seconds
     */
    public void setArrivalTime(int time) {
        this.arrivalTime.time = time;
    }

    /**
     * Get the geolocation of the delivery
     *
     * @return
     */
    public Geolocation getGeolocation() {
        return geolocation;
    }

    /**
     * Sets the geolocation of the delivery
     *
     * @warning be sure to keep this consistent with the address id
     * @param geolocation
     */
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
    }

    public String toString() {
        String str = "";

        str += "Delivery - Location: " + this.address + " \t| ";
        str += "Duration: " + this.duration;

        return str;
    }

}
