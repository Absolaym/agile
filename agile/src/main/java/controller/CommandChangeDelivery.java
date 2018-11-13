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
public class CommandChangeDelivery implements Command {
        
    private CommandDeleteDelivery comDelDel;
    private CommandAddDelivery comAddDel;  
    
    public CommandChangeDelivery(Delivery d, Circuit oc, Circuit tc){

        comDelDel = new CommandDeleteDelivery(d, oc);
        comAddDel = new CommandAddDelivery(d, tc);
    }
    
    @Override
    public void doCde() {
        
        comDelDel.doCde();
        comAddDel.doCde();        
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void undoCde() {
        
        comAddDel.undoCde();
        comDelDel.undoCde();
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
