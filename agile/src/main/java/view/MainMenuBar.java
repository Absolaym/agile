package view;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import controller.Controller;

@SuppressWarnings("serial")
public class MainMenuBar extends JMenuBar {
	JMenu menu;
	
	MainMenuBar(Window window, Controller controller) {
		menu = new JMenu("Load");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("You can load maps and deliveries from this menu");
		
		this.add( menu );
		window.getContentPane().add(this);
	}
}
