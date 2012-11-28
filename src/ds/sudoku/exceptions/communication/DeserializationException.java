package ds.sudoku.exceptions.communication;

public class DeserializationException extends RuntimeException {

	/**
	 * Unique identifier used for serialization.
	 */
	private static final long serialVersionUID = -1094039862975410026L;
	
	/**
	 * Create a new {@link DeserializationException}.
	 */
	public DeserializationException() {
		super();
	}
	
	/**
	 * Create a new {@link DeserializationException} with the
	 * given detailed exception message.
	 * @param detailedMessage The detailed message.
	 */
	public DeserializationException(String detailedMessage) {
		super(detailedMessage);
	}

}
