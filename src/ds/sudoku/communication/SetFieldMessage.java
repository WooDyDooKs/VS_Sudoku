package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

/**
 * This message is sent to tell the receiver that the described
 * field should be set to the value passed within the message.
 * @author dalhai
 *
 */
public class SetFieldMessage extends Message {

	/**
	 * The assumed width of a sudoku field when not provided
	 * other values.
	 */
	public static final int DEFAULT_SUDOKU_WIDTH = 9;
	
	/**
	 * The assumed height of a sudoku field when not provided
	 * other values.
	 */
	public static final int DEFAULT_SUDOKU_HEIGHT = 9;
	
	/**
	 * Per default, the sudoku field is assumed to be zero based.
	 */
	public static final boolean DEFAULT_ZERO_BASED = false;
	
	private final boolean zeroBased;	
	private final int sudokuWidth;
	private final int sudokuHeight;
	private final int index;
	private final int value;
	private final String sender;
	
	/**
	 * Create a new message which expresses, that the field in row {@code row} 
	 * and column {@code column} should be set to {@code value}. 
	 * @param row The row of the target field.
	 * @param column The column of the target field.
	 * @param value The value to assign to the target field.
	 * @param sender Indicates the origin of this message.
	 */
	public SetFieldMessage(int row, int column, int value, String sender) {
		this(row, column, value, sender, DEFAULT_ZERO_BASED);
	}
	
	/**
	 * Create a new message which expresses, that the field in row {@code row}
	 * and column {@code column} should be set to {@code value}. Additionally,
	 * if {@code zeroBased} is true, the sudoku field is assumed to be zero based.
	 * @param row The row of the target field.
	 * @param column The column of the target field.
	 * @param value The value to assign to the target field.
     * @param sender Indicates the origin of this message.
	 * @param zeroBased If true, the sudoku field is assumed to be zero based.
	 */
	public SetFieldMessage(int row, int column, int value, String sender, boolean zeroBased) {
		this(row, column, value, sender, zeroBased, DEFAULT_SUDOKU_WIDTH, DEFAULT_SUDOKU_HEIGHT);
	}
	
	/**
	 * Create a new message which expresses, that the field in row {@code row}
	 * and column {@code column} should be set to {@code value}. Additionally,
	 * if {@code zeroBased} is true, the sudoku field is assumed to be zero based.
	 * Using this constructor, you are also allowed to set the assumed width and
	 * height of the sudoku field.
	 * @param row The row of the target field.
	 * @param column The column of the target field.
	 * @param value The value to assign to the target field.
     * @param sender Indicates the origin of this message.
	 * @param zeroBased If true, the sudoku field is assumed to be zero based.
	 * @param sudokuWidth The number of columns per row.
	 * @param sudokuHeight The number of rows per column.
	 */
	public SetFieldMessage(int row, int column, int value, String sender, boolean zeroBased, 
			int sudokuWidth, int sudokuHeight) {
		this(
		        zeroBased ? 
		                row * sudokuWidth + column :
		                (row - 1) * sudokuWidth + column,
		        value,
		        sender,
		        zeroBased, 
		        sudokuWidth, 
		        sudokuHeight
		    );
	}
	
	/**
	 * Create a new message which expresses, that the field described by the given
	 * {@code index} should be set to the given {@code value}.
	 * @param index The index of the target field.
	 * @param value The value to assign to the target field.
     * @param sender Indicates the origin of this message.
	 */
	public SetFieldMessage(int index, int value, String sender) {
		this(index, value, sender, DEFAULT_ZERO_BASED);
	}
	
	/**
	 * Create a new message which expresses, that the field described by the given
	 * {@code index} should be set to the given {@code value}. Additionally, if
	 * {@code zeroBased} is true, the sudoku field is assumed to be zero based.
	 * @param index The index of the target field.
	 * @param value The value to assign to the target field.
     * @param sender Indicates the origin of this message.
	 * @param zeroBased If true, the sudoku field is assumed to be zero based.
	 */
	public SetFieldMessage(int index, int value, String sender, boolean zeroBased) {
		this(index, value, sender, zeroBased, DEFAULT_SUDOKU_WIDTH, DEFAULT_SUDOKU_HEIGHT);
	}
	
	/**
	 * Create a new message which expresses, that the field described by the given
	 * {@code index} should be set to the given {@code value}. Additionally, if
	 * {@code zeroBased} is true, the sudoku field is assumed to be zero based.
	 * Using this constructor, you are also allowed to set the assumed width and
	 * height of the sudoku field.
	 * @param index The index of the target field.
	 * @param value The value to assign to the target field.
     * @param sender Indicates the origin of this message.
	 * @param zeroBased If true, the sudoku field is assumed to be zero based.
	 * @param sudokuWidth The number of columns per row.
	 * @param sudokuHeight The number of rows per column.
	 */
	public SetFieldMessage(int index, int value, 
	        String sender, boolean zeroBased,
	        int sudokuWidth, int sudokuHeight) {
		this.zeroBased = zeroBased;
		this.sudokuWidth = sudokuWidth;
		this.sudokuHeight = sudokuHeight;
		this.index = index;
		this.value = value;
		this.sender = sender;
	}
	
	/**
	 * Create a new message which expresses, that the field described by the given
	 * {@code index} should be set to the given {@code value}. Additionally, if
	 * {@code zeroBased} is true, the sudoku field is assumed to be zero based.
	 * using this constructor, you are also allowed to set the assumed width
	 * and height of the sudoku field.
	 * @param index The index of the target field.
	 * @param value The value to assign to the target field.
     * @param sender Indicates the origin of this message.
	 * @param zeroBased If true, t he sudoku field is assumed to be zero based.
	 * @param sudokuWidth The number of columns per row.
	 * @param sudokuHeight The number of rows per column.
	 * @param customValues The custom values stored in this message.
	 * @param customProperties The custom properties stored in this message.
	 */
	public SetFieldMessage(int index, int value, 
	        String sender, boolean zeroBased,
	        int sudokuWidth, int sudokuHeight,
	        List<String> customValues, Map<String, String> customProperties) {
	    super(customValues, customProperties);
	    this.zeroBased = zeroBased;
	    this.sudokuWidth = sudokuWidth;
	    this.sudokuHeight = sudokuHeight;
	    this.index = index;
	    this.value = value;
	    this.sender = sender;
	}
	
	/**
	 * Get the index stored in this message.
	 * @return The target index of the value.
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Get the width of the sudoku field assumed in this message.
	 * @return The assumed width of the sudoku field.
	 */
	public int getSudokuWidth() {
		return sudokuWidth;
	}
	
	/**
	 * Get the height of the sudoku field assumed in this message.
	 * @return The assumed height of the sudoku field.
	 */
	public int getSudokuHeight() {
		return sudokuHeight;
	}
	
	/**
	 * Get the target row for the value stored in this message.
	 * @return The row to be set to the value.
	 */
	public int getRow() {
	    if(!zeroBased)
	        return ((index - 1) / sudokuWidth) + 1;
	    return index / sudokuWidth;
	}
	
	/**
	 * Get the target column for the value stored in this message.
	 * @return The column to be set to the value.
	 */
	public int getColumn() {
		if(!zeroBased)
		    return ((index - 1) % sudokuWidth) + 1;
		return index % sudokuWidth;
	}
	
	/**
	 * Get the value stored in this message.
	 * @return The value to be set.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Check if the message is assumed to be zero based or not.
	 * @return {@code true}, if zero based, {@code false}, else.
	 */
	public boolean isZeroBased() {
		return zeroBased;
	}
	
	/**
	 * Get the sender of this message. This string indicated where
	 * the message was originally created.
	 * @return The origin of the message as string.
	 */
	public String getSender() {
	    return sender;
	}
}