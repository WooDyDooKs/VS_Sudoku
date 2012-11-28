package ds.sudoku.server;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class SudokuSolution {
	
	enum Difficulty {
		VERY_EASY("very easy"),
		EASY("easy"),
		MEDIUM("medium"),
		HARD("hard"),
		FIENDISH("fiendish");		
		
		private String difficulty;

		Difficulty(String difficulty) {
			this.difficulty = difficulty;
		}
		
		public String toString() {
			return this.difficulty;
		}
	}
	
	private int solutionID;
	private DBCollection cells;
	private BasicDBObject ref;

	public SudokuSolution(int solutionID) {
		DB db = DBHelper.getDB();
		cells = db.getCollection(DBHelper.SOLUTIONCELLS);
		
		this.solutionID = solutionID;
		
		ref = new BasicDBObject();
		ref.put("solution_id", solutionID);		
	}
	
	public static SudokuSolution getRandomSolution() {
		DB db = DBHelper.getDB();
		DBCollection solutions = db.getCollection(DBHelper.SOLUTIONS);

		double random = Math.random();
		BasicDBObject query = new BasicDBObject();
		query.put("random", new BasicDBObject().append("$gte", random));
		
		DBObject solution = solutions.findOne(query);
		if(solution == null) {
			query.put("random", new BasicDBObject().append("$lte", random));
			solution = solutions.findOne(query);
		}
		
		int solutionID =((Number) solution.get("solution_id")).intValue();		
		return new SudokuSolution(solutionID);
	}

	int getField(int row, int column) {
		ref.put("row", row);
		ref.put("column", column);
		DBObject cell = cells.findOne(ref);
		
		return ((Number) cell.get("value")).intValue();
	}
	
	boolean isClue(int row, int column) {
		ref.put("row", row);
		ref.put("column", column);
		DBObject cell = cells.findOne(ref);
		
		return (Boolean) cell.get("clue");
	}
	
	int getSolutionID() {
		return solutionID;
	}

}
