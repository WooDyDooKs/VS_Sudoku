package ds.sudoku.communication;

import java.util.List;
import java.util.Map;

public class NamedSetFieldMessage extends SetFieldMessage {

    private final String sender;

    /**
     * Create a new message which expresses, that the field in row {@code row}
     * and column {@code column} should be set to {@code value}.
     * 
     * @param sender
     *            Indicates the origin of this message.
     * @param row
     *            The row of the target field.
     * @param column
     *            The column of the target field.
     * @param value
     *            The value to assign to the target field.
     */
    public NamedSetFieldMessage(String sender, int row, int column, int value) {
        this(sender, row, column, value, DEFAULT_ZERO_BASED);
    }

    /**
     * Create a new message which expresses, that the field in row {@code row}
     * and column {@code column} should be set to {@code value}. Additionally,
     * if {@code zeroBased} is true, the sudoku field is assumed to be zero
     * based.
     * 
     * @param sender
     *            Indicates the origin of this message.
     * @param row
     *            The row of the target field.
     * @param column
     *            The column of the target field.
     * @param value
     *            The value to assign to the target field.
     * @param zeroBased
     *            If true, the sudoku field is assumed to be zero based.
     */
    public NamedSetFieldMessage(String sender, int row, int column, int value,
            boolean zeroBased) {
        this(sender, row, column, value, zeroBased, DEFAULT_SUDOKU_WIDTH,
                DEFAULT_SUDOKU_HEIGHT);
    }

    /**
     * Create a new message which expresses, that the field in row {@code row}
     * and column {@code column} should be set to {@code value}. Additionally,
     * if {@code zeroBased} is true, the sudoku field is assumed to be zero
     * based. Using this constructor, you are also allowed to set the assumed
     * width and height of the sudoku field.
     * 
     * @param sender
     *            Indicates the origin of this message.
     * @param row
     *            The row of the target field.
     * @param column
     *            The column of the target field.
     * @param value
     *            The value to assign to the target field.
     * @param zeroBased
     *            If true, the sudoku field is assumed to be zero based.
     * @param sudokuWidth
     *            The number of columns per row.
     * @param sudokuHeight
     *            The number of rows per column.
     */
    public NamedSetFieldMessage(String sender, int row, int column, int value,
            boolean zeroBased, int sudokuWidth, int sudokuHeight) {
        super(row, column, value, zeroBased, sudokuWidth, sudokuHeight);
        this.sender = sender;
    }

    /**
     * Create a new message which expresses, that the field described by the
     * given {@code index} should be set to the given {@code value}.
     * Additionally, if {@code zeroBased} is true, the sudoku field is assumed
     * to be zero based.
     * 
     * @param sender
     *            Indicates the origin of this message.
     * @param index
     *            The index of the target field.
     * @param value
     *            The value to assign to the target field.
     * @param zeroBased
     *            If true, the sudoku field is assumed to be zero based.
     */
    public NamedSetFieldMessage(String sender, int index, int value) {
        this(sender, index, value, DEFAULT_ZERO_BASED);
    }

    /**
     * Create a new message which expresses, that the field described by the
     * given {@code index} should be set to the given {@code value}.
     * Additionally, if {@code zeroBased} is true, the sudoku field is assumed
     * to be zero based.
     * 
     * @param sender
     *            Indicates the origin of this message.
     * @param index
     *            The index of the target field.
     * @param value
     *            The value to assign to the target field.
     * @param zeroBased
     *            If true, the sudoku field is assumed to be zero based.
     */
    public NamedSetFieldMessage(String sender, int index, int value,
            boolean zeroBased) {
        this(sender, index, value, zeroBased, DEFAULT_SUDOKU_WIDTH,
                DEFAULT_SUDOKU_HEIGHT);
    }

    /**
     * Create a new message which expresses, that the field described by the
     * given {@code index} should be set to the given {@code value}.
     * Additionally, if {@code zeroBased} is true, the sudoku field is assumed
     * to be zero based. using this constructor, you are also allowed to set the
     * assumed width and height of the sudoku field.
     * 
     * @param sender
     *            Indicates the origin of this message.
     * @param index
     *            The index of the target field.
     * @param value
     *            The value to assign to the target field.
     * @param zeroBased
     *            If true, t he sudoku field is assumed to be zero based.
     * @param sudokuWidth
     *            The number of columns per row.
     * @param sudokuHeight
     *            The number of rows per column.
     */
    public NamedSetFieldMessage(String sender, int index, int value,
            boolean zeroBased, int sudokuWidth, int sudokuHeight) {
        super(index, value, zeroBased, sudokuWidth, sudokuHeight);
        this.sender = sender;
    }

    /**
     * Create a new message which expresses, that the field described by the
     * given {@code index} should be set to the given {@code value}.
     * Additionally, if {@code zeroBased} is true, the sudoku field is assumed
     * to be zero based. using this constructor, you are also allowed to set the
     * assumed width and height of the sudoku field.
     * 
     * @param sender
     *            Indicates the origin of this message.
     * @param index
     *            The index of the target field.
     * @param value
     *            The value to assign to the target field
     * @param zeroBased
     *            If true, t he sudoku field is assumed to be zero based.
     * @param sudokuWidth
     *            The number of columns per row.
     * @param sudokuHeight
     *            The number of rows per column.
     * @param customValues
     *            The custom values stored in this message.
     * @param customProperties
     *            The custom properties stored in this message.
     */
    public NamedSetFieldMessage(String sender, int index, int value,
            boolean zeroBased, int sudokuWidth, int sudokuHeight,
            List<String> customValues, Map<String, String> customProperties) {
        super(index, value, zeroBased, sudokuWidth, sudokuHeight, customValues,
                customProperties);
        this.sender = sender;
    }

    /**
     * Get the sender of this message.
     * 
     * @return The sender.
     */
    public String getSender() {
        return this.sender;
    }
}
