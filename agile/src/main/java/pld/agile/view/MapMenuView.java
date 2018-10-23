package pld.agile.view;

import controller.Controller;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.*;

public class MapMenuView extends JPanel {

    private JButton computeCircuitsButton;
    private JButton loadNewMapButton;
    private int menuHeight = 80;
    private int menuWidth = 200;

    public MapMenuView(Window w, Controller controller) {
        computeCircuitsButton = new JButton("Compute circuits");
        computeCircuitsButton.addActionListener(new ButtonListener(controller,w));
        loadNewMapButton = new JButton("Load a new map");
        loadNewMapButton.addActionListener(new ButtonListener(controller,w));
        this.add(computeCircuitsButton);
        this.add(loadNewMapButton);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Menu :"));
        w.getContentPane().add(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    // to change
    public int getHeight() {
        return menuHeight;
    }

    // to change
    public int getWidth() {
        return menuWidth;
    }

    public JButton getComputeCircuitsButton() {
        return computeCircuitsButton;
    }

    public JButton getLoadNewMapButton() {
        return loadNewMapButton;
    }

    
}
