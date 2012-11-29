package ds.sudoku.exceptions.communication;

/**
 * This exception is thrown whenever an expected type does not
 * match an actual type.
 * @author dalhai
 *
 */
public class WrongTypeException extends RuntimeException {

	/**
	 * Unique identifier used for serialization.
	 */
	private static final long serialVersionUID = 2069134525344263520L;
	
	/**
	 * Creates an empty exception.
	 */
	public WrongTypeException() {
		super();
	}
	
	/**
	 * Creates an exception with the given message.
	 * @param detailedMessage 
	 * 				The message to be delivered
	 * 				with this exception.
	 */
	public WrongTypeException(String detailedMessage) {
		super(detailedMessage);
	}
}
