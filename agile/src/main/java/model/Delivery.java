/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author olivi & Johnny
 */
public class Delivery {
	
	/**
	 * The id of the intersection corresponding to the address
	 */
	private String address = "";
	
	/**
	 * The time required to deliver the item to the customer (minutes)
	 */
	private int duration = 0;
    
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
	
	public String toString() {
		String str = "";
		
		str += "Delivery - Location: " + this.address + " \t| ";
		str += "Duration: " + new Time(this.duration * 60);
		
		return str;
	}
}
