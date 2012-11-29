package ds.sudoku.server;

import ds.sudoku.communication.ConnectionManager;


public class ServerFrontend {
	
	public static final UserManagement userManagement = new UserManagement();
	public static final GamesManager gamesManager = new GamesManager();
	
	public static void main(String[] args) {
		ConnectionManager connectionManager = null;
		
		//connectionManager = new ConnectionManagerImpl();
		
		connectionManager.setConnectionHandler(new DefaultConnectionHandler());
		connectionManager.acceptConnections();
	}

}
