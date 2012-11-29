package ds.sudoku.server;

public class GameMove {

	private User executingPlayer;
	private int row, column, value;
	
	GameMove(User executingPlayer, int row, int column, int value) {
		this.executingPlayer = executingPlayer;
		this.row = row;
		this.column = column;
		this.value = value;
	}
	
	public User getExecutingPlayer() {
		return executingPlayer;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	public int getValue() {
		return value;
	}

	
}
