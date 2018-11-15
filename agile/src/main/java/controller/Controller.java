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
 * This class is the global controller of the system. It's main goal is to be the link between the model and the view.
 * @author olivi
 */
public class Controller {


	protected static CommandsList commandsList;

	private State state;
	protected static final StateDefault STATE_DEFAULT 															= new StateDefault();
	protected static final StateInit STATE_INIT 																		= new StateInit();
	protected static final StateCityMapLoaded STATE_CITYMAP_LOADED 									= new StateCityMapLoaded();
	protected static final StateDeliveryRequestLoaded STATE_DELIVERYREQUEST_LOADED 	= new StateDeliveryRequestLoaded();
	protected static final StateCircuitsComputed STATE_CIRCUITS_COMPUTED 						= new StateCircuitsComputed();

	public Controller() {
		//this.setCityMap(new CityMap());
		this.setState(STATE_INIT);
		//this.preload();
		Controller.commandsList = new CommandsList();
	}

	private void preload() {
		this.loadCityMap("src/main/assets/maps/petitPlan.xml");
		this.loadDeliveryRequest("src/main/assets/deliveries/dl-petit-6.xml");
	}

	/**
	 * Loads a map given its location
	 * @param path The absolute path to the map file
	 */
	public void loadCityMap(String path) {
		this.state.loadCityMap( path,this );
	}

	/**
	 * Loads a delivery given its location
	 * @param path The absolute path to the delivery file
	 */
	public void loadDeliveryRequest(String path) {
		this.state.loadDeliveryRequest(path, this);
	}

	/**
	 * Compute a set of circuits given the number of circuits
	 * @param numberOfCouriers the number of couriers/circuits
	 */
	public void computeCircuits(int numberOfCouriers) {
		Model.getInstance().setNumberOfCouriers(numberOfCouriers);
		this.state.computeCircuits(this);
	}

	/**
	 * Set the delivery currently selected
	 * @param delivery The delivery selected
	 */
	public void setSelectDelivery(Delivery delivery){
		this.getModel().setSelectedDelivery(delivery);
	}

	/**
	 * Add a delivery to circuit
	 * @param d The delivery you want to add
	 * @param c The circuit in which you want to add the delivery
	 */
	public void addDelivery(Delivery d, Circuit c) {
		this.state.addDelivery(d, c);
	}

	/**
	 * Removes a delivery from a circuit
	 * @param d The delivery you want to remove
	 * @param c The circuit in which the delivery is
	 */
	public void deleteDelivery(Delivery d, Circuit c) {
		this.state.deleteDelivery(d, c);
	}

	/**
	 * Moves a delivery from a circuit to an other circuit
	 * @param d The delivery you want to move
	 * @param oc The circuit in which the delivery originally was
	 * @param tc The circuit in which the delivery will be
	 */
	public void changeCircuit(Delivery d, Circuit oc, Circuit tc) {
		this.state.changeCircuit(d, oc, tc);
	}

	/**
	 * Reorders a delivery in a circuit by a decrement of one (if it's the 3rd delivrey, it's gonna be the 2nd after a call)
	 * @param d The delivery you want to move
	 * @param c The circuit in which the delivery is
	 */
	public void moveDeliveryBefore(Delivery d, Circuit c) {
		this.state.moveDeliveryBefore(d, c);
	}

	/**
	 * Reorders a delivery in a circuit by a decrement of one (if it's the 3rd delivrey, it's gonna be the 4th after a call)
	 * @param d The delivery you want to move
	 * @param c The circuit in which the delivery is
	 */
	public void moveDeliveryAfter(Delivery d, Circuit c) {
		this.state.moveDeliveryAfter(d, c);
	}

	//    public void addDeliveryToDeliveryRequest(Delivery d, Circuit c) {
	//        this.state.addDeliveryToDeliveryRequest(d, c);
	//    }

	/**
	 * Undo the last command
	 */
	public void undo() {
		this.state.undoCde();
	}

	/**
	 * Redo the last undone command
	 */
	public void redo() {
		this.state.redoCde();
	}

	/**
	 * Check wether it's possible to undo an action or not
	 * @return true if it's possible to undo
	 */
	public boolean canUndo() {
		return Controller.commandsList.canUndo();
	}

	/**
	 * Check wether it's possible to redo an action or not
	 * @return trueif it's possible to redo
	 */
	public boolean canRedo() {
		return Controller.commandsList.canRedo();
	}


	/**
	 * Get the model (result of a bad design since the model is a singleton)
	 * @return
	 */
	public Model getModel(){
		return Model.getInstance();
	}

	/**
	 * Change the state of the controller
	 * @param state
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Get the state of controller
	 * @return
	 */
	public State getState(){
		return state;
	}
}


