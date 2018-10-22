package pld.agile.view;

import controller.Controller;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
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

import model.Geolocation;
import model.Intersection;
import model.Plan;
import model.Section;

public class MapContainerView extends JPanel implements Observer {

    private JButton loadMapButton;
    private JSlider zoomSlider;
    
    private int planHeight = 600;
    private int planWidth = 800;
    private Controller controller;
    
    static public double KM_TO_PIXEL = 0.0001;
    private double offsetX = 0;
    private double offsetY = 0;
    
    private class SliderListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider)e.getSource();
            source.getParent().repaint();
        }
    }
    
    private class MouseListener implements MouseMotionListener {
    	
    		private int previousX = -1;
    		private int previousY = -1;
    
    		public void mouseDragged(MouseEvent e) {

    			if(previousX == -1 && previousY == -1) {
    				previousX = e.getX();
    				previousY = e.getY();
    			}
    			
    			MapContainerView.this.offsetX += e.getX() - previousX;
    			MapContainerView.this.offsetY += e.getY() - previousY;
    			
    			previousX = e.getX();
    			previousY = e.getY();
    			
    			MapContainerView.this.repaint();
    			
    		}

		public void mouseMoved(MouseEvent e) {
	
		}
    }

    public MapContainerView(Window w, Controller c) {
        super();
        this.controller = c;
        
        setLayout(null);
        setBorder(BorderFactory.createTitledBorder("Map :"));
        
        this.zoomSlider = new JSlider();
        this.zoomSlider.setMinimum(20);
        this.zoomSlider.setMaximum(100);
        this.zoomSlider.setValue(50);
        this.zoomSlider.setSize(100, 30);
        this.zoomSlider.setAlignmentX( this.getWidth() - this.zoomSlider.getWidth() - 20 );
        this.zoomSlider.setAlignmentY( this.zoomSlider.getHeight() + 200 );
        this.zoomSlider.addChangeListener(new SliderListener());

        this.add(this.zoomSlider);
        
        
        this.controller.getPlan().addObserver(this);
        loadMapButton = new JButton("Load a plan");
        loadMapButton.addActionListener(new ButtonListener(c));
        
        loadMapButton.setSize(100,100);
        loadMapButton.setLocation(100,100);
        add(loadMapButton);
        
        setBackground(Color.DARK_GRAY);
        
        this.addMouseMotionListener(new MouseListener());
        w.getContentPane().add(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        this.drawMap(g);
    }
    
    private void drawMap(Graphics g) {
 
        Plan plan = this.controller.getPlan();
        
        if(plan.getIntersections().size() == 0)	return;
        
        Geolocation origin = null;
        // Origin to the top left corner
        for(Intersection inter : plan.getIntersections().values()) {
        		if(origin == null) {
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
        
        for(Section sec : plan.getSections()) {
        		Geolocation start 	= sec.getStartIntersection().getGeolocation();
        		Geolocation end 		= sec.getEndIntersection().getGeolocation();
        		
        		Geolocation pxStart 	= this.geolocationToPixels(origin, start);
        		Geolocation pxEnd 	= this.geolocationToPixels(origin, end);
        		
        		g.drawLine((int)pxStart.getLongitude(), (int)pxStart.getLatitude(), (int)pxEnd.getLongitude(), (int)pxEnd.getLatitude());
        }
        
        g.setColor(new Color(180, 140, 180));
        int dotSize = 6;
        
        for(Intersection inter : plan.getIntersections().values()) {
        		Geolocation geo = inter.getGeolocation();
        		Geolocation target = geolocationToPixels( origin, geo );
        		
        		
        		g.fillArc((int)target.getLongitude() - dotSize / 2, (int)target.getLatitude() - dotSize / 2, dotSize, dotSize, 0, 360);
        }
        
        
    }
    
    private Geolocation geolocationToPixels(Geolocation origin, Geolocation target) {
    		double coeff = this.zoomSlider.getValue() * KM_TO_PIXEL;
		Geolocation geoY = new Geolocation( target.getLatitude(), origin.getLongitude() );
		Geolocation geoX = new Geolocation( origin.getLatitude(), target.getLongitude() );
		Geolocation ret = new Geolocation( origin.distance( geoY ) / coeff + offsetY, origin.distance( geoX ) / coeff + offsetX);
		return ret;
    }
    
    // to change
    public int getHeight() {
        return planHeight;
    }
    
    // to change
    public int getWidth() {
        return planWidth;
    }

    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
