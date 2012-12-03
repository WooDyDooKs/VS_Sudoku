package ds.sudoku.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static ds.sudoku.server.ServerFrontend.userManagement;

public class GameHandler implements Runnable {
	
	private Thread actorThread;
	private BlockingQueue<GameMove> gameMoveQueue = new LinkedBlockingQueue<GameMove>();
	private Game game;
	private Scoring score;

	public GameHandler(Game game) {
		actorThread = new Thread(this);

		this.game = game;
		game.setHandler(this);
	}
	
	public void startGame() {
		actorThread.start();
	}
	
	public void stopGame() {
		try {
			actorThread.interrupt();
			actorThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		game.destroy();
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
				// TODO send error
			}
			
			String leadingUsername = score.updateScore(nextMove);
			User leadingUser = userManagement.getUser(leadingUsername);
			
			game.updateCell(nextMove);
			
			for (User user : game.getPlayers()) {
				user.getClient().score(user == leadingUser);
				user.getClient().setField(nextMove.getRow(), nextMove.getColumn(), nextMove.getValue(), user.getUsername());
			}
			
			
			// TODO handle game move (send to players etc) 
		}
	}
	
	public boolean isValidGameMove(GameMove gameMove) {
		int row = gameMove.getRow();
		int column = gameMove.getColumn();
		int value = gameMove.getValue();
		
		if(!(
				row    >= 1 && row    <= 9 && 
				column >= 1 && column <= 9 &&
				value  >= 1 && value  <= 9
		)) {
			return false;
		}
		
		return game.getSolution().isClue(row, column);
	}


}
