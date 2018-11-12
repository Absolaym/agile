package pld.agile.view;

import controller.Controller;
import java.awt.Dimension;

import javax.swing.*;

public class Window extends JFrame {

    // Subcomponents of the view
    private CityMapMenuView mapMenuPanel;
    private CityMapContainerView mapContainerPanel;
    private DeliveryRequestView deliveryRequestPanel;
    private ErrorAreaView errorAreaPanel;

    //list of buttons
    protected final static String LOAD_CITY_MAP = "Load a city map";
    protected final static String COMPUTE_CIRCUITS = "Compute circuits";
    protected final static String LOAD_NEW_CITY_MAP = "Load a new city map";
    protected final static String LOAD_DELIVERY_REQUESTS = "Load delivery requests";
    protected final static String ADD_DELIVERY = "Add a delivery";
    protected final static String DELETE_DELIVERY = "Delete";
    protected final static String MOVE_DELIVERY_BEFORE = "Move delivery before";
    protected final static String MOVE_DELIVERY_AFTER = "Move delivery after";

    
    
    private int width;
    private int height;
    private int waitingState = 2;

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
        mapMenuPanel = new CityMapMenuView(this, controller);
        mapContainerPanel = new CityMapContainerView(this, controller);
        deliveryRequestPanel = new DeliveryRequestView(this, controller);
        errorAreaPanel = new ErrorAreaView(this, controller);

        setWindowSize();
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        //setSize(1000,800);
        setVisible(true);
    }

    private void setWindowSize() {
        height = Math.max(mapMenuPanel.getHeight() + mapContainerPanel.getHeight() + errorAreaPanel.getHeight(), deliveryRequestPanel.getHeight()) + 30;
        width = Math.max(mapMenuPanel.getWidth(), mapContainerPanel.getWidth()) + deliveryRequestPanel.getWidth() + 50;

        mapMenuPanel.setLocation(10, 10);
        mapContainerPanel.setLocation(10, 20 + mapMenuPanel.getHeight());
        errorAreaPanel.setLocation(10, 20 + mapContainerPanel.getHeight() + mapMenuPanel.getHeight());
        deliveryRequestPanel.setLocation(10 + Math.max(mapMenuPanel.getWidth(), mapContainerPanel.getWidth()), 10);

        mapMenuPanel.setSize(mapMenuPanel.getWidth(), mapMenuPanel.getHeight());
        mapContainerPanel.setSize(mapContainerPanel.getWidth(), mapContainerPanel.getHeight());
        deliveryRequestPanel.setSize(deliveryRequestPanel.getWidth(), deliveryRequestPanel.getHeight());
    }

    public CityMapMenuView getCityMapMenuPanel() {
        return mapMenuPanel;
    }

    public CityMapContainerView getCityMapContainerPanel() {
        return mapContainerPanel;
    }

    public DeliveryRequestView getDeliveryRequestPanel() {
        return deliveryRequestPanel;
    }

    public ErrorAreaView getErrorAreaPanel() {
        return errorAreaPanel;
    }
    
    public void setWaitingState(int ws) {
        if (ws == 0){
            mapMenuPanel.getComputeCircuitsButton().setEnabled(false);
            mapMenuPanel.getLoadDeliveryRequestButton().setEnabled(false);
            mapMenuPanel.getLoadNewCityMapButton().setEnabled(false);
            mapMenuPanel.getAddNewDeliveryButton().setEnabled(false);
            
        } else if (ws == 1){
            mapMenuPanel.addNewDelivery("time");
            
        } else if (ws == 2){
            mapMenuPanel.addNewDelivery("");
            mapMenuPanel.getComputeCircuitsButton().setEnabled(true);
            mapMenuPanel.getLoadDeliveryRequestButton().setEnabled(true);
            mapMenuPanel.getLoadNewCityMapButton().setEnabled(true);
            mapMenuPanel.getAddNewDeliveryButton().setEnabled(true);
        }
        mapMenuPanel.repaint();
        waitingState = ws;
    }
    
    public int getWaitingState() {
        return waitingState;
    }
}
