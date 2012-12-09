package ds.sudoku.server;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ServerLog {
	
	private static PrintWriter out = new PrintWriter(System.out);
	
	public synchronized static void setLogFile(String fileName) {
		PrintWriter newOut;
		try {
			newOut = new PrintWriter(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		out = newOut;
	}
	
	public synchronized static void l(String message) {
		out.println(message);
		out.flush();
	}
		
	public synchronized static void l(String format, Object... args) {
		out.format(format + "\n", args);
		out.flush();
	}

}
