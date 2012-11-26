package ds.sudoku.server.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import ds.sudoku.exceptions.server.AlreadyExistingUsername;
import ds.sudoku.exceptions.server.NonExistingUsername;
import ds.sudoku.server.DBHelper;

public class UserManagement {
	
	private static final UserManagement instance = new UserManagement();
	private DBCollection users;
	
	private UserManagement() {
		DB db = DBHelper.getDB();
		this.users = db.getCollection(DBHelper.USERS);
	}
	
	/**
	 * Registers a new user and returns their id
	 * @param username
	 * @return user-id
	 * @throws AlreadyExistingUsername 
	 */
	public String register(String username) throws AlreadyExistingUsername {
		BasicDBObject user = new BasicDBObject();
		user.put("username", username);
		
		if(users.findOne(user) != null) {
			throw new AlreadyExistingUsername();
		}
		
		users.insert(user);
		
		return user.getString("_id");
	}
	
	public void deregister(String username) throws NonExistingUsername {
		// TODO: wer darf sich hier deregistriere? Passwort/ID check?
		
		BasicDBObject user = new BasicDBObject();
		user.put("username", username);
		
		if(users.findOne(user) == null) {
			throw new NonExistingUsername();
		}
		
		users.remove(user);
	}
	
	public static UserManagement getInstance() {
		return instance;
	}
	
	
}
