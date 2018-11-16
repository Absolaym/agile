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
     * This method is implementes in all the states, except the StateInit.
     * @param path
     * @param c 
     */
    public abstract void loadCityMap(String path, Controller c);

    /**
     * Loads a delivery request by parsing the xml and updating the model.
     * Updates the controller's state.
     * This method is implementes in all the states, except the StateInit and 
     * StateDefault, since a city map must be loaded for a delivery to get 
     * loaded.
     * @param path
     * @param c 
     */
    public abstract void loadDeliveryRequest(String path, Controller c);

    /**
     * 
     * @param c 
     */
    public abstract void computeCircuits(Controller c);
    
    /**
     * 
     * @param d
     * @param c 
     */
    public abstract void addDelivery(Delivery d, Circuit c);

    /**
     * 
     * @param d
     * @param c 
     */
    public abstract void deleteDelivery(Delivery d, Circuit c);

    /**
     * 
     * @param d
     * @param oc
     * @param tc 
     */
    public abstract void changeCircuit(Delivery d, Circuit oc, Circuit tc);
    
    /**
     * 
     * @param d
     * @param c 
     */
    public abstract void moveDeliveryBefore(Delivery d, Circuit c);

    /**
     * 
     * @param d
     * @param c 
     */
    public abstract void moveDeliveryAfter(Delivery d, Circuit c);

    /**
     * 
     */
    public abstract void undoCde();

    /**
     * 
     */
    public abstract void redoCde();
}
