package ds.sudoku.server;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class DBHelper {

	public static final String DB_NAME = "vs_sudoku";
	public static final String GAMES = "games";
	public static final String SOLUTIONS = "solutions";
	public static final String CELLS = "cells";
	public static final String USERS = "users";


	private static DB db;
		
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
