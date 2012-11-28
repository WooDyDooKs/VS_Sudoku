package ds.sudoku.communication;

public class JoinMessage extends Message {

	@Override
	public String getMessageType() {
		return "JoinMessage";
	}
}