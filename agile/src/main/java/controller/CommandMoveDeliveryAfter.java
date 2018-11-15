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
public class CommandMoveDeliveryAfter implements Command {

    private Delivery delivery;
    private int deliveryIndex;
    
    private Circuit circuit;
    private int circuitIndex;
    
    public CommandMoveDeliveryAfter(Delivery d, Circuit c){
        this.delivery = d;
                
        this.circuit = c;
        this.circuitIndex = Model.getInstance().getCircuits().indexOf(c);
        this.deliveryIndex = Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().indexOf(d);
    }
    
    @Override
    public void execute() {
        
        String trip1Origin, trip1Target;
        String trip2Origin, trip2Target;
        String trip3Origin, trip3Target;
        
        if(deliveryIndex == Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().size()-1) {
            return;
        }
        
        if(deliveryIndex == 0) {
            trip1Origin = Model.getInstance().getDeliveryRequest().getWarehouseAddress();
        } else {
            trip1Origin = Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().get(deliveryIndex-1).getAddress();
        }
        
        trip1Target = Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().get(deliveryIndex+1).getAddress();
        
        trip2Origin = trip1Target;
        trip2Target = Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().get(deliveryIndex).getAddress();
        
        trip3Origin = trip2Target;
        
        if(deliveryIndex == Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().size()-2){
            trip3Target = Model.getInstance().getDeliveryRequest().getWarehouseAddress();
        } else {
            trip3Target = Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().get(deliveryIndex+2).getAddress();
        }
        
        Trip trip1 = Model.getInstance().getTripBetweenIntersections(trip1Origin, trip1Target);
        Trip trip2 = Model.getInstance().getTripBetweenIntersections(trip2Origin, trip2Target);
        Trip trip3 = Model.getInstance().getTripBetweenIntersections(trip3Origin, trip3Target);
        
        
        Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().remove(deliveryIndex);
        Model.getInstance().getCircuits().get(circuitIndex).addDelivery(deliveryIndex+1,delivery);
        
        Model.getInstance().getCircuits().get(circuitIndex).getTrips().remove(deliveryIndex);
        Model.getInstance().getCircuits().get(circuitIndex).getTrips().remove(deliveryIndex);
        Model.getInstance().getCircuits().get(circuitIndex).getTrips().remove(deliveryIndex);
        Model.getInstance().getCircuits().get(circuitIndex).addTrip(deliveryIndex, trip3);
        Model.getInstance().getCircuits().get(circuitIndex).addTrip(deliveryIndex, trip2);
        Model.getInstance().getCircuits().get(circuitIndex).addTrip(deliveryIndex, trip1);
        
        Model.getInstance().getCircuits().get(circuitIndex).updateDeliveryInfos();
        
        Model.getInstance().rearrangeDeliveries();
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancel() {
        
        Model.getInstance().getCircuits().set(circuitIndex, circuit);
        Model.getInstance().rearrangeDeliveries();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}