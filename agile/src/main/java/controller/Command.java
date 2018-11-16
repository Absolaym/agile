package controller;

/**
 * The command interface represents objects that have a do and cancel action.
 * @author pagilles
 */
public interface Command {
    /**
     * Executes the command.
     * The code for the execution of the command should be placed in this method
     */
    void execute();
    
    /**
     * Undoes all the effects of the command.
     * The code for the cancellation of the command should be placed in this 
     * method
     */
    void cancel();
}
