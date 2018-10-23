package pld.agile.view;

import controller.Controller;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Circuit;
import model.Geolocation;
import model.Intersection;
import model.CityMap;
import model.Section;
import model.Trip;

public class CityMapContainerView extends JPanel implements Observer {

    private JButton loadCityMapButton;
    private JSlider zoomSlider;

    private int cityMapHeight = 600;
    private int cityMapWidth = 600;
    private Controller controller;

    static public double KM_TO_PIXEL = 0.0001;
    // For mouse listener
    private double offsetX = 0;
    private double offsetY = 0;
    private int originX = 0;
    private int originY = 0;

    public CityMapContainerView(Window w, Controller c) {
        super();
        this.controller = c;

        setLayout(null);
        setBorder(BorderFactory.createTitledBorder("City Map :"));

        this.createSlider();

        this.controller.getCityMap().addObserver(this);
        loadCityMapButton = new JButton("Load a city map");
        loadCityMapButton.addActionListener(new ButtonListener(c, w, this));

        loadCityMapButton.setSize(100, 100);
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


            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mouseClicked(MouseEvent e) {}
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

        this.drawCityMap(g);

        CityMap cityMap = controller.getCityMap();
        if (cityMap != null) {
            Intersection warehouseIntersection = cityMap.getIntersectionById("25611425");
            drawDeliveriesOnCityMap(g, Color.red, warehouseIntersection);
        }

    }

    private void drawDeliveriesOnCityMap(Graphics g, Color color, Intersection intersection) {
        g.setColor(color);
        int dotSize = 6;

        if (intersection != null) {
            Geolocation geo = geolocationToPixels(intersection.getGeolocation(), intersection.getGeolocation());
            g.fillArc((int) geo.getLongitude() - dotSize / 2, (int) geo.getLatitude() - dotSize / 2, dotSize, dotSize, 0, 360);
        }

    }

    private void drawCityMap(Graphics g) {

        CityMap cityMap = this.controller.getCityMap();

        if (cityMap.getIntersections().size() == 0) {
            return;
        }

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

        g.setColor(new Color(100, 100, 105));
        int lineThickness = 4;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(lineThickness));

        for (Section sec : cityMap.getSections()) {
            Geolocation start = sec.getStartIntersection().getGeolocation();
            Geolocation end = sec.getEndIntersection().getGeolocation();

            Geolocation pxStart = this.geolocationToPixels(origin, start);
            Geolocation pxEnd = this.geolocationToPixels(origin, end);

            g.drawLine((int) pxStart.getLongitude(), (int) pxStart.getLatitude(), (int) pxEnd.getLongitude(), (int) pxEnd.getLatitude());

        }

        g.setColor(new Color(180, 140, 180));
        int dotSize = 6;

        for (Intersection inter : cityMap.getIntersections().values()) {
            Geolocation geo = inter.getGeolocation();
            Geolocation target = geolocationToPixels(origin, geo);

            g.fillArc((int) target.getLongitude() - dotSize / 2, (int) target.getLatitude() - dotSize / 2, dotSize, dotSize, 0, 360);

        }

        /*
         This is for debug right now, there is smthg weird happenning with the section comming from the first node
        g.setColor(new Color(180, 140, 140));
        lineThickness = 6;
        g2.setStroke(new BasicStroke(lineThickness));
        
        Circuit circuit = new Circuit();
        Trip t = new Trip();
        //t.addSection(plan.getSections().get(0));
        //t.addSection(plan.getSections().get(1));
        //t.addSection(plan.getSections().get(2));
        //t.addSection(plan.getSections().get(3));
        //t.addSection(plan.getSections().get(4));
        circuit.getTrips().add(t);
        
        t = new Trip();
        t.addSection(plan.getSections().get(10));
        t.addSection(plan.getSections().get(11));
        t.addSection(plan.getSections().get(12));
        t.addSection(plan.getSections().get(13));
        t.addSection(plan.getSections().get(14));
        circuit.getTrips().add(t);
        
        int i = 0;
        for(Trip trip : circuit.getTrips()) {
            g.setColor(new Color(180, 150 - 40 * i, 120 + 40 * i));
            i++;
            for(Section sec : trip.getSections()) {
            Geolocation start 	= sec.getStartIntersection().getGeolocation();
            Geolocation end 		= sec.getEndIntersection().getGeolocation();

            Geolocation pxStart 	= this.geolocationToPixels(origin, start);
            Geolocation pxEnd 	= this.geolocationToPixels(origin, end);

            g.drawLine((int)pxStart.getLongitude(), (int)pxStart.getLatitude(), (int)pxEnd.getLongitude(), (int)pxEnd.getLatitude());
            }
        }
         */
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
        return cityMapHeight;
    }

    // to change
    public int getWidth() {
        return cityMapWidth;
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