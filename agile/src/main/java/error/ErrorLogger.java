package error;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import utils.Time;

/**
 * Singleton class used to log errors from anywhere in the code
 * 
 * @author johnny
 *
 */
public class ErrorLogger {

    // Singleton part
    private static ErrorLogger INSTANCE;

    public static ErrorLogger getInstance() {
        if (ErrorLogger.INSTANCE == null) {
            ErrorLogger.INSTANCE = new ErrorLogger();
        }
        return ErrorLogger.INSTANCE;
    }

    // Class part
    private LinkedBlockingQueue<ProjectError> errors;
    private LinkedList<ErrorObserver> observers;

    private ErrorLogger() {
        this.errors = new LinkedBlockingQueue<ProjectError>(30);
        this.observers = new LinkedList<ErrorObserver>();
    }

    /**
     * Logs an error and notifies it to every registered observer
     *
     * @param error The error to log
     * @param verbose Whether it's written in the error stream or not
     */
    public void log(ProjectError error, boolean verbose) {
        if (verbose) {
            errors.add(error);
        }
        System.err.println(new Time((int) Math.floor(System.currentTimeMillis() / 1000)).toString() + " || " + error);
        for (ErrorObserver obs : observers) {
            obs.update(error);
        }
    }

    /**
     * Logs an error and notifies it to every registered observer
     *
     * @param error The error to log
     */
    public void log(ProjectError error) {
        log(error, true);
    }

    /**
     * Registers an observer to the logger that'll be notified for every error
     * raised
     *
     * @param obs
     */
    public void addObserver(ErrorObserver obs) {
        this.observers.add(obs);
    }

    /**
     * Unregister an observer and removes it from the list of notified observers
     *
     * @param obs
     */
    public void removeObserver(ErrorObserver obs) {
        this.observers.remove(obs);
    }

    /**
     * Retrieve the oldest error
     *
     * @return
     */
    public ProjectError pollError() {
        return this.errors.poll();
    }

}
