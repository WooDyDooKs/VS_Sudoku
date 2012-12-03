package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

import ds.sudoku.logic.SudokuTemplate;

/**
 * This message is sent whenever a new game should be
 * started by a client. It contains information about
 * the new game being started.
 * 
 * @author dalhai
 *
 */
public class NewGameMessage extends Message {
	
	private final SudokuTemplate sudoku;
	
	/**
	 * Create a new {@link NewGameMessage} with the given sudoku field
	 * attached to it.
	 * @param sudoku The sudoku field to be sent with this message.
	 */
	public NewGameMessage(SudokuTemplate sudoku) {
		this.sudoku = sudoku;
	}
	
	/**
	 * Create a new {@link NewGameMessage} with the given sudoku field
	 * attached to it. Additionaly, custom values and properties can be
	 * stored in this message.
	 * @param sudoku The sudoku field to be sent with this message.
	 * @param customValues The custom values to be stored in this message.
	 * @param customProperties The custom properties to be stored in this message.
	 */
	public NewGameMessage(SudokuTemplate sudoku,
			List<String> customValues, Map<String, String> customProperties) {
		super(customValues, customProperties);
		this.sudoku = sudoku;		
	}
	
	/**
	 * Get the sudoku field sent with this message.
	 * @return The soduko field sent with this message.
	 */
	public SudokuTemplate getSudokuField() {
		return sudoku;
	}
}
