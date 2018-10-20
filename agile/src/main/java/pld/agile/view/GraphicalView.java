package pld.agile.view;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import model.Map;

public class GraphicalView extends JPanel{
	
	private JPanel map;
	private JPanel mapMenu;
	
	public GraphicalView(Map m, Window w){
            map = new MapContainer(m,w);
            mapMenu = new MapMenu();
            setLayout(new BorderLayout());
            this.add(map,BorderLayout.CENTER);
            this.add(mapMenu, BorderLayout.PAGE_START);
            setBackground(Color.white);
	}

}
