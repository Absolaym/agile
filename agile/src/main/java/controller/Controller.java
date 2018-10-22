/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.DeliveryRequest;
import model.Plan;
import model.XmlParser;

/**
 *
 * @author olivi
 */
public class Controller {
    
	private Plan plan;
	
	public Controller() { 
		this.setPlan(new Plan());
	}
	
	
	
    public void loadMap(String path) {
        //TO DO
    		// This code is for testing purpose and by no means, should stay in place
    	try {
    		XmlParser xmlParser = new XmlParser();
    		//Plan plan = xmlParser.parseMap("src/main/assets/maps/grandPlan.xml");
    		Plan plan = xmlParser.parseMap(path);
    		this.setPlan( plan );
    	}catch(Exception e) {
    		//display exception in a pop up
    		//make specific error for reading exception
    	}
    		
        System.out.println("It has to load map.");
    }
    
    public void loadDeliveryRequests(String path) {
        //TO DO
        System.out.println("It has to load delivery requests.");
        
        XmlParser parser = new XmlParser();
        DeliveryRequest dr = parser.parseDeliveryRequest(path);
    }
    
    public void computeCircuits() {
        //TO DO
        System.out.println("It has to compute circuits.");
    }
    

	public Plan getPlan() {
		return plan;
	}



	public void setPlan(Plan plan) {
		this.plan = plan;
	}
    
}
