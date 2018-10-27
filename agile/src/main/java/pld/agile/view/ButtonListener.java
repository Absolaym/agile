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
        
        String root = System.getProperty("user.dir");
        String assets = root + "/src/main/assets";
        
        if (e.getActionCommand().equals(Window.LOAD_CITY_MAP) || (e.getActionCommand().equals(Window.LOAD_NEW_CITY_MAP))) {
            
            JFileChooser jfc = new JFileChooser( assets + "/maps" );
            
            int result = jfc.showOpenDialog(window);
            if (result == JFileChooser.APPROVE_OPTION) {
                controller.loadCityMap(jfc.getSelectedFile().getAbsolutePath());
                //the button "Load aCityMap should become invisible once theCityMap is loaded"
                if(e.getActionCommand().equals(Window.LOAD_CITY_MAP)) ((JButton) e.getSource()).setVisible(false);
                window.getCityMapContainerPanel().repaint();
            }
        } else if (e.getActionCommand().equals(Window.COMPUTE_CIRCUITS)) {
            
            controller.computeCircuits();
            window.getDeliveryRequestPanel().setCircuitNumber();
            window.getCityMapContainerPanel().repaint();
            
        } else if (e.getActionCommand().equals(Window.LOAD_DELIVERY_REQUESTS)) {

            try {
                JFileChooser jfc = new JFileChooser( assets + "/deliveries" );
                int result = jfc.showOpenDialog(window);
                DeliveryRequest dr;
                if (result == JFileChooser.APPROVE_OPTION) {
                    dr = controller.loadDeliveryRequest(jfc.getSelectedFile().getAbsolutePath());
                    //get deliveries and send to JTable to be displayed
                    String[] deliveries = new String[dr.getDeliveries().size()];
                    System.out.println("size " + dr.getDeliveries().size());
                    for(int i =0; i<dr.getDeliveries().size(); i++) {
                        deliveries[i] = dr.getDeliveries().get(i).getAddress();
                    }
                    window.getDeliveryRequestPanel().addDeliveries(deliveries);
                    window.getCityMapContainerPanel().repaint();                   
                  
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
