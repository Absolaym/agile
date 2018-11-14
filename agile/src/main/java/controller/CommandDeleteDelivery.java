/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Circuit;
import model.Delivery;
import model.Model;
import model.Trip;

/**
 *
 * @author pagilles
 */
public class CommandDeleteDelivery implements Command {

    private Delivery delivery;
    private int deliveryIndex;
    
    private Circuit circuit;
    private int circuitIndex;
    
    public CommandDeleteDelivery(Delivery d, Circuit c){
        this.delivery = d;
                
        this.circuit = c;
        this.circuitIndex = Model.getInstance().getCircuits().indexOf(c);
        this.deliveryIndex = Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().indexOf(d);
    }

    CommandDeleteDelivery() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void doCde() {
        
        String tripOrigin;
        String tripTarget;
        
        if(deliveryIndex == 0) {
            tripOrigin = Model.getInstance().getDeliveryRequest().getWarehouseAddress();
        } else {
            tripOrigin = Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().get(deliveryIndex-1).getAddress();
        }
        
        if(deliveryIndex == Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().size()-1){
            tripTarget = Model.getInstance().getDeliveryRequest().getWarehouseAddress();
        } else {
            tripTarget = Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().get(deliveryIndex+1).getAddress();
        }
        
        Model.getInstance().getDeliveryRequest().removeDelivery(delivery);
        
        Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().remove(delivery);
        Model.getInstance().getCircuits().get(circuitIndex).getTrips().remove(deliveryIndex);
        Model.getInstance().getCircuits().get(circuitIndex).getTrips().remove(deliveryIndex);
        
        Trip trip = Model.getInstance().getTripBetweenIntersections(tripOrigin, tripTarget);
        Model.getInstance().getCircuits().get(circuitIndex).addTrip(deliveryIndex, trip);
        
        Model.getInstance().getCircuits().get(circuitIndex).updateDeliveryInfos();
        Model.getInstance().rearrangeDeliveries();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void undoCde() {
        
        Model.getInstance().getDeliveryRequest().addDelivery(delivery);
        Model.getInstance().getCircuits().set(circuitIndex, circuit);
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
