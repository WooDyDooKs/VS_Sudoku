package ds.sudoku.server;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class SudokuSolution {
	
	public enum Difficulty {
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
		
		BasicDBObject query = new BasicDBObject();
		DBObject solution = DBHelper.getRandomDocument(query, solutions);
		
		int solutionID =((Number) solution.get("_id")).intValue();		
		return new SudokuSolution(solutionID);
	}
	
	public static SudokuSolution getRandomSolution(Difficulty difficulty) {
		DB db = DBHelper.getDB();
		DBCollection solutions = db.getCollection(DBHelper.SOLUTIONS);
		
		BasicDBObject query = new BasicDBObject();
		query.put("difficulty", difficulty.toString());
		DBObject solution = DBHelper.getRandomDocument(query, solutions);
		
		int solutionID =((Number) solution.get("_id")).intValue();		
		return new SudokuSolution(solutionID);
	}
	
	public int getField(int row, int column) {
		ref.put("row", row);
		ref.put("column", column);
		DBObject cell = cells.findOne(ref);
		
		return ((Number) cell.get("value")).intValue();
	}
	
	public boolean isClue(int row, int column) {
		ref.put("row", row);
		ref.put("column", column);
		DBObject cell = cells.findOne(ref);
		
		return (Boolean) cell.get("clue");
	}
	
	public int getSolutionID() {
		return solutionID;
	}
	
	public SudokuTemplate createTemplate() {
		int[][] template = new int[9][9];
		
		for(int row=0; row<9; row++) {
			for(int col=0; col<9; col++) {
				if(isClue(row+1, col+1)) {
					template[row][col] = getField(row+1, col+1);
				}
			}
		}
		
		return new SudokuTemplate(template);
	}

}
