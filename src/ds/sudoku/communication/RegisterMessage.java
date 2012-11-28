package ds.sudoku.communication;

public class RegisterMessage extends Message {

	@Override
	public String getMessageType() {
		return "RegisterMessage";
	}
}