package pld.agile.view;

import java.awt.Button;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.*;

public class DeliveryRequest extends JPanel{
	
	private JTable deliveryRequests;
	
	public DeliveryRequest() {
		this.add(new JButton("OK"));
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
	}

	
}
