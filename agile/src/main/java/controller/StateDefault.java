/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.CityMap;
import model.DeliveryRequest;
import utils.XmlParser;

/**
 *
 * @author lgalle
 */
public class StateDefault implements State{
    
    public CityMap LoadCityMap(String path){
        CityMap cityMap = null;
        try {
            XmlParser xmlParser = new XmlParser();
            //Plan plan = xmlParser.parseMap("src/main/assets/maps/grandPlan.xml");
            cityMap = xmlParser.parseMap(path);
    	}catch(Exception e) {
            //display exception in a pop up
            //make specific error for reading exception
    	}
        return cityMap;
    }
    
    public DeliveryRequest LoadDeliveryRequest(String path){
       return null;
    }
    
    public void ComputeCircuits(Controller c){
        
    }
}
