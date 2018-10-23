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
    private DeliveryRequest deliveryRequest;
    private State state;
    private StateDefault stateDefault = new StateDefault();
    private StateInit stateInit = new StateInit();
    private StateCityMapLoaded stateCityMapLoaded = new StateCityMapLoaded();
    private StateDeliveryRequestLoaded stateDeliveryRequestLoaded = new StateDeliveryRequestLoaded();
    private StateCircuitsComputed stateCircuitsComputed = new StateCircuitsComputed();
    
    public Controller() { 
        this.setCityMap(new CityMap());
        this.setState(stateInit);
    }
	
    public CityMap LoadCityMap(String path) {
        
        CityMap cityMap = this.state.LoadCityMap(path);
        if(cityMap != null ){
            this.setCityMap( cityMap );
            this.setState(this.stateCityMapLoaded);
        }
    	return cityMap;	
        //System.out.println("It has to load map.");
    }
    
    public DeliveryRequest loadDeliveryRequest(String path) {
        
        System.out.println("It has to load delivery requests.");
        
        DeliveryRequest deliveryRequest = this.state.LoadDeliveryRequest(path);
        if(deliveryRequest != null ){
            // POTENTIELLEMENT A DEPLACER => une classe service ?
            this.setDeliveryRequestGeolocation(deliveryRequest);
            //
            this.setDeliveryRequest( deliveryRequest );
            this.setState(this.stateDeliveryRequestLoaded);
        }
        
        this.deliveryRequest = deliveryRequest;
        return deliveryRequest;
    }
    
    
    public void ComputeCircuits() {
        //TO DO
        System.out.println("It has to compute circuits.");
        this.state.ComputeCircuits(this);
    }
    
    public CityMap getCityMap() {
        return cityMap;
    }

    public void setCityMap(CityMap cityMap) {
        this.cityMap = cityMap;
    }
    
    public DeliveryRequest getDeliveryRequest(){
        return this.deliveryRequest;
    }
    
    public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
        this.deliveryRequest = deliveryRequest;
    }
    public void setState(State state) {
        this.state = state;
    }
    
//    public State getState(){
//        return this.state;
//    }
    
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
