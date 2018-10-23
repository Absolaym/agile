package pld.agile.view;

import controller.Controller;
import java.awt.*;
import java.util.LinkedList;
import java.util.Iterator;

import javax.swing.*;
import model.DeliveryRequest;
import model.Plan;

public class Window extends JFrame {

    // Subcomponents of the view
    private MapMenuView mapMenuPanel;
    private MapContainerView mapContainerPanel;
    private DeliveryRequestView deliveryRequestPanel;

    //list of buttons
    protected final static String LOAD_MAP = "Load a map";
    protected final static String COMPUTE_CIRCUITS = "Compute circuits";
    protected final static String LOAD_NEW_MAP = "Load a new map";
    protected final static String LOAD_DELIVERY_REQUESTS = "Load delivery requests";
    
    private int width;
    private int height;

    public Window() {
        this(new Controller());
    }

    // It's not the responsibility of the window to keep track of the map and deliveryRequest
    // This should be placed in the controller
    public Window(Controller controller) {
        super("Agility is the delivery");
        //this.setLayout(new BorderLayout());
        //Dimensions 
        //setSize(new Dimension(1000, 800)); // to do in a separate method
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(null);
        mapMenuPanel = new MapMenuView(this, controller);
        mapContainerPanel = new MapContainerView(this, controller);
        deliveryRequestPanel = new DeliveryRequestView(this, controller);

        setWindowSize();
        setSize(width, height);
        //setSize(1000,800);
        setVisible(true);
    }

    private void setWindowSize() {
        height = Math.max(mapMenuPanel.getHeight() + mapContainerPanel.getHeight(), deliveryRequestPanel.getHeight()) + 30;
        width = Math.max(mapMenuPanel.getWidth(), mapContainerPanel.getWidth()) + deliveryRequestPanel.getWidth() + 10;

        mapMenuPanel.setLocation(10, 10);
        mapContainerPanel.setLocation(10, 20 + mapMenuPanel.getHeight());
        deliveryRequestPanel.setLocation(10 + Math.max(mapMenuPanel.getWidth(), mapContainerPanel.getWidth()), 10);

        mapMenuPanel.setSize(mapMenuPanel.getWidth(), mapMenuPanel.getHeight());
        mapContainerPanel.setSize(mapContainerPanel.getWidth(), mapContainerPanel.getHeight());
        deliveryRequestPanel.setSize(deliveryRequestPanel.getWidth(), deliveryRequestPanel.getHeight());
    }

    public MapMenuView getMapMenuPanel() {
        return mapMenuPanel;
    }

    public MapContainerView getMapContainerPanel() {
        return mapContainerPanel;
    }

    public DeliveryRequestView getDeliveryRequestPanel() {
        return deliveryRequestPanel;
    }
 
    

}
