package ds.sudoku.communication;

public class SetFieldMessage extends Message {

	@Override
	public String getMessageType() {
		return "SetFieldMessage";
	}
}