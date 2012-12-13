package ds.sudoku.server;

import ds.sudoku.communication.ClientListener;
import ds.sudoku.communication.ConnectionManager;


public class ServerFrontend {
	
	private static final int SERVER_PORT = 8888;
	
	public static final UserManagement userManagement = new UserManagement();
	public static final GamesManager gamesManager = new GamesManager();
	public static final DefaultMessageHandler messageHandler = new DefaultMessageHandler();

	
	public static void main(String[] args) throws InterruptedException {
		ServerLog.l("Starting server, listeing on port %d", SERVER_PORT);
		
		ConnectionManager connectionManager = new ClientListener(SERVER_PORT);
		connectionManager.setConnectionHandler(new DefaultConnectionHandler());
		connectionManager.acceptConnections();
		
		
		/*SudokuSolution s = SudokuSolution.getRandomSolution(SudokuSolution.Difficulty.MEDIUM);
		
		int[][] t = s.createTemplate().getTemplate();
		for(int i=0; i<9; i++) {
			System.out.println(Arrays.toString(t[i]));
		}*/
		
//		User p1 = null, p2 = null;
//		
//		try {
//			p1 = userManagement.register("a", null);
//			p2 = userManagement.register("b", null);
//		} catch (AlreadyExistingUsername e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		//System.out.println(userManagement.getUser("a"));
//
//
//		GameHandler h = p1.getGame().getHandler();
//		
//		h.setField(p2, 3, 3, 3);
//		
//		Thread.sleep(10000);
//		h.stopGame();
//		
		
	}

}
