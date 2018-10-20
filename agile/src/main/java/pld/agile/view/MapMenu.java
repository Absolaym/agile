package pld.agile.view;

import controller.Controller;
import java.awt.Graphics;

import javax.swing.*;

public class MapMenu extends JPanel{
	
	private JButton computeCircuitsButton;
	private JButton loadNewMapButton;
	
	public MapMenu(Controller controller) {
		computeCircuitsButton = new JButton("Compute circuits");
                computeCircuitsButton.addActionListener(new ButtonListener(controller));
		loadNewMapButton = new JButton("Load a new map");
                loadNewMapButton.addActionListener(new ButtonListener(controller));
		this.add(computeCircuitsButton);
		this.add(loadNewMapButton);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
	}	
}