//package pld.agile.view;
//
//import controller.Controller;
//import javax.swing.*;
//import java.awt.*;
//import java.util.Iterator;
//import java.util.LinkedList;
//import model.Map;
//
//public class GraphicalView extends JPanel {
//
//    private JPanel map;
//    private JPanel mapMenu;
//    private int height;
//    private int width;
//
//    public GraphicalView(Map m, Window w, Controller c) {
//        map = new MapContainer(m, w, c);
//        mapMenu = new MapMenu(c);
//        
//        height = map.getHeight() + mapMenu.getHeight() + 10;
//        width = Math.max(map.getWidth(), mapMenu.getWidth()) + 10;
//        
//        setLayout(null);
//        this.add(map);
//        this.add(mapMenu);
//        setBackground(Color.white);
//        
//        w.getContentPane().add(this);
//    }
//    
//    // to change
//    public int getHeight() {
//        return height;
//    }
//    
//    // to change
//    public int getWidth() {
//        return width;
//    }
//
//}
