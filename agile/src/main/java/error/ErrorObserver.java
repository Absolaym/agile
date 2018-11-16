package error;

public interface ErrorObserver {

	/**
	 * Implement here what the observer should do on error log
	 * @param error
	 */
    public void update(ProjectError error);
}
