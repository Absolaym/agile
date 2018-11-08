package pld.agile.view;

import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.*;

public class CityMapMenuView extends JPanel {

    private JButton computeCircuitsButton;
    private JButton loadNewCityMapButton;
    private JButton loadDeliveryRequestButton;
    private JButton addNewDeliveryButton;
    private final int buttonHeight = 30;
    private int menuHeight = 120;
    private int menuWidth = 600;
    private JLabel courierNumberLabel;
    private JTextField courierNumberField;
    private JPanel messages;

    public CityMapMenuView(Window w, Controller controller) {
        setLayout(new FlowLayout());
        loadNewCityMapButton = new JButton("Load a new city map");
        loadNewCityMapButton.setEnabled(false);
        ButtonListener buttonListener = new ButtonListener(controller,w);
        
        computeCircuitsButton = new JButton("Compute circuits");
        computeCircuitsButton.addActionListener(buttonListener);
        computeCircuitsButton.setEnabled(false);

        loadNewCityMapButton.addActionListener(new ButtonListener(controller,w));
        loadDeliveryRequestButton = new JButton("Load delivery requests");
        loadDeliveryRequestButton.addActionListener(buttonListener);
        loadDeliveryRequestButton.setEnabled(false);
        
        addNewDeliveryButton = new JButton("Add a delivery");
        addNewDeliveryButton.addActionListener(buttonListener);
//        addNewDeliveryButton.setEnabled(false);
        
        courierNumberLabel = new JLabel("Couriers' number : ");
        courierNumberField = new JTextField("1", 4);
        courierNumberField.setEditable(true);
        
        messages = new JPanel(new FlowLayout());
        messages.setBackground(Color.WHITE);
        messages.setPreferredSize(new Dimension(600, 50));

        this.add(loadNewCityMapButton);
        this.add(loadDeliveryRequestButton);
        this.add(computeCircuitsButton);
        this.add(addNewDeliveryButton);
        this.add(courierNumberLabel);
        this.add(courierNumberField);
        this.add(messages);
        
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
    
    public JTextField getCourierNumberField() {
    	return courierNumberField;
    }
    
    public int getCourierNumber() {
    	String numberString = this.courierNumberField.getText();
    	return Integer.parseInt(numberString);
    }

    public JButton getAddNewDeliveryButton() {
        return addNewDeliveryButton;
    }

    public JPanel getMessages() {
        return messages;
    }
    

}
