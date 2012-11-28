package ds.sudoku.server;

import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import ds.sudoku.exceptions.server.AlreadyExistingUsername;
import ds.sudoku.server.user.UserManagement;

public class ServerFrontend {
	
	

	public static void main(String[] args) {
		
		SudokuSolution s = SudokuSolution.getRandomSolution();
		Player p1 = new Player("nicu");
		Player p2 = new Player("wicki");
		List<Player> l = Arrays.asList(p1, p2);
		GameHandler gh = new GameHandler(s, l);
		
		gh.setField(p1, 3, 3, 4); 
		
		/*UserManagement um = UserManagement.getInstance();
		
		try {
			um.register("wicki");

		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}

}
