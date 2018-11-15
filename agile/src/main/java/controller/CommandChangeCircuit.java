/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Circuit;
import model.Delivery;
import model.Model;

/**
 *
 * @author pagilles
 */
public class CommandChangeCircuit implements Command {

	private CommandDeleteDelivery comDelDel;
	private CommandAddDelivery comAddDel;  

	/**
	 * Create a move delivery command that moves a delivery from a circuit to an other when executed (and revert it when cancelled)
	 * @param d The delivery moved
	 * @param oc The circuit in which the delivery were
	 * @param tc The circuit in which the delivery will be
	 */
	public CommandChangeCircuit(Delivery d, Circuit oc, Circuit tc){

		comDelDel = new CommandDeleteDelivery(d, oc);
		comAddDel = new CommandAddDelivery(d, tc);
	}

	@Override
	public void execute() {

		comDelDel.execute();
		comAddDel.execute();        

	}

	@Override
	public void cancel() {

		comAddDel.cancel();
		comDelDel.cancel();

	}

}
