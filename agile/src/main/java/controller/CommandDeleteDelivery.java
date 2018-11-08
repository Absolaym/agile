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
public class CommandDeleteDelivery implements Command {

    private Delivery delivery;
    private Circuit circuit;
    //add elements to know how to redo
    
    public CommandDeleteDelivery(Circuit c, Delivery d){
        this.delivery = d;
        this.circuit = c;
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
