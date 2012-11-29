package ds.sudoku.server;

import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class Game {
	public String gameID;
	public SudokuSolution solution;
	public List<User> players;
	public DBCollection cells;
	private GameHandler handler;

	public Game(SudokuSolution solution, List<User> players) {
		this.solution = solution;
		this.players = players;
		
		// add new game to games collection
		DB db = DBHelper.getDB();
		DBCollection games = db.getCollection(DBHelper.GAMES);
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
		BasicDBObject searchedCell = createCellDBObject(gameMove.getRow(), gameMove.getColumn());
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
}