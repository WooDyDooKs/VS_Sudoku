package ds.sudoku.server;

import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class Game {
	private String gameID;
	private SudokuSolution solution;
	private List<User> players;
	
	private DBCollection cells;
	private DBCollection games;

	private GameHandler handler;

	public Game(SudokuSolution solution, List<User> players) {
		this.solution = solution;
		this.players = players;
		
		// add new game to games collection
		DB db = DBHelper.getDB();
		games = db.getCollection(DBHelper.GAMES);
		BasicDBObject game = new BasicDBObject();
		
		// add players to game
		DBObject dbPlayers = new BasicDBList();
		for(int i=0; i<players.size(); i++) {
			dbPlayers.put(String.valueOf(i), players.get(i).getUsername());
		}
		game.put("players", dbPlayers);	
		
		// add solution id to game
		game.put("solution_id", solution.getSolutionID());
		
		// insert and retrieve ID
		games.insert(game);
		this.gameID = game.get("_id").toString();
		
		// add cells for game
		this.cells = db.getCollection(DBHelper.GAMECELLS);
		for(int row=1; row<10; row++) {
			for(int col=1; col<10; col++) {
				BasicDBObject cell = createCellDBObject(row, col);
				
				cell.put("clue", solution.isClue(row, col));
				if(solution.isClue(row, col)) {
					cell.put("value", solution.getField(row, col));
				}
				this.cells.insert(cell);
			}
		}
	}
	
	private BasicDBObject createCellDBObject(int row, int col) {
		BasicDBObject cell = new BasicDBObject();
		cell.put("game_id", gameID);
		
		cell.put("row", row);
		cell.put("column", col);
		return cell;
	}
	
	public void updateCell(GameMove gameMove) {
		int row = gameMove.getRow();
		int column = gameMove.getColumn();
		
		if(solution.isClue(row, column)) {
			System.err.format("Field (%d,%d) is clue field!", row, column);
		}
		
		BasicDBObject searchedCell = createCellDBObject(row, column);
		searchedCell.put("clue", false);
		
		BasicDBObject updateValues = new BasicDBObject();
		updateValues.put("value", gameMove.getValue());
		updateValues.put("player", gameMove.getExecutingPlayer().getUsername());
				
		cells.update(searchedCell, new BasicDBObject().append("$set", updateValues));
	}

	public void setHandler(GameHandler handler) {
		this.handler = handler;
	}
	
	public GameHandler getHandler() {
		return handler;
	}

	public void destroy() {
		for(User p : players) {
			p.setGame(null);
		}
		
		// remove from cells
		BasicDBObject query = new BasicDBObject();
		query.put("game_id", gameID);
		cells.remove(query);
		
		// remove from games
		query = new BasicDBObject();
		query.put("_id", new ObjectId(gameID));
		games.remove(query);
	}
	
	public SudokuSolution getSolution() {
		return solution;
	}
}