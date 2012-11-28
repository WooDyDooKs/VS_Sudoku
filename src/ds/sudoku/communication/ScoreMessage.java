package ds.sudoku.communication;

public class ScoreMessage extends Message {

	@Override
	public String getMessageType() {
		return "ScoreMessage";
	}
}