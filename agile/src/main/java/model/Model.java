/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashMap;
import java.util.LinkedList;

import utils.CircuitComputer;
import utils.ShortestPathComputer;

/**
 *
 * @author Lucie
 */
public class Model {
    
    private CityMap cityMap;
    private DeliveryRequest deliveryRequest;
	private HashMap<String,HashMap<String,Trip>> shortestPaths;		
    private LinkedList<Circuit> circuits;
    private int numberOfCouriers;
    
    private static Model INSTANCE = null;
    
    private Model(){
//        deliveryRequest = new DeliveryRequest();
//        circuits = new LinkedList<Circuit>();
        cityMap = new CityMap();
        deliveryRequest = null;
        circuits = null;
        numberOfCouriers = -1;
    }
    
    public static Model getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Model();
        }
        return INSTANCE;
    }
    
    public LinkedList<Circuit> getCircuits() {
        return circuits;
    }
    
    public void setCircuits(LinkedList<Circuit> circuits) {
        this.circuits = circuits;
    }
    public CityMap getCityMap() {
        return cityMap;
    }

    public void setCityMap(CityMap cityMap) {
        this.cityMap = cityMap;
    }

    public DeliveryRequest getDeliveryRequest() {
        return deliveryRequest;
    }

    public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
        this.deliveryRequest = deliveryRequest;
    }
    
    public void computeCircuits() {

        if(this.deliveryRequest == null) {
            System.out.println("Error: cannot compute circuits without a delivery request");//error
            return;
        }

		if(this.numberOfCouriers == -1) {
			System.out.println("Error: cannot compute circuits without a set number of couriers");//error
			return;
		}
	        
		if(this.shortestPaths == null) {
			System.out.println("Error: shortest paths have not yet been computed");//error
			return;
		}
	
	    CircuitComputer circuitComputer = new CircuitComputer();
		
			circuitComputer.init(this.deliveryRequest, this.shortestPaths);
			circuitComputer.execute(this.numberOfCouriers);
			this.setCircuits(circuitComputer.result());

    }
    
    public int getNumberOfCouriers() {
        return numberOfCouriers;
    }
    
    public void setNumberOfCouriers(int numberOfCouriers) {
        this.numberOfCouriers = numberOfCouriers;
    }

    public void setSelectedDelivery(Delivery delivery){
        LinkedList<Delivery> deliveries = deliveryRequest.getDeliveries();
        
        for(int i =0; i < deliveries.size(); i++){
            if(deliveries.get(i) == delivery)
                deliveries.get(i).setIsSelected(true);
            else 
                deliveries.get(i).setIsSelected(false);
        }
       
    }
    
    public void computeShortestPaths(){
    	if(this.cityMap == null) {
            System.out.println("Error: cannot compute shortest path without a city map");//error
            return;
        }
        if(this.deliveryRequest == null) {
            System.out.println("Error: cannot compute shortest path without a delivery request");//error
            return;
        }
    	ShortestPathComputer shortestPathComputer = new ShortestPathComputer();
        shortestPathComputer.init(cityMap, deliveryRequest);
        shortestPathComputer.computeAllShortestPaths();
        this.shortestPaths = shortestPathComputer.result();
        
        /*
        //TESTS
        Delivery newDelivery = new Delivery();
        newDelivery.setAddress("26316432");
        newDelivery.setGeolocation(this.cityMap.getIntersectionGeolocation("26316432"));
        this.addDelivery(newDelivery);
        System.out.println("hey");
        getTripBetweenIntersections(newDelivery.getAddress(), this.deliveryRequest.getWarehouseAddress());
        */
    }

    
    public void resetShortestPaths(){
    	this.shortestPaths = null;
    }
    
    public void addDelivery(Delivery delivery) {
    	deliveryRequest.addDelivery(delivery);
    	ShortestPathComputer shortestPathComputer = new ShortestPathComputer();
      shortestPathComputer.init(cityMap, deliveryRequest);
      shortestPathComputer.setShortestPathsForNewDelivery(delivery, this.shortestPaths);
      this.shortestPaths = shortestPathComputer.result();
    }
    
    public Trip getTripBetweenIntersections(String originAddress, String targetAddress) {
    	if(this.shortestPaths == null) {
    		System.out.println("Error: shortest paths have not yet been computed");//error
  			return null;
    	}
    	HashMap<String, Trip> shortestPathsFromOrigin = this.shortestPaths.get(originAddress);
    	if(shortestPathsFromOrigin == null) {
    		System.out.println("Error: shortest paths from delivery " + originAddress +" were not found");//error
  			return null;
    	}
    	return shortestPathsFromOrigin.get(targetAddress);
    }
}
