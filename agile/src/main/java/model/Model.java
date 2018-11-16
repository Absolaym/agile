/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.HashMap;
import java.util.LinkedList;

import error.ErrorLogger;
import error.ProjectError;
import utils.CircuitComputer;
import utils.ShortestPathComputer;

/**
 * This class links the controller to the model
 */
public class Model {

	private CityMap cityMap;
	private DeliveryRequest deliveryRequest;
	private HashMap<String,HashMap<String,Trip>> shortestPaths;
	private LinkedList<Circuit> circuits;
	private int numberOfCouriers;

	private static Model INSTANCE = null;

	private Model(){
		cityMap = new CityMap();
		deliveryRequest = null;
		circuits = null;
		numberOfCouriers = -1;
	}

	public static Model getInstance(){
		return INSTANCE == null ?	INSTANCE = new Model() : INSTANCE;
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
	
	
	/**
	 * Compute the circuits it can be quite long for large groups of deliveries
	 */
	public void computeCircuits() {

		if(this.deliveryRequest == null) {
			ErrorLogger.getInstance().log( ProjectError.NO_DR_BEF_CIRCUIT );
			return;
		}

		if(this.numberOfCouriers <= 0) {
			ErrorLogger.getInstance().log( ProjectError.NO_COURRIER_ASSIGNED );
			return;
		}

		if(this.numberOfCouriers > this.deliveryRequest.getDeliveries().size()) {
			ErrorLogger.getInstance().log( ProjectError.TOO_MUCH_COURRIER );
		}

		if(this.shortestPaths == null) {
			ErrorLogger.getInstance().log( ProjectError.TIMEOUT );
			return;
		}

		CircuitComputer circuitComputer = new CircuitComputer();

		circuitComputer.init(this.deliveryRequest, this.shortestPaths);
		circuitComputer.execute(this.numberOfCouriers);
		this.setCircuits(circuitComputer.result());

		rearrangeDeliveries();
	}

	/**
	 * Rearrange the deliveries to get them in the order of the circuits (should be changed in a 5th iterations)
	 */
	public void rearrangeDeliveries(){
		LinkedList<Delivery> rearrangedDeliveries = new LinkedList<Delivery>();
		for(Circuit circuit : circuits){
			for(Delivery delivery : circuit.getDeliveries()){
				rearrangedDeliveries.add(delivery);
			}
		}
		this.deliveryRequest.setDeliveries(rearrangedDeliveries);
	}

	public int getNumberOfCouriers() {
		return numberOfCouriers;
	}

	public void setNumberOfCouriers(int numberOfCouriers) {
		if (this.deliveryRequest == null) {
			ErrorLogger.getInstance().log(ProjectError.NO_DR_BEF_CIRCUIT);
			return;
		}
		if (numberOfCouriers <= 0) {
			ErrorLogger.getInstance().log(ProjectError.NEGATIVE_COURRIER);
			return;
		}
		if (this.deliveryRequest.getDeliveries().size() < numberOfCouriers) {
			ErrorLogger.getInstance().log(ProjectError.TOO_MUCH_COURRIER);
			numberOfCouriers = Math.min( this.deliveryRequest.getDeliveries().size(), numberOfCouriers);
		}
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

	/**
	 * Compute the shortest paths between deliveries
	 */
	public void computeShortestPaths(){
		if(this.cityMap == null) {
			ErrorLogger.getInstance().log( ProjectError.NO_MAP_BEF_CIRCUIT );
			return;
		}
		if(this.deliveryRequest == null) {
			ErrorLogger.getInstance().log( ProjectError.NO_DR_BEF_CIRCUIT );
			return;
		}
		ShortestPathComputer shortestPathComputer = new ShortestPathComputer();
		shortestPathComputer.init(cityMap, deliveryRequest);
		shortestPathComputer.computeAllShortestPaths();
		this.shortestPaths = shortestPathComputer.result();

	}


	/**
	 * Resets all the shortest paths
	 */
	public void resetShortestPaths(){
		this.shortestPaths = null;
	}

	/**
	 * Add a delivery 
	 * @param delivery
	 */
	public void addDelivery(Delivery delivery) {
		deliveryRequest.addDelivery(delivery);
		ShortestPathComputer shortestPathComputer = new ShortestPathComputer();
		shortestPathComputer.init(cityMap, deliveryRequest);
		shortestPathComputer.setShortestPathsForNewDelivery(delivery, this.shortestPaths);
		this.shortestPaths = shortestPathComputer.result();
	}

	/**
	 * Gets the trip between two addresses
	 * @param originAddress id of the original address
	 * @param targetAddress id of the target address
	 * @return
	 */
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
