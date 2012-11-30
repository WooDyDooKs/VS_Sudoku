package ds.sudoku.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameHandler implements Runnable {
	
	private Thread actorThread;
	private BlockingQueue<GameMove> gameMoveQueue = new LinkedBlockingQueue<GameMove>();
	private Game game;

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
		
			game.updateCell(nextMove);
			
			// TODO handle game move (send to players etc) 
		}
	}
	



}
