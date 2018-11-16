package controller;

/**
 * The command interface represents objects that have a do and cancel action.
 * @author pagilles
 */
public interface Command {
    /**
     * The code for execution of the command should be placed in this method
     */
    void execute();
    /**
     * The code for the cancellation of the command should be placed in this method
     */
    void cancel();
}
