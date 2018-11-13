package view;


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

class TableRow extends Observable{
    private JPanel row;
    private String deliveryAddress;
    private boolean isSelected = false;

    public TableRow(JPanel row, String address) {
        this.row = row;
        this.deliveryAddress = address;
    }

    public JPanel getRow() {
        return row;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (isSelected) {
            setChanged();
            notifyObservers(deliveryAddress);
        }
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    } 
}

public class DeliveryRequestView extends JPanel{
    private ButtonListener buttonListener;
    private JPanel deliveryRequestViewPanel /*= new JPanel()*/;
    private JPanel deliveriesContainer /*= new JPanel()*/;
    private JPanel deliveriesListContainer /*= new JPanel()*/;
    private JScrollPane deliveriesListScrollPane;
    
    private static Controller controller;
    private ArrayList<TableRow> rows;
    private static Window window;
    private final int width = 350;
    private final int height = 700;

    
    public DeliveryRequestView(Window w, Controller c) {
        super();
        controller = c;
        window = w;
        buttonListener = new ButtonListener(c,w);
        
        deliveryRequestViewPanel = new JPanel();
        deliveriesContainer = new JPanel();
        deliveriesListContainer = new JPanel();
        rows = new ArrayList<>();
        deliveriesListScrollPane = new JScrollPane(deliveriesListContainer, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setLayout(new BorderLayout());
        
        w.getContentPane().add(this);
    }
    
    private void createDeliveryRequestViewPanel(){
        deliveryRequestViewPanel.removeAll();
        rows.clear();
        deliveryRequestViewPanel.setLayout(new BorderLayout());
        deliveryRequestViewPanel.setBorder(BorderFactory.createTitledBorder("Delivery requests :"));
        deliveryRequestViewPanel.setBackground(Color.LIGHT_GRAY);
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
        deliveryRequestViewPanel.add(deliveriesContainer);
    }
    
    public void loadDeliveryRequest(Window window){
        createDeliveryRequestViewPanel();
        createDeliveriesContainer();
        addDeliveries();
        
//        deliveriesContainer.add(deliveriesListContainer);
        deliveriesContainer.add(deliveriesListScrollPane);
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
        LinkedList<Delivery> deliveries = controller.getModel().getDeliveryRequest().getDeliveries();
        System.out.println("in addDeliveries - deliveries: "+deliveries.size());
        System.out.println("in addDeliveries - got the deliveries");
        
        deliveriesListContainer.removeAll();
        deliveriesListContainer.setLayout(new BoxLayout(deliveriesListContainer, BoxLayout.Y_AXIS));
//        deliveriesContainer.setLayout(new BorderLayout(10,10));
        
        for (int i = 0; i < deliveries.size(); i++) {
            Delivery d = deliveries.get(i);
            addRow(d.getAddress(), "", ""+d.getDuration(), ""/*+d.getCircuit().getCourierId()*/, d.getIsSelected());
        }
        deliveriesListContainer.revalidate();
    }
    
    private void addRow(String address, String arrivalTime, String duration, 
                        String circuit, boolean isSelected){
        JPanel row = new JPanel();
        
        JTextArea txtAddress = new JTextArea(address);
        JTextArea txtArrivalTime = new JTextArea(arrivalTime);
        JTextArea txtDuration = new JTextArea(duration);
        JTextArea txtCircuit = new JTextArea(circuit);
        
        JButton btnMoveBefore = new JButton("");
        JButton btnMoveAfter = new JButton("");
        JButton btnDelete = new JButton("Delete");
        
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
        
        btnMoveBefore.setActionCommand(Window.MOVE_DELIVERY_BEFORE);
        btnMoveAfter.setActionCommand(Window.MOVE_DELIVERY_AFTER);
        btnDelete.setActionCommand(Window.DELETE_DELIVERY);

        row.add(txtAddress);
        row.add(txtArrivalTime);
        row.add(txtDuration);
        row.add(txtCircuit);
        row.add(btnMoveBefore);
        row.add(btnMoveAfter);
        row.add(btnDelete);
        
        TableRow tableRow = new TableRow(row,address);
        //observable
        tableRow.addObserver(window.getCityMapContainerPanel());
        rows.add(tableRow);
                       
        row.addMouseListener(new java.awt.event.MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Component row = e.getComponent();
                for (TableRow r : rows) {
                    if(r.getRow() == row) {
                        r.setIsSelected(true);
                        e.getComponent().setBackground(Color.yellow);
                    } else {
                        r.setIsSelected(false);
                        r.getRow().setBackground(null);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {
                Component row = e.getComponent();
                for (TableRow r : rows) {
                    if(r.getRow() == row && !r.getIsSelected()) {
                        e.getComponent().setBackground(Color.green);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Component row = e.getComponent();
                for (TableRow r : rows) {
                    if(r.getRow() == row && !r.getIsSelected()) {
                        e.getComponent().setBackground(null);
                    }
                }
            }
        });
        
        deliveriesListContainer.add(row);
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

}