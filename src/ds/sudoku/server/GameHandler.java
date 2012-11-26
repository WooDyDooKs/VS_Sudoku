package ds.sudoku.server;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class GameHandler implements Runnable {
	
	private Thread actorThread;
	private String gameID;
	private SudokuSolution solution;
	private List<Player> players;
	
	private BlockingQueue<GameMove> gameMoveQueue = new LinkedBlockingQueue<GameMove>();
	private DBCollection cells;
	
	public GameHandler(SudokuSolution solution, List<Player> players) {
		actorThread = new Thread(this);

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
		gameID = game.get("_id").toString();
		
		// add cells for game
		cells = db.getCollection(DBHelper.CELLS);
		for(int row=1; row<10; row++) {
			for(int col=1; col<10; col++) {
				BasicDBObject cell = createCellDBObject(row, col);
				
				cell.put("clue", solution.isClue(row, col));
				if(solution.isClue(row, col)) {
					cell.put("value", solution.getField(row, col));
				}
				cells.insert(cell);
			}
		}
		
		// start actor		
		actorThread.start();
	}
	
	public void setField(Player player, int row, int column, int value) {
		try {
			gameMoveQueue.put(new GameMove(player, row, column, value));
		} catch (InterruptedException e) {
			Exception error = new Exception("more than Integer.MAX_VALUE elements in queue");
			error.initCause(e);
			error.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(!actorThread.isInterrupted()) {
			GameMove nextMove;
			try {
				nextMove = gameMoveQueue.take();
			} catch (InterruptedException e) {
				break;
			}
		
			updateCell(nextMove);
			
			// TODO handle game move (send to players etc) 
		}
	}
	

	private BasicDBObject createCellDBObject(int row, int col) {
		BasicDBObject cell = new BasicDBObject();
		cell.put("game_id", gameID);
		
		cell.put("row", row);
		cell.put("column", col);
		return cell;
	}
	
	private void updateCell(GameMove gameMove) {
		BasicDBObject searchedCell = createCellDBObject(gameMove.getRow(), gameMove.getColumn());
		searchedCell.put("clue", false);
		
		BasicDBObject updateValues = new BasicDBObject();
		updateValues.put("value", gameMove.getValue());
		updateValues.put("player", gameMove.getExecutingPlayer().getUsername());
				
		cells.update(searchedCell, new BasicDBObject().append("$set", updateValues));
	}

}
