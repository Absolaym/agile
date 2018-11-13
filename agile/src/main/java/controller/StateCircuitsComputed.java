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

	}

	public void addDeliveryToComputedCircuit(Circuit c, Delivery d) {
		// add code to add delivery to chosen circuit

		Controller.getInstance().commandsList.addCommand(new CommandAddDeliveryToDeliveryRequest(c, d));
	}

	public void deleteDelivery(Circuit c, Delivery d) {
		// add code to delete delivery from given circuit

		Controller.getInstance().commandsList.addCommand(new CommandDeleteDelivery(d, c));
	}

	public void moveDelivery(Delivery d, Circuit oc, Circuit tc) {
		// add code to move delivery from a circuit to another

		Controller.getInstance().commandsList.addCommand(new CommandChangeDelivery(d, oc, tc));
	}

	public void undoCde() {
		Controller.getInstance().commandsList.undo();
	}

	public void redoCde() {
		Controller.getInstance().commandsList.redo();
	}
}
