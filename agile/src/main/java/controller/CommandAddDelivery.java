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
public class CommandAddDelivery implements Command {

    private Delivery originalDelivery;
    private int originalDeliveryIndex;

    private Circuit originalCircuit;
    private int originalCircuitIndex;

    private Delivery refToAddedDelivery;

    /**
     * Create an add delivery command that adds a delivery to a circuit when
     * executed (and removed when cancelled)
     *
     * @param d The delivery added
     * @param c The circuit in which the delivery is added
     */
    public CommandAddDelivery(Delivery d, Circuit c) {

        this.originalDelivery = new Delivery(d);
        this.originalCircuit = new Circuit(c);

        this.originalDeliveryIndex = c.getDeliveries().indexOf(d);
        this.originalCircuitIndex = Model.getInstance().getCircuits().indexOf(c);
    }

    @Override
    public void execute() {

        Delivery copyOfOriginalDelivery = new Delivery(originalDelivery);
        Circuit updatedCircuit = new Circuit(originalCircuit);
        refToAddedDelivery = copyOfOriginalDelivery;

        int deliveryIndex = originalDeliveryIndex;
        int circuitIndex = originalCircuitIndex;

        Model model = Model.getInstance();

        model.addDelivery(copyOfOriginalDelivery);

        String trip1Origin = updatedCircuit.getDeliveries().getLast().getAddress();

        updatedCircuit.addDelivery(copyOfOriginalDelivery);
        updatedCircuit.getTrips().removeLast();

        String trip1Target = updatedCircuit.getDeliveries().getLast().getAddress();

        Trip trip1 = model.getTripBetweenIntersections(trip1Origin, trip1Target);

        String trip2Origin = trip1Target;
        String trip2Target = model.getDeliveryRequest().getWarehouseAddress();

        Trip trip2 = model.getTripBetweenIntersections(trip2Origin, trip2Target);

        updatedCircuit.addTrip(trip1);
        updatedCircuit.addTrip(trip2);

        updatedCircuit.updateSections();
        updatedCircuit.updateDeliveryInfos();

        model.getCircuits().set(circuitIndex, updatedCircuit);
        model.rearrangeDeliveries();
    }

    @Override
    public void cancel() {

        Model model = Model.getInstance();

        Delivery copyOfOriginalDelivery = new Delivery(originalDelivery);
        Circuit copyOfOriginalCircuit = new Circuit(originalCircuit);

        boolean removedFromDR
                = model.getDeliveryRequest().removeDelivery(refToAddedDelivery);

        copyOfOriginalCircuit.updateSections();
        copyOfOriginalCircuit.updateDeliveryInfos();

        model.getCircuits().set(originalCircuitIndex, copyOfOriginalCircuit);

        refToAddedDelivery.setCircuit(null);
        refToAddedDelivery.setIsSelected(false);

        model.rearrangeDeliveries();
    }

}
