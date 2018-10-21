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

    private final JTable deliveryRequests;
    private JButton loadDeliveryRequestsButton;
    private final int loadDeliveryRequestsButtonWidth = 200;
    private final int loadDeliveryRequestsButtonHeight = 30;
    private final int buttonLocationY = 50;
    private int height = 800;
    private int width = 300;

    public DeliveryRequestView(Window w, Controller c) {
        super();
        setLayout(null);
        
        //set button
        setBorder(BorderFactory.createTitledBorder("Delivery requests :"));
        loadDeliveryRequestsButton = new JButton("Load delivery requests");
        loadDeliveryRequestsButton.addActionListener(new ButtonListener(c));
        loadDeliveryRequestsButton.setSize(loadDeliveryRequestsButtonWidth,loadDeliveryRequestsButtonHeight);
        loadDeliveryRequestsButton.setLocation(0, buttonLocationY);
        add(loadDeliveryRequestsButton);
        
        //set table
        String column[] = {"Address", "Delivery time", "Circuit"};
        final String data[][]={ {"101","12:00","1"},    
                          {"102","14:50","2"},    
                          {"101","15:02","7"}};
        deliveryRequests = new JTable(data,column) {
             public boolean isCellEditable(int row,int column){
                return false;}
        };
        JScrollPane scrollPanel = new JScrollPane(deliveryRequests); 
        scrollPanel.setBounds(30,40,width - 10, height - 10);
        scrollPanel.setLocation(0, 50 + loadDeliveryRequestsButtonHeight + 50);
        add(scrollPanel);
        
        setBackground(Color.LIGHT_GRAY);
        w.getContentPane().add(this);

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
