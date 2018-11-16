package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import controller.Controller;
import model.DeliveryRequest;

public class MainMenuBar extends JMenuBar {

    private Controller controller;
    private Window window;

    private JMenuItem comUItem;
    private JMenuItem comRItem;

    public MainMenuBar(Window window, Controller controller) {

        // Pre computed data
        String root = System.getProperty("user.dir");
        final String assets = root + "/src/main/assets";

        this.controller = controller;
        this.window = window;

        // First menu items
        JMenu lMenu = new JMenu("Load");

        final JMenuItem lmItem = new JMenuItem("Load map");
        final JMenuItem ldItem = new JMenuItem("Load delivery");

        JMenu cMenu = new JMenu("Circuits");

        final JMenu ccItem = new JMenu("Compute circuits");
        final JMenuItem[] ccCourNumbItem = new JMenuItem[10];
        final JMenuItem ccItemHead = new JMenuItem("Courrier's count");

        JMenu comMenu = new JMenu("Edit");

        comUItem = new JMenuItem("Undo (Ctrl + Z)");
        comRItem = new JMenuItem("Redo (Ctrl + Y)");

        ccItemHead.setEnabled(false);
        ccItem.add(ccItemHead);
        for (int i = 0; i < 10; i++) {
            ccCourNumbItem[i] = new JMenuItem("" + (i + 1));
            ccItem.add(ccCourNumbItem[i]);
        }

        ldItem.setEnabled(false);
        ccItem.setEnabled(false);

        lmItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser jfc = new JFileChooser(assets + "/maps/");
                    int result = jfc.showOpenDialog(MainMenuBar.this.window);

                    if (JFileChooser.APPROVE_OPTION != result) {
                        return;
                    }

                    MainMenuBar.this.controller.loadCityMap(jfc.getSelectedFile().getAbsolutePath());
                    //MainMenuBar.this.controller.loadCityMap( assets + "/maps/grandPlan.xml");

                    Window window = MainMenuBar.this.window;
                    window.getCityMapContainerPanel().getLoadCityMapButton().setVisible(false);

                    ldItem.setEnabled(true);

                } catch (Exception exc) {
                    System.err.println(exc.toString());
                }
            }
        });

        ldItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser jfc = new JFileChooser(assets + "/deliveries/");
                    int result = jfc.showOpenDialog(MainMenuBar.this.window);

                    if (JFileChooser.APPROVE_OPTION != result) {
                        return;
                    }

                    MainMenuBar.this.controller.loadDeliveryRequest(jfc.getSelectedFile().getAbsolutePath());

                    DeliveryRequest delReq = MainMenuBar.this.controller.getModel().getDeliveryRequest();

                    Window window = MainMenuBar.this.window;
                    if (delReq != null) {
                        ccItem.setEnabled(true);
                        window.getDeliveryRequestPanel().loadDeliveryRequest(window);
                    }

                    for (int i = 0; i < 10; i++) {
                        ccCourNumbItem[i].setEnabled(i < delReq.getDeliveries().size());
                    }
                } catch (Exception exc) {
                    System.err.println(exc.toString());
                }
            }
        });

        for (int i = 0; i < 10; i++) {
            final int val = i + 1;
            ccCourNumbItem[i].addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub
                    MainMenuBar.this.controller.computeCircuits(val);
                    MainMenuBar.this.window.getCityMapMenuPanel().getAddNewDeliveryButton().setEnabled(true);
                    MainMenuBar.this.window.getCityMapContainerPanel().repaint();
                }

            });
        }

        comUItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenuBar.this.controller.undo();
                MainMenuBar.this.onUndo();
            }
        });

        comRItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenuBar.this.controller.redo();
                MainMenuBar.this.onRedo();
            }
        });

        comMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenuBar.this.onRedo();
                MainMenuBar.this.onUndo();
            }
        });

        lMenu.add(lmItem);
        lMenu.add(ldItem);

        this.add(lMenu);

        cMenu.add(ccItem);

        this.add(cMenu);

        comMenu.add(comUItem);
        comMenu.add(comRItem);

        this.add(comMenu);
    }

    public void onUndo() {
        comUItem.setEnabled(MainMenuBar.this.controller.canUndo());
        comRItem.setEnabled(MainMenuBar.this.controller.canRedo());
        System.out.println("Trying to perform an undo");
    }

    public void onRedo() {
        comUItem.setEnabled(MainMenuBar.this.controller.canUndo());
        comRItem.setEnabled(MainMenuBar.this.controller.canRedo());
        System.out.println("Trying to perform an redo");
    }

}
