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
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
            
            JFileChooser jfc = new JFileChooser( assets + "/maps/" );
            
           // int result = jfc.showOpenDialog(window);
           // if (result == JFileChooser.APPROVE_OPTION) {
                //controller.loadCityMap(jfc.getSelectedFile().getAbsolutePath());
                controller.loadCityMap(assets +"/maps/moyenPlan.xml");
                //the button "Load aCityMap should become invisible once theCityMap is loaded"
             
                window.getCityMapContainerPanel().getLoadCityMapButton().setVisible(false);
                window.getCityMapMenuPanel().getLoadNewCityMapButton().setEnabled(true);
                window.getCityMapMenuPanel().getLoadDeliveryRequestButton().setEnabled(true);
                
                window.getCityMapContainerPanel().repaint();
                
          //  }
        }  else if (e.getActionCommand().equals(Window.LOAD_DELIVERY_REQUESTS)) {

            try {
                    JFileChooser jfc = new JFileChooser( assets + "/deliveries" );
                    int result = jfc.showOpenDialog(window);
                    
                    if (result == JFileChooser.APPROVE_OPTION) {
                        controller.loadDeliveryRequest(jfc.getSelectedFile().getAbsolutePath());
                
                        //controller.loadDeliveryRequest( assets + "/deliveries/dl-petit-3.xml" );
                        DeliveryRequest deliveryRequest;
                        deliveryRequest = controller.getModel().getDeliveryRequest();
                        //get deliveries and send to JTable to be displayed
//                        window.getCityMapMenuPanel().getComputeCircuitsButton().setEnabled(true);
//                        window.getCityMapMenuPanel().getAddNewDeliveryButton().setEnabled(true);
                        System.out.println("dfsji " + (deliveryRequest==null));
                        // PROBLEM HERE
                        if(deliveryRequest != null){
//                            window.getDeliveryRequestPanel().addDeliveries();
                            System.out.println("uiejwuw");
                            //window.getDeliveryRequestPanel().addToTable(); 
                            window.getCityMapMenuPanel().getComputeCircuitsButton().setEnabled(true);
                            window.getCityMapMenuPanel().getAddNewDeliveryButton().setEnabled(true);
                            System.out.println("dfej93wei");
                        }
                        window.getCityMapContainerPanel().repaint();                   
                    }
            } catch (Exception e2) {

           }
        }else if (e.getActionCommand().equals(Window.COMPUTE_CIRCUITS)) {
            int numberOfCouriers = window.getCityMapMenuPanel().getCourierNumber();
            controller.computeCircuits(numberOfCouriers);
            //window.getDeliveryRequestPanel().setCircuitNumber();
            window.getCityMapContainerPanel().repaint();
            
        }
        else if (e.getActionCommand().equals(Window.ADD_DELIVERY)) {
            window.getCityMapMenuPanel().addNewDelivery("select");
            window.setWaitingState(true);
        }

    }

    public Controller getController() {
        return controller;
    }

    public Window getWindow() {
        return window;
    }
}