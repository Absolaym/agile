package pld.agile.view;

import controller.Controller;

import java.awt.*;
import java.awt.color.ColorSpace;
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

    private final int HEIGHT = 750;
    private final int WIDTH = 1000;
    private Controller controller;
    private Window window;

    static public double KM_TO_PIXEL = 10;
    // For mouse listener
    private double offsetX = 0;
    private double offsetY = 0;
    private int originX = 0;
    private int originY = 0;

    private Intersection hoveredInter = null;
    private Intersection newDelivery = null;
    private Section hoveredSection = null;
    private Circuit circuitNewDelivery = null;

    // Map elements
    private int delivDotSize = 12;
    private int dotSize = 6;
    private Delivery selectedDelivery = null;

    public CityMapContainerView(Window w, Controller c) {
        super();
        this.controller = c;
        this.window = w;

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
        this.zoomSlider.setMinimum(0);
        this.zoomSlider.setMaximum(80);
        this.zoomSlider.setValue(50);
        this.zoomSlider.setSize(100, 30);
        this.zoomSlider.setAlignmentX(this.getWidth() - this.zoomSlider.getWidth() - 20);
        this.zoomSlider.setAlignmentY(this.zoomSlider.getHeight() + 200);
        this.zoomSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                dotSize = (int) source.getMaximum() / 10 - (int) source.getValue() / 10 + 4;
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
                int x = (int) (e.getX());
                int y = (int) (e.getY());

                if (controller.getModel().getCityMap() == null) {
                    return;
                }
                DeliveryRequest dr = controller.getModel().getDeliveryRequest();
                LinkedList<Delivery> delivs = dr.getDeliveries();
                Geolocation origin = getOrigin(controller.getModel().getCityMap());
                for (Delivery d : delivs) {
                    Geolocation geo = geolocationToPixels(origin, d.getGeolocation());

                    if (Math.abs(x - geo.getLongitude()) <= delivDotSize
                            && Math.abs(y - geo.getLatitude()) <= delivDotSize) {
                        selectedDelivery = d;
                        selectedDelivery.setIsSelected(true);
                        controller.setSelectDelivery(selectedDelivery);

                    } else {
                        d.setIsSelected(false);
                    }
                    window.getDeliveryRequestPanel().repaint();
                }
                if (window.getWaitingState() == 0) {
                    //getClosestLocation

                    newDelivery = getIntersectionByCoordinates(x, y);
                    if (newDelivery != null)
                        window.setWaitingState(1);
                } else if (window.getWaitingState() == 1) {
                    Section sectionNewDelivery = getSectionByCoordinates(x, y);
                    circuitNewDelivery = sectionNewDelivery.getCircuit();
                    System.out.println("circuit:" + circuitNewDelivery);
                    if (circuitNewDelivery == null) {
                        //add an error: CHoose a valid circuit
                        // window.getCityMapMenuPanel().add(new JTextField("Choose a valid circuit"));
                    } else {
                        window.setWaitingState(2);
                    }
                }
                repaint();
            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent e) {
                CityMapContainerView that = CityMapContainerView.this;

                Vector2D dims = that.controller.getModel().getCityMap().getCoveredAreaDimensions();

                that.offsetX += e.getX() - that.originX;
                that.offsetY += e.getY() - that.originY;
                that.offsetX = Math.min(that.offsetX, that.kmToPixelCoeff() * 0.1);
                that.offsetX = Math.max(that.offsetX, -that.kmToPixelCoeff() * (0.1 + dims.x) + WIDTH);
                that.offsetY = Math.min(that.offsetY, that.kmToPixelCoeff() * 0.1);
                that.offsetY = Math.max(that.offsetY, -that.kmToPixelCoeff() * (0.1 + dims.y) + HEIGHT);

                that.originX = e.getX();
                that.originY = e.getY();
                CityMapContainerView.this.repaint();
            }

            public void mouseMoved(MouseEvent e) {
                final int maxpx = 16;

                int x = (int) (e.getX());
                int y = (int) (e.getY());

                Section section = getSectionByCoordinates(x, y);
                Intersection intersection = getIntersectionByCoordinates(x, y);

                if (CityMapContainerView.this.hoveredInter != intersection || CityMapContainerView.this.hoveredSection != section) {
                    CityMapContainerView.this.repaint();
                }
                CityMapContainerView.this.hoveredSection = section;
                CityMapContainerView.this.hoveredInter = intersection;
            }
        });
    }

    public Intersection getIntersectionByCoordinates(int x, int y) {
        Intersection intersection = null;
        Geolocation origin = getOrigin(controller.getModel().getCityMap());
        CityMap cityMap = controller.getModel().getCityMap();

        for (Intersection inter : cityMap.getIntersections().values()) {
            Geolocation geo = geolocationToPixels(origin, inter.getGeolocation());
            if (Math.abs(x - geo.getLongitude()) <= delivDotSize
                    && Math.abs(y - geo.getLatitude()) <= delivDotSize) {
                intersection = inter;
            }
        }
        return intersection;
    }

    public Section getSectionByCoordinates(int x, int y) {
        Section sect = null;
        Geolocation origin = getOrigin(controller.getModel().getCityMap());
        CityMap cityMap = controller.getModel().getCityMap();

        double coeff = CityMapContainerView.this.kmToPixelCoeff();
        double distanceSec = 2e300;

        for (Section section : cityMap.getSections()) {
            Geolocation start = section.getStartIntersection().getGeolocation();
            Geolocation end = section.getEndIntersection().getGeolocation();
            Geolocation sectionCenter = Geolocation.center(start, end);
            Geolocation px = CityMapContainerView.this.geolocationToPixels(origin, sectionCenter);

            double length = start.distance(end) * coeff / 2;
            double dist = Math.pow(x - px.getLongitude(), 2) + Math.pow(y - px.getLatitude(), 2);

            if (dist > length) {
                continue;
            }

            if (dist < distanceSec) {
                sect = section;
                distanceSec = dist;
            }
        }

        return sect;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.drawCityMap(g, dotSize);
        this.drawDeliveriesOnCityMap(g);
        this.drawCircuits(g);
        window.getCityMapMenuPanel().getLoadNewCityMapButton().setVisible(true);
        window.getCityMapMenuPanel().getLoadDeliveryRequestButton().setVisible(true);
    }

    /**
     * Get an origin for the city map ( a geolocation reference that enables the
     * transformation from degrees to kms then pixels)
     *
     * @param cityMap
     * @return a north west geolocation on the map resolved as origin
     */
    private Geolocation getOrigin(CityMap cityMap) {
        Geolocation origin = null;

        for (Intersection inter : cityMap.getIntersections().values()) {
            if (origin == null) {
                origin = inter.getGeolocation().copy();
            } else {
                origin.setLatitude(Math.max(origin.getLatitude(), inter.getGeolocation().getLatitude()));
                origin.setLongitude(Math.min(origin.getLongitude(), inter.getGeolocation().getLongitude()));
            }
        }
        return origin;
    }

    private void colorDelivery(Graphics g, Color c, Delivery delivery, Geolocation origin, int dotSize) {

        if (delivery == null) {
            return;
        }

        Geolocation geo = geolocationToPixels(origin, delivery.getGeolocation());

        if (hoveredInter != null && hoveredInter.getGeolocation().equals(delivery.getGeolocation())) {
            g.setColor(new Color(140, 100, 100));
            g.fillArc((int) geo.getLongitude() - dotSize, (int) geo.getLatitude() - dotSize, dotSize * 2, dotSize * 2, 0, 360);
        } else if (selectedDelivery == delivery) {
            g.setColor(Color.YELLOW);
            g.fillArc((int) geo.getLongitude() - dotSize / 2, (int) geo.getLatitude() - dotSize / 2, dotSize, dotSize, 0, 360);
            // window.getDeliveryRequestPanel().colorTable(2, Color.YELLOW,delivery);
        } else {
            g.setColor(c);
            g.fillArc((int) geo.getLongitude() - dotSize / 2, (int) geo.getLatitude() - dotSize / 2, dotSize, dotSize, 0, 360);
        }

    }

    private void colorWarehouse(Graphics g, Color c, Geolocation geolocation, Geolocation origin, int dotSize) {

        if (geolocation == null) {
            return;
        }

        Geolocation geo = geolocationToPixels(origin, geolocation);

        if (hoveredInter != null && hoveredInter.getGeolocation().equals(geolocation)) {
            g.setColor(new Color(140, 100, 100));
            g.fillArc((int) geo.getLongitude() - dotSize, (int) geo.getLatitude() - dotSize, dotSize * 2, dotSize * 2, 0, 360);
        } else if (selectedDelivery != null && geolocation.equals(selectedDelivery.getGeolocation())) {

            g.setColor(Color.YELLOW);
            g.fillArc((int) geo.getLongitude() - dotSize / 2, (int) geo.getLatitude() - dotSize / 2, dotSize, dotSize, 0, 360);

        } else {
            g.setColor(c);
            g.fillArc((int) geo.getLongitude() - dotSize / 2, (int) geo.getLatitude() - dotSize / 2, dotSize, dotSize, 0, 360);
        }

    }

    private void colorSectionUtil(Graphics g, Section s, Color c, Geolocation origin, boolean hovered) {
        g.setColor(c);
        int lineThickness = 4;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        Geolocation start = s.getStartIntersection().getGeolocation();
        Geolocation end = s.getEndIntersection().getGeolocation();

        Geolocation pxStart = geolocationToPixels(origin, start);
        Geolocation pxEnd = geolocationToPixels(origin, end);

        if (hovered) {
            g2.setStroke(new BasicStroke(lineThickness * 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawLine((int) pxStart.getLongitude(), (int) pxStart.getLatitude(), (int) pxEnd.getLongitude(), (int) pxEnd.getLatitude());
            g2.setStroke(new BasicStroke(lineThickness));
        } else {
            g.drawLine((int) pxStart.getLongitude(), (int) pxStart.getLatitude(), (int) pxEnd.getLongitude(), (int) pxEnd.getLatitude());
        }

    }

    private void colorSections(Graphics g, Color c, java.util.List<Section> sections, Geolocation origin) {
        for (Section sec : sections) {
            if (sec == hoveredSection) {
                colorSectionUtil(g, sec, c, origin, true);
                // if section is part of circuit then hover the whole circuit 
                if (sec.getCircuit() != null) {
                    LinkedList<Trip> circuitTrips = sec.getCircuit().getTrips();
                    for (Trip t : circuitTrips) {
                        java.util.List<Section> circuitSections = t.getSections();
                        for (Section s : circuitSections) {
                            colorSectionUtil(g, s, c, origin, true);
                        }
                    }
                }
            } else colorSectionUtil(g, sec, c, origin, false);
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

        Geolocation origin = getOrigin(cityMap);

        LinkedList<Delivery> delivs = dr.getDeliveries();
        for (Delivery d : delivs) {
            colorDelivery(g, Color.green, d, origin, delivDotSize);
        }

        Intersection warehouseIntersection = cityMap.getIntersectionById(dr.getWarehouseAddress());
        if (warehouseIntersection != null) {
            colorWarehouse(g, Color.red, warehouseIntersection.getGeolocation(), origin, delivDotSize);
        }
    }

    private void drawCircuits(Graphics g) {
        CityMap cityMap = controller.getModel().getCityMap();
        LinkedList<Circuit> circuits = this.controller.getModel().getCircuits();

        if (cityMap == null || circuits == null) {
            return;
        }
        Geolocation origin = getOrigin(cityMap);
        int i = 0;
        for (Circuit circuit : circuits) {
            Color c = new Color(180, Math.floorMod(50 + 40 * i, 250), Math.floorMod(120 + 40 * i, 250));
            for (Trip trip : circuit.getTrips()) {
                colorSections(g, c, trip.getSections(), origin);
            }
            i++;
        }
    }

    private void drawCityMap(Graphics g, int dotSize) {

        Graphics2D g2 = (Graphics2D) g;

        CityMap cityMap = this.controller.getModel().getCityMap();
        if (cityMap.getIntersections().size() == 0) {
            return;
        }
        Geolocation origin = getOrigin(cityMap);

        colorSections(g, new Color(100, 100, 105), cityMap.getSections(), origin);
        g.setColor(new Color(180, 140, 180));

        for (Intersection inter : cityMap.getIntersections().values()) {
            Geolocation geo = inter.getGeolocation();
            Geolocation target = geolocationToPixels(origin, geo);

            g.fillArc((int) target.getLongitude() - dotSize / 2, (int) target.getLatitude() - dotSize / 2, dotSize, dotSize, 0, 360);
        }

        if (hoveredInter != null) {
            g.setColor(Color.CYAN);
            Geolocation target = geolocationToPixels(origin, this.hoveredInter.getGeolocation());
            g.fillArc(
                    (int) (target.getLongitude() - dotSize),
                    (int) (target.getLatitude() - dotSize),
                    (int) Math.round(dotSize * 2),
                    (int) Math.round(dotSize * 2),
                    0, 360);
        }
        if (newDelivery != null) {
            g.setColor(Color.GREEN);
            Geolocation target = geolocationToPixels(origin, newDelivery.getGeolocation());
            g.fillArc((int) target.getLongitude() - delivDotSize / 2, (int) target.getLatitude() - delivDotSize / 2, delivDotSize, delivDotSize, 0, 360);
        }

        // Write the name of every sections if it's big enough
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        FontMetrics fm = g2.getFontMetrics();

        for (Section section : cityMap.getSections()) {
            Geolocation first = section.getStartIntersection().getGeolocation();
            Geolocation last = section.getEndIntersection().getGeolocation();

            double length = distanceInPixels(first, last);
            double strpx = fm.stringWidth(section.getStreetName());

            if (strpx > length) {
                continue;
            }

            double angle = this.angleBetweenPositions(first, last);
            double offsetleft = Math.cos(angle) * strpx / 2;
            double offsettop = Math.sin(angle) * strpx / 2;

            Geolocation center = new Geolocation(
                    (first.getLatitude() + last.getLatitude()) / 2,
                    (first.getLongitude() + last.getLongitude()) / 2);
            Geolocation target = geolocationToPixels(origin, center);
            int x = (int) Math.round(target.getLongitude() - offsetleft);
            int y = (int) Math.round(target.getLatitude() + offsettop);

            g2.rotate(-angle, x, y);
            g2.drawString(section.getStreetName(), x, y);
            g2.rotate(+angle, x, y);

        }
    }

    private double kmToPixelCoeff() {
        return (100 - this.zoomSlider.getValue()) * KM_TO_PIXEL;
    }

    private Geolocation pixelsToGeolocation(Geolocation origin, int x, int y) {
        double coeff = kmToPixelCoeff();
        Geolocation ret = new Geolocation(y / coeff - offsetY, x / coeff - offsetX);
        return ret;
    }

    private double distanceInPixels(Geolocation origin, Geolocation target) {
        double coeff = kmToPixelCoeff();
        return origin.distance(target) * coeff;
    }

    private Geolocation geolocationToPixels(Geolocation origin, Geolocation target) {
        double coeff = kmToPixelCoeff();
        Geolocation geoY = new Geolocation(target.getLatitude(), origin.getLongitude());
        Geolocation geoX = new Geolocation(origin.getLatitude(), target.getLongitude());
        Geolocation ret = new Geolocation(origin.distance(geoY) * coeff + offsetY, origin.distance(geoX) * coeff + offsetX);

        return ret;
    }

    private double angleBetweenPositions(Geolocation A, Geolocation B) {
        double a = Math.atan((A.getLatitude() - B.getLatitude()) / (A.getLongitude() - B.getLongitude()));
        if (a > Math.PI / 2) {
            a = a - Math.PI;
        }
        return a;
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
