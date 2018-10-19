package pld.agile.view;

import java.awt.*;
import java.util.LinkedList;
import java.util.Iterator;

import javax.swing.*;

public class Window extends JFrame {

	public Window() {
        super("App Name");
        
        //Dimensions 
        this.setSize(new Dimension(1000,800));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        this.setContentPane(mainPanel);
        
        JPanel graphicalViewPanel = new GraphicalView();
        JPanel deliveryRequestPanel = new DeliveryRequest();

        mainPanel.add(graphicalViewPanel,BorderLayout.CENTER);
        mainPanel.add(deliveryRequestPanel, BorderLayout.SOUTH);
   
        this.setVisible(true);
    }

}
