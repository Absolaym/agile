/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Model;
import model.Delivery;

/**
 *
 * @author olivi
 */
public class Controller {

	private static Controller controller;
	
	private Model model;
	protected CommandsList commandsList;

	private State state;
	protected static final StateDefault STATE_DEFAULT = new StateDefault();
	protected static final StateInit STATE_INIT = new StateInit();
	protected static final StateCityMapLoaded STATE_CITYMAP_LOADED = new StateCityMapLoaded();
	protected static final StateDeliveryRequestLoaded STATE_DELIVERYREQUEST_LOADED = new StateDeliveryRequestLoaded();
	protected static final StateCircuitsComputed STATE_CIRCUITS_COMPUTED = new StateCircuitsComputed();

	public static Controller getInstance() {
		if(Controller.controller == null)	Controller.controller = new Controller();
		return Controller.controller;
	}
	
	private Controller() {
		this.commandsList = new CommandsList();
		
		this.model = Model.getInstance();
		//this.setCityMap(new CityMap());
		this.setState(STATE_INIT);
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
		this.computeCircuits( 1 );
	}

	public void computeCircuits(int numberOfCouriers) {
		this.model.setNumberOfCouriers(numberOfCouriers);
		this.state.computeCircuits(this);
	}

	public void setSelectDelivery(Delivery delivery){
		model.setSelectedDelivery(delivery);
	}

	public void addDelivery() {
		this.state.addDelivery();
	}

	public void addDeliveryToDeliveryRequest() {
			this.state.addDeliveryToDeliveryRequest();
	}

	public void deleteDelivery() {
		this.state.deleteDelivery();
	}

	public void moveDelivery() {
		this.state.moveDelivery();
	}
	
	// These 4 methods aren't state dependent and thus aren't called in a state
	
	public boolean canUndo() {
		return this.commandsList.canUndo();
	}
	
	public boolean canRedo() {
		return this.commandsList.canRedo();
	}

	public void undo() {
		this.commandsList.undo();
	}

	public void redo() {
		this.commandsList.redo();
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
