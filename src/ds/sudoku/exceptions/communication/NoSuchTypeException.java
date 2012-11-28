package ds.sudoku.exceptions.communication;

public class NoSuchTypeException extends RuntimeException {
	/**
	 * Unique identifier used for serialization.
	 */
	private static final long serialVersionUID = 5948983174969747753L;
	
	/**
	 * Creates an empty exception.
	 */
	public NoSuchTypeException() {
		super();
	}
	
	/**
	 * Creates an exception with the given message.
	 * @param detailedMessage The message to be 
	 * delivered with this exception.
	 */
	public NoSuchTypeException(String detailedMessage) {
		super(detailedMessage);
	}
}
