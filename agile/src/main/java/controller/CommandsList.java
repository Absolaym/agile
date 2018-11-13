/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.LinkedList;

/**
 *
 * @author pagilles
 */
public class CommandsList {
    
    private LinkedList<Command> list;
    private int i;
    
    
    public CommandsList() {
        list = new LinkedList<>();
        i = -1;
    }
    
    public void addCommand(Command c) {
        for(int j=0; j<list.size()-i-1;j++){
            list.removeLast();
        }
        list.add(c);
        i++;
        c.doCde();
    }
    
    public void undo() {
        if(i >= 0){
            list.get(i--).undoCde();
        }
    }
    
    public void redo() {
    		if(i + 1 >= list.size() ) return;
    		if(i < 0) 						return;
    		
    		list.get(++i).doCde();

    }
    
    public boolean canUndo() {
    		return this.list.size() > 0;
    }
    
    public boolean canRedo() {
    		return this.list.size() > i+1;
    }
}
