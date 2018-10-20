package pld.agile.view;

import controller.Controller;
import java.awt.*;
import java.util.LinkedList;
import java.util.Iterator;

import javax.swing.*;
import model.Map;

public class Window extends JFrame {
    
        private GraphicalView graphicalViewPanel;
        private DeliveryRequestView deliveryRequestPanel;
        
        protected final static String LOAD_MAP = "Load a map";
        protected final static String COMPUTE_CIRCUITS = "Compute circuits";
        protected final static String LOAD_NEW_MAP = "Load a new map";
        
	public Window(Map map, Controller controller) {
        super("App Name");
        this.setLayout(new BorderLayout());
        //Dimensions 
        this.setSize(new Dimension(1000,800)); // to do in a separate method
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Main container
//        JPanel mainPanel = new JPanel();
//        this.setContentPane(mainPanel);
        
        graphicalViewPanel = new GraphicalView(map,this,controller);
        deliveryRequestPanel = new DeliveryRequestView(this);

        getContentPane().add(graphicalViewPanel, BorderLayout.CENTER);
        getContentPane().add(deliveryRequestPanel, BorderLayout.EAST);

        this.setVisible(true);
    }

}
