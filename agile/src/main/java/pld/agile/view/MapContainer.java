package pld.agile.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import model.Map;

public class MapContainer extends JPanel implements Observer{
	
        private Map map;
	private JButton loadMapButton;
	
	public MapContainer(Map m, Window w) {
            super();
            m.addObserver(this);
	    loadMapButton = new JButton("Load a map");
            this.add(loadMapButton);
            this.map = m;
            setBackground(Color.white);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
	}	

    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}