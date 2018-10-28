package pld.agile.view;

import controller.Controller;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.*;

public class CityMapMenuView extends JPanel {

    private JButton computeCircuitsButton;
    private JButton loadNewCityMapButton;
    private JButton loadDeliveryRequestButton;
    private final int buttonHeight = 30;
    private int menuHeight = 60;
    private int menuWidth = 600;

    public CityMapMenuView(Window w, Controller controller) {
        setLayout(new FlowLayout());
        loadNewCityMapButton = new JButton("Load a new city map");
        loadNewCityMapButton.setEnabled(false);
        
        computeCircuitsButton = new JButton("Compute circuits");
        computeCircuitsButton.addActionListener(new ButtonListener(controller,w));
        computeCircuitsButton.setEnabled(false);

        loadNewCityMapButton.addActionListener(new ButtonListener(controller,w));
        loadDeliveryRequestButton = new JButton("Load delivery requests");
        loadDeliveryRequestButton.addActionListener(new ButtonListener(controller, w));
        loadDeliveryRequestButton.setEnabled(false);
        
        this.add(loadNewCityMapButton);
        this.add(loadDeliveryRequestButton);
        this.add(computeCircuitsButton);
        
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

    public int getWidth() {
        return menuWidth;
    }

    public JButton getComputeCircuitsButton() {
        return computeCircuitsButton;
    }

    public JButton getLoadNewCityMapButton() {
        return loadNewCityMapButton;
    }
    
    public JButton getLoadDeliveryRequestButton() {
        return loadDeliveryRequestButton;
    }

}
