/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Circuit;
import model.Model;
import model.Delivery;

/**
 *
 * @author olivi
 */
public class Controller {
    
    // le singleton c'est sympa mais on va pas s'amuser à tout changer maintenant
    
    private Model model;
    protected static CommandsList commandsList;
    
    private State state;
    protected static final StateDefault STATE_DEFAULT = new StateDefault();
    protected static final StateInit STATE_INIT = new StateInit();
    protected static final StateCityMapLoaded STATE_CITYMAP_LOADED = new StateCityMapLoaded();
    protected static final StateDeliveryRequestLoaded STATE_DELIVERYREQUEST_LOADED = new StateDeliveryRequestLoaded();
    protected static final StateCircuitsComputed STATE_CIRCUITS_COMPUTED = new StateCircuitsComputed();
    
    public Controller() {
        this.model = Model.getInstance();
        //this.setCityMap(new CityMap());
        this.setState(STATE_INIT);
        //this.preload();
        Controller.commandsList = new CommandsList();
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

    public void computeCircuits(int numberOfCouriers) {
    	this.model.setNumberOfCouriers(numberOfCouriers);
        this.state.computeCircuits(this);
    }
    
    public void setSelectDelivery(Delivery delivery){
        model.setSelectedDelivery(delivery);
    }
    
    public void addDelivery(Delivery d, Circuit c) {
        this.state.addDelivery(d, c);
    }
        
    public void deleteDelivery(Delivery d, Circuit c) {
        this.state.deleteDelivery(d, c);
    }
    
    public void changeCircuit(Delivery d, Circuit oc, Circuit tc) {
        this.state.changeCircuit(d, oc, tc);
    }
    
    public void moveDeliveryBefore(Delivery d, Circuit c) {
        this.state.moveDeliveryBefore(d, c);
    }
    
    public void moveDeliveryAfter(Delivery d, Circuit c) {
        this.state.moveDeliveryAfter(d, c);
    }
    
//    public void addDeliveryToDeliveryRequest(Delivery d, Circuit c) {
//        this.state.addDeliveryToDeliveryRequest(d, c);
//    }
    
    // undo et redo appelés uniquement dans le stateCircuitsComputed ...
    
    public void undo() {
        this.state.undoCde();
    }
    
    public void redo() {
        this.state.redoCde();
    }
    
    public boolean canUndo() {
        return this.commandsList.canUndo();
    }
	
    public boolean canRedo() {
        return this.commandsList.canRedo();
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
    

