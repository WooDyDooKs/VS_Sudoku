package ds.sudoku.server;

import ds.sudoku.communication.Client;
import ds.sudoku.communication.ConnectionHandler;

public class DefaultConnectionHandler implements ConnectionHandler {
	
	@Override
	public void onNewConnectionAccepted(Client newClient) {
		newClient.setMessageHandler(ServerFrontend.messageHandler);
		newClient.start();
	}

}