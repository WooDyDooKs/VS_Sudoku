package ds.sudoku.server;

public class GameMove {

	private Player executingPlayer;
	private int row, column, value;
	
	GameMove(Player executingPlayer, int row, int column, int value) {
		this.executingPlayer = executingPlayer;
		this.row = row;
		this.column = column;
		this.value = value;
	}
	
	public Player getExecutingPlayer() {
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
