/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import error.ErrorLogger;
import error.ProjectError;
import model.DeliveryRequest;
import model.CityMap;
import model.Delivery;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;

/**
 * This class is used to implement the action listeners when the main buttons are clicked.
 */
public class ButtonListener implements ActionListener {

    private Controller controller;
    private Window window;
    private Delivery delivery;

    public ButtonListener(Controller c, Window w, Delivery d) {
        this.controller = c;
        this.window = w;
        this.delivery = d;
    }

    public ButtonListener(Controller c, Window w) {
        this.controller = c;
        this.window = w;
        this.delivery = null;
    }

    public void actionPerformed(ActionEvent e) {

        String root = System.getProperty("user.dir");
        String assets = root + "/src/main/assets";

        if (e.getActionCommand().equals(Window.LOAD_CITY_MAP) || (e.getActionCommand().equals(Window.LOAD_NEW_CITY_MAP))) {

            window.getCityMapMenuPanel().addNewDelivery("cancel");

            JFileChooser jfc = new JFileChooser(assets + "/maps/");

            int result = jfc.showOpenDialog(window);
            if (result == JFileChooser.APPROVE_OPTION) {
                controller.loadCityMap(jfc.getSelectedFile().getAbsolutePath());
                CityMap cityMap = controller.getModel().getCityMap();
                if (!cityMap.isEmpty()) {
                    window.getCityMapContainerPanel().getLoadCityMapButton().setVisible(false);
                    window.getCityMapMenuPanel().getLoadNewCityMapButton().setEnabled(true);
                    window.getCityMapMenuPanel().getLoadDeliveryRequestButton().setEnabled(true);
                    window.getCityMapMenuPanel().getComputeCircuitsButton().setEnabled(false);
                    window.getCityMapMenuPanel().getAddNewDeliveryButton().setEnabled(false);
                    window.getCityMapMenuPanel().getUndoButton().setEnabled(false);
                    window.getCityMapMenuPanel().getRedoButton().setEnabled(false);
                    window.getDeliveryRequestPanel().loadDeliveryRequest(window);
                    window.getCityMapContainerPanel().repaint();
                }

            }
        } else if (e.getActionCommand().equals(Window.LOAD_DELIVERY_REQUESTS)) {

            window.getCityMapMenuPanel().addNewDelivery("cancel");

            try {
                JFileChooser jfc = new JFileChooser(assets + "/deliveries");
                int result = jfc.showOpenDialog(window);

                if (result == JFileChooser.APPROVE_OPTION) {
                    controller.loadDeliveryRequest(jfc.getSelectedFile().getAbsolutePath());
                    DeliveryRequest deliveryRequest;
                    deliveryRequest = controller.getModel().getDeliveryRequest();
                    window.getCityMapContainerPanel().setNewDeliveryIntersection(null);
                    window.getCityMapContainerPanel().setNewDelivery(null);

                    if (deliveryRequest != null) {
                        window.getCityMapMenuPanel().getComputeCircuitsButton().setEnabled(true);
                        window.getDeliveryRequestPanel().loadDeliveryRequest(window);
                        window.getCityMapMenuPanel().getAddNewDeliveryButton().setEnabled(false);
                        window.getCityMapMenuPanel().getUndoButton().setEnabled(false);
                        window.getCityMapMenuPanel().getRedoButton().setEnabled(false);
                    }
                    window.getCityMapContainerPanel().repaint();
                    window.getDeliveryRequestPanel().repaint();
                    window.repaint();
                }
            } catch (Exception e2) {
                System.out.print(e2.toString());
            }
        } else if (e.getActionCommand().equals(Window.COMPUTE_CIRCUITS)) {
            int numberOfCouriers = window.getCityMapMenuPanel().getCourierNumber();
            controller.computeCircuits(numberOfCouriers);
            window.getCityMapMenuPanel().getAddNewDeliveryButton().setEnabled(true);
            //window.getDeliveryRequestPanel().setCircuitNumber();
            window.getCityMapContainerPanel().repaint();
            window.getDeliveryRequestPanel().loadDeliveryRequest(window);
            window.getCityMapMenuPanel().getUndoButton().setEnabled(false);
            window.getCityMapMenuPanel().getRedoButton().setEnabled(false);
            System.out.println("computed circuits");
        } else if (e.getActionCommand().equals(Window.ADD_DELIVERY)) {
            window.getCityMapMenuPanel().addNewDelivery("select");
            window.setWaitingState(0);
            window.getDeliveryRequestPanel().loadDeliveryRequest(window);
            updateUndoRedoButtons();
            window.repaint();
        } else if (e.getActionCommand().equals(Window.GO_BACK_TO_PREVIOUS_STEP)) {
            window.getCityMapMenuPanel().addNewDelivery("select");
            window.setWaitingState(0);
            window.getDeliveryRequestPanel().loadDeliveryRequest(window);
            updateUndoRedoButtons();
            window.repaint();
        } else if (e.getActionCommand().equals(Window.CANCEL_ADDING_DELIVERY)) {

            window.getCityMapContainerPanel().setNewDeliveryIntersection(null);
            window.getCityMapMenuPanel().addNewDelivery("cancel");
            window.setWaitingState(0);
            window.getCityMapMenuPanel().getLoadNewCityMapButton().setEnabled(true);
            window.getCityMapMenuPanel().getLoadDeliveryRequestButton().setEnabled(true);
            window.getCityMapMenuPanel().getAddNewDeliveryButton().setEnabled(true);
            updateUndoRedoButtons();
            window.repaint();
        } else if (e.getActionCommand().equals(Window.DELETE_DELIVERY)) {
            window.getCityMapContainerPanel().setNewDeliveryIntersection(null);
            controller.deleteDelivery(this.delivery, this.delivery.getCircuit());
            window.getDeliveryRequestPanel().loadDeliveryRequest(window);
            updateUndoRedoButtons();
            System.out.println("delete delivery");
        } else if (e.getActionCommand().equals(Window.MOVE_DELIVERY_BEFORE)) {
            controller.moveDeliveryBefore(delivery, this.delivery.getCircuit());
            window.getDeliveryRequestPanel().loadDeliveryRequest(window);
            updateUndoRedoButtons();
            window.repaint();
            System.out.println("move delivery before");
        } else if (e.getActionCommand().equals(Window.MOVE_DELIVERY_AFTER)) {
            controller.moveDeliveryAfter(delivery, this.delivery.getCircuit());
            window.getDeliveryRequestPanel().loadDeliveryRequest(window);
            updateUndoRedoButtons();
            window.repaint();
            System.out.println("move delivery after");
        } else if (e.getActionCommand().equals(Window.UNDO)) {
            controller.undo();
            updateUndoRedoButtons();
            window.getDeliveryRequestPanel().loadDeliveryRequest(window);
            window.repaint();
            System.out.println("undo");
        } else if (e.getActionCommand().equals(Window.REDO)) {
            controller.redo();
            updateUndoRedoButtons();
            window.getDeliveryRequestPanel().loadDeliveryRequest(window);
            window.repaint();
            System.out.println("redo");
        }

    }

    public Controller getController() {
        return controller;
    }

    public Window getWindow() {
        return window;
    }
    
    /**
     * this method checks whether the undo and redo buttons must be enabled
     * and updates the user interface as needed.
     */
    private void updateUndoRedoButtons() {
        if (controller.canUndo()) {
            window.getCityMapMenuPanel().getUndoButton().setEnabled(true);
        } else {
            window.getCityMapMenuPanel().getUndoButton().setEnabled(false);
        }
        if (controller.canRedo()) {
            window.getCityMapMenuPanel().getRedoButton().setEnabled(true);
        } else {
            window.getCityMapMenuPanel().getRedoButton().setEnabled(false);
        }
    }
}
