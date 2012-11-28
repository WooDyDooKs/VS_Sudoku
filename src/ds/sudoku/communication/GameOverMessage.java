package ds.sudoku.communication;

public class GameOverMessage extends Message {

	@Override
	public String getMessageType() {
		return "GameOverMessage";
	}
}