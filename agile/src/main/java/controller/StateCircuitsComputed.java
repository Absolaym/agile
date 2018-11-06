/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Circuit;
import model.Delivery;
import model.DeliveryRequest;
import model.Model;
import utils.XmlParser;

/**
 *
 * @author lgalle
 */
public class StateCircuitsComputed extends StateDefault{
    
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
                c.setState(c.stateDeliveryRequestLoaded);
            }
        }catch (Exception e){
            
        }
    }
    
    public void computeCircuits(Controller c){
        
    }
    
    public void addDeliveryToComputedCircuit(Circuit c, Delivery d) {
        // add code to add delivery to chosen circuit
        
        Controller.commandsList.addCommand(new CommandAddDeliveryToComputedCircuit(c, d));
    }
    
    public void deleteDelivery(Circuit c, Delivery d) {
        // add code to delete delivery from given circuit
        
        Controller.commandsList.addCommand(new CommandDeleteDelivery(c, d));
    }
    
    public void moveDelivery(Delivery d, Circuit oc, Circuit tc) {
        // add code to move delivery from a circuit to another
        
        Controller.commandsList.addCommand(new CommandMoveDelivery(d, oc, tc));
    }
    
    public void undoCde() {
        Controller.commandsList.undo();
    }
    
    public void redoCde() {
        Controller.commandsList.redo();
    }
}
