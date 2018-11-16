/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.LinkedList;

/**
 * Represents the collection of the commands successively executed that can be
 * undone or redone.
 */
public class CommandsList {

    private LinkedList<Command> list;
    private int i;

    public CommandsList() {
        list = new LinkedList<>();
        i = -1;
    }

    /**
     * Adds a command to the commands list.
     * @param c 
     */
    public void addCommand(Command c) {
        for (int j = 0; j < list.size() - i - 1; j++) {
            list.removeLast();
        }
        list.add(c);
        i++;
        c.execute();
    }

    /**
     * Undoes the last executed command from the commands list.
     */
    public void undo() {
        if (i >= 0) {
            list.get(i--).cancel();
        }
    }

    /**
     * Re-does the last undone command from the commands list.
     */
    public void redo() {
        if (i < list.size() - 1) {
            list.get(++i).execute();
        }
    }

    /**
     * Returns a boolean that says whether any command is currently undo-able
     * @return 
     */
    public boolean canUndo() {
        return (i >= 0);
    }

    /**
     * Returns a boolean that says whether any command is currently redo-able
     * @return 
     */
    public boolean canRedo() {
        return (i < list.size() - 1);
    }

}
