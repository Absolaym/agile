/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pld.agile.view;

import controller.Controller;
import model.DeliveryRequest;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTable;
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
        if (e.getActionCommand().equals(Window.LOAD_MAP) || (e.getActionCommand().equals(Window.LOAD_NEW_MAP))) {
            JFileChooser jfc = new JFileChooser();
            int result = jfc.showOpenDialog(window);
            if (result == JFileChooser.APPROVE_OPTION) {
                controller.LoadCityMap(jfc.getSelectedFile().getAbsolutePath());
                //the button "Load a map should become invisible once the map is loaded"
                ((JButton) e.getSource()).setVisible(false);
            }
        } else if (e.getActionCommand().equals(Window.COMPUTE_CIRCUITS)) {
            controller.ComputeCircuits();
        } else if (e.getActionCommand().equals(Window.LOAD_DELIVERY_REQUESTS)) {

            try {
                JFileChooser jfc = new JFileChooser();
                int result = jfc.showOpenDialog(window);
                DeliveryRequest dr;
                if (result == JFileChooser.APPROVE_OPTION) {
                    dr = controller.LoadDeliveryRequest(jfc.getSelectedFile().getAbsolutePath());
                    System.out.println("DR:" + dr.getDeliveries().size());
                    
                    //get deliveries and send to JTable to be displayed
                    String[] deliveries = new String[dr.getDeliveries().size()];
                    for(int i =0; i<dr.getDeliveries().size(); i++) {
                        deliveries[i] = dr.getDeliveries().get(i).getAddress();
                    }
                    //faire un string[][] à partir de dr.getDeliveries()
                    window.getDeliveryRequestPanel().addDeliveries(deliveries);
                }
            } catch (Exception e2) {

            }
        }

    }

    public Controller getController() {
        return controller;
    }

    public Window getWindow() {
        return window;
    }
    
}
