/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.CityMap;
import model.DeliveryRequest;
import model.Model;
import utils.XmlParser;

/**
 *
 * @author lgalle
 */
public class StateDefault implements State{
    
    public void loadCityMap(String path,Controller c){
        try {
            CityMap cityMap = null;
            Model model = c.getModel();
            XmlParser xmlParser = new XmlParser();
            
            cityMap = xmlParser.parseMap(path);
            if (cityMap != null){
                model.setCityMap(cityMap);
                model.setDeliveryRequest(null);
                model.setCircuits(null);
                c.setState(c.stateCityMapLoaded);
            }
    	}catch(Exception e) {
            //display exception in a pop up
            //make specific error for reading exception
    	}
    }
    
    public void loadDeliveryRequest(String path,Controller c){
    }
    
    public void computeCircuits(Controller c){
        
    }
    
    public void addDelivery() {
        
    }
    
    public void addDeliveryToComputedCircuit() {
        
    }
    
    public void deleteDelivery() {
        
    }
    
    public void moveDelivery() {
        
    }
    
    public void undoCde() {
        
    }
    
    public void redoCde() {
        
    }
}
