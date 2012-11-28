package ds.sudoku.exceptions.communication;

public class NoSuchPropertyException extends RuntimeException {

	/**
	 * Unique identifier used for serialization. 
	 */
	private static final long serialVersionUID = -1453572711655311127L;

	/**
	 * Creates a new exception
	 */
	public NoSuchPropertyException() {
		super();
	}
	
	/**
	 * Creates an ew exception with the given detailed message.
	 * @param detailedMessage 	The message to be delivered
	 * 							with this exception.
	 */
	public NoSuchPropertyException(String detailedMessage) {
		super(detailedMessage);
	}

}
