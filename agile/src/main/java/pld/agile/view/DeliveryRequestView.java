package pld.agile.view;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.*;

public class DeliveryRequestView extends JPanel {

    private JTable deliveryRequests;
    private JButton loadDeliveryRequestsButton;
    private int height = 800;
    private int width = 300;

    public DeliveryRequestView(Window w, Controller c) {
        super();
        setLayout(null);
        setBorder(BorderFactory.createTitledBorder("Delivery requests :"));
        loadDeliveryRequestsButton = new JButton("Load delivery requests");
        loadDeliveryRequestsButton.addActionListener(new ButtonListener(c));
        loadDeliveryRequestsButton.setSize(200,30);
        loadDeliveryRequestsButton.setLocation(0, 50);
        
        add(loadDeliveryRequestsButton);
        
        setBackground(Color.LIGHT_GRAY);
        w.getContentPane().add(this);
       // w.getContentPane().add(loadDeliveryRequestsButton);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    // to change
    public int getHeight() {
        return height;
    }

    // to change
    public int getWidth() {
        return width;
    }

}
