package ds.sudoku.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import ds.sudoku.communication.Client;
import ds.sudoku.exceptions.SudokuError;
import ds.sudoku.logic.SudokuTemplate;
import static ds.sudoku.server.ServerFrontend.userManagement;

public class GameHandler implements Runnable {
	
	private Thread actorThread;
	private BlockingQueue<GameMove> gameMoveQueue = new LinkedBlockingQueue<GameMove>();
	private Game game;
	private Scoring score;

	public GameHandler(Game game) {
		actorThread = new Thread(this);

		this.game = game;
		score = new Scoring(game);
		game.setHandler(this);
	}
	
	public void startGame() {
		actorThread.start();
		
		SudokuTemplate template = game.getSolution().createTemplate();
		
		// notify all players that the game started
		for(User p : game.getActivePlayers()) {
			p.getClient().newGame(template);
		}
	}
	
//	public void stopGame() {
//		try {
//			actorThread.interrupt();
//			actorThread.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		game.destroy();
//	}
	
	public synchronized void playerLeft(User user) {
		game.removeActivePlayer(user);
		
		for(User player : game.getActivePlayers()) {
			player.getClient().playerLeft(user.getUsername());
		}
		
		if(game.getActivePlayers().isEmpty()) {
			try {
				actorThread.interrupt();
				actorThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			game.destroy();
		}
	}
	
	public void setField(User player, int row, int column, int value) {
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
			
			if(!isValidGameMove(nextMove)) {
				User user = nextMove.getExecutingPlayer();
				user.getClient().sendError(SudokuError.INVALID_FIELD_SET, "Invalid field set");
				continue;
			}
			
			// calculate scores			
			String leadingUsername = score.updateScore(nextMove);
			User leadingUser = userManagement.getUser(leadingUsername);
			String executingUsername = nextMove.getExecutingPlayer().getUsername();
			
			// update game
			game.updateCell(nextMove);
			
			boolean gameOver = game.isOver();
			
			for (User user : game.getActivePlayers()) {
				Client client = user.getClient();
				
				client.score(user == leadingUser);
				client.setField(
						nextMove.getRow(), 
						nextMove.getColumn(), 
						nextMove.getValue(), 
						executingUsername);
	
				if(gameOver) {
					client.gameOver(leadingUsername, score.getScoreTable());
				}
			}
			
			ServerLog.l(
					"Game Score for %s : %s", 
					game.getID(), 
					score.getScoreTable().toString());
			
			if(gameOver) {
				game.destroy();
				actorThread.interrupt();
				break;
			}
		}
	}
	
	public boolean isValidGameMove(GameMove gameMove) {
		int row = gameMove.getRow();
		int column = gameMove.getColumn();
		int value = gameMove.getValue();
		
		if(game.getSolution().isClue(row, column)) {
			return false;
		}
		
		if(!(
				row    >= 1 && row    <= 9 && 
				column >= 1 && column <= 9
		)) {
			return false;
		}
		
		if(value == 0) {
			// removal of value
			String executingUser = gameMove.getExecutingPlayer().getUsername();
			String fieldOwner = game.getUser(row, column);
			return executingUser.equals(fieldOwner);
		}
		
		if(value < 1 || value > 9) {
			return false;
		}
		
		return true;
	}


}
