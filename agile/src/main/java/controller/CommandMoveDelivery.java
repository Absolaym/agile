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
public class CommandMoveDelivery implements Command {

    private Circuit originCircuit;
    private Circuit targetCircuit;
    private Delivery delivery;
    //add elements to know how to redo
    
    public CommandMoveDelivery(Delivery d, Circuit oc, Circuit tc){
        this.delivery = d;
        this.originCircuit = oc;
        this.targetCircuit = tc;
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
