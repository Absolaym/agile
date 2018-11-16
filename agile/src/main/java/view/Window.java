package view;

import controller.Controller;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

@SuppressWarnings("serial")
/**
 * Main class containing the view and the references to all panels. 
 * Allows communication between all the view classes and the controller.
 */
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
    protected final static String GO_BACK_TO_PREVIOUS_STEP = "Go back to previous step";
    protected final static String CANCEL_ADDING_DELIVERY = "Cancel";
    protected final static String DELETE_DELIVERY = "Delete";
    protected final static String MOVE_DELIVERY_BEFORE = "Move delivery before";
    protected final static String MOVE_DELIVERY_AFTER = "Move delivery after";
    protected final static String UNDO = "Undo";
    protected final static String REDO = "Redo";

    public Color[] colors;

    private int width;
    private int height;
    private int waitingState = 2;

    private Controller controller;

    public Window() {
        this(new Controller());
    }

    public Window(Controller controller) {
        super("Agility is the delivery");

        this.controller = controller;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        mapMenuPanel = new CityMapMenuView(this, controller);
        mapContainerPanel = new CityMapContainerView(this, controller);
        deliveryRequestPanel = new DeliveryRequestView(this, controller);
        errorAreaPanel = new ErrorAreaView(this, controller);

        //add colors
        colors = new Color[20];
        setColors(colors);

        setWindowSize();
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));

        this.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                if (e.isControlDown() || e.isMetaDown()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_Z:
                            Window.this.controller.undo();
                            break;
                        case KeyEvent.VK_Y:
                            Window.this.controller.redo();
                            break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
            }
        });

        setVisible(true);
    }
    /**
     * Sets the window size and place the elements
     */
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

    /**
     * Creates an array with all colors that will be used for the circuits
     * @param colors array with colors
     */
    public void setColors(Color[] colors) {
        colors[10] = new Color(138, 252, 235);
        colors[2] = new Color(0, 155, 0);
        colors[3] = new Color(0, 155, 216);
        colors[4] = new Color(168, 0, 216);
        colors[5] = new Color(250, 210, 197);
        colors[6] = new Color(252, 144, 241);
        colors[7] = new Color(113, 59, 241);
        colors[8] = new Color(162, 164, 70);
        colors[9] = new Color(245, 164, 70);
        colors[1] = new Color(244, 88, 101);
        colors[11] = new Color(205, 0, 0);
        colors[12] = new Color(251, 1, 161);
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
    
    /**
     * Utility method used when adding a new delivery
     * state 0 : waits for the user to choose a point on the map
     * state 1 : waits for the user to set duration and choose a circuit
     * state 2 : a new delivery is added
     * @param ws waiting state
     */
    public void setWaitingState(int ws) {
        if (ws == 0) {
            mapMenuPanel.getComputeCircuitsButton().setEnabled(false);
            mapMenuPanel.getLoadDeliveryRequestButton().setEnabled(false);
            mapMenuPanel.getLoadNewCityMapButton().setEnabled(false);
            mapMenuPanel.getAddNewDeliveryButton().setEnabled(false);

        } else if (ws == 1) {
            mapMenuPanel.addNewDelivery("time");

        } else if (ws == 2) {
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
