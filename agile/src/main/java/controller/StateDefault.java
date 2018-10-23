/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Plan;
import utils.XmlParser;

/**
 *
 * @author lgalle
 */
public class StateDefault implements State{
    
    public void LoadCityMap(String path){
        try {
            XmlParser xmlParser = new XmlParser();
            //Plan plan = xmlParser.parseMap("src/main/assets/maps/grandPlan.xml");
            Plan plan = xmlParser.parseMap(path);
            //this.setPlan( plan );
            //CALL setPlan from Controller
    	}catch(Exception e) {
            //display exception in a pop up
            //make specific error for reading exception
    	}
    }
    
    public void LoadDeliveryRequest(){
        
    }
    
    public void ComputeCircuits(){
        
    }
}
