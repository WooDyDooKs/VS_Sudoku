package ds.sudoku.server;

import static ds.sudoku.server.ServerFrontend.userManagement;
import static ds.sudoku.server.ServerFrontend.gamesManager;

import ds.sudoku.communication.DeregisterMessage;
import ds.sudoku.communication.JoinMessage;
import ds.sudoku.communication.LeaveMessage;
import ds.sudoku.communication.SetFieldMessage;
import ds.sudoku.exceptions.server.NonExistingUsername;

public class UserMessageHandler extends DefaultMessageHandler {

	private User user;

	public UserMessageHandler(User user) {
		super(user.getClient());
		this.user = user;
	}
	
	@Override
	public void onDeregisterMessageReceived(DeregisterMessage message) {
		try {
			userManagement.deregister(user);
		} catch (NonExistingUsername e) {
			// this should never happen!
			e.printStackTrace();
		}
	}

	@Override
	public void onJoinMessageReceived(JoinMessage message) {
		
		if(message.hasCustomValue()) {
			// TODO: read other player from message
			User other = userManagement.getUser(null);

			gamesManager.startNewGame(user, other);
		} else {
			gamesManager.startNewGameWithRandom(user);
		}	
		
	}

	@Override
	public void onLeaveMessageReceived(LeaveMessage message) {
		// TODO what to do?		
	}

	@Override
	public void onSetFieldMessageReceived(SetFieldMessage message) {
		GameHandler handler = user.getGame().getHandler();
		
		if(message.isZeroBased()) {
			throw new RuntimeException("zero based indeces not (yet) supported!");
		}
		
		handler.setField(user, message.getRow(), message.getColumn(), message.getValue());
		
		//TODO message.accept?
	}

}
