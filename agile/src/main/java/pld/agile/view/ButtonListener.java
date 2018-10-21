/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pld.agile.view;

import controller.Controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author olivi
 */
public class ButtonListener implements ActionListener {
    
    private Controller controller;
    
    public ButtonListener(Controller c) {
        this.controller = c;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(Window.LOAD_MAP)) controller.loadMap();
        else if (e.getActionCommand().equals(Window.COMPUTE_CIRCUITS)) controller.computeCircuits();
        else if (e.getActionCommand().equals(Window.LOAD_NEW_MAP)) controller.loadMap();
        else if (e.getActionCommand().equals(Window.LOAD_DELIVERY_REQUESTS)) controller.loadDeliveryRequests();
        
    }
    
}
