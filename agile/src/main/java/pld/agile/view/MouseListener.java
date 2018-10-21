package pld.agile.view;

import controller.Controller;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author olivi
 */
public class MouseListener extends MouseAdapter{
    
    private Controller controller;
    
    public MouseListener(Controller c) {
        this.controller = c;
    }
    
    public void mouseClick(MouseEvent e) {
        //when click on an item on delivery list /  click on map
    }
}
