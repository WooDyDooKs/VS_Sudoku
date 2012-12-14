package ds.sudoku.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import ds.sudoku.server.SudokuSolution.Difficulty;

public class GamesManager {
	
	//private Queue<User> idleUsers = new LinkedList<User>();
	private Map<Difficulty, Queue<User>> randomRequests = new HashMap<Difficulty, Queue<User>>();
	
	public GamesManager() {
		for(Difficulty diff : Difficulty.values()) {
			randomRequests.put(diff, new LinkedList<User>());
		}
	}
	
	public synchronized void startNewGame(User p1, User p2, String difficulty) {
		SudokuSolution solution = SudokuSolution.getRandomSolution(Difficulty.fromString(difficulty));	
		List<User> players = Arrays.asList(p1, p2);
		Game game = new Game(solution, players);
				
		p1.setGame(game);
		p2.setGame(game);
		
		GameHandler hander = new GameHandler(game);
		hander.startGame();
	}
	
	/** returns other random user if there is one, or null otherwise*/
	public synchronized User matchWithOtherRandomUser(User p1, String difficulty) {
		Difficulty requestedDiff = Difficulty.fromString(difficulty);
		Queue<User> waitingQueue = randomRequests.get(requestedDiff);
		
		Queue<User> currentQueue = waitingQueue;
		for(Difficulty diff : Difficulty.values()) {
			currentQueue = randomRequests.get(diff);
			if(!currentQueue.isEmpty()) break;
		}
		
		if(!currentQueue.isEmpty()) {
			User other = currentQueue.poll();
			if(other.getUsername().equals(p1.getUsername())) {
				waitingQueue.add(p1);
				return null;
			}
			return other;
		} else {
			waitingQueue.add(p1);
			return null;
		}
	}
	
	public synchronized void removeRandomRequest(User user) {
		for(Difficulty diff : Difficulty.values()) {
			randomRequests.get(diff).remove(user);
		}
	}


}
