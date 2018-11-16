package view;

import controller.Controller;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Circuit;
import model.Delivery;
import model.DeliveryRequest;
import utils.Time;
import java.time.*;
import java.time.format.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * This is a wrap class containing a table row (the delivery address, time, duration).
 * Allows o determine which rows is selected and to synchronize the table with the city map.
 */
class TableRow extends Observable {

    private JPanel row;
    private Delivery delivery;
    private boolean isSelected = false;

    public TableRow(JPanel row, Delivery delivery) {
        this.row = row;
        this.delivery = delivery;
    }

    public JPanel getRow() {
        return row;
    }

    public boolean getIsSelected() {
        return isSelected;
    }
    /**
     * Notifies the observers that a row is selected. 
     */
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (isSelected) {
            setChanged();
            notifyObservers(delivery);
        }
    }

    public Delivery getDelivery() {
        return delivery;
    }
}

/**
 * Panel containing the textual view (delivery requests with all available deliveries)
 * Contains methods to add deliveries information in the textual view
 */
public class DeliveryRequestView extends JPanel {

    private JPanel deliveryRequestViewPanel;
    private JPanel deliveriesContainer;
    private JPanel deliveriesListContainer;
    private JScrollPane deliveriesListScrollPane;

    private ArrayList<TableRow> rows;
    private int idRow;
    private static Controller controller;
    private static Window window;
    private final int width = 350;
    private final int height = 700;

    public DeliveryRequestView(Window w, Controller c) {
        super();
        controller = c;
        window = w;

        deliveryRequestViewPanel = new JPanel();
        deliveriesContainer = new JPanel();
        deliveriesListContainer = new JPanel();
        rows = new ArrayList<>();
        deliveriesListScrollPane = new JScrollPane(deliveriesListContainer, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setLayout(new BorderLayout());

        w.getContentPane().add(this);
    }
    
    /**
     * this method is called when the textual view (the delivery request view)
     * needs to be updated, for example when a new delivery is loaded, when
     * a delivery is added, deleted, moved in a circuit 
     * @param window 
     */
    public void loadDeliveryRequest(Window window) {
        createDeliveryRequestViewPanel();
        createDeliveriesContainer();
        addDeliveries();

        deliveriesContainer.add(deliveriesListScrollPane);
        deliveryRequestViewPanel.add(deliveriesContainer);
        add(deliveryRequestViewPanel);
        window.getContentPane().add(this);

        deliveriesListContainer.repaint();
        deliveriesContainer.repaint();
        deliveryRequestViewPanel.repaint();
        window.revalidate();
        window.repaint();
    }
    
    /**
     * this method initializes the panel that contains all the textual view.
     * it is called by loadDeliveryRequest
     */
    private void createDeliveryRequestViewPanel() {
        deliveryRequestViewPanel.removeAll();
        rows.clear();
        deliveryRequestViewPanel.setLayout(new BorderLayout());
        deliveryRequestViewPanel.setBorder(BorderFactory.createTitledBorder("Delivery requests :"));
        deliveryRequestViewPanel.setBackground(Color.LIGHT_GRAY);
    }

    /**
     * this method initializes and fills the panel that contains the heading of 
     * the textual view
     * it is called by loadDeliveryRequest
     */
    private void createDeliveriesContainer() {

        deliveriesContainer.removeAll();
        deliveriesContainer.setLayout(new BoxLayout(deliveriesContainer, BoxLayout.Y_AXIS));

        JPanel head = new JPanel();

        JTextArea address = new JTextArea("Address |");
        JTextArea duration = new JTextArea("Duration |");
        JTextArea arrivalTime = new JTextArea("Arrival time |");
        JTextArea circuit = new JTextArea("Circuit");

        address.setOpaque(false);
        duration.setOpaque(false);
        arrivalTime.setOpaque(false);
        circuit.setOpaque(false);

        head.add(address);
        head.add(duration);
        head.add(arrivalTime);
        head.add(circuit);
        deliveriesContainer.add(head);
        deliveryRequestViewPanel.add(deliveriesContainer);
    }

    

    /**
     * this method initializes the panel that contains the list of deliveries 
     * for the textual view, it fills it with the deliveries from the delivery 
     * request of the model.
     * it is called by loadDeliveryRequest
     */
    public void addDeliveries() {

        deliveriesListContainer.removeAll();
        deliveriesListContainer.setLayout(new BoxLayout(deliveriesListContainer, BoxLayout.Y_AXIS));

        if (controller.getModel().getDeliveryRequest() != null) {
            LinkedList<Delivery> deliveries = controller.getModel().getDeliveryRequest().getDeliveries();
            for (int i = 0; i < deliveries.size(); i++) {
                Delivery d = deliveries.get(i);
                addRow(d);
            }
        }
        deliveriesListContainer.revalidate();
    }

    /**
     * this method creates a row for each delivery and fills it with the right
     * informations and the required buttons, depending on whether a circuit
     * has already been calculated for the delivery. the row is then added to 
     * the delivery list panel.
     * it is called in addDeliveries
     * @param delivery 
     */
    private void addRow(Delivery delivery) {

        Time time = delivery.getArrivalTime();
        String strArrivalTime = time.toStringWithoutSeconds();

        JPanel row = new JPanel();

        JTextArea txtAddress = new JTextArea(delivery.getAddress());
        JTextArea txtDuration = new JTextArea("" + delivery.getDuration().getMinutes() + " min");

        txtAddress.setOpaque(false);
        txtDuration.setOpaque(false);

        txtAddress.setEditable(false);
        txtDuration.setEditable(false);

        row.add(txtAddress);
        row.add(txtDuration);

        if (delivery.getCircuit() != null) {

            JTextArea txtArrivalTime = new JTextArea(strArrivalTime);
            JTextArea txtCircuit = new JTextArea("n°" + delivery.getCircuit().getCourierId());

            JButton btnMoveBefore = new JButton("");
            JButton btnMoveAfter = new JButton("");
            JButton btnDelete = new JButton("Delete");
            
            ButtonListener btnListener = new ButtonListener(controller, window, delivery);

            txtArrivalTime.setOpaque(false);
            txtCircuit.setOpaque(false);

            txtArrivalTime.setEditable(false);
            txtCircuit.setEditable(false);

            String root = System.getProperty("user.dir");
            String img = root + "/src/main/assets/img/";

            String iconfilePath = img + "arrow-up.png";
            btnMoveBefore.setIcon(new ImageIcon(iconfilePath));
            btnMoveBefore.setBorder(BorderFactory.createEmptyBorder());
            btnMoveBefore.setContentAreaFilled(false);
            btnMoveBefore.setFocusable(false);

            iconfilePath = img + "arrow-down.png";
            btnMoveAfter.setIcon(new ImageIcon(iconfilePath));
            btnMoveAfter.setBorder(BorderFactory.createEmptyBorder());
            btnMoveAfter.setContentAreaFilled(false);
            btnMoveAfter.setFocusable(false);

            btnMoveBefore.addActionListener(btnListener);
            btnMoveAfter.addActionListener(btnListener);
            btnDelete.addActionListener(btnListener);

            btnMoveBefore.setActionCommand(Window.MOVE_DELIVERY_BEFORE);
            btnMoveAfter.setActionCommand(Window.MOVE_DELIVERY_AFTER);
            btnDelete.setActionCommand(Window.DELETE_DELIVERY);

            row.add(txtArrivalTime);
            row.add(txtCircuit);

            row.add(btnMoveBefore);
            row.add(btnMoveAfter);
            row.add(btnDelete);
        }

        setRowColor(row, delivery.getCircuit());

        TableRow tableRow = new TableRow(row, delivery);
        //observable
        tableRow.addObserver(window.getCityMapContainerPanel());
        rows.add(tableRow);

        if (delivery.getIsSelected()) {
            row.setBackground(Color.yellow);
        }

        row.addMouseListener(new java.awt.event.MouseListener() {
            @Override
            /**
             * On click, sets a yellow row's background
             */
            public void mouseClicked(MouseEvent e) {
                Component row = e.getComponent();
                for (TableRow r : rows) {
                    if (r.getRow() == row) {
                        r.setIsSelected(true);
                        e.getComponent().setBackground(Color.yellow);
                    } else {
                        r.setIsSelected(false);
                        setRowColor(r.getRow(), r.getDelivery().getCircuit());
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            /**
             * On hover, sets a green background
             */
            public void mouseEntered(MouseEvent e) {
                Component row = e.getComponent();
                for (TableRow r : rows) {
                    if (r.getRow() == row && !r.getIsSelected() && !r.getDelivery().getIsSelected()) {
                        e.getComponent().setBackground(Color.green);
                    }
                }
            }

            @Override
            /**
             * On mouse out, sets the previous background
             */
            public void mouseExited(MouseEvent e) {
                Component row = e.getComponent();
                for (TableRow r : rows) {
                    if (r.getRow() == row && !r.getIsSelected() && !r.getDelivery().getIsSelected()) {
                        setRowColor(r.getRow(), r.getDelivery().getCircuit());
                    }
                }
            }
        });

        deliveriesListContainer.add(row);
    }

    /**
     * Sets the color of a row(containing a specific delivery) according to its circuit number 
     * @param component
     * @param circuit 
     */
    public void setRowColor(Component component, Circuit circuit) {
        int i = 0;
        if (circuit != null) {
            i = circuit.getCourierId();
        }
        Color c = window.colors[i%(window.colors.length)];
        if (i == 0) {
            component.setBackground(null);
        } else {
            component.setBackground(c);
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

}
