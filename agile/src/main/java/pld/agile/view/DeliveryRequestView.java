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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Circuit;
import model.Delivery;
import model.DeliveryRequest;
import utils.Time;

public class DeliveryRequestView extends JPanel implements Observer {

    private JTable deliveriesTable;
    private DefaultTableModel tabModel;
    
    private Controller controller;
    private final int buttonLocationY = 50;
    private final int spaceElements = 30;
    private final int height = 800;
    private final int width = 400;
    
    static class Renderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(null);
            Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            for(int i=0; i<table.getRowCount(); i++) {
                if (!table.getValueAt(i, 3).equals("unknown")){
                    int circuitNb = (int)table.getValueAt(i, 3) - 1;
                    if (row == i)
                        setBackground(new Color(180, Math.floorMod(50 + 40 * circuitNb, 100), Math.floorMod(120 + 40 * circuitNb, 100)));
                }
            }
            return tableCellRendererComponent;
        }
  
    }
    
    public DeliveryRequestView(Window w, Controller c) {
        super();
        controller = c;

        setLayout(null);
        //set button
        setBorder(BorderFactory.createTitledBorder("Delivery requests :"));
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
        tabModel.addColumn("Arrival time");
        tabModel.addColumn("Departure time");
        tabModel.addColumn("Circuit");

        deliveriesTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPanel = new JScrollPane(deliveriesTable);
        scrollPanel.setBounds(30, 40, width - 30, height - 10);
        scrollPanel.setLocation(0, spaceElements);
        add(scrollPanel);
    }

    public void addDeliveries() {
        DeliveryRequest dr = controller.getModel().getDeliveryRequest();
        String[] deliveries = new String[dr.getDeliveries().size()];
        for (int i = 0; i < dr.getDeliveries().size(); i++) {
            deliveries[i] = dr.getDeliveries().get(i).getAddress();
        }
        emptyTable();
        for (int i = 0; i < deliveries.length; i++) {
            tabModel.addRow(new String[]{deliveries[i], "unknown", "unknown", "unknown"});
        }
      
    }

    public void emptyTable() {
        tabModel.setRowCount(0);
    }
    
    public void setCircuitNumber(){
        LinkedList<Circuit> circuits = controller.getModel().getCircuits();
        if (circuits == null) return;

        for (Circuit circuit : circuits) {
            int i = 1;
            
            int departureTimeSec = circuit.getDepartureTime().time;

            int j = 0;
            for (Delivery deliv : circuit.getDeliveries()) {
                
                for (int row = 0; row < tabModel.getRowCount(); row++) {
                    if(deliv.getAddress().equals(tabModel.getValueAt(row, 0))) {
                        tabModel.setValueAt(i, row, 3);

                        departureTimeSec += circuit.getTrips().get(j).getLength() / (Circuit.SPEED / 3.6);
                        tabModel.setValueAt(new Time((int)departureTimeSec).toString(), row, 1);
                        departureTimeSec += deliv.getDuration() * 60;
                    }
                }
                j++;
            }
            i++;
        }
        Renderer cellRenderer = new Renderer();
        deliveriesTable.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        deliveriesTable.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        deliveriesTable.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
        deliveriesTable.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
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
}
