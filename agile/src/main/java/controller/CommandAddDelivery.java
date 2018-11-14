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
import model.Trip;

/**
 *
 * @author pagilles
 */
public class CommandAddDelivery implements Command {

    private Delivery delivery;
    
    private Circuit circuit;
    private int circuitIndex;
    
    public CommandAddDelivery(Delivery d, Circuit c){
        this.delivery = d;
        
        this.circuit = c;
        this.circuitIndex = Model.getInstance().getCircuits().indexOf(c);
    }
    
    @Override
    public void doCde() {
        
        Model.getInstance().addDelivery(delivery);
        
        String trip1Origin = Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().getLast().getAddress();
        
        Model.getInstance().getCircuits().get(circuitIndex).addDelivery(delivery);
        Model.getInstance().getCircuits().get(circuitIndex).getTrips().removeLast();
        
        
        String trip1Target = Model.getInstance().getCircuits().get(circuitIndex).getDeliveries().getLast().getAddress();
        Trip trip1 = Model.getInstance().getTripBetweenIntersections(trip1Origin, trip1Target);
        
        
        String trip2Origin = trip1Target;
        String trip2Target = Model.getInstance().getDeliveryRequest().getWarehouseAddress();
        Trip trip2 = Model.getInstance().getTripBetweenIntersections(trip2Origin, trip2Target);

        
        Model.getInstance().getCircuits().get(circuitIndex).addTrip(trip1);
        Model.getInstance().getCircuits().get(circuitIndex).addTrip(trip2);
        
        Model.getInstance().getCircuits().get(circuitIndex).updateDeliveryInfos();
        Model.getInstance().rearrangeDeliveries();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void undoCde() {
        Model.getInstance().getDeliveryRequest().removeDelivery(delivery);
        Model.getInstance().getCircuits().set(circuitIndex, circuit);
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
