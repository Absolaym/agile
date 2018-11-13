/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Circuit;
import model.Delivery;

/**
 *
 * @author pagilles
 */
public class CommandAddDeliveryToDeliveryRequest implements Command {

    private Delivery delivery;
    private Circuit circuit;
    
    public CommandAddDeliveryToDeliveryRequest(Delivery d, Circuit c){
        this.delivery = d;
        this.circuit = c;
    }
    
    @Override
    public void doCde() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void undoCde() {
        //circuit.getDeliveries().removeLast();
        // remove last 2 trips + recreate last trip
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
