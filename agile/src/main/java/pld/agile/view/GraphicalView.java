package pld.agile.view;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class GraphicalView extends JPanel{
	
	private JPanel map;
	private JPanel mapMenu;
	
	public GraphicalView(){
		map = new Map();
		mapMenu = new MapMenu();
		this.add(map);
		this.add(mapMenu);
	}

}
