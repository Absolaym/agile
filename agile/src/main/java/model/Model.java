/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.LinkedList;

/**
 *
 * @author Lucie
 */
public class Model {
    
    private CityMap cityMap;
    private DeliveryRequest deliveryRequest;
    private LinkedList<Circuit> circuits;
    
    private static Model INSTANCE = null;
    
    private Model(){
//        deliveryRequest = new DeliveryRequest();
//        circuits = new LinkedList<Circuit>();
        cityMap = new CityMap();
        deliveryRequest = null;
        circuits = null;
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

}
