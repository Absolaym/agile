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

    private Delivery originalDelivery;
    private int originalDeliveryIndex;

    private Circuit originalCircuit;
    private int originalCircuitIndex;

    private Delivery refToDeletedDelivery;

    public CommandDeleteDelivery(Delivery d, Circuit c) {

        this.originalDelivery = new Delivery(d);
        this.originalCircuit = new Circuit(c);

        this.originalDeliveryIndex = c.getDeliveries().indexOf(d);
        this.originalCircuitIndex = Model.getInstance().getCircuits().indexOf(c);

        refToDeletedDelivery = d;
    }

    @Override
    public void execute() {

        Delivery updatedDelivery = new Delivery(originalDelivery);
        Circuit updatedCircuit = new Circuit(originalCircuit);

        int deliveryIndex = originalDeliveryIndex;
        int circuitIndex = originalCircuitIndex;

        Model model = Model.getInstance();

        String tripOrigin;
        String tripTarget;

        if (deliveryIndex == 0) {
            tripOrigin = model.getDeliveryRequest().getWarehouseAddress();
        } else {
            tripOrigin = updatedCircuit.getDeliveries().get(deliveryIndex - 1).getAddress();
        }

        if (deliveryIndex == updatedCircuit.getDeliveries().size() - 1) {
            tripTarget = model.getDeliveryRequest().getWarehouseAddress();
        } else {
            tripTarget = updatedCircuit.getDeliveries().get(deliveryIndex + 1).getAddress();
        }

        boolean removedFromDR
                = model.getDeliveryRequest().removeDelivery(refToDeletedDelivery);

        updatedCircuit.getDeliveries().remove(deliveryIndex);
        updatedCircuit.getTrips().remove(deliveryIndex);
        updatedCircuit.getTrips().remove(deliveryIndex);

        Trip trip = model.getTripBetweenIntersections(tripOrigin, tripTarget);
        updatedCircuit.addTrip(deliveryIndex, trip);

        updatedCircuit.updateSections();
        updatedCircuit.updateDeliveryInfos();

        model.getCircuits().set(circuitIndex, updatedCircuit);
        model.rearrangeDeliveries();

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cancel() {

        Model model = Model.getInstance();

        Delivery copyOfOriginalDelivery = new Delivery(originalDelivery);
        Circuit copyOfOriginalCircuit = new Circuit(originalCircuit);

        model.getDeliveryRequest().addDelivery(copyOfOriginalDelivery);
        refToDeletedDelivery = copyOfOriginalDelivery;

        copyOfOriginalCircuit.updateSections();
        copyOfOriginalCircuit.updateDeliveryInfos();

        model.getCircuits().set(originalCircuitIndex, copyOfOriginalCircuit);

        model.rearrangeDeliveries();

        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
