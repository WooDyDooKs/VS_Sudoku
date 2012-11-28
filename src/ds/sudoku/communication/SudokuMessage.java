package ds.sudoku.communication;

public class SudokuMessage extends Message {

	@Override
	public String getMessageType() {
		return "SudokuMessage";
	}
}