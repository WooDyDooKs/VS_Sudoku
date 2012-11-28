package ds.sudoku.communication;

public class LeaveMessage extends Message {

	@Override
	public String getMessageType() {
		return "LeaveMessage";
	}
}