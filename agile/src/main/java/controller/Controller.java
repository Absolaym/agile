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
import model.Circuit;
import model.CityMap;
import utils.CircuitAlgorithm;
import utils.XmlParser;

/**
 *
 * @author olivi
 */
public class Controller {

    private CityMap cityMap;
    private DeliveryRequest deliveryRequest;
    private LinkedList<Circuit> circuits;

    private State state;
    private StateDefault stateDefault = new StateDefault();
    private StateInit stateInit = new StateInit();
    private StateCityMapLoaded stateCityMapLoaded = new StateCityMapLoaded();
    private StateDeliveryRequestLoaded stateDeliveryRequestLoaded = new StateDeliveryRequestLoaded();
    private StateCircuitsComputed stateCircuitsComputed = new StateCircuitsComputed();

    public Controller() {
        this.setCityMap(new CityMap());
        this.setState(stateInit);
        //this.preload();
    }

    public void preload() {
        this.loadCityMap("src/main/assets/maps/petitPlan.xml");
        this.loadDeliveryRequest("src/main/assets/deliveries/dl-petit-6.xml");
    }

    public CityMap loadCityMap(String path) {

        CityMap cityMap = this.state.LoadCityMap(path);
        if (cityMap != null) {
            this.setCityMap(cityMap);
            this.setState(this.stateCityMapLoaded);
        }
        return cityMap;
        //System.out.println("It has to load map.");
    }

    public DeliveryRequest loadDeliveryRequest(String path) {

        System.out.println("It has to load delivery requests.");

        DeliveryRequest deliveryRequest = this.state.LoadDeliveryRequest(path, this);
        if (deliveryRequest != null) {
            this.setDeliveryRequest(deliveryRequest);
            this.setState(this.stateDeliveryRequestLoaded);
        }

        this.deliveryRequest = deliveryRequest;
        
        this.circuits = null;
        
        return deliveryRequest;
    }

    public void ComputeCircuits() {
        CircuitAlgorithm algo = new CircuitAlgorithm();
        algo.init(this.cityMap, this.deliveryRequest);
        algo.execute();
        this.circuits = algo.result();

        this.state.ComputeCircuits(this);

    }

    public LinkedList<Circuit> getCircuits() {
        return this.circuits;
    }

    public CityMap getCityMap() {
        return cityMap;
    }

    public void setCityMap(CityMap cityMap) {
        this.cityMap = cityMap;
    }

    public DeliveryRequest getDeliveryRequest() {
        return this.deliveryRequest;
    }

    public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
        this.deliveryRequest = deliveryRequest;
    }

    public void setState(State state) {
        this.state = state;
    }
}
//    public State getState(){
//        return this.state;
//    }

