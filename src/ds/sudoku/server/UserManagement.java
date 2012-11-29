package ds.sudoku.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import ds.sudoku.communication.Client;
import ds.sudoku.exceptions.server.AlreadyExistingUsername;
import ds.sudoku.exceptions.server.NonExistingUsername;

public class UserManagement {
		
	private Map<String, User> connectedUsers = new HashMap<String, User>();
	
	private DBCollection dbUsers;
	
	public UserManagement() {
		this.dbUsers = DBHelper.getDB().getCollection(DBHelper.USERS);
	}
	
	/**
	 * Registers a new user and returns their token
	 * @param username
	 * @return user token
	 * @throws AlreadyExistingUsername 
	 */
	public synchronized User register(String username, Client client) throws AlreadyExistingUsername {
		BasicDBObject dbUser = new BasicDBObject();
		
		dbUser.put("username", username);
		
		if(dbUsers.findOne(dbUser) != null) {
			throw new AlreadyExistingUsername();
		}
		
		dbUsers.insert(dbUser);
		
		String id = dbUser.getString("_id");
		User user = new User(username, id, client);
		
		connectedUsers.put(username, user);
		
		return user;
	}
	
	
	public synchronized void deregister(User user) throws NonExistingUsername {		
		BasicDBObject dbUser = new BasicDBObject();
		dbUser.put("username", user.getUsername());
		
		if(dbUsers.findOne(dbUser) == null) {
			throw new NonExistingUsername();
		}
		
		connectedUsers.remove(user.getUsername());
		dbUsers.remove(dbUser);
	}

	public synchronized User getUser(String username) {
		return connectedUsers.get(username);
	}

	
	
}
