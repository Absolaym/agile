package pld.agile.view;

import controller.Controller;
import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import model.Map;

public class GraphicalView extends JPanel{
	
	private JPanel map;
	private JPanel mapMenu;
	
	public GraphicalView(Map m, Window w, Controller c){
            map = new MapContainer(m,w,c);
            mapMenu = new MapMenu(c);
            
            setLayout(new BorderLayout());
            this.add(map,BorderLayout.CENTER);
            this.add(mapMenu, BorderLayout.PAGE_START);
            setBackground(Color.white);
	}

}
