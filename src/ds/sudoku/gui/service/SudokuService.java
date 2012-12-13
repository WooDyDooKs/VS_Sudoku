package ds.sudoku.gui.service;

import ds.sudoku.communication.ACKMessage;
import ds.sudoku.communication.DeathHandler;
import ds.sudoku.communication.DeregisterMessage;
import ds.sudoku.communication.ErrorMessage;
import ds.sudoku.communication.GameOverMessage;
import ds.sudoku.communication.InviteMessage;
import ds.sudoku.communication.LeftMessage;
import ds.sudoku.communication.Message;
import ds.sudoku.communication.NACKMessage;
import ds.sudoku.communication.NamedSetFieldMessage;
import ds.sudoku.communication.NewGameMessage;
import ds.sudoku.communication.RegisterMessage;
import ds.sudoku.communication.ScoreMessage;
import ds.sudoku.communication.Server;
import ds.sudoku.communication.ServerFactory;
import ds.sudoku.communication.ServerMessageHandler;
import ds.sudoku.communication.SetFieldMessage;
import ds.sudoku.logic.SudokuHandler;
import ds.sudoku.logic.SudokuTemplate;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import static ds.sudoku.logic.SudokuHandler.*;


public class SudokuService extends Service implements Handler.Callback, DeathHandler<Server> {
	
	private static final int REGISTERED_MSG			= 1;
	private static final int DEREGISTERED_MSG		= 2;
	private static final int INVITED_MSG			= 3;
	private static final int INVITE_REJECTED_MSG	= 4;
	private static final int NEW_GAME_MSG			= 5;
	private static final int ERROR_MSG				= 6;

	private Server server;
	private volatile SudokuHandler sudokuHandler;
	private UserStateListener userStateListener;
	private String username;
	
	public static final String SERVER_HOST = "sudoku.gebaschtel.ch";
	public static final int SERVER_PORT = 8888;
	
	//////////// Public Methods for GUI  ////////////
	
	public class SudokuServerBinder extends Binder {
		/**
		 * This value is null if the service is not running.
		 * 
		 * @return Connected Server instance
		 */
		public Server getServer() {
			return server;
		}
		
		/**
		 * This value is null if no game is running.
		 * 
		 * @return SudokuHandler for the current game
		 */
		public SudokuHandler getSudokuHandler() {
			return sudokuHandler;
		}
		
		/**
		 * Checks if the client is registered.
		 * 
		 * @return true if the client is registered
		 */
		public boolean isRegistered() {
			return username != null;
		}
		
		/**
		 * Gets the username if the client is registered.
		 * 		
		 * @return the username
		 */
		public String getUsername() {
			return username;
		}
		
		/**
		 * Sets a listener for the MainActivity to get informed about
		 * changes in the user state (e.g. invite requests)
		 * 	
		 * @param listener
		 */
		public void setUserStateListener(UserStateListener listener) {
			userStateListener = listener;
		}
    }
	
	/////////////////////////////////////////////////
	
	@Override
	public IBinder onBind(Intent intent) {
		try {
			if(server == null) {
				server = ServerFactory.create(SERVER_HOST, 8888);
				server.setMessageHandler(messageHandler);
				server.setDeathHandler(this);
				server.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			stopSelf();
		}
		
		return sudokuServiceBinder;
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
			sudokuHandler = new SudokuHandler(template, username, server);
			if(userStateListener != null) {
				userStateListener.onGameStarted(template);
			}
			return true;
		case ERROR_MSG:
			if(userStateListener != null) {
				userStateListener.onError((String) msg.obj);
			}
		}
		
		return false;
	}

	@Override
	public void onDeath(Server instance, String message) {
		if(userStateListener != null) {
			userStateListener.onDeath(message);
		}
		this.stopSelf();
	}
	
	private final IBinder sudokuServiceBinder = new SudokuServerBinder();

	
	private final Handler serviceHandler = new Handler(this);
	private final ServerMessageHandler messageHandler = new ServerMessageHandler() {

		@Override
		public void onRawMessageReceived(Server server, Message message) {
			// ignore
		}

		@Override
		public void onDeregisterMessageReceived(Server server,
				DeregisterMessage message) {
			// unused			
		}

		@Override
		public void onLeftMessageReceived(Server server, LeftMessage message) {
			redirectToSudokuHandler(ServerInfoPlayerLeft, message);	
		}

		@Override
		public void onSetFieldMessageReceived(Server server, SetFieldMessage message) {
			// unused			
		}

		@Override
		public void onNamedSetFieldMessageReceived(Server server, NamedSetFieldMessage message) {
			redirectToSudokuHandler(ServerRequestSetDigit, message);
		}

		@Override
		public void onErrorMesssageReceived(Server server, ErrorMessage message) {
			serviceHandler.obtainMessage(ERROR_MSG, message.getMessage()).sendToTarget();
		}

		@Override
		public void onInviteMessageReceived(Server server, InviteMessage message) {
			serviceHandler.obtainMessage(INVITED_MSG, message).sendToTarget();			
		}

		@Override
		public void onACKReceived(Server server, ACKMessage message) {
			Message confirmed = message.getConfirmedMessage();
			
			if(confirmed instanceof RegisterMessage) {
				serviceHandler
					.obtainMessage(REGISTERED_MSG, ((RegisterMessage) confirmed).getName())
					.sendToTarget();
			} else if(confirmed instanceof DeregisterMessage) {
				serviceHandler
					.obtainMessage(DEREGISTERED_MSG, confirmed)
					.sendToTarget();
			}
		}

		@Override
		public void onNACKReceived(Server server, NACKMessage message) {
			Message confirmed = message.getConfirmedMessage();
			if(confirmed instanceof InviteMessage) {
				serviceHandler
					.obtainMessage(INVITE_REJECTED_MSG, confirmed)
					.sendToTarget();
			}
		}

		@Override
		public void onGameOverMessageReceived(Server server, GameOverMessage message) {
			redirectToSudokuHandler(ServerInfoGameFinished, message);
		}

		@Override
		public void onScoreMessageReceived(Server server, ScoreMessage message) {
			redirectToSudokuHandler(ServerInfoLeaderChanged, message);			
		}

		@Override
		public void onNewGameMessageReceived(Server client,	NewGameMessage message) {
			serviceHandler
				.obtainMessage(NEW_GAME_MSG, message.getSudokuField())
				.sendToTarget();			
		}
		
		private void redirectToSudokuHandler(int what, Message message) {
			if(sudokuHandler != null) {				
				sudokuHandler
					.obtainMessage(what, message)
					.sendToTarget();
			}
		}
		
	};

}
