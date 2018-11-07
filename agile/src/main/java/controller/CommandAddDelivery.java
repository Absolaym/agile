/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Delivery;
import model.DeliveryRequest;

/**
 *
 * @author pagilles
 */
public class CommandAddDelivery implements Command {

    private Delivery delivery;
    private DeliveryRequest deliveryRequest;
    
    public CommandAddDelivery(DeliveryRequest dr, Delivery d){
        this.delivery = d;
        this.deliveryRequest = dr;
    }
    
    @Override
    public void doCde() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void undoCde() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
