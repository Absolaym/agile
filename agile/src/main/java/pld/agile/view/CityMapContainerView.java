package pld.agile.view;

import controller.Controller;

import java.awt.*;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import model.*;

public class CityMapContainerView extends JPanel implements Observer {

    private JButton loadCityMapButton;
    private JSlider zoomSlider;

    private final int HEIGHT = 600;
    private final int WIDTH = 600;
    private Controller controller;
    private Window window;

    static public double KM_TO_PIXEL = 0.0001;
    // For mouse listener
    private double offsetX = 0;
    private double offsetY = 0;
    private int originX = 0;
    private int originY = 0;

    // Map elements
    private final int DELIV_DOT_SIZE = 15;
    private Delivery selectedDelivery = null;
    private ArrayList<Ellipse2D> deliveriesPoints;

    public CityMapContainerView(Window w, Controller c) {
        super();
        this.controller = c;
        this.window = w;
        deliveriesPoints = new ArrayList<>();

        setLayout(null);
        setBorder(BorderFactory.createTitledBorder("City Map :"));
        this.createSlider();

        this.controller.getModel().getCityMap().addObserver(this);
        loadCityMapButton = new JButton("Load a city map");
        loadCityMapButton.addActionListener(new ButtonListener(c, w));

        loadCityMapButton.setSize(200, 80);
        loadCityMapButton.setLocation(100, 100);
        add(loadCityMapButton);

        setBackground(Color.DARK_GRAY);

        this.createMouseListener();
        w.getContentPane().add(this);
    }

    private void createSlider() {
        this.zoomSlider = new JSlider();
        this.zoomSlider.setMinimum(20);
        this.zoomSlider.setMaximum(100);
        this.zoomSlider.setValue(50);
        this.zoomSlider.setSize(100, 30);
        this.zoomSlider.setAlignmentX(this.getWidth() - this.zoomSlider.getWidth() - 20);
        this.zoomSlider.setAlignmentY(this.zoomSlider.getHeight() + 200);
        this.zoomSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                source.getParent().repaint();
            }
        });

        this.add(this.zoomSlider);
    }

    private void createMouseListener() {
        this.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent e) {
                CityMapContainerView that = CityMapContainerView.this;
                that.originX = e.getX();
                that.originY = e.getY();
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
//                System.out.println("MOUSE");
//                System.out.println(e.getX() + " " + e.getY());
                DeliveryRequest dr = controller.getModel().getDeliveryRequest();
                LinkedList<Delivery> delivs = dr.getDeliveries();
                Geolocation origin = getOrigin(controller.getModel().getCityMap());
                for (Delivery d : delivs) {
                    Geolocation geo = geolocationToPixels(origin, d.getGeolocation());
//                    System.out.println(geo.getLongitude() + " " +geo.getLatitude());
                    if (Math.abs(e.getX() - geo.getLongitude()) <= DELIV_DOT_SIZE
                            && Math.abs(e.getY() - geo.getLatitude()) <= DELIV_DOT_SIZE) {
                        selectedDelivery = d;
                        //for test purpose
                        selectedDelivery.setIsSelected(true);
                    } else d.setIsSelected(false);
                }
                repaint();
            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                CityMapContainerView that = CityMapContainerView.this;
                that.offsetX += e.getX() - that.originX;
                that.offsetY += e.getY() - that.originY;
                that.originX = e.getX();
                that.originY = e.getY();
                CityMapContainerView.this.repaint();
            }

            public void mouseMoved(MouseEvent e) {
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.drawCityMap(g, 6);
        this.drawDeliveriesOnCityMap(g);
        this.drawCircuits(g);
        window.getCityMapMenuPanel().getLoadNewCityMapButton().setVisible(true);
        window.getCityMapMenuPanel().getLoadDeliveryRequestButton().setVisible(true);
    }

    private void drawCircuits(Graphics g) {
        CityMap cityMap = controller.getModel().getCityMap();
        LinkedList<Circuit> circuits = this.controller.getModel().getCircuits();

        if (cityMap == null || circuits == null) {
            return;
        }

        Geolocation origin = getOrigin(cityMap);

        for (Circuit circuit : circuits) {
            int i = 0;
            Color c = new Color(180, Math.floorMod(50 + 40 * i, 100), Math.floorMod(120 + 40 * i, 100));
            for (Trip trip : circuit.getTrips()) {
                colorSections(g, c, trip.getSections(), cityMap);
            }

            for (Delivery deliv : circuit.getDeliveries()) {
                colorDelivery(g, c, deliv.getGeolocation(), cityMap, DELIV_DOT_SIZE, deliveriesPoints);
            }
            i++;
        }
    }

    private void drawDeliveriesOnCityMap(Graphics g) {
        CityMap cityMap = controller.getModel().getCityMap();
        if (cityMap == null) {
            return;
        }

        DeliveryRequest dr = controller.getModel().getDeliveryRequest();
        if (dr == null) {
            return;
        }

        LinkedList<Delivery> delivs = dr.getDeliveries();
        if (delivs.size() > 0) {
            for (Delivery d : delivs) {
                colorDelivery(g, Color.green, d.getGeolocation(), cityMap, DELIV_DOT_SIZE, deliveriesPoints);
            }
        }

        Intersection warehouseIntersection = cityMap.getIntersectionById(dr.getWarehouseAddress());
        if (warehouseIntersection != null) {
            colorDelivery(g, Color.red, warehouseIntersection.getGeolocation(), cityMap, DELIV_DOT_SIZE, deliveriesPoints);
        }
    }

    private void colorDelivery(Graphics g, Color c, Geolocation geolocation, CityMap cm, int dotSize, ArrayList<Ellipse2D> delivPoints) {
       
        Geolocation origin = getOrigin(cm);
        if (geolocation != null) {
            Geolocation geo = geolocationToPixels(origin, geolocation);
            //when user clicks on a particular delivery
            if (selectedDelivery != null && selectedDelivery.getGeolocation() == geolocation) {
                g.setColor(Color.YELLOW);
                Geolocation selectedGeo = geolocationToPixels(origin, selectedDelivery.getGeolocation());
                g.fillArc((int) selectedGeo.getLongitude() - dotSize / 2, (int) selectedGeo.getLatitude() - dotSize / 2, dotSize, dotSize, 0, 360);;
                // should call a method in delivery request to notify that a row in a table has to be colored in yellow
                // the following method doesn't work
                window.getDeliveryRequestPanel().colorTable(1,Color.YELLOW);
            }
            else {
                g.setColor(c);
                g.fillArc((int) geo.getLongitude() - dotSize / 2, (int) geo.getLatitude() - dotSize / 2, dotSize, dotSize, 0, 360);
                }
            }

    }

    private void colorSections(Graphics g, Color c, java.util.List<Section> sections, CityMap cm) {
        g.setColor(c);
        int lineThickness = 4;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(lineThickness));

        Geolocation origin = getOrigin(cm);

        for (Section sec : sections) {
            Geolocation start = sec.getStartIntersection().getGeolocation();
            Geolocation end = sec.getEndIntersection().getGeolocation();

            Geolocation pxStart = this.geolocationToPixels(origin, start);
            Geolocation pxEnd = this.geolocationToPixels(origin, end);

            g.drawLine((int) pxStart.getLongitude(), (int) pxStart.getLatitude(), (int) pxEnd.getLongitude(), (int) pxEnd.getLatitude());
        }
    }

    private Geolocation getOrigin(CityMap cityMap) {
        Geolocation origin = null;
        // Origin to the top left corner
        for (Intersection inter : cityMap.getIntersections().values()) {
            if (origin == null) {
                origin = inter.getGeolocation();
            } else {
                origin.setLatitude(Math.max(origin.getLatitude(), inter.getGeolocation().getLatitude()));
                origin.setLongitude(Math.min(origin.getLongitude(), inter.getGeolocation().getLongitude()));
            }
        }
        return origin;
    }

    private void drawCityMap(Graphics g, int dotSize) {

        Graphics2D g2 = (Graphics2D) g;

        CityMap cityMap = this.controller.getModel().getCityMap();
        if (cityMap.getIntersections().size() == 0) {
            return;
        }
        Geolocation origin = getOrigin(cityMap);

        colorSections(g, new Color(100, 100, 105), cityMap.getSections(), cityMap);

        g.setColor(new Color(180, 140, 180));

        for (Intersection inter : cityMap.getIntersections().values()) {
            Geolocation geo = inter.getGeolocation();
            Geolocation target = geolocationToPixels(origin, geo);

            g.fillArc((int) target.getLongitude() - dotSize / 2, (int) target.getLatitude() - dotSize / 2, dotSize, dotSize, 0, 360);
        }

        g2.setFont(new Font("Arial", Font.PLAIN, 10));

        for (Section section : cityMap.getSections()) {
            Geolocation first = section.getStartIntersection().getGeolocation();
            Geolocation last = section.getEndIntersection().getGeolocation();

            double length = distanceInPixels(first, last);
            //System.out.println(section.getStreetName().length() + " " + length * 100000);
            if (section.getStreetName().length() * 1 < length * 100000) {
                continue;
            }

            Geolocation center = new Geolocation(
                    (first.getLatitude() + last.getLatitude()) / 2,
                    (first.getLongitude() + last.getLongitude()) / 2);
            Geolocation target = geolocationToPixels(origin, center);
            g2.drawString(section.getStreetName(), (int) target.getLongitude(), (int) target.getLatitude());
        }
    }

    private double distanceInPixels(Geolocation origin, Geolocation target) {
        double coeff = this.zoomSlider.getValue() * KM_TO_PIXEL;
        return origin.distance(target) * coeff;
    }

    private Geolocation geolocationToPixels(Geolocation origin, Geolocation target) {
        double coeff = this.zoomSlider.getValue() * KM_TO_PIXEL;
        Geolocation geoY = new Geolocation(target.getLatitude(), origin.getLongitude());
        Geolocation geoX = new Geolocation(origin.getLatitude(), target.getLongitude());
        Geolocation ret = new Geolocation(origin.distance(geoY) / coeff + offsetY, origin.distance(geoX) / coeff + offsetX);

        return ret;
    }

    // to change
    public int getHeight() {
        return HEIGHT;
    }

    // to change
    public int getWidth() {
        return WIDTH;
    }

    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public JButton getLoadCityMapButton() {
        return loadCityMapButton;
    }

    public JSlider getZoomSlider() {
        return zoomSlider;
    }

    public Controller getController() {
        return controller;
    }

    public static double getKM_TO_PIXEL() {
        return KM_TO_PIXEL;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public int getOriginX() {
        return originX;
    }

    public int getOriginY() {
        return originY;
    }

}
