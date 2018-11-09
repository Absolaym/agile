/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pld.agile.view;

import controller.Controller;
import error.ErrorLogger;
import error.ErrorObserver;
import error.Error;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author olivi
 */
public class ErrorAreaView extends JPanel {
    private final int HEIGHT = 150;
    private final int WIDTH = 1000;
    private JTextArea text;
    private Controller controller;

    public ErrorAreaView(Window w, Controller controller) {
        this.controller = controller;
        text = new JTextArea(20, 90);
        add(text);
        
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Error area :"));
        w.getContentPane().add(this);
    }
    
    public void update(Error error) {
    		this.text.setText(error.toString());
    		this.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    // to change
    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }

    
}
