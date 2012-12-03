package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

import ds.sudoku.exceptions.SudokuError;

/**
 * This message is sent whenever a participant encounters
 * an error or an exception.
 * 
 * @author dalhai
 *
 */
public class ErrorMessage extends Message {
	
	private final SudokuError error;
	private final String message;
	
	/**
	 * Create a new {@link ErrorMessage} with the given error constant as reason.
	 * @param error The error associated with this error message.
	 */
	public ErrorMessage(SudokuError error) {
		this(error, "");
	}
	
	/**
	 * Create a new {@link ErrorMessage} with the given error constant as reason
	 * and the given message.
	 * @param error The error associated with this error message.
	 * @param message The error message associated with this message.
	 */
	public ErrorMessage(SudokuError error, String message) {
		this.error = error;
		this.message = message;
	}
	
	/**
	 * Create a new {@link ErrorMessage} with the given error constant as reason
	 * and the given message. Additionally allows you to store custom values and
	 * custom properties. 
	 * @param error The error associated with this error message.
	 * @param message The error message associated with this message.
	 * @param customValues The custom values stored in this message.
	 * @param customProperties The custom properties stored in this message.
	 */
	public ErrorMessage(SudokuError error, String message,
			List<String> customValues, Map<String, String> customProperties) {
		super(customValues, customProperties);
		this.error = error;
		this.message = message;
	}
	
	/**
	 * Get the message string sent with this message.
	 * @return The message sent with this message.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Get the error sent with the message.
	 * @return The error sent with this message.
	 */
	public SudokuError getError() {
		return error;
	}
}