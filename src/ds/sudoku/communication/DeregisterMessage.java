package ds.sudoku.communication;

public class DeregisterMessage extends Message {

	@Override
	public String getMessageType() {
		return "DeregisterMessage";
	}
}