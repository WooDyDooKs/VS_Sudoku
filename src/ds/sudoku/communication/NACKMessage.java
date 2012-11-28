package ds.sudoku.communication;

public class NACKMessage extends Message {

	@Override
	public String getMessageType() {
		return "NACKMessage";
	}
}