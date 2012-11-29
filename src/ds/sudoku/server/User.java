package ds.sudoku.server;

import ds.sudoku.communication.Client;

public class User {
	
	private String username;
	private Client client;
	private String id;
	private Game game;
	
	public User(String id, String username, Client client) {
		this.id = id;
		this.username = username;
		this.client = client;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getUserID() {
		return id;
	}
	
	public Client getClient() {
		return client;
	}

	public void setGame(Game game) {
		this.game = game;		
	}
	
	public Game getGame() {
		return game;
	}
	
}