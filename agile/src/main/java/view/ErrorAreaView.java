/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Controller;
import error.ErrorLogger;
import error.ErrorObserver;
import error.ProjectError;

import java.awt.*;
import java.util.Locale;

import javax.swing.*;

@SuppressWarnings("serial")
/**
 * This class extends a panel containing a text area with information about potential errors
 */
public class ErrorAreaView extends JPanel implements ErrorObserver {

    private final int HEIGHT = 80;
    private final int WIDTH = 1000;
    private ProjectError error = null;

    private Controller controller;

    public ErrorAreaView(Window w, Controller controller) {
        this.controller = controller;
        ErrorLogger.getInstance().addObserver(this);

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Error area :"));
        this.setLocation(0, 0);

        w.getContentPane().add(this);
    }

    public void update(ProjectError error) {
        this.error = error;
        this.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.error == null) {
            return;
        }

        g.setColor(new Color(120, 20, 20));
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(this.error.toString(), 10, 30);
        g.setFont(new Font("Arial", Font.ITALIC, 8));
        g.drawString("(Note that this error is the last encountered, it might have been resolved by one of your subsequent actions)", 10, 48);
    }

    // to change
    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }

}
