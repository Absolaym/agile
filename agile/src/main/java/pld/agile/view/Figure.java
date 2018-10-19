package pld.agile.view;

import java.awt.*;
import javax.swing.*;

public class Figure {
	
	private Point pointAncrage;	

    public Figure(int c1, int c2){     
    	pointAncrage = new Point(c1,c2);
        
    }
    public Point getPoint(){
    	return pointAncrage;
    }
    
    public void dessiner(Graphics g) {
    	
    }

}