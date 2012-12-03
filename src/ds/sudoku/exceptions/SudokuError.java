package ds.sudoku.exceptions;

/**
 * An enumeration used to identify errors in communication.
 * These errors do not represent exceptions as in a java exception, but are
 * used to indicate to clients and servers what went wrong.
 * 
 * @author dalhai
 *
 */
public enum SudokuError {
	UNEXPECTED_MESSAGE_RECEIVED,	
	/** Registered username is already registered */
	USERNAME_ALREADY_EXISTS,
	/** Invited user does not exist */
	USERNAME_DOES_NOT_EXIST,
	/** Tried to set invalid field */ 
	INVALID_FIELD_SET
}
