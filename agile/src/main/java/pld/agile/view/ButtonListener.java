/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pld.agile.view;

import controller.Controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;

/**
 *
 * @author olivi
 */
public class ButtonListener implements ActionListener {
    
    private Controller controller;
    private Window window;
    
    public ButtonListener(Controller c, Window w) {
        this.controller = c;
        this.window = w;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(Window.LOAD_MAP)||(e.getActionCommand().equals(Window.LOAD_NEW_MAP))) {
        	JFileChooser jfc = new JFileChooser();
        	int result = jfc.showOpenDialog(window);
        	if(result==JFileChooser.APPROVE_OPTION)
        		controller.loadMap(jfc.getSelectedFile().getAbsolutePath());
        }
        else if (e.getActionCommand().equals(Window.COMPUTE_CIRCUITS)) controller.computeCircuits();
        else if (e.getActionCommand().equals(Window.LOAD_DELIVERY_REQUESTS)) controller.loadDeliveryRequests();
        
    }
    
}
