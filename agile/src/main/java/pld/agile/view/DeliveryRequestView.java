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
import model.Circuit;
import model.Delivery;
import model.DeliveryRequest;

public class DeliveryRequestView extends JPanel implements Observer {

    private JTable deliveriesTable;
    private DefaultTableModel tabModel;
    private JButton loadDeliveryRequestButton;
    private Controller controller;

    private final int loadDeliveryRequestsButtonWidth = 200;
    private final int loadDeliveryRequestsButtonHeight = 30;
    private final int buttonLocationY = 50;
    private final int spaceElements = 30;
    private final int height = 800;
    private final int width = 300;

    public DeliveryRequestView(Window w, Controller c) {
        super();
        controller = c;

        setLayout(null);

        //set button
        setBorder(BorderFactory.createTitledBorder("Delivery requests :"));
        loadDeliveryRequestButton = new JButton("Load delivery requests");
        loadDeliveryRequestButton.addActionListener(new ButtonListener(c, w));
        loadDeliveryRequestButton.setSize(loadDeliveryRequestsButtonWidth, loadDeliveryRequestsButtonHeight);
        loadDeliveryRequestButton.setLocation(0, buttonLocationY);
        loadDeliveryRequestButton.setEnabled(false);
        add(loadDeliveryRequestButton);

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
        scrollPanel.setBounds(30, 40, width - 30, height - 10);
        scrollPanel.setLocation(0, spaceElements + loadDeliveryRequestsButtonHeight + buttonLocationY);
        add(scrollPanel);
    }

    public void addDeliveries() {
        DeliveryRequest dr = controller.getDeliveryRequest();
        String[] deliveries = new String[dr.getDeliveries().size()];
        System.out.println("size " + dr.getDeliveries().size());
        for (int i = 0; i < dr.getDeliveries().size(); i++) {
            deliveries[i] = dr.getDeliveries().get(i).getAddress();
        }
        emptyTable();
        for (int i = 0; i < deliveries.length; i++) {
            tabModel.addRow(new String[]{deliveries[i], "unknown", "unknown"});
        }
    }

    public void emptyTable() {
        tabModel.setRowCount(0);
    }

    public void setCircuitNumber() {
        LinkedList<Circuit> circuits = controller.getCircuits();
        if (circuits == null) {
            return;
        }

        for (Circuit circuit : circuits) {
            int i = 1;
            for (Delivery deliv : circuit.getDeliveries()) {
                for (int row = 0; row < tabModel.getRowCount(); row++) {
                    if (deliv.getAddress().equals(tabModel.getValueAt(row, 0))) {
                        tabModel.setValueAt(i, row, 2);
                        // to do : set delivery time
                        // tabModel.setValueAt(circuit.getDepartureTime() + deliv.getDuration(), row, 1);
                    }
                }
            }
            i++;
        }

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

    public JButton getLoadDeliveryRequestButton() {
        return loadDeliveryRequestButton;
    }

}
