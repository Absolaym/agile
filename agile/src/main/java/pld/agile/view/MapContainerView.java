package pld.agile.view;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import model.Map;

public class MapContainerView extends JPanel implements Observer {

    private Map map;
    private JButton loadMapButton;
    private int mapHeight = 800;
    private int mapWidth = 600;

    public MapContainerView(Map m, Window w, Controller c) {
        super();
        setLayout(null);
        setBorder(BorderFactory.createTitledBorder("Map :"));
        m.addObserver(this);
        loadMapButton = new JButton("Load a map");
        loadMapButton.addActionListener(new ButtonListener(c));
        
        loadMapButton.setSize(100,100);
        loadMapButton.setLocation(100,100);
        add(loadMapButton);
        
        
        this.map = m;
        setBackground(Color.DARK_GRAY);
        w.getContentPane().add(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
    
    // to change
    public int getHeight() {
        return mapHeight;
    }
    
    // to change
    public int getWidth() {
        return mapWidth;
    }

    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
