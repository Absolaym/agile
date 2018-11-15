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

public class DeliveryRequestView extends JPanel {

//    private ButtonListener buttonListener;
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
//        buttonListener = new ButtonListener(c, w);

        deliveryRequestViewPanel = new JPanel();
        deliveriesContainer = new JPanel();
        deliveriesListContainer = new JPanel();
        rows = new ArrayList<>();
        deliveriesListScrollPane = new JScrollPane(deliveriesListContainer, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setLayout(new BorderLayout());

        w.getContentPane().add(this);
    }

    private void createDeliveryRequestViewPanel() {
        deliveryRequestViewPanel.removeAll();
        rows.clear();
        deliveryRequestViewPanel.setLayout(new BorderLayout());
        deliveryRequestViewPanel.setBorder(BorderFactory.createTitledBorder("Delivery requests :"));
        deliveryRequestViewPanel.setBackground(Color.LIGHT_GRAY);
    }

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

    public void loadDeliveryRequest(Window window) {
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

    private void addRow(Delivery d) {

        Time time = d.getArrivalTime();
        String strArrivalTime = time.getHours() + ":" + time.getMinutes();

        JPanel row = new JPanel();

        JTextArea txtAddress = new JTextArea(d.getAddress());
        JTextArea txtDuration = new JTextArea("" + d.getDuration().getMinutes() + " min");

        txtAddress.setOpaque(false);
        txtDuration.setOpaque(false);

        txtAddress.setEditable(false);
        txtDuration.setEditable(false);

        row.add(txtAddress);
        row.add(txtDuration);

        if (d.getCircuit() != null) {

            JTextArea txtArrivalTime = new JTextArea(strArrivalTime);
            JTextArea txtCircuit = new JTextArea("n°" + d.getCircuit().getCourierId());

            JButton btnMoveBefore = new JButton("");
            JButton btnMoveAfter = new JButton("");
            JButton btnDelete = new JButton("Delete");
            
            ButtonListener btnListener = new ButtonListener(controller, window, d);

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

        setRowColor(row, d.getCircuit());

        TableRow tableRow = new TableRow(row, d);
        //observable
        tableRow.addObserver(window.getCityMapContainerPanel());
        rows.add(tableRow);

        if (d.getIsSelected()) {
            row.setBackground(Color.yellow);
        }

        row.addMouseListener(new java.awt.event.MouseListener() {
            @Override
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
            public void mouseEntered(MouseEvent e) {
                Component row = e.getComponent();
                for (TableRow r : rows) {
                    if (r.getRow() == row && !r.getIsSelected() && !r.getDelivery().getIsSelected()) {
                        e.getComponent().setBackground(Color.green);
                    }
                }
            }

            @Override
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

    public void setRowColor(Component comp, Circuit circuit) {
        int i = 0;
        if (circuit != null) {
            i = circuit.getCourierId();
        }
        Color c = window.colors[i];
        //Color c = new Color ((int)(255*0.5),0, 0);
        if (i == 0) {
            comp.setBackground(null);
        } else {
            comp.setBackground(c);
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
