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

	/**
	 * Create an add delivery command that adds a delivery to a circuit when executed (and removed when cancelled)
	 * @param d The delivery added
	 * @param c The circuit in which the delivery is added
	 */
	public CommandAddDelivery(Delivery d, Circuit c){
		this.delivery = d;

		this.circuit = c;
		this.circuitIndex = Model.getInstance().getCircuits().indexOf(c);
	}

	@Override
	public void execute() {

		Model model = Model.getInstance();

		model.addDelivery(delivery);

		String trip1Origin = model.getCircuits().get(circuitIndex).getDeliveries().getLast().getAddress();

		model.getCircuits().get(circuitIndex).addDelivery(delivery);
		model.getCircuits().get(circuitIndex).getTrips().removeLast();


		String trip1Target = model.getCircuits().get(circuitIndex).getDeliveries().getLast().getAddress();
		Trip trip1 = model.getTripBetweenIntersections(trip1Origin, trip1Target);


		String trip2Origin = trip1Target;
		String trip2Target = model.getDeliveryRequest().getWarehouseAddress();
		Trip trip2 = model.getTripBetweenIntersections(trip2Origin, trip2Target);


		model.getCircuits().get(circuitIndex).addTrip(trip1);
		model.getCircuits().get(circuitIndex).addTrip(trip2);

		model.getCircuits().get(circuitIndex).updateDeliveryInfos();

	}

	@Override
	public void cancel() {
		Model model = Model.getInstance();
		model.getDeliveryRequest().removeDelivery(delivery);
		model.getCircuits().set(circuitIndex, circuit);
	}

}
