/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Delivery;
import model.DeliveryRequest;
import model.Model;
import utils.XmlParser;
import utils.CircuitAlgorithm;

/**
 *
 * @author lgalle
 */
public class StateDeliveryRequestLoaded extends StateDefault{
    
    public void loadDeliveryRequest(String path,Controller c){ 
        try{
            Model model = c.getModel();
            DeliveryRequest deliveryRequest = null;
            XmlParser parser = new XmlParser();
            
            deliveryRequest = parser.parseDeliveryRequest(path);
            if(deliveryRequest!=null){
                deliveryRequest.computeDeliveryRequestGeolocation(model.getCityMap());
                model.setDeliveryRequest(deliveryRequest);
                model.setCircuits(null);
                c.setState(c.STATE_DELIVERYREQUEST_LOADED);
            }
        }catch (Exception e){
            
        }
    }
    
    public void computeCircuits(Controller c){
        Model model = c.getModel();
        model.computeCircuits();
    }
    
    public void addDelivery(DeliveryRequest dr, Delivery d) {
        // add code to add delivery to delivery request
        
        Controller.commandsList.addCommand(new CommandAddDelivery(dr, d));
    }
}
