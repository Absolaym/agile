/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.DeliveryRequest;
import model.CityMap;

/**
 *
 * @author lgalle
 */
public interface State{
    
    public abstract void loadCityMap(String path,Controller c);
    
    public abstract void loadDeliveryRequest(String path,Controller c);
    
    public abstract void computeCircuits(Controller c);
}
