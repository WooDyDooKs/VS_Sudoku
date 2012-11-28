package ds.sudoku.communication;

public class ErrorMessage extends Message {

	@Override
	public String getMessageType() {
		return "ErrorMessage";
	}
}