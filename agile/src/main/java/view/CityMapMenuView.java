package view;


import controller.Controller;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.*;

public class CityMapMenuView extends JPanel {

    private final int buttonHeight = 30;
    private final int HEIGHT = 100;
    private final int WIDTH = 1000;
    
    private JButton computeCircuitsButton;
    private JButton loadNewCityMapButton;
    private JButton loadDeliveryRequestButton;
    private JButton addNewDeliveryButton;
    private JButton undoButton;
    private JButton redoButton;
    
    private JLabel courierNumberLabel;
    
    private JTextField courierNumberField;
    private JTextField deliveryDurationField;
    
    private JPanel messages;
    
    ButtonListener buttonListener;

    public CityMapMenuView(Window w, Controller controller) {
        setLayout(new FlowLayout());
        loadNewCityMapButton = new JButton("Load a new city map");
        loadNewCityMapButton.setEnabled(false);
        buttonListener = new ButtonListener(controller, w);

        computeCircuitsButton = new JButton("Compute circuits");
        computeCircuitsButton.addActionListener(buttonListener);
        computeCircuitsButton.setEnabled(false);

        loadNewCityMapButton.addActionListener(new ButtonListener(controller, w));
        loadDeliveryRequestButton = new JButton("Load delivery requests");
        loadDeliveryRequestButton.addActionListener(buttonListener);
        loadDeliveryRequestButton.setEnabled(false);

        addNewDeliveryButton = new JButton("Add a delivery");
        addNewDeliveryButton.addActionListener(buttonListener);
        addNewDeliveryButton.setEnabled(false);
        
        undoButton = new JButton("Undo");
        undoButton.addActionListener(buttonListener);
        undoButton.setEnabled(false);
        
        redoButton = new JButton("Redo");
        redoButton.addActionListener(buttonListener);
        redoButton.setEnabled(false);

        courierNumberLabel = new JLabel("Number of couriers: ");
        courierNumberField = new JTextField("1", 4);
        courierNumberField.setEditable(true);
        
        

        messages = new JPanel(new FlowLayout());
        messages.setBackground(Color.WHITE);
        messages.setPreferredSize(new Dimension(800, 50));

        this.add(loadNewCityMapButton);
        this.add(loadDeliveryRequestButton);
        this.add(courierNumberLabel);
        this.add(courierNumberField);
        this.add(computeCircuitsButton);
        this.add(addNewDeliveryButton);
 
        this.add(addNewDeliveryButton);
        
        this.add(messages);
        this.add(undoButton);
        this.add(redoButton);
        
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createTitledBorder("Menu :"));
        w.getContentPane().add(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    public void addNewDelivery(String step) {
        messages.removeAll();
        if (step.equals("")) return;
        if (step.equals("cancel")) return;
        
        JLabel text = new JLabel();
        deliveryDurationField = null;
        
        JButton cancelAddingDeliveryButton = new JButton("Cancel");
        cancelAddingDeliveryButton.addActionListener(buttonListener);
        messages.add(cancelAddingDeliveryButton);
        
        if (step.equals("select")) 
            text.setText("Please select the delivery address on map.");
            
        else if (step.equals("time")) {
            text.setText("Please choose a circuit on the map.  Delivery duration : ");
            deliveryDurationField = new JTextField("5", 4);
            JButton goBackToPreviousStepButton = new JButton("Go back to previous step");
            goBackToPreviousStepButton.addActionListener(buttonListener);
            messages.add(goBackToPreviousStepButton);
        }
        text.setSize(text.getPreferredSize().width, 30);
        int textLocationX = messages.getWidth() / 2 - text.getSize().width/2;
        text.setLocation(textLocationX, 0);
        text.setVisible(true);
        messages.add(text);
        
        if(deliveryDurationField != null) {
            deliveryDurationField.setSize(50, 20);
            deliveryDurationField.setLocation(textLocationX + text.getSize().width + 10 , 0);
            deliveryDurationField.setVisible(true);
            messages.add(deliveryDurationField);
        }
        
    }

    // to change
    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
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
    
    public JButton getAddNewDeliveryButton() {
        return addNewDeliveryButton;
    }
    
    public JButton getUndoButton() {
        return undoButton;
    }
    
    public JButton getRedoButton() {
        return redoButton;
    }
    
    public JTextField getCourierNumberField() {
        return courierNumberField;
    }
    
    public JPanel getMessages() {
        return messages;
    }

    public int getCourierNumber() {
        String numberString = this.courierNumberField.getText();
        return Integer.parseInt(numberString);
    }
    
    public int getDeliveryDuration() {
        String numberString = this.deliveryDurationField.getText();
        return Integer.parseInt(numberString)*60;
    }

}
