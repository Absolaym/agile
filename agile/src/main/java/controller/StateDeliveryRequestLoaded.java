/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.DeliveryRequest;
import model.Model;
import utils.XmlParser;

/**
 *
 * @author lgalle
 */
public class StateDeliveryRequestLoaded extends StateDefault {

    public void loadDeliveryRequest(String path, Controller c) {
        try {
            Model model = c.getModel();
            DeliveryRequest deliveryRequest = null;
            XmlParser parser = new XmlParser();

            deliveryRequest = parser.parseDeliveryRequest(path);
            if (deliveryRequest != null) {
                deliveryRequest.computeDeliveryRequestGeolocation(model.getCityMap());
                model.setDeliveryRequest(deliveryRequest);
                model.computeShortestPaths();
                model.setCircuits(null);
                c.setState(c.STATE_DELIVERYREQUEST_LOADED);
            }
        } catch (Exception e) {

        }
    }

    public void computeCircuits(Controller c) {
        Model model = c.getModel();
        model.computeCircuits();
        Controller.commandsList = new CommandsList();
        c.setState(c.STATE_CIRCUITS_COMPUTED);
    }
    
}
