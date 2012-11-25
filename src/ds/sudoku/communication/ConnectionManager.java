package ds.sudoku.communication;

public interface ConnectionManager {
	public void acceptConnections();
	public void acceptConnections(int maximumConnections);
	public void rejectConnections();
	
	public void setConnectionHandler(ConnectionHandler handler);
}
