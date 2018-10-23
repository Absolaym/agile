/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.LinkedList;

import model.Delivery;
import model.DeliveryRequest;
import model.Geolocation;
import model.CityMap;
import utils.XmlParser;

/**
 *
 * @author olivi
 */
public class Controller {
    
    private CityMap cityMap;
    private DeliveryRequest dr;
    
    public Controller() { 
        this.setCityMap(new CityMap());
    }
	
    public void LoadCityMap(String path) {
        //TO DO
        // This code is for testing purpose and by no means, should stay in place
    	try {
    		XmlParser xmlParser = new XmlParser();
    		//Plan cityMap = xmlParser.parseMap("src/main/assets/maps/grandPlan.xml");
    		CityMap cityMap = xmlParser.parseMap(path);
    		this.setCityMap( cityMap );
    	}catch(Exception e) {
    		//display exception in a pop up
    		//make specific error for reading exception
    	}
    		
        System.out.println("It has to load map.");
    }
    
    public DeliveryRequest loadDeliveryRequest(String path) {
        //TO DO
        System.out.println("It has to load delivery requests.");
        
        XmlParser parser = new XmlParser();
        DeliveryRequest dr = parser.parseDeliveryRequest(path);
        dr = setDeliveryRequestGeolocation(dr);
        this.dr = dr;
        return dr;
    }
    
    
    public void ComputeCircuits() {
        //TO DO
        System.out.println("It has to compute circuits.");
    }
    
    public CityMap getCityMap() {
        return cityMap;
    }

    public void setCityMap(CityMap cityMap) {
        this.cityMap = cityMap;
    }
    
    public DeliveryRequest getDeliveryRequest(){
        return this.dr;
    }
    //je sais pas ou mettre ca oups --MF
    private DeliveryRequest setDeliveryRequestGeolocation(DeliveryRequest dr){
        LinkedList<Delivery> deliveries = dr.getDeliveries();
        for (Delivery delivery : deliveries){
            Geolocation geolocation = cityMap.getIntersectionGeolocation(delivery.getAddress());
            if(geolocation == null){
            System.out.println("The address " + delivery.getAddress() + " was not found");
            deliveries.remove(delivery);
            continue;
            }
            delivery.setGeolocation(geolocation);
        }
        dr.setDeliveries(deliveries);
        return dr;
    }
}
