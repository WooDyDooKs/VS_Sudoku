package ds.sudoku.server;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GamesManager {
	
	public Queue<User> idleUsers = new LinkedList<User>(); // TODO private
	
	public synchronized void addIdleUser(User user) {
		idleUsers.add(user);
	}
	
	public synchronized void startNewGame(User p1, User p2) {
		SudokuSolution solution = SudokuSolution.getRandomSolution();
		List<User> players = Arrays.asList(p1, p2);
		Game game = new Game(solution, players);
		
		p1.setGame(game);
		p2.setGame(game);
		
		// TODO: this is slow and cumbersome
		removeIdleUser(p1);
		removeIdleUser(p2);
		
		GameHandler hander = new GameHandler(game);
		hander.startGame();
	}
	
	public synchronized void startNewGameWithRandom(User p1) {
		// TODO: what if no idle users?
		removeIdleUser(p1);
		User p2 = idleUsers.peek();
		
		startNewGame(p1, p2);
	}

	public void removeIdleUser(User user) {
		idleUsers.remove(user);
	}

}
