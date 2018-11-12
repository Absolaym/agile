package pld.agile.view;

import controller.Controller;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
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
    private ButtonListener buttonListener;
    private JPanel deliveriesContainer = new JPanel();
    private JPanel deliveriesListContainer = new JPanel();
    private JPanel DeliveryRequestView = new JPanel();
    
    private static Controller controller;
    private final int buttonLocationY = 50;
    private final int spaceElements = 30;
    private final int height = 800;
    private final int width = 400;


    
    public DeliveryRequestView(Window w, Controller c) {
        super();
        controller = c;
        buttonListener = new ButtonListener(c,w);
        
        setLayout(new BorderLayout());
        //set button
        setBorder(BorderFactory.createTitledBorder("Delivery requests :"));
        setBackground(Color.LIGHT_GRAY);
        
        //createTable();
        createDeliveriesContainer();
        add(deliveriesContainer);
        
        w.getContentPane().add(this);
    }
    
    private void createDeliveryRequestViewPanel(){
        DeliveryRequestView = new JPanel();
        DeliveryRequestView.setLayout(new BorderLayout());
        DeliveryRequestView.setBorder(BorderFactory.createTitledBorder("Delivery requests :"));
        DeliveryRequestView.setBackground(Color.LIGHT_GRAY);
    }
    
    private void  createDeliveriesContainer(){
        
        deliveriesContainer.removeAll();
        deliveriesContainer.setLayout(new BoxLayout(deliveriesContainer, BoxLayout.Y_AXIS));
        
        JPanel head = new JPanel();
        
        JTextArea address = new JTextArea("Address");
        JTextArea arrivalTime = new JTextArea("Arrival time");
        JTextArea duration = new JTextArea("Duration");
        JTextArea circuit = new JTextArea("Circuit");
        
        address.setOpaque(false);
        arrivalTime.setOpaque(false);
        duration.setOpaque(false);
        circuit.setOpaque(false);
        
        head.add(address);
        head.add(arrivalTime);
        head.add(duration);
        head.add(circuit);
        deliveriesContainer.add(head);
        deliveriesContainer.revalidate();
        
        add(deliveriesContainer);
    }
    

    public void addDeliveries() {
        System.out.println("in addDeliveries");
//        System.out.println("in addDeliveries - got the dr");
        emptyDeliveriesContainer();
//        System.out.println("in addDeliveries " + tabModel.getRowCount());
        
//        int i=0;
//        LinkedList<Delivery> deliv = dr.getDeliveries();

        LinkedList<Delivery> deliveries = controller.getModel().getDeliveryRequest().getDeliveries();
        System.out.println("in addDeliveries - deliveries: "+deliveries.size());
        System.out.println("in addDeliveries - got the deliveries");
        
        deliveriesListContainer.removeAll();
        //deliveriesListContainer = new JPanel();
        deliveriesListContainer.setLayout(new BoxLayout(deliveriesListContainer, BoxLayout.Y_AXIS));
        
        for (int i = 0; i < deliveries.size(); i++) {
            System.out.println("in addDeliveries - in the loop");
//        for (Delivery d : deliv ) {
            //tabModel.addRow(new String[]{deliveries[i], "unknown", "unknown", "unknown"});
//            String duration = (d.getDuration())+"";
            //int departureTimeSec = d.getDepartureTime().time;
            
//            tabModel.addRow(new String[] {d.getAddress(), "unknown", duration, "unknown"});
//            if (d.getIsSelected())
//                colorTable(i, Color.yellow, d);
//            else colorTable(i, Color.red, d);
            
            Delivery d = deliveries.get(i);
            System.out.println("in addDeliveries - got the delivery");
            //addRow("a","v","f","g",true);
            addRow(d.getAddress(), "", ""+d.getDuration(), ""/*+d.getCircuit().getCourierId()*/, d.getIsSelected());
            System.out.println("in addDeliveries - called addRow");
            System.out.println("selected" + d.getAddress() + " " + d.getIsSelected());
            //i++;
            
           //add(deliveriesContainer);
        }
        deliveriesListContainer.revalidate();
        deliveriesContainer.add(deliveriesListContainer);
    }
    
    private void addRow(String address, String arrivalTime, String duration, 
                        String circuit, boolean isSelected){
        System.out.println("in addRow");
        JPanel row = new JPanel();
        
        JTextArea txtAddress = new JTextArea(address);
        JTextArea txtArrivalTime = new JTextArea(arrivalTime);
        JTextArea txtDuration = new JTextArea(duration);
        JTextArea txtCircuit = new JTextArea(circuit);
        
        JButton btnMoveBefore = new JButton("");
        JButton btnMoveAfter = new JButton("");
        JButton btnDelete = new JButton("Delete");
        
        System.out.println("in addRow - declarated the elements");
        
        if(isSelected)
            row.setBackground(Color.yellow);
        txtAddress.setOpaque(false);
        txtArrivalTime.setOpaque(false);
        txtDuration.setOpaque(false);
        txtCircuit.setOpaque(false);
        
        txtAddress.setEditable(false);
        txtArrivalTime.setEditable(false);
        txtDuration.setEditable(false);
        txtCircuit.setEditable(false);
        
        String root = System.getProperty("user.dir");
        String img = root + "/src/main/assets/img/";
        
        String iconfilePath = img +"arrow-up.png";
        btnMoveBefore.setIcon(new ImageIcon(iconfilePath));
//        btnMoveBefore.setBounds(10, 438, 39, 31);
        btnMoveBefore.setBorder(BorderFactory.createEmptyBorder());
        btnMoveBefore.setContentAreaFilled(false);
        btnMoveBefore.setFocusable(false);
        
        iconfilePath = img +"arrow-down.png";
        btnMoveAfter.setIcon(new ImageIcon(iconfilePath));
//        btnMoveAfter.setBounds(10, 438, 39, 31);
        btnMoveAfter.setBorder(BorderFactory.createEmptyBorder());
        btnMoveAfter.setContentAreaFilled(false);
        btnMoveAfter.setFocusable(false);
        
//        contentPane.add(btnNewButton);
        
        btnMoveBefore.addActionListener(buttonListener);
        btnMoveAfter.addActionListener(buttonListener);
        btnDelete.addActionListener(buttonListener);
        
        System.out.println("in addRow - did all the treatments");

        row.add(txtAddress);
        row.add(txtArrivalTime);
        row.add(txtDuration);
        row.add(txtCircuit);
        row.add(btnMoveBefore);
        row.add(btnMoveAfter);
        row.add(btnDelete);
        
        System.out.println("in addRow - added all to the row");

        deliveriesListContainer.add(row);
        
        System.out.println("in addRow - added the row");

    }

    public void emptyDeliveriesContainer() {
        deliveriesContainer = new JPanel();
        createDeliveriesContainer();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
//        if (controller.getModel().getDeliveryRequest() != null)
//            addDeliveries();
//        System.out.println("in paint component");
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void update(Observable o, Object arg) {
        System.out.println("state" + arg);
    }

    public JTable getDeliveriesTable() {
        return deliveriesTable;
    }

    public DefaultTableModel getTabModel() {
        return tabModel;
    }
}
