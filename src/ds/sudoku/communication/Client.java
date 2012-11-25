package ds.sudoku.communication;

public interface Client {
	public void deregister();
	public void invite(String otherPlayer);
	public void playerLeft(String otherPlayer);
	public void setField(int x, int y, int value),
	public void setField(int index, int value);
	public void sendError(String message);
	public void ACK();
	public void NACK();
	public void gameOver(boolean won);
	public void gameOver(String winner);
	public void score(boolean winning);
	
	public void setMessageHandler(ClientMessageHandler handler);
	
	//	Note: Not yet fully discussed. Formal parameters may vary.
	//public void sudoku(Serializable sudoku);
}