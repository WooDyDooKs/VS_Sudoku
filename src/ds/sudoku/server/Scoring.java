package ds.sudoku.server;

import java.util.HashMap;
import java.util.Map;

public class Scoring {
	private Game game;
	private Map<String, Integer> scoreTable = new HashMap<String, Integer>();
	
	public Scoring(Game game){
		this.game = game;
		for (User us: game.getInitialPlayers()){
			scoreTable.put(us.getUsername(), 0);
		}
	}
	
	public String updateScore(GameMove gameMove){
		String newUser = gameMove.getExecutingPlayer().getUsername();
		int row = gameMove.getRow();
		int column = gameMove.getColumn();
		int newValue = gameMove.getValue();
		int oldValue = game.getField(row, column);
		int solutionValue = game.getSolution().getField(row, column);
		if ( oldValue != newValue ){
			if ( newValue == solutionValue ){
				
				int scoreNewUser = scoreTable.get(newUser);
				scoreNewUser += 1;
				scoreTable.put(newUser, scoreNewUser);
				
				if ( oldValue != 0 ){
					scoreNewUser += 1;
					scoreTable.put(newUser, scoreNewUser);
					String oldUser = game.getUser(row, column);
					int scoreOldUser = scoreTable.get(oldUser);
					scoreOldUser -= 3;
					scoreTable.put(oldUser, scoreOldUser);
				}
			} else {
				if ( oldValue != solutionValue && oldValue != 0 ){
					String oldUser = game.getUser(row, column);
					int scoreOldUser = scoreTable.get(oldUser);
					scoreOldUser -= 3;
					scoreTable.put(oldUser, scoreOldUser);
				}
			}
		} 
		
		// find highest score
		Map.Entry<String, Integer> maxEntry = null;
		for (Map.Entry<String, Integer> entry : scoreTable.entrySet()){
			if( maxEntry == null || entry.getValue() > maxEntry.getValue()){
				maxEntry = entry;
			}
		}
		
		// return player with that highest score
		return maxEntry.getKey();
	}

}
