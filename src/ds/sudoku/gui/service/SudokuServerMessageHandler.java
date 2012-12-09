package ds.sudoku.gui.service;

import static ds.sudoku.gui.service.SudokuService.*;


import android.os.Handler;
import ds.sudoku.communication.ACKMessage;
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
import ds.sudoku.communication.ServerMessageHandler;
import ds.sudoku.communication.SetFieldMessage;
import ds.sudoku.logic.ServerSetFieldInfo;
import ds.sudoku.logic.SudokuHandler;

public class SudokuServerMessageHandler implements ServerMessageHandler {
	
	private Handler guiHandler;

	SudokuServerMessageHandler(Handler guiHandler) {
		this.guiHandler = guiHandler;
	}

	@Override
	public void onRawMessageReceived(Server server, Message message) {
		// ignored
	}

	@Override
	public void onDeregisterMessageReceived(Server server,
			DeregisterMessage message) {
		// unused
	}

	@Override
	public void onLeftMessageReceived(Server server, LeftMessage message) {
		// TODO send to sudoku handler 
	}

	@Override
	public void onSetFieldMessageReceived(Server server, SetFieldMessage message) {
		// unused
	}

	@Override
	public void onNamedSetFieldMessageReceived(Server server,
			NamedSetFieldMessage msg) {
		
		ServerSetFieldInfo info = new ServerSetFieldInfo(
				msg.getRow(), msg.getColumn(), 
				msg.getValue(), msg.getSender());
		
		guiHandler
			.obtainMessage(SudokuHandler.ServerRequestSetDigit, info)
			.sendToTarget();
	}

	@Override
	public void onErrorMesssageReceived(Server server, ErrorMessage message) {
		
	}

	@Override
	public void onInviteMessageReceived(Server server, InviteMessage message) {
		guiHandler.obtainMessage(INVITED_MSG, message).sendToTarget();
	}

	@Override
	public void onACKReceived(Server server, ACKMessage message) {
		Message confirmed = message.getConfirmedMessage();
		
		if(confirmed instanceof RegisterMessage) {
			guiHandler
				.obtainMessage(
						REGISTERED_MSG, 
						((RegisterMessage) confirmed).getName())
				.sendToTarget();
		} else if(confirmed instanceof DeregisterMessage) {
			guiHandler.obtainMessage(DEREGISTERED_MSG, confirmed).sendToTarget();
		}
	}

	@Override
	public void onNACKReceived(Server server, NACKMessage message) {
		Message confirmed = message.getConfirmedMessage();
		if(confirmed instanceof InviteMessage) {
			guiHandler
				.obtainMessage(INVITE_REJECTED_MSG, confirmed)
				.sendToTarget();
		}
	}

	@Override
	public void onGameOverMessageReceived(Server server, GameOverMessage message) {
		// TODO send to sudoku handler
	}

	@Override
	public void onScoreMessageReceived(Server server, ScoreMessage message) {
		// TODO send to sudoku handler
	}

	@Override
	public void onNewGameMessageReceived(Server client, NewGameMessage message) {
		guiHandler.obtainMessage(NEW_GAME_MSG, message.getSudokuField()).sendToTarget();
	}
	

}
