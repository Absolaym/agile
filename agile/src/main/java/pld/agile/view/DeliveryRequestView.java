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

    
    private ButtonListener buttonListener;
    private JPanel deliveryRequestViewPanel /*= new JPanel()*/;
    private JPanel deliveriesContainer /*= new JPanel()*/;
    private JPanel deliveriesListContainer /*= new JPanel()*/;
    
    private static Controller controller;
    private final int buttonLocationY = 50;
    private final int spaceElements = 30;
    private final int height = 800;
    private final int width = 400;

    
    public DeliveryRequestView(Window w, Controller c) {
        super();
        controller = c;
        buttonListener = new ButtonListener(c,w);
        
        deliveryRequestViewPanel = new JPanel();
        deliveriesContainer = new JPanel();
        deliveriesListContainer = new JPanel();
        
        setLayout(new BorderLayout());
//        setBorder(BorderFactory.createTitledBorder("Delivery requests :"));
//        setBackground(Color.LIGHT_GRAY);
        
        //createTable();
//        createDeliveryRequestViewPanel();
//        createDeliveriesContainer();
        //add(deliveriesContainer);
//        deliveryRequestViewPanel.add(deliveriesContainer);
        
//        deliveryRequestViewPanel.add(deliveriesContainer, BorderLayout.CENTER);
//        add(deliveryRequestViewPanel);
//        w.getContentPane().add(deliveryRequestViewPanel);
        w.getContentPane().add(this);
    }
    
    private void createDeliveryRequestViewPanel(){
        deliveryRequestViewPanel.removeAll();
//        deliveryRequestViewPanel = new JPanel();
        deliveryRequestViewPanel.setLayout(new BorderLayout());
        deliveryRequestViewPanel.setBorder(BorderFactory.createTitledBorder("Delivery requests :"));

//        deliveryRequestViewPanel.setLayout(new BoxLayout(deliveryRequestViewPanel, BoxLayout.Y_AXIS));
        
        deliveryRequestViewPanel.setBackground(Color.LIGHT_GRAY);
        
        
        
        JTextArea title = new JTextArea("Delivery requests :");
//        deliveryRequestViewPanel.add(title);
//        createDeliveriesContainer();
//        
//        deliveryRequestViewPanel.add(deliveriesContainer, BorderLayout.CENTER);
//
//        add(deliveryRequestViewPanel);
        
    }
    
    private void  createDeliveriesContainer(){
        
        deliveriesContainer.removeAll();
//        deliveriesContainer = new JPanel();
        deliveriesContainer.setLayout(new BoxLayout(deliveriesContainer, BoxLayout.Y_AXIS));
//        deliveriesContainer.setPreferredSize(new Dimension(100,100));
        
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
//        deliveriesContainer.add(head);
//        deliveriesContainer.add(address);
//        deliveriesContainer.setBackground(Color.red);

//        deliveryRequestViewPanel.add(head);
        
//        deliveriesContainer.revalidate();
        deliveryRequestViewPanel.add(deliveriesContainer);
        System.out.println("in create ------------");        
        System.out.println("size deliveriesContainer : "+deliveriesContainer.getSize().height+" - "+deliveriesContainer.getSize().width);

    }
    
    public void loadDeliveryRequest(Window window){
        
//        window.getContentPane().remove(this);
        createDeliveryRequestViewPanel();
        createDeliveriesContainer();
        System.out.println("in loadDeliveryRequest ------------");
        System.out.println("size deliveriesContainer : "+deliveriesContainer.getSize().height+" - "+deliveriesContainer.getSize().width);
//        System.out.println(deliveriesContainer);
//        System.out.println(deliveriesContainer==null);
        addDeliveries();
//        
        deliveriesContainer.add(deliveriesListContainer);
        deliveryRequestViewPanel.add(deliveriesContainer/*, BorderLayout.CENTER*/);
        add(deliveryRequestViewPanel);
        window.getContentPane().add(this);
        deliveriesListContainer.repaint();
        deliveriesContainer.repaint();
        deliveryRequestViewPanel.repaint();
        window.revalidate();
        window.repaint();
    }
    
  
    public void addDeliveries() {
        System.out.println("in addDeliveries");
//        System.out.println("in addDeliveries - got the dr");
//        emptyDeliveriesContainer();

//        createDeliveryRequestViewPanel();
//        createDeliveriesContainer();
//        System.out.println("in addDeliveries " + tabModel.getRowCount());
        

        LinkedList<Delivery> deliveries = controller.getModel().getDeliveryRequest().getDeliveries();
        System.out.println("in addDeliveries - deliveries: "+deliveries.size());
        System.out.println("in addDeliveries - got the deliveries");
        
//        if((deliveriesListContainer!=null) && (deliveriesListContainer.getSize().height!=0))
            deliveriesListContainer.removeAll();
//        deliveriesListContainer = new JPanel();
        deliveriesListContainer.setLayout(new BoxLayout(deliveriesListContainer, BoxLayout.Y_AXIS));
        
        for (int i = 0; i < deliveries.size(); i++) {
            System.out.println("in addDeliveries - in the loop");
            Delivery d = deliveries.get(i);
            System.out.println("in addDeliveries - got the delivery");
            addRow(d.getAddress(), "", ""+d.getDuration(), ""/*+d.getCircuit().getCourierId()*/, d.getIsSelected());
            System.out.println("in addDeliveries - called addRow");
            System.out.println("selected" + d.getAddress() + " " + d.getIsSelected());
            //i++;
//            deliveriesListContainer.repaint();
//            deliveriesContainer.repaint();
           //add(deliveriesContainer);
        }
        deliveriesListContainer.revalidate();
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
        btnMoveAfter.setBorder(BorderFactory.createEmptyBorder());
        btnMoveAfter.setContentAreaFilled(false);
        btnMoveAfter.setFocusable(false);
        
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
        txtAddress = new JTextArea(address);
        
//        
//        row.validate();
//        row.repaint();
        
        deliveriesListContainer.add(row);
        
//        deliveryRequestViewPanel.add(row);
        
        System.out.println("in addRow - added the row");

    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
//        if (controller.getModel().getDeliveryRequest() != null)
//            addDeliveries();
        System.out.println("in paint component");
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

}
