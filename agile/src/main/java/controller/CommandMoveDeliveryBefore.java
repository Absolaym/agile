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
public class CommandMoveDeliveryBefore implements Command {
    
    private boolean hasMoved;
    
    private Delivery originalDelivery;
    private int originalDeliveryIndex;
    
    private Circuit originalCircuit;
    private int originalCircuitIndex;
    
    public CommandMoveDeliveryBefore(Delivery d, Circuit c){
                
        this.originalDelivery = new Delivery(d);
        this.originalCircuit = new Circuit(c);
        
        this.originalDeliveryIndex = c.getDeliveries().indexOf(d);
        this.originalCircuitIndex = Model.getInstance().getCircuits().indexOf(c);
        
        hasMoved = false;
    }
    
    @Override
    public void execute() {
        
        hasMoved = false;
        
        Delivery updatedDelivery = new Delivery(originalDelivery);
        Circuit updatedCircuit = new Circuit(originalCircuit);
        
        int deliveryIndex = originalDeliveryIndex;
        int circuitIndex = originalCircuitIndex;
        
        Model model = Model.getInstance();
        
        String trip1Origin, trip1Target;
        String trip2Origin, trip2Target;
        String trip3Origin, trip3Target;
        
        if(deliveryIndex == 0) {
            return;
        }
        
        if(deliveryIndex == 1) {
            trip1Origin = model.getDeliveryRequest().getWarehouseAddress();
        } else {
            trip1Origin = updatedCircuit.getDeliveries().get(deliveryIndex-2).getAddress();
        }
        
        trip1Target = updatedCircuit.getDeliveries().get(deliveryIndex).getAddress();
        
        trip2Origin = trip1Target;
        trip2Target = updatedCircuit.getDeliveries().get(deliveryIndex-1).getAddress();
        
        trip3Origin = trip2Target;
        
        if(deliveryIndex == updatedCircuit.getDeliveries().size()-1){
            trip3Target = model.getDeliveryRequest().getWarehouseAddress();
        } else {
            trip3Target = updatedCircuit.getDeliveries().get(deliveryIndex+1).getAddress();
        }
        
        Trip trip1 = model.getTripBetweenIntersections(trip1Origin, trip1Target);
        Trip trip2 = model.getTripBetweenIntersections(trip2Origin, trip2Target);
        Trip trip3 = model.getTripBetweenIntersections(trip3Origin, trip3Target);
        
        
        updatedCircuit.getDeliveries().remove(deliveryIndex);
        updatedCircuit.addDelivery(deliveryIndex-1,updatedDelivery);
        
        updatedCircuit.getTrips().remove(deliveryIndex-1);
        updatedCircuit.getTrips().remove(deliveryIndex-1);
        updatedCircuit.getTrips().remove(deliveryIndex-1);
        updatedCircuit.addTrip(deliveryIndex-1, trip3);
        updatedCircuit.addTrip(deliveryIndex-1, trip2);
        updatedCircuit.addTrip(deliveryIndex-1, trip1);
        
        updatedCircuit.updateSections();
        updatedCircuit.updateDeliveryInfos();        
        
        model.getCircuits().set(circuitIndex, updatedCircuit);
        model.rearrangeDeliveries();
        
        hasMoved = true;
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancel() {
        
        if(!hasMoved){
            return;
        }
        
        Model model = Model.getInstance();
        
        Circuit copyOfOriginalCircuit = new Circuit(originalCircuit);
        
        copyOfOriginalCircuit.updateSections();
        copyOfOriginalCircuit.updateDeliveryInfos();
        
        model.getCircuits().set(originalCircuitIndex, copyOfOriginalCircuit);
        
        model.rearrangeDeliveries();
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
