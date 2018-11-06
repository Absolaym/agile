/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pld.agile.view;

import controller.Controller;
import error.ErrorLogger;
import error.ErrorObserver;
import error.PlacoError;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author olivi
 */
public class ErrorAreaView extends JPanel implements ErrorObserver {
	
    private int height = 100;
    private int width = 600;
    private JTextArea text;
    private Controller controller;

    public ErrorAreaView( Window w ) {
        this.controller = controller;
        
        ErrorLogger.getInstance().addObserver(this);
        
        setBackground( new Color(255, 240, 240) );
        setBorder(BorderFactory.createTitledBorder("Error area :"));
        
        text = new JTextArea("If anything goes wrong, check nearby");
        text.setBounds(0, 0, this.width, this.height);
        this.add(text);
        
        w.getContentPane().add(this);
    }
    
    public void update(PlacoError error) {
    		this.text.setText(error.toString());
    		this.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    // to change
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    
}
