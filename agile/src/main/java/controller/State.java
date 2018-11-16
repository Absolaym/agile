/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Circuit;
import model.DeliveryRequest;
import model.CityMap;
import model.Delivery;

/**
 *
 * @author lgalle
 */
public interface State {
    
    /**
     * Loads a city map by parsing the xml and updating the model.
     * Updates the controller's state.
     * This method is implemented in all the states, except the StateInit.
     * @param path
     * @param c 
     */
    public abstract void loadCityMap(String path, Controller c);

    /**
     * Loads a delivery request by parsing the xml and updating the model.
     * Updates the controller's state.
     * This method is implemented in all the states, except the StateInit and 
     * StateDefault, since a city map must be loaded for a delivery to get 
     * loaded.
     * @param path
     * @param c 
     */
    public abstract void loadDeliveryRequest(String path, Controller c);

    /**
     * Computes circuits from a delivery request.
     * Updates the controller's state.
     * This method is  only implemented the states StateDeliveryRequestLoaded
     * and StateCircuitsComputed, since a city map and a delivery request have 
     * to be loaded to compute circuits.
     * @param c 
     */
    public abstract void computeCircuits(Controller c);
    
    /**
     * Adds a delivery to a circuit.
     * Updates the controller's state.
     * This method is  only implemented the state StateCircuitsComputed, since 
     * a delivery can only be added to a circuit once the circuits have been 
     * computed     
     * @param d
     * @param c 
     */
    public abstract void addDelivery(Delivery d, Circuit c);

    /**
     * Deletes a delivery from a circuit.
     * Updates the controller's state.
     * This method is only implemented the state StateCircuitsComputed, since 
     * a delivery can only be deleted from a circuit once the circuits have been
     * computed  
     * @param d
     * @param c 
     */
    public abstract void deleteDelivery(Delivery d, Circuit c);

    /**
     * Moves a delivery from a circuit to another circuit.
     * Updates the controller's state.
     * This method is only implemented the state StateCircuitsComputed, since 
     * a delivery can only be moved from a circuit to another once the circuits 
     * have been computed.
     * This functionality has not yet been integrated to the interface
     * @param d
     * @param oc
     * @param tc 
     */
    public abstract void changeCircuit(Delivery d, Circuit oc, Circuit tc);
    
    /**
     * Places a delivery one step before in the deliveries order of the 
     * circuit.
     * Updates the controller's state.
     * This method is only implemented the state StateCircuitsComputed, since 
     * a delivery place in the list can only be changed once the circuits 
     * have been computed.
     * @param d
     * @param c 
     */
    public abstract void moveDeliveryBefore(Delivery d, Circuit c);

    /**
     * Places a delivery one step after in the deliveries order of the 
     * circuit.
     * Updates the controller's state.
     * This method is only implemented the state StateCircuitsComputed, since 
     * a delivery place in the list can only be changed once the circuits 
     * have been computed.
     * @param d
     * @param c 
     */
    public abstract void moveDeliveryAfter(Delivery d, Circuit c);

    /**
     * Un-does a command.
     */
    public abstract void undoCde();

    /**
     * Re-does a command.
     */
    public abstract void redoCde();
}
