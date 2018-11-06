package error;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class ErrorLogger {
	
	// Singleton part
	private static ErrorLogger INSTANCE;
	public static ErrorLogger getInstance() {
		if(ErrorLogger.INSTANCE == null)	ErrorLogger.INSTANCE = new ErrorLogger();
		return ErrorLogger.INSTANCE;
	}
	
	// Class part
	private LinkedBlockingQueue<PlacoError> errors;
	private LinkedList<ErrorObserver> observers;
	
	private ErrorLogger() {
		this.errors = new LinkedBlockingQueue<PlacoError>(30);
		this.observers = new LinkedList<ErrorObserver>();
	}
	
	/**
	 * Logs an error and notifies it to every registered observer
	 * @param error
	 * @param verbose Wether it's written in the error stream or not
	 */
	public void log(PlacoError error, boolean verbose) {
		if(verbose) errors.add(error);
		System.err.println(error);
		for(ErrorObserver obs : observers) {
			obs.update( error );
		}
	}
	
	public void log(PlacoError error) {
		log(error, true);
	}
	
	/**
	 * Registers an observer to the logger that'll be notified for every error raised
	 * @param obs
	 */
	public void addObserver(ErrorObserver obs) {
		this.observers.add( obs );
	}
	
	/**
	 * Unregister an observer and removes it from the list of notified observers
	 * @param obs
	 */
	public void removeObserver(ErrorObserver obs) {
		this.observers.remove( obs );
	}
	
	/**
	 * Retrieve the oldest error
	 * @return
	 */
	public PlacoError pollError() {
		return this.errors.poll();
	}

}
