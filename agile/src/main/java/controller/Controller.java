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
    
    private CityMap plan;

    public Controller() { 
        this.setPlan(new CityMap());
    }
	
	
	
    public void LoadCityMap(String path) {
        //TO DO
        // This code is for testing purpose and by no means, should stay in place
    	try {
    		XmlParser xmlParser = new XmlParser();
    		//Plan plan = xmlParser.parseMap("src/main/assets/maps/grandPlan.xml");
    		CityMap plan = xmlParser.parseMap(path);
    		this.setPlan( plan );
    	}catch(Exception e) {
    		//display exception in a pop up
    		//make specific error for reading exception
    	}
    		
        System.out.println("It has to load map.");
    }
    
    public DeliveryRequest LoadDeliveryRequest(String path) {
        //TO DO
        System.out.println("It has to load delivery requests.");
        
        XmlParser parser = new XmlParser();
        DeliveryRequest dr = parser.parseDeliveryRequest(path);
        dr = setDeliveryRequestGeolocation(dr);
        return dr;
    }
    
    
    public void ComputeCircuits() {
        //TO DO
        System.out.println("It has to compute circuits.");
    }
    

    public CityMap getPlan() {
        return plan;
    }



    public void setPlan(CityMap plan) {
        this.plan = plan;
    }
    
	
    //je sais pas ou mettre ca oups --MF
    private DeliveryRequest setDeliveryRequestGeolocation(DeliveryRequest dr){
        LinkedList<Delivery> deliveries = dr.getDeliveries();
        for (Delivery delivery : deliveries){
            Geolocation geolocation = plan.getIntersectionGeolocation(delivery.getAddress());
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
