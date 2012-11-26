package ds.sudoku.server;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class SudokuSolution {
	
	private int solutionID;
	private DBCollection coll;
	private BasicDBObject ref;

	public SudokuSolution(int solutionID) {
		DB db = DBHelper.getDB();
		coll = db.getCollection(DBHelper.SOLUTIONS);
		
		// TODO: real solution id here
		this.solutionID = 1;
		
		ref = new BasicDBObject();
		ref.put("solution_id", solutionID);		
	}

	int getField(int row, int column) {
		ref.put("row", row);
		ref.put("column", column);
		DBObject cell = coll.findOne(ref);
		
		return ((Number) cell.get("value")).intValue();
	}
	
	boolean isClue(int row, int column) {
		ref.put("row", row);
		ref.put("column", column);
		DBObject cell = coll.findOne(ref);
		
		return (Boolean) cell.get("clue");
	}
	
	int getSolutionID() {
		return solutionID;
	}

}
