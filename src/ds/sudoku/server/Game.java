package ds.sudoku.server;

import java.util.LinkedList;
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
	private List<User> initialPlayers;
	private List<User> activePlayers;
	
	private DBCollection cells;
	private DBCollection games;

	private GameHandler handler;

	public Game(SudokuSolution solution, List<User> players) {
		this.solution = solution;
		this.initialPlayers = players;
		this.activePlayers = new LinkedList<User>(initialPlayers);
		
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
				} else{
					cell.put("value", 0);
				}
				this.cells.insert(cell);
			}
		}
		
		ServerLog.l(
				"Created game id: %s, solution id: %d, players: %s",
				gameID, solution.getSolutionID(), initialPlayers.toString());

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
	
	public String getID() {
		return gameID;
	}

	public void destroy() {
		ServerLog.l("Destroying game id: %s", gameID);
		
		for(User p : initialPlayers) {
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
	
	public int getField(int row, int column){
		BasicDBObject searchedField = createCellDBObject(row, column);
		return ((Number) cells.findOne(searchedField).get("value")).intValue();
	}
	
	public String getUser(int row, int column){
		BasicDBObject searchedField = createCellDBObject(row, column);
		return ( cells.findOne(searchedField).get("player").toString() );
	}
	
	public List<User> getInitialPlayers(){
		return initialPlayers;
	}
	
	public List<User> getActivePlayers() {
		return activePlayers;
	}
	
	public void removeActivePlayer(User user) {
		user.setGame(null);
		activePlayers.remove(user);
	}
	
	public boolean isOver() {		
		for(int row=1; row<=9; row++) {
			for(int col=1; col<=9; col++) {
				if(solution.getField(row, col) != getField(row, col)) {
					return false;
				}
			}
		}
		
		return true;
	}
}