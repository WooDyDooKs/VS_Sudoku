package ds.sudoku.communication;

public interface Server {
	public void register(String name);
	public void register(String name, Object id);
	public void deregister();
	public void joinName(String otherPlayer);
	public void joinRandom();
	public void leave();
	public void setField(int x, int y, int value);
	public void setField(int index, int value);
	public void sendError(String message);
	public void ACK();
	public void NACK();
	
	public void setMessageHandler(ServerMessageHandler handler);
}
