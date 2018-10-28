package pld.agile.view;

import controller.Controller;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.*;

public class CityMapMenuView extends JPanel {

    private JButton computeCircuitsButton;
    private JButton loadNewCityMapButton;
    private int menuHeight = 80;
    private int menuWidth = 200;

    public CityMapMenuView(Window w, Controller controller) {
        computeCircuitsButton = new JButton("Compute circuits");
        computeCircuitsButton.addActionListener(new ButtonListener(controller,w));
        loadNewCityMapButton = new JButton("Load a new city map");
        loadNewCityMapButton.setEnabled(false);
        computeCircuitsButton.setEnabled(false);
        loadNewCityMapButton.addActionListener(new ButtonListener(controller,w));
        this.add(computeCircuitsButton);
        this.add(loadNewCityMapButton);
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

    public JButton getLoadNewCityMapButton() {
        return loadNewCityMapButton;
    }

    
}
