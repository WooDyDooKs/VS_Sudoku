package ds.sudoku.server;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GamesManager {
	
	private Queue<User> idleUsers = new LinkedList<User>();
	
	public synchronized void startNewGame(User p1, User p2) {
		SudokuSolution solution = SudokuSolution.getRandomSolution();
		List<User> players = Arrays.asList(p1, p2);
		Game game = new Game(solution, players);
		
		p1.setGame(game);
		p2.setGame(game);
		
		GameHandler hander = new GameHandler(game);
		hander.startGame();
	}
	
	/** returns other random user if there is one, or null otherwise*/
	public synchronized User matchWithOtherRandomUser(User p1) {
		if(!idleUsers.isEmpty()) {
			return idleUsers.poll();
		} else {
			idleUsers.add(p1);
			return null;
		}
	}


}
