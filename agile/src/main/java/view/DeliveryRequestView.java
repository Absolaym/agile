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

public class DeliveryRequestView extends JPanel implements Observer {

    private JTable deliveriesTable;
    private DefaultTableModel tabModel;
    
    private static Controller controller;
    private final int buttonLocationY = 50;
    private final int spaceElements = 30;
    private final int height = 800;
    private final int width = 400;
    private int selectedRow = -1;
    private Renderer cellRenderer = new Renderer(tabModel);
    private HashMap<Delivery,JTextArea> table;

    
    //I implemented this class because I have to override getTableCell.. method
    // it doesn't work because the method is called only when the JTable has changes
    // since selecting a point on the map is not a change on JTable the method is not called and the row is not colored :(
    static class Renderer extends DefaultTableCellRenderer {
        private int selectedRow;
        private Color color;
        private DefaultTableModel model;
        public Renderer(int row, Color c) {
            selectedRow = row;
            color = c;
        }
        public Renderer(DefaultTableModel model) {
            this.model = model;
        }
        public void setSelectedRow(int row) {
            selectedRow = row;
        }
        
        public void setColor(Color c){
            color = c;
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            //if (model != null)
               // c.setBackground(model.getRowColour(row));
            return c;
    
        }
  
    }
    
    public DeliveryRequestView(Window w, Controller c) {
        super();
        controller = c;
        
        

        setLayout(new BorderLayout());
        //set button
        setBorder(BorderFactory.createTitledBorder("Delivery requests :"));
        createTable();
        
        
        setBackground(Color.LIGHT_GRAY);
        
        w.getContentPane().add(this);

    }
    
  // just for test purposes
    public void addToTable() {
        DeliveryRequest dr = controller.getModel().getDeliveryRequest();
        //String[] deliveries = new String[dr.getDeliveries().size()];
        LinkedList<Delivery> deliveries = dr.getDeliveries();
//        for (int i = 0; i < dr.getDeliveries().size(); i++) {
//            deliveries[i] = dr.getDeliveries().get(i).getAddress();
//        }
        for (int i = 0; i<deliveries.size(); i++) {
//            System.out.println("deliveries:" + deliveries.size());
            JTextArea textArea = new JTextArea("adress");
            Delivery d = deliveries.get(i);
            textArea.append("adr:" + d.getAddress());
            table.put(d,textArea);
            table.get(d).append("adress");
            table.get(d).insert("time", 2);
            textArea.setVisible(true);
            textArea.setSize(10, 10);
 //           this.add((JTextArea)table.get(d));
            this.add(textArea,BorderLayout.NORTH);
            
           
        }
        this.revalidate();
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
        tabModel.addColumn("Duration");
        tabModel.addColumn("Circuit");

        deliveriesTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPanel = new JScrollPane(deliveriesTable);
        scrollPanel.setBounds(30, 40, width - 30, height - 10);
        scrollPanel.setLocation(0, spaceElements);
        add(scrollPanel);
    }

    public void addDeliveries() {
        DeliveryRequest dr = controller.getModel().getDeliveryRequest();
//        String[] deliveries = new String[dr.getDeliveries().size()];
//        for (int i = 0; i < dr.getDeliveries().size(); i++) {
//            deliveries[i] = dr.getDeliveries().get(i).getAddress();
//        }
        emptyTable();
//        System.out.println("in addDeliveries " + tabModel.getRowCount());
        
        int i=0;
        LinkedList<Delivery> deliv = dr.getDeliveries();
        //for (int i = 0; i < deliveries.length; i++) {
        for (Delivery d : deliv ) {
            //tabModel.addRow(new String[]{deliveries[i], "unknown", "unknown", "unknown"});
            String duration = (d.getDuration())+"";
            //int departureTimeSec = d.getDepartureTime().time;
            
            tabModel.addRow(new String[] {d.getAddress(), "unknown", duration, "unknown"});
            if (d.getIsSelected())
                colorTable(i, Color.yellow, d);
            else colorTable(i, Color.red, d);
            i++;
//            System.out.println("slected" + d.getAddress() + " " + d.getIsSelected());
        }

      
    }

//    
//        public void addDeliveries() {
//        JPanel panel = new JPanel(new BorderLayout());
//        DeliveryRequest dr = controller.getModel().getDeliveryRequest();
//        int i=0;
//        LinkedList<Delivery> deliv = dr.getDeliveries();
//        for (Delivery d : deliv ) {
//            JTextField 
//            //tabModel.addRow(new String[]{deliveries[i], "unknown", "unknown", "unknown"});
//            String duration = (d.getDuration())+"";
//            //int departureTimeSec = d.getDepartureTime().time;
//            
//            tabModel.addRow(new String[] {d.getAddress(), "unknown", duration, "unknown"});
//            if (d.getIsSelected())
//                colorTable(i, Color.yellow, d);
//            else colorTable(i, Color.red, d);
//            i++;
//            System.out.println("slected" + d.getAddress() + " " + d.getIsSelected());
//        }
//
//      
//    }
    
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
                ////////////////
                deliv.addObserver(this);
                /////////////////////
                for (int row = 0; row < tabModel.getRowCount(); row++) {
                    if(deliv.getAddress().equals(tabModel.getValueAt(row, 0))) {
                        tabModel.setValueAt(i, row, 3);
                       // colorTable(row,new Color(180, Math.floorMod(50 + 40 * i, 100), Math.floorMod(120 + 40 * i, 100)) );
                        departureTimeSec += circuit.getTrips().get(j).getLength() / (Circuit.SPEED / 3.6);
                        tabModel.setValueAt(new Time((int)departureTimeSec).toString(), row, 1);
                        departureTimeSec += deliv.getDuration() * 60;
                        
                        colorTable(row, Color.red,deliv);
                    }
                }
                j++;
            }
            i++;
        }
        
    }
    
    public Color getRowColour(int row){
        if (row == 1 ) return Color.YELLOW;
        return Color.RED;
    }
   
    // this method doesn't work well :(
    public void colorTable(int row, Color c, Delivery d){
        
        cellRenderer.setSelectedRow(row);
        cellRenderer.setColor(c);
        //tabModel.setValueAt(d.getAddress(), row,0);
//        System.out.println("in color table");
        //tabModel.setValueAt(1, row, 3);
       // deliveriesTable.getColumnModel().getColumn(0).setHeaderValue("Address");
        deliveriesTable.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        deliveriesTable.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        deliveriesTable.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
        deliveriesTable.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        System.out.println(cellRenderer.color);
        deliveriesTable.repaint();
        //tabModel.setValueAt(d.getAddress(), row, 0);
        this.repaint();
        
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
