package ds.sudoku.communication;

/**
 * Specifies the methods presented by a sudoku participant.
 *
 * <p>Each sudoku participant has an internal representation of a 
 * sudoku field which is either set locally or updated remotely.
 * No matter which approach is taken, this interface provides the
 * basic specification of such a participant.
 */
public interface SudokuParticipant {

        /**
         * Set the field at {@code row} and {@code column} to {@code value}.
         *
         * @param row the row in which the value should be put.
         * @param column the column in which the value should be put.
         * @param value the value which the field should be set to (normally 0...9).
         */ 	
	public void setField(int row, int column, int value);

        /**
         * Set the field at {@code index} to {@code value}.
         *
         * <p>From a given row and column, where the row and column are both
         * zero based, the index is formed as {@code index = row * FIELDS_PER_ROW + column}.
         *
         * <p>From a given row and column, where the row and column are both
         * zero based, the row and column are formed as {@code row = index / FIELDS_PER_ROW}
         * and {@code column = index % FIELDS_PER_ROW}, where '/' is the integer divison and
         * '%' is the modulus.
         *
         * @param index the index in which the value should be put.
         * @param value the value which the field should be set to (normally 0..9).
         */
	public void setField(int index, int value);
}
