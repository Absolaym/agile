package pld.agile.view;

import java.awt.Graphics;

import javax.swing.*;

public class MapMenu extends JPanel{
	
	private JButton computeCircuitsButton;
	private JButton loadMapButton;
	
	public MapMenu() {
		computeCircuitsButton = new JButton("Compute circuits");
		loadMapButton = new JButton("Load a new map");
		this.add(computeCircuitsButton);
		this.add(loadMapButton);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
	}	
}