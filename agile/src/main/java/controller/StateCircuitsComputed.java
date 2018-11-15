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
                model.computeShortestPaths();
                model.setCircuits(null);
                c.setState(c.STATE_DELIVERYREQUEST_LOADED);
            }
        }catch (Exception e){
            
        }
    }
    
    public void computeCircuits(Controller c){
        Model model = c.getModel();
        model.computeCircuits();
        Controller.commandsList = new CommandsList();
    }
    
    public void addDelivery(Delivery d, Circuit c) {
        Controller.commandsList.addCommand(new CommandAddDelivery(d, c));
    }
    
    public void deleteDelivery(Delivery d, Circuit c) {        
        Controller.commandsList.addCommand(new CommandDeleteDelivery(d, c));

    }
    
    public void changeCircuit(Delivery d, Circuit oc, Circuit tc) {        
        Controller.commandsList.addCommand(new CommandChangeCircuit(d, oc, tc));
    }
    
    public void moveDeliveryBefore(Delivery d, Circuit c) {
        Controller.commandsList.addCommand(new CommandMoveDeliveryBefore(d, c));
    }
    
    public void moveDeliveryAfter(Delivery d, Circuit c) {
        Controller.commandsList.addCommand(new CommandMoveDeliveryAfter(d, c));
    }
    
    public void undoCde() {
        Controller.commandsList.undo();
    }
    
    public void redoCde() {
        Controller.commandsList.redo();
    }
}
