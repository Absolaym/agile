/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.LinkedList;

import utils.CircuitAlgorithm;

/**
 *
 * @author Lucie
 */
public class Model {
    
    private CityMap cityMap;
    private DeliveryRequest deliveryRequest;
    private LinkedList<Circuit> circuits;
    private CircuitAlgorithm circuitAlgorithm;
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
        if(this.cityMap == null) {
            System.out.println("Error: cannot compute circuits without a city map");//error
            return;
        }
        if(this.deliveryRequest == null) {
            System.out.println("Error: cannot compute circuits without a delivery request");//error
            return;
        }
	///////////////////////////////////////////////////////////
	//CHANGE THIS
	if(this.numberOfCouriers == -1) {
	//System.out.println("Error: cannot compute circuits without a set number of couriers");//error
	//return;
            this.numberOfCouriers = 5;
	}
	////////////////////////////////////////////////////
        
	if(this.circuitAlgorithm == null) 
            this.circuitAlgorithm = new CircuitAlgorithm();
	
	this.circuitAlgorithm.init(this.cityMap, this.deliveryRequest);
	this.circuitAlgorithm.execute(this.numberOfCouriers); //ATTENTION CHANGER
	this.circuits = this.circuitAlgorithm.result();

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
        }
       
    }
}
