package ds.sudoku.server;

import ds.sudoku.communication.Client;

public class Player {
	
	private String username;
	private Client client;
	
	public Player(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	public Client getClient() {
		return client;
	}
	
}