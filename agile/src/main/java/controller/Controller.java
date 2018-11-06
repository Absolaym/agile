/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Model;

/**
 *
 * @author olivi
 */
public class Controller {

    private Model model;
    protected static CommandsList commandsList;
    
    private State state;
    protected static  final StateDefault stateDefault = new StateDefault();
    public static final StateInit stateInit = new StateInit();
    protected static final StateCityMapLoaded stateCityMapLoaded = new StateCityMapLoaded();
    protected static final StateDeliveryRequestLoaded stateDeliveryRequestLoaded = new StateDeliveryRequestLoaded();
    protected static final StateCircuitsComputed stateCircuitsComputed = new StateCircuitsComputed();

    public Controller() {
        this.model = Model.getInstance();
        //this.setCityMap(new CityMap());
        this.setState(stateInit);
        //this.preload();
    }

    public void preload() {
        this.loadCityMap("src/main/assets/maps/petitPlan.xml");
        this.loadDeliveryRequest("src/main/assets/deliveries/dl-petit-6.xml");
    }

    public void loadCityMap(String path) {
        this.state.loadCityMap(path,this);
        //System.out.println("It has to load map.");
    }

    public void loadDeliveryRequest(String path) {
        //System.out.println("It has to load delivery requests.");
        this.state.loadDeliveryRequest(path, this);
    }

    public void computeCircuits() {
        this.state.computeCircuits(this);
    }
    
    public void addDelivery() {
        this.state.addDelivery();
    }
    
    public void addDeliveryToComputedCircuit() {
        this.state.addDeliveryToComputedCircuit();
    }
    
    public void deleteDelivery() {
        this.state.deleteDelivery();
    }
    
    public void moveDelivery() {
        this.state.moveDelivery();
    }
    
    public void undo() {
        this.state.undoCde();
    }
    
    public void redo() {
        this.state.redoCde();
    }
    

    public Model getModel(){
        return model;
    }
    
    public void setState(State state) {
        this.state = state;
    }
    
    public State getState(){
        return state;
    }
}
    

