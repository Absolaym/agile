package pld.agile.view;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

public class Map extends JPanel{
	
	private JButton loadMapButton;
	
	public Map() {
		loadMapButton = new JButton("Load a map");
		this.add(loadMapButton);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
	}	
}