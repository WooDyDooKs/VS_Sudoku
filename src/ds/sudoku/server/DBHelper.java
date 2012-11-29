package ds.sudoku.server;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class DBHelper {

	public static final String DB_NAME = "vs_sudoku";
	
	public static final String GAMES = "games";
	public static final String GAMECELLS = "games.cells";
	
	public static final String SOLUTIONS = "solutions";
	public static final String SOLUTIONCELLS = "solutions.cells";
	
	public static final String USERS = "users";


	private static DB db;
	
	public static DBObject getRandomDocument(BasicDBObject query, DBCollection coll) {
		double random = Math.random();
		
		query.put("random", new BasicDBObject().append("$gte", random));		
		DBObject user = coll.findOne(query);
		if(user == null) {
			query.put("random", new BasicDBObject().append("$lte", random));
			user = coll.findOne(query);
		}
		
		return user;
	}
		
	public static synchronized DB getDB() {
		if(db == null) {
			Mongo mongo;
			try {
				mongo = new Mongo("localhost");
				db = mongo.getDB(DB_NAME);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		
		return db;
	}
	
}
