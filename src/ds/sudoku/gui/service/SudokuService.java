package ds.sudoku.gui.service;

import ds.sudoku.communication.DeathHandler;
import ds.sudoku.communication.InviteMessage;
import ds.sudoku.communication.Server;
import ds.sudoku.communication.ServerFactory;
import ds.sudoku.communication.ServerMessageHandler;
import ds.sudoku.logic.SudokuHandler;
import ds.sudoku.logic.SudokuTemplate;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class SudokuService extends Service implements Handler.Callback, DeathHandler<Server> {
	
	// make sure these don't clash with the SudokuHandler
	protected static final int REGISTERED_MSG		= 0xFFFF0001;
	protected static final int DEREGISTERED_MSG		= 0xFFFF0002;
	protected static final int INVITED_MSG			= 0xFFFF0003;
	protected static final int INVITE_REJECTED_MSG	= 0xFFFF0004;
	protected static final int NEW_GAME_MSG			= 0xFFFF0005;
	protected static final int ERROR_MSG			= 0xFFFF0006;

	private static Server server;
	private static SudokuHandler sudokuHandler;
	private static UserStateListener userStateListener;
	private static String username;
	
	public static final String SERVER_HOST = "sudoku.gebaschtel.ch";
	public static final int SERVER_PORT = 8888;
	
	//////////// Public Methods for GUI  ////////////
	
	/**
	 * This value is null if the service is not running.
	 * 
	 * @return Connected Server instance
	 */
	public static Server getServer() {
		return server;
	}
	
	/**
	 * This value is null if no game is running.
	 * 
	 * @return SudokuHandler for the current game
	 */
	public static SudokuHandler getSudokuHandler() {
		return sudokuHandler;
	}
	
	/**
	 * Sets a listener for the MainActivity to get informed about
	 * changes in the user state (e.g. invite requests)
	 * 	
	 * @param listener
	 */
	public static void setUserStateListener(UserStateListener listener) {
		userStateListener = listener;
	}
	
	/////////////////////////////////////////////////


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			server = ServerFactory.create(SERVER_HOST, 8888);
			ServerMessageHandler messageHandler = new SudokuServerMessageHandler(new Handler());
			server.setMessageHandler(messageHandler);
			server.setDeathHandler(this);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy() {
		if(server != null) {
			server.stop();
		}
	}

	@Override
	public boolean handleMessage(android.os.Message msg) {
		switch(msg.what) {
		case REGISTERED_MSG:
			username = (String) msg.obj;
			if(userStateListener != null) {
				userStateListener.onRegistered(username);
			}
			return true;
		case DEREGISTERED_MSG:
			username = null;
			if(userStateListener != null) {
				userStateListener.onDeregistered();
			}
			return true;
		case INVITED_MSG:
			if(userStateListener != null) {
				userStateListener.onInviteRequest((InviteMessage) msg.obj);
			}
			return true;
		case INVITE_REJECTED_MSG:
			if(userStateListener != null) {
				userStateListener.onInviteRequestRejected((InviteMessage) msg.obj);
			}
			return true;
		case NEW_GAME_MSG:
			SudokuTemplate template = (SudokuTemplate) msg.obj;
			sudokuHandler = new SudokuHandler(template, username);
			if(userStateListener != null) {
				userStateListener.onGameStarted();
			}
			return true;
		}
		
		return false;
	}

	@Override
	public void onDeath(Server instance, String message) {
		Toast.makeText(this, "Disconnected from server.", Toast.LENGTH_LONG);
		if(userStateListener != null) {
			userStateListener.onDeath(message);
		}
		this.stopSelf();
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
