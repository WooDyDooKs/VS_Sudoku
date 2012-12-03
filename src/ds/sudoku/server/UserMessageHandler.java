package ds.sudoku.server;

import ds.sudoku.communication.ACKMessage;
import ds.sudoku.communication.Client;
import ds.sudoku.communication.DeregisterMessage;
import ds.sudoku.communication.InviteMessage;
import ds.sudoku.communication.LeaveMessage;
import ds.sudoku.communication.NACKMessage;
import ds.sudoku.communication.SetFieldMessage;
import ds.sudoku.exceptions.server.NonExistingUsername;
import static ds.sudoku.server.ServerFrontend.userManagement;
import static ds.sudoku.server.ServerFrontend.gamesManager;

public class UserMessageHandler extends DefaultMessageHandler {

	private User user;

	public UserMessageHandler(User user) {
		this.user = user;
	}

	@Override
	public void onDeregisterMessageReceived(Client client, DeregisterMessage message) {
		try {
			userManagement.deregister(user);
		} catch (NonExistingUsername e) {
			// this should never happen!
			e.printStackTrace();
		}
	}

	@Override
	public void onInviteMessageReceived(Client client, InviteMessage message) {
		//TODO
	}

	@Override
	public void onLeaveMessageReceived(Client client, LeaveMessage message) {
		gamesManager.addIdleUser(user);
		user.getGame().getHandler().stopGame();
	}

	@Override
	public void onSetFieldMessageReceived(Client client, SetFieldMessage message) {
		GameHandler handler = user.getGame().getHandler();
		if(message.isZeroBased()) {
			throw new RuntimeException("zero based indeces not (yet) supported!");
		}
		handler.setField(user, message.getRow(), message.getColumn(), message.getValue());
		//TODO message.accept?
	}

	@Override
	public void onACKMessageReceived(Client client, ACKMessage message) {
		// test if ack is reply to inviteRequest
		if(message.hasCustomValue()) {
			// TODO: read other player from message
			User other = userManagement.getUser(null);
			gamesManager.startNewGame(user, other);
		} else {
			gamesManager.startNewGameWithRandom(user);
		}	
		
	}

	@Override
	public void onNACKMessageReceived(Client client, NACKMessage message) {
		// TODO Auto-generated method stub
		super.onNACKMessageReceived(client, message);
	}
	
}