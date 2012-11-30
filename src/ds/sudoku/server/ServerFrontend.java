package ds.sudoku.server;

import java.util.Arrays;
import java.util.List;

import ds.sudoku.communication.ConnectionManager;
import ds.sudoku.exceptions.server.AlreadyExistingUsername;


public class ServerFrontend {
	
	public static final UserManagement userManagement = new UserManagement();
	public static final GamesManager gamesManager = new GamesManager();
	
	public static void main(String[] args) throws InterruptedException {
		ConnectionManager connectionManager = null;
		
		//connectionManager = new ConnectionManagerImpl();
		
		//connectionManager.setConnectionHandler(new DefaultConnectionHandler());
		//connectionManager.acceptConnections();
		
		/*SudokuSolution s = SudokuSolution.getRandomSolution(SudokuSolution.Difficulty.MEDIUM);
		
		int[][] t = s.createTemplate().getTemplate();
		for(int i=0; i<9; i++) {
			System.out.println(Arrays.toString(t[i]));
		}*/
		
		User p1 = null, p2 = null;
		
		try {
			p1 = userManagement.register("a", null);
			p2 = userManagement.register("b", null);
		} catch (AlreadyExistingUsername e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//System.out.println(userManagement.getUser("a"));
		
		System.out.println(gamesManager.idleUsers);
		
		
		gamesManager.startNewGameWithRandom(p1);
		//gamesManager.startNewGame(p1, p2);
		
		System.out.println(gamesManager.idleUsers);

		GameHandler h = p1.getGame().getHandler();
		
		h.setField(p2, 3, 3, 3);
		
		Thread.sleep(10000);
		h.stopGame();
		
		
	}

}
