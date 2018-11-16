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

    public abstract void loadCityMap(String path, Controller c);

    public abstract void loadDeliveryRequest(String path, Controller c);

    public abstract void computeCircuits(Controller c);

    public abstract void addDelivery(Delivery d, Circuit c);

    public abstract void deleteDelivery(Delivery d, Circuit c);

    public abstract void changeCircuit(Delivery d, Circuit oc, Circuit tc);

    public abstract void moveDeliveryBefore(Delivery d, Circuit c);

    public abstract void moveDeliveryAfter(Delivery d, Circuit c);

    public abstract void addDeliveryToDeliveryRequest(Delivery d, Circuit c);

    public abstract void undoCde();

    public abstract void redoCde();
}
