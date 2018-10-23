/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author lgalle
 */
public interface State{
    
    public abstract void LoadCityMap(String path);
    
    public abstract void LoadDeliveryRequest();
    
    public abstract void ComputeCircuits();
}
