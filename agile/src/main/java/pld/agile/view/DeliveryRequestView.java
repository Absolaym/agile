package pld.agile.view;

import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.*;

public class DeliveryRequestView extends JPanel{
	
	private JTable deliveryRequests;
	
	public DeliveryRequestView(Window w) {
		this.add(new JButton("OK"));
                //this.setSize(400,700);
                setBackground(Color.red);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
	}

	
}
