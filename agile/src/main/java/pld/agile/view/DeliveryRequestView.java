package pld.agile.view;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.DeliveryRequest;

public class DeliveryRequestView extends JPanel implements Observer {

    private JTable deliveriesTable;
    private DefaultTableModel tabModel;
    private JButton loadDeliveryRequestsButton;
    public String[][] deliveries;
    
    private final int loadDeliveryRequestsButtonWidth = 200;
    private final int loadDeliveryRequestsButtonHeight = 30;
    private final int buttonLocationY = 50;
    private final int spaceElements = 30;
    private final int height = 800;
    private final int width = 300;
    

    public DeliveryRequestView(Window w, Controller c) {
        super();
        setLayout(null);

        //set button
        setBorder(BorderFactory.createTitledBorder("Delivery requests :"));
        loadDeliveryRequestsButton = new JButton("Load delivery requests");
        loadDeliveryRequestsButton.addActionListener(new ButtonListener(c,w));
        loadDeliveryRequestsButton.setSize(loadDeliveryRequestsButtonWidth, loadDeliveryRequestsButtonHeight);
        loadDeliveryRequestsButton.setLocation(0, buttonLocationY);
        add(loadDeliveryRequestsButton);

        createTable();
  
        setBackground(Color.LIGHT_GRAY);
        w.getContentPane().add(this);

    }
    
    public void createTable() {
        tabModel = new DefaultTableModel(); 
        deliveriesTable = new JTable(tabModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabModel.addColumn("Address");
        tabModel.addColumn("Delivery time");
        tabModel.addColumn("Circuit");
        
        deliveriesTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPanel = new JScrollPane(deliveriesTable);
        scrollPanel.setBounds(30, 40, width-30, height - 10);
        scrollPanel.setLocation(0, spaceElements + loadDeliveryRequestsButtonHeight + buttonLocationY);
        add(scrollPanel);
    }
   
    public void addDeliveries(String[] deliv){
        for(int i=0; i<deliv.length; i++) {
           tabModel.addRow(new String[]{deliv[i],"unknown","unknown"});
        }
        
    }
    
    public void setCircuitNumber(){
        tabModel.setValueAt("12:00", 1, 2);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public JTable getDeliveriesTable() {
        return deliveriesTable;
    }

    public DefaultTableModel getTabModel() {
        return tabModel;
    }

    public String[][] getDeliveries() {
        return deliveries;
    }
    
}
